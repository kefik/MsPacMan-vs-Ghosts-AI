/*
 * Implementation of "Ms Pac-Man" for the "Ms Pac-Man versus Ghost Team Competition", brought
 * to you by Philipp Rohlfshagen, David Robles and Simon Lucas of the University of Essex.
 * 
 * www.pacman-vs-ghosts.net
 * 
 * Code written by Philipp Rohlfshagen, based on earlier implementations of the game by
 * Simon Lucas and David Robles. 
 * 
 * You may use and distribute this code freely for non-commercial purposes. This notice 
 * needs to be included in all distributions. Deviations from the original should be 
 * clearly documented. We welcome any comments and suggestions regarding the code.
 */
package game.core;

import java.util.BitSet;

public class _G_ extends G
{
	public static final int EDIBLE_ALERT=30;	//for display only (ghosts turning blue)
	
	//to save replays
	private int pacManDir=G.INITIAL_PAC_DIR;
	private StringBuilder sb;
	
	public _G_(){}
	
	//Instantiates everything to start a new game
	public void newGame()
	{	
		init();		//load mazes if not yet loaded
		
		curMaze=0;
		
		curGhostLocs=new int[G.NUM_GHOSTS];
		lastGhostDirs=new int[G.NUM_GHOSTS];
		edibleTimes=new int[G.NUM_GHOSTS];
		lairTimes=new int[G.NUM_GHOSTS];

		pills=new BitSet(getNumberPills());
		pills.set(0,getNumberPills());
		powerPills=new BitSet(getNumberPowerPills());
		powerPills.set(0,getNumberPowerPills());
		score=0;
		levelTime=0;
		totalTime=0;
		totLevel=0;
		livesRemaining=G.NUM_LIVES;
		extraLife=false;
		gameOver=false;
		
		reset(false);
		
		//for replays
		this.sb=new StringBuilder();
	}
	
	//Size of the Maze (for display only)
	public int getWidth()
	{
		return mazes[curMaze].width;
	}
	
	//Size of the Maze (for display only)
	public int getHeight()
	{
		return mazes[curMaze].height;
	}
	
	//for the web-site javascript replays
    public void monitorGame()
    {
        sb.append("{");

        //maze
        sb.append("ma:"+curMaze+",");
        sb.append("tt:"+totalTime+",");
        sb.append("li:"+livesRemaining+",");
        sb.append("sc:"+score+",");
        sb.append("lt:"+levelTime+",");
        sb.append("le:"+totLevel+",");
        
        // pacman
        sb.append("pn:"+curPacManLoc+",");
        
        int pacDir=lastPacManDir;
        
    	if(pacDir>=0 && pacDir<4)
    		pacManDir=pacDir;
        
        sb.append("pd:"+pacManDir+",");
        
        // ghosts
        sb.append("gh:[");
        sb.append("{gn:"+curGhostLocs[0]+",");
        sb.append("di:"+lastGhostDirs[0]+",et:"+edibleTimes[0]);
        sb.append(",lt:"+lairTimes[0]);
        sb.append("},");
        sb.append("{gn:"+curGhostLocs[1]+",");
        sb.append("di:"+lastGhostDirs[1]+",et:"+edibleTimes[1]);
        sb.append(",lt:"+lairTimes[1]);
        sb.append("},");
        sb.append("{gn:"+curGhostLocs[2]+",");
        sb.append("di:"+lastGhostDirs[2]+",et:"+edibleTimes[2]);
        sb.append(",lt:"+lairTimes[2]);
        sb.append("},");
        sb.append("{gn:"+curGhostLocs[3]+",");
        sb.append("di:"+lastGhostDirs[3]+",et:"+edibleTimes[3]);
        sb.append(",lt:"+lairTimes[3]);
        sb.append("}");
        sb.append("],");

        // pills
        sb.append("pi:\"");

        for (int i = 0; i < getPillIndices().length; i++)
            if(checkPill(i))
                sb.append("1");
            else
                sb.append("0");

        sb.append("\",");
        sb.append("po:\"");

        for (int i = 0; i < getPowerPillIndices().length; i++)
            if(checkPowerPill(i))
                sb.append("1");
            else
                sb.append("0");

        sb.append("\"");
        sb.append("},\n");
    }
    
    public StringBuilder getRecordedMatch()
    {
    	return sb;
    }
}