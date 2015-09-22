package game.core;

import java.util.Arrays;

import game.controllers.ghosts.GhostsActions;

/**
 * This class is to replay games that were recorded using Replay. The only differences are:
 * 1. Ghost reversals are removed
 * 2. Directions are not checked (since they are necessarily valid)
 * This class should only be used in conjunction with stored directions, not to play the game itself.
 */
public final class _RG_ extends _G_
{	
	//Updates the locations of the ghosts without reversals
	protected void updateGhosts(GhostsActions ghosts, boolean reverse)
	{
		super.updateGhosts(ghosts,false);
	}
	
	public int[] getGhostNeighbours(int whichGhost)
	{
		int[] neighbours=Arrays.copyOf(mazes[curMaze].graph[curGhostLocs[whichGhost]].neighbours,mazes[curMaze].graph[curGhostLocs[whichGhost]].neighbours.length);			
		return neighbours;
	}
	
	public int checkGhostDir(int whichGhost,int direction)
	{
		int origCheck = super.checkGhostDir(whichGhost, direction);
		if (origCheck != direction) {
			super.checkGhostDir(whichGhost, direction);
		}
		return direction;
	}
	
	public int checkPacManDir(int direction)
	{
		return direction;		
	}
	
}