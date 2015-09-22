package game;

import game.controllers.ghosts.GhostsActions;
import game.controllers.ghosts.IGhostsController;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.HumanPacMan;
import game.controllers.pacman.IPacManController;
import game.controllers.pacman.PacManAction;
import game.core.Game;
import game.core.GameView;
import game.core.Replay;
import game.core._G_;

import java.awt.event.KeyListener;
import java.io.File;
import java.util.concurrent.Semaphore;

/**
 * One simulator can run one instance of PacMan-vs-Ghosts game.
 * 
 * Can be used for both head/less games.
 * 
 * @author Jimmy
 */
public class PacManSimulator {
	
	public static class SimulatorConfig {
		
		public boolean visualize = true;
		public boolean visualizationScale2x = true;
		
		public boolean mayBePaused = true;
		
		public IPacManController pacManController;
		public IGhostsController ghostsController;
		
		/**
		 * How long can PacMan / Ghost controller think about the game before we compute next frame.
		 * If {@ #visualize} than it also determines the speed of the game.
		 * 
		 * DEFAULT: 25 FPS
		 */
		public int thinkTimeMillis = 40;
		
		public boolean replay = false;
		public File replayFile = null;
	}

	private SimulatorConfig config;
	
	private GameView gv;

	private _G_ game;
	
    private long due; 
    
    // REPLAY STUFF
    private StringBuilder replayData;
    private boolean replayFirstWrite;
	
	public synchronized Game run(final SimulatorConfig config) {
		// RESET INSTANCE & SAVE CONFIG
		reset(config);
		
		// INITIALIZE THE SIMULATION
		game = new _G_();
		game.newGame();
		
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
				pacManThread.alert();
				ghostsThread.alert();
				
				// GIVE THINKING TIME (BUSY WAITING ... TODO: we should know better!)
		        try{
		        	long start = System.currentTimeMillis();
		        	boolean pacmanThinking = true;
		        	boolean ghostsThinking = true;
		        	while (System.currentTimeMillis() - start < config.thinkTimeMillis) {
		        		long sleepTime = config.thinkTimeMillis - (System.currentTimeMillis() - start);
		        		if (sleepTime > 40) sleepTime = 20;
		        		Thread.sleep(sleepTime);		        		
		        		if (pacmanThinking) {
		        			if (pacManThread.thinking.tryAcquire()) {
		        				pacManThread.thinking.release();
		        				pacmanThinking = false;
		        			}
		        		}
		        		if (ghostsThinking) {
		        			if (ghostsThread.thinking.tryAcquire()) {
		        				ghostsThread.thinking.release();
		        				ghostsThinking = false;
		        			}
		        		}
		        		if (!pacmanThinking && !ghostsThinking) break;
		        	}	
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
		        
		        // OBTAIN ACTIONS
		        PacManAction  pacManAction  = config.pacManController.getAction().clone();
		        GhostsActions ghostsActions = (config.ghostsController == null ? null : config.ghostsController.getActions().clone());
				
		        // SIMULATION PAUSED?
		        boolean advanceGame = true;
		        if (config.mayBePaused) {
			        if (pacManAction.pauseSimulation || (ghostsActions != null && ghostsActions.pauseSimulation)) {
			        	if (!pacManAction.nextFrame && (ghostsActions != null && !ghostsActions.nextFrame)) {
			        		advanceGame = false;
			        	}
			        	config.pacManController.getAction().nextFrame = false;
			        	if (config.ghostsController != null) config.ghostsController.getActions().nextFrame = false;
			        }
		        }
		        
		        // ADVANCE GAME
		        if (advanceGame) {
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
			    			Replay.saveActions(replayData.toString(), config.replayFile, replayFirstWrite);
			        		replayFirstWrite = false;
			        		replayData = new StringBuilder();
			    		}
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
				Replay.saveActions(replayData.toString(), config.replayFile, replayFirstWrite);
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
	
	private class ThinkingThread extends Thread 
	{
	    private IThinkingMethod method;
	    private boolean alive;
	    public Semaphore thinking;

	    public ThinkingThread(String name, IThinkingMethod method) 
	    {
	    	super(name);
	        this.method = method;
	        alive=true;
	        thinking = new Semaphore(1);
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
		        		boolean acquired = false;
		        		try {
		        			thinking.acquire();
		        			acquired = true;
		        			method.think();   	 			
		        		} catch (InterruptedException e) {
		        			e.printStackTrace();
		        		} finally {
		        			if (acquired) thinking.release();
		        		}
		        	} 
		        	
		        }
	        } finally {
	        	alive = false;
	        }
	    }
	}
	
	/**
	 * Run simulation visualized.
	 * @param pacMan
	 * @param ghosts
	 * @return
	 */
	public static Game play(IPacManController pacMan, IGhostsController ghosts) {
		PacManSimulator simulator = new PacManSimulator();
		
		SimulatorConfig config = new SimulatorConfig();		
		
		config.pacManController = pacMan;
		config.ghostsController = ghosts;
		
		config.replay = true;
		config.replayFile = new File("./replay.log");
		
		return simulator.run(config);		
	}
	
	/**
	 * Run simulation visualized w/o ghosts.
	 * @param pacMan
	 * @param ghosts
	 * @return
	 */
	public static Game play(IPacManController pacMan) {
		PacManSimulator simulator = new PacManSimulator();
		
		SimulatorConfig config = new SimulatorConfig();		
		
		config.pacManController = pacMan;
		config.ghostsController = null;
		
		config.replay = true;
		config.replayFile = new File("./replay.log");
		
		return simulator.run(config);		
	}
	
	/**
	 * Run simulation headless.
	 * @param pacMan
	 * @param ghosts
	 * @return
	 */
	public static Game simulate(IPacManController pacMan, IGhostsController ghosts) {
		PacManSimulator simulator = new PacManSimulator();
		
		SimulatorConfig config = new SimulatorConfig();		
		
		config.visualize = false;
		
		config.pacManController = pacMan;
		config.ghostsController = ghosts;
		
		config.replay = true;
		config.replayFile = new File("./replay.log");
		
		return simulator.run(config);		
	}
		
	public static void main(String[] args) {
		// PLAY WITH GHOSTS
		play(new HumanPacMan(), new GameGhosts(true));
		
		// PLAY WITHOUT GHOSTS
		//play(new HumanPacMan());
	}
	
}
