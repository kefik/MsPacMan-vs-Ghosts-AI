package game.controllers.ghosts.examples;

import game.controllers.ghosts.GhostsControllerBase;
import game.core.Game;

public class Legacy2TheReckoning extends GhostsControllerBase
{
	public static final int CROWDED_DISTANCE=30;
	public static final int PACMAN_DISTANCE=10;
    public static final int PILL_PROXIMITY=15;

    private final int[] dirs;
    
    public Legacy2TheReckoning()
    {
    	super(Game.NUM_GHOSTS);
        dirs=new int[Game.NUM_GHOSTS];
    }

    @Override
    public void tick(Game game, long timeDue) {
    	int pacmanLoc=game.getCurPacManLoc();
    	
        for(int i=0;i<dirs.length;i++)      
        {
        	if(game.ghostRequiresAction(i))
        	{
        		//if ghosts are all in close proximity and not near Ms Pac-Man, disperse
        		if(isCrowded(game) && !closeToMsPacMan(game,game.getCurGhostLoc(i)))
        			dirs[i]=getRetreatActions(game,i);                          				//go towards the power pill locations
        		//if edible or Ms Pac-Man is close to power pill, move away from Ms Pac-Man
        		else if(game.getEdibleTime(i)>0 || closeToPower(game))
        			dirs[i]=game.getNextGhostDir(i,pacmanLoc,false,Game.DM.PATH);      			//move away from ms pacman
        		//else go towards Ms Pac-Man
        		else        		
        			dirs[i]=game.getNextGhostDir(i,pacmanLoc,true,Game.DM.PATH);       			//go towards ms pacman
        	}
        }
        
        input.set(dirs);
    }

    private boolean closeToPower(Game game)
    {
    	int pacmanLoc=game.getCurPacManLoc();
    	int[] powerPills=game.getPowerPillIndicesActive();
    	
    	for(int i=0;i<powerPills.length;i++)
    		if(game.getPathDistance(powerPills[i],pacmanLoc)<PILL_PROXIMITY)
    			return true;

        return false;
    }

    private boolean closeToMsPacMan(Game game,int location)
    {
    	if(game.getPathDistance(game.getCurPacManLoc(),location)<PACMAN_DISTANCE)
    		return true;

    	return false;
    }

    private boolean isCrowded(Game game)
    {
        float distance=0;

        for (int i=0;i<Game.NUM_GHOSTS-1;i++)
            for(int j=i+1;j<Game.NUM_GHOSTS;j++)
                distance+=game.getPathDistance(game.getCurGhostLoc(i),game.getCurGhostLoc(j));
        
        return (distance/6)<CROWDED_DISTANCE ? true : false;
    }

    private int getRetreatActions(Game game,int index)
    {
        if(game.getEdibleTime(index)==0 && game.getPathDistance(game.getCurGhostLoc(index),game.getCurPacManLoc())<PACMAN_DISTANCE)
            return game.getNextGhostDir(index,game.getCurPacManLoc(),true,Game.DM.PATH);
        else
            return game.getNextGhostDir(index,game.getPowerPillIndices()[index],true,Game.DM.PATH);
    }
}