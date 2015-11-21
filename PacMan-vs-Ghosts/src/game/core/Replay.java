package game.core;

import game.PacManSimulator.GameConfig;
import game.controllers.Direction;
import game.controllers.ghosts.GhostsActions;
import game.controllers.ghosts.IGhostsController;
import game.controllers.pacman.IPacManController;
import game.controllers.pacman.PacManAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

@SuppressWarnings({"rawtypes","unchecked"})
public class Replay
{
	public GameConfig gameConfig = new GameConfig();
	
	private int ghostCount;
	
    private ReplayMsPacman pacMan;
    private ReplayGhosts ghosts;

    private ArrayList<Integer> pacManActions;
    private ArrayList<Integer> pacManLocations;
    private ArrayList<int[]> ghostActions;
    private ArrayList<int[]> ghostLocations;

    public Replay(File file)
    {
        loadActions(file);
        this.pacMan=new ReplayMsPacman();
        this.ghosts=new ReplayGhosts(ghostCount);
    }
 
	public void loadActions(File file)
    {
        ArrayList[] data=loadData(file);
        pacManActions=data[0];
        ghostActions=data[1];
        pacManLocations=data[2];
        ghostLocations=data[3];
    }

    public static void saveActions(GameConfig gameConfig, int ghostCount, String actions, File replayFile, boolean firstWrite)
    {
        try
        {
            FileOutputStream outS=new FileOutputStream(replayFile, !firstWrite);
            PrintWriter pw=new PrintWriter(outS);

            if (firstWrite) {
            	System.out.println("Saving replay into: " + replayFile.getAbsolutePath());
            	pw.println(gameConfig.asString());
            	pw.println(ghostCount);
            }
            
            pw.println(actions);

            pw.flush();
            outS.close();

        }
        catch (Exception e)
        {
            System.out.println("Could not save data!");
        }
    }
    
    public ReplayMsPacman getPacMan()
    {
        return pacMan;
    }

    public ReplayGhosts getGhosts()
    {
        return ghosts;
    }

	public ArrayList[] loadData(File file)
    {
    	ArrayList[] data=new ArrayList[4];
        data[0]=new ArrayList<Integer>();
        data[1]=new ArrayList<int[]>();
        data[2]=new ArrayList<Integer>();
        data[3]=new ArrayList<int[]>();

        try
        {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            
            gameConfig.fromString(br.readLine());
            
            String ghostCount = br.readLine();
            this.ghostCount = Integer.parseInt(ghostCount);
            
            String input=br.readLine();

            while(input!=null && !input.equals(""))
            {
                input=input.trim();
                String[] numbers=input.split("\t");

                if(!numbers[0].equals("#"))                     //ignore comments
                {
                    data[0].add(Integer.parseInt(numbers[1]));  //action for Ms Pac-Man

                    int[] ghostActions=new int[4];              //actions for ghosts

                    for(int i=0;i<ghostActions.length;i++)
                	    ghostActions[i]=Integer.parseInt(numbers[i+2]);

                    data[1].add(ghostActions);
                    
                    data[2].add(Integer.parseInt(numbers[6]));  //pacman locations
                    
                    int[] ghostLocations=new int[4];            //ghosts locations
                    
                    for(int i=0;i<ghostLocations.length;i++)
                    	ghostLocations[i]=Integer.parseInt(numbers[i+7]);
                    
                    data[3].add(ghostLocations);                    
                }

                input=br.readLine();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return data;
    }
        
	//Simple controller that simply plays the next recorded action
    public class ReplayMsPacman implements IPacManController
    {
    	private PacManAction action = new PacManAction();

		@Override
		public void reset(Game game) {
			action.reset();
		}
		
		@Override
		public void nextLevel(Game game) {		
		}
    	
		@Override
		public void tick(Game game, long timeDue) {
			int actionIndex = pacManActions.get(game.getTotalTime());
			Direction actionDir = Direction.forIndex(actionIndex);
			
			int locationIndex = pacManLocations.get(game.getTotalTime());
			Direction locationDir = Direction.NONE;
			for (int i = 0; i < 4; ++i) {
				if (locationIndex == game.getNeighbour(game.getCurPacManLoc(), i)) locationDir = Direction.forIndex(i);
			}
			
			if (actionDir != locationDir) {
				actionDir = locationDir;
			}
			
			action.set(actionDir);
		}
		
		@Override
		public void killed() {
		}
		
		public int getLocation(Game game) {
			return pacManLocations.get(game.getTotalTime());
		}

		@Override
		public PacManAction getAction() {
			return action;
		}
    }

	//Simple controller that simply plays the next recorded action
    public class ReplayGhosts implements IGhostsController
    {
    	private GhostsActions actions;
    	
    	public ReplayGhosts(int ghostCount) {
			actions = new GhostsActions(ghostCount);
		}

		@Override
    	public int getGhostCount() {
    		return ghostCount;
    	}
    	
    	@Override
		public void reset(Game game) {    		
    		actions.reset();
		}
    	
    	@Override
    	public void nextLevel(Game game) {		
    	}
    	
		@Override
		public void tick(Game game, long timeDue) {
			int[] actions = ghostActions.get(game.getTotalTime());
			for (int ghost = 0; ghost < actions.length; ++ghost) {
				Direction actionDir = Direction.forIndex(actions[ghost]);
				
				int locationIndex = ghostLocations.get(game.getTotalTime())[ghost];
				Direction locationDir = Direction.NONE;
				for (int j = 0; j < 4; ++j) {
					if (locationIndex == game.getNeighbour(game.getCurGhostLoc(ghost), j)) locationDir = Direction.forIndex(j);
				}
				
				if (actionDir != locationDir && game.getLairTime(ghost) == 0) {
					actionDir = locationDir;
				}
				
				this.actions.ghost(ghost).set(actionDir);
			}
		}
		
		public int[] getLocations(Game game) {
			return ghostLocations.get(game.getTotalTime());
		}

		@Override
		public GhostsActions getActions() {
			return actions;
		}	
    }
    
}