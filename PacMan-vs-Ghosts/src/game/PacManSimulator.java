package game;

import game.controllers.ghosts.GhostsActions;
import game.controllers.ghosts.IGhostsController;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.HumanPacMan;
import game.controllers.pacman.IPacManController;
import game.controllers.pacman.PacManAction;
import game.core.G;
import game.core.Game;
import game.core.GameView;
import game.core.Replay;
import game.core._G_;

import java.awt.event.KeyListener;
import java.io.File;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * One simulator can run one instance of PacMan-vs-Ghosts game.
 * 
 * Can be used for both head/less games.
 * 
 * @author Jimmy
 */
public class PacManSimulator {
	
	public static class GameConfig {
		
		public int seed = -1;
		
		/**
		 * Whether POWER PILLS should be present within the environment.
		 */
		public boolean powerPillsEnabled = true;
		
		/**
		 * Total percentage of PILLS present within the level. If < 1, some (random) pills will be taken away.
		 */
		public double totalPills = 1;
		
		/**
		 * How many levels Ms PacMan may play (-1 => unbound).
		 */
		public int levelsToPlay = -1;
		
		public GameConfig clone() {
			GameConfig result = new GameConfig();
			
			result.seed = seed;
			result.powerPillsEnabled = powerPillsEnabled;
			result.totalPills = totalPills;
			result.levelsToPlay = levelsToPlay;
			
			return result;
		}

		public String asString() {
			return "" + seed + ";" + powerPillsEnabled + ";" + totalPills + ";" + levelsToPlay;
		}
		
		public void fromString(String line) {
			String[] all = line.split(";");
			seed = Integer.parseInt(all[0]);
			powerPillsEnabled = Boolean.parseBoolean(all[1]);
			totalPills = Double.parseDouble(all[2]);
			levelsToPlay = Integer.parseInt(all[3]);
		}

		public String getCSVHeader() {
			return "seed;powerPillsEnabled;totalPills;levelsToPlay";
		}
		
		public String getCSV() {
			return "" + seed + ";" + powerPillsEnabled + ";" + totalPills + ";" + levelsToPlay;
		}
		
	}

	private SimulatorConfig config;
	
	private GameView gv;

	private _G_ game;
	
    private long due; 
    
    // REPLAY STUFF
    private StringBuilder replayData;
    private boolean replayFirstWrite;
	
	public synchronized Game run(final SimulatorConfig config) {
		System.out.println("[PacManSimulator] RUNNING: " + config.getOptions());
		
		// RESET INSTANCE & SAVE CONFIG
		reset(config);
		
		// INIT RANDOMNESS
		if (config.game.seed <= 0) {
			config.game.seed = new Random(System.currentTimeMillis()).nextInt();
			while (config.game.seed < 0) config.game.seed += Integer.MAX_VALUE;
		}
		G.rnd = new Random(config.game.seed);
		
		// INITIALIZE THE SIMULATION
		game = new _G_();
		game.newGame(config.game);
		
		// RESET CONTROLLERS
		config.pacManController.reset(game);
		if (config.ghostsController != null) config.ghostsController.reset(game);

		// INITIALIZE THE VIEW
		if (config.visualize) {
			gv = new GameView(game);
			if (config.visualizationScale2x) gv.setScale2x(true);
			gv.showGame();
			
			if (config.pacManController instanceof KeyListener) {				
				gv.getFrame().addKeyListener((KeyListener)config.pacManController);
			}
			if (config.ghostsController != null && config.ghostsController instanceof KeyListener) {				
				gv.getFrame().addKeyListener((KeyListener)config.ghostsController);
			}
		} 
		
		// SETUP REPLAY RECORDING
		int lastLevel = game.getCurLevel();
		if (config.replay) {
			replayData = new StringBuilder();
			replayFirstWrite = true;
		}
		
		// START CONTROLLERS (threads auto-start during instantiation)
		ThinkingThread pacManThread = 
			new ThinkingThread(
				"PAC-MAN",
				new IThinkingMethod() {
					@Override
					public void think() {
						PacManSimulator.this.config.pacManController.tick(game.copy(), due);		
					}
				}
			);
		ThinkingThread ghostsThread =
			new ThinkingThread(
				"GHOSTS",
				new IThinkingMethod() {
					@Override
					public void think() {
						if (PacManSimulator.this.config.ghostsController != null) PacManSimulator.this.config.ghostsController.tick(game, due);			
					}
				}
			);
		 
		// START THE GAME
		try {
			while(!game.gameOver())
			{
				due = System.currentTimeMillis() + config.thinkTimeMillis;
				
				// WAKE UP THINKING THREADS
				thinkingLatch = new CountDownLatch(2);
				
				long start = System.currentTimeMillis();
				
				pacManThread.alert();
				ghostsThread.alert();
				
				// GIVE THINKING TIME
		        try{		        			        	
		        	thinkingLatch.await(config.thinkTimeMillis, TimeUnit.MILLISECONDS);
		        	
		        	if (config.visualize) {
		        		if (System.currentTimeMillis() - start < config.thinkTimeMillis) {
		        			long sleepTime = config.thinkTimeMillis - (System.currentTimeMillis() - start);
		        			if (sleepTime > 4) {
		        				Thread.sleep(sleepTime);
		        			}
		        		}
		        	}
		        } catch(Exception e) {		        	
		        }
		        
		        if (pacManThread.thinking) {
		        	System.out.println("[SIMULATOR] PacMan is still thinking!");
		        }
		        if (ghostsThread.thinking) {
		        	System.out.println("[SIMULATOR] Ghosts are still thinking!");
		        }
		        
		        thinkingLatch = null;
		        
		        // OBTAIN ACTIONS
		        PacManAction  pacManAction  = config.pacManController.getAction().clone();
		        GhostsActions ghostsActions = (config.ghostsController == null ? null : config.ghostsController.getActions().clone());
				
		        // SIMULATION PAUSED?
		        boolean advanceGame = true;
		        if (config.mayBePaused) {
			        if (pacManAction.pauseSimulation || (ghostsActions != null && ghostsActions.pauseSimulation)) {
			        	if (!pacManAction.nextFrame && (ghostsActions == null || !ghostsActions.nextFrame)) {
			        		advanceGame = false;
			        	}
			        	config.pacManController.getAction().nextFrame = false;
			        	if (config.ghostsController != null) config.ghostsController.getActions().nextFrame = false;
			        }
		        }
		        
		        // ADVANCE GAME
		        if (advanceGame) {
		        	int pacManLives = game.getLivesRemaining();
		        	
			        int replayStep[] = game.advanceGame(pacManAction, ghostsActions);
			        
			        // SAVE ACTIONS TO REPLAY
			        if (config.replay) {
			        	// STORE ACTIONS
			        	storeActions(replayStep, game.getCurLevel()==lastLevel);
			        }
			        
			        // NEW LEVEL?
			        if (game.getCurLevel() != lastLevel) {
			        	lastLevel=game.getCurLevel();
			        	
			        	// INFORM CONTROLLERS
			        	config.pacManController.nextLevel(game.copy());
			    		if (config.ghostsController != null) config.ghostsController.nextLevel(game.copy());
			    		
			    		// FLUSH REPLAY DATA TO FILE
			    		if (config.replay) {
			    			Replay.saveActions(config.game, (config.ghostsController == null ? 0 : config.ghostsController.getGhostCount()), replayData.toString(), config.replayFile, replayFirstWrite);
			        		replayFirstWrite = false;
			        		replayData = new StringBuilder();
			    		}
			        }
			        
			        // PAC MAN KILLED?
			        if (pacManLives != game.getLivesRemaining()) {
			        	config.pacManController.killed();
			        }
		        }
		        
		        // VISUALIZE GAME
		        if (config.visualize) {
		        	gv.repaint();
		        }
			}
		} finally {		
			// KILL THREADS
			pacManThread.kill();
			ghostsThread.kill();
			
			// SAVE REPLAY DATA
			if (config.replay) {
				Replay.saveActions(config.game, (config.ghostsController == null ? 0 : config.ghostsController.getGhostCount()), replayData.toString(), config.replayFile, replayFirstWrite);
			}
			
			// CLEAN UP
			if (config.visualize) {
				if (config.pacManController instanceof KeyListener) {				
					gv.getFrame().removeKeyListener((KeyListener)config.pacManController);
				}
				if (config.ghostsController instanceof KeyListener) {				
					gv.getFrame().removeKeyListener((KeyListener)config.ghostsController);
				}
				
				gv.getFrame().setTitle("[FINISHED]");
				gv.repaint();
			}					
		}
		
		return game;
	}

	private void reset(SimulatorConfig config) {
		this.config = config;
		
		gv = null;
		game = null;		
	}
	
	private void storeActions(int[] replayStep, boolean newLine) {
		replayData.append( (game.getTotalTime()-1) + "\t" );
	
	    for (int i=0;i < replayStep.length; i++) {
	    	replayData.append(replayStep[i]+"\t");
	    }
	
	    if(newLine) {
	    	replayData.append("\n");
	    }
	}
	
	private interface IThinkingMethod {
		
		public void think();
		
	}
	
	private CountDownLatch thinkingLatch;
	
	private class ThinkingThread extends Thread 
	{
		public boolean thinking = false;
	    private IThinkingMethod method;
	    private boolean alive;
	    
	    public ThinkingThread(String name, IThinkingMethod method) 
	    {
	    	super(name);
	        this.method = method;
	        alive=true;
	        start();
	    }

	    public synchronized  void kill() 
	    {
	        alive=false;
	        notify();
	    }
	    
	    public synchronized void alert()
	    {
	        notify();
	    }

	    public synchronized void run() 
	    {
	    	 try {
	        	while(alive) 
		        {
		        	try {
		        		synchronized(this)
		        		{
	        				wait(); // waked-up via alert()
		                }
		        	} catch(InterruptedException e)	{
		                e.printStackTrace();
		            }
	
		        	if (alive) {
		        		thinking = true;
		        		method.think();
		        		thinking = false;
		        		try {
		        			thinkingLatch.countDown();
		        		} catch (Exception e) {
		        			// thinkingLatch may be nullified...
		        		}
		        	} 
		        	
		        }
	        } finally {
	        	alive = false;
	        }
	    }
	}
	
	/**
	 * Run simulation according to the configuration.
	 * @param config
	 * @return
	 */
	public static Game play(SimulatorConfig config) {
		PacManSimulator simulator = new PacManSimulator();
		return simulator.run(config);		
	}
	
	/**
	 * Run simulation visualized with ghosts.
	 * @param pacMan
	 * @param ghosts
	 * @return
	 */
	public static Game play(IPacManController pacMan, IGhostsController ghosts) {
		SimulatorConfig config = new SimulatorConfig();
		
		config.pacManController = pacMan;
		config.ghostsController = ghosts;
		
		config.replay = true;
		config.replayFile = new File("./replay.log");
		
		return play(config);	
	}
	
	/**
	 * Run simulation visualized w/o ghosts.
	 * @param pacMan
	 * @param ghosts
	 * @return
	 */
	public static Game play(IPacManController pacMan) {
		return play(pacMan, null);		
	}
	
	/**
	 * Run simulation headless.
	 * @param pacMan
	 * @param ghosts
	 * @return
	 */
	public static Game simulate(IPacManController pacMan, IGhostsController ghosts) {
		SimulatorConfig config = new SimulatorConfig();		
		
		config.visualize = false;
		
		config.pacManController = pacMan;
		config.ghostsController = ghosts;
		
		config.replay = true;
		config.replayFile = new File("./replay.log");
		
		return play(config);		
	}
		
	public static void main(String[] args) {
		// PLAY WITHOUT GHOSTS
		//play(new HumanPacMan());
		
		// PLAY WITH 1 GHOST
		// play(new HumanPacMan(), new GameGhosts(1, false));
		
		// PLAY WITH 2 GHOSTS
		//play(new HumanPacMan(), new GameGhosts(2, false));
		
		// PLAY WITH 3 GHOSTS
		//play(new HumanPacMan(), new GameGhosts(3, false));
		
		// PLAY WITH 4 GHOSTS
		play(new HumanPacMan(), new GameGhosts(4, false));		
	}
	
}
