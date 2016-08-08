package game.controllers.pacman.examples;

import game.PacManSimulator;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.PacManHijackController;
import game.core.G;
import game.core.Game;
import game.core.GameView;

import java.awt.Color;

/*
 * Same as NearestPillPacMan but does some visuals to illustrate what can be done.
 * Please note: the visuals are just to highlight different functionalities and may
 * not make sense from a controller's point of view (i.e., they might not be useful)
 * Comment/un-comment code below as desired (drawing all visuals would probably be too much).
 */
public final class NearestPillPacManVS extends PacManHijackController
{	
	@Override
	public void tick(Game game, long timeDue) {
		int current=game.getCurPacManLoc();
		int[] activePills=game.getPillIndicesActive();
		int[] activePowerPills=game.getPowerPillIndicesActive();
		int[] targetsArray=new int[activePills.length+activePowerPills.length];
		
		for(int i=0;i<activePills.length;i++)
			targetsArray[i]=activePills[i];
		
		for(int i=0;i<activePowerPills.length;i++)
			targetsArray[activePills.length+i]=activePowerPills[i];		
		
		int nearest=game.getTarget(current,targetsArray,true,G.DM.PATH);		
		
		//add the path that Ms Pac-Man is following
//		GameView.addPoints(game,Color.GREEN,game.getPath(current,nearest));
		
		//add the path from Ms Pac-Man to the nearest existing power pill
		int nearestPP = game.getTarget(current,activePowerPills,true,G.DM.PATH);
		GameView.addPoints(game,Color.CYAN,game.getPath(current,nearestPP));
		
		int[] ghostDistances = new int[]{ game.getPathDistance(current, game.getCurGhostLoc(0)), game.getPathDistance(current, game.getCurGhostLoc(1)), game.getPathDistance(current, game.getCurGhostLoc(2)), game.getPathDistance(current, game.getCurGhostLoc(3))};
		GameView.addText(0, 10, Color.YELLOW, "Ghost distances: " + ghostDistances[0] + ", " + ghostDistances[1] + ", " + ghostDistances[2] + ", " + ghostDistances[3]);
		
		//add the path AND ghost path from Ghost 0 to the first power pill (to illustrate the differences)
//		if(game.getLairTime(0)==0)
//		{
//			GameView.addPoints(game,Color.ORANGE,game.getPath(game.getCurGhostLoc(0),powerPills[0]));
//			GameView.addPoints(game,Color.YELLOW,game.getGhostPath(0,powerPills[0]));
//		}
		
		//add the path from Ghost 0 to the closest power pill
//		if(game.getLairTime(0)==0)
//			GameView.addPoints(game,Color.WHITE,game.getGhostPath(0,game.getGhostTarget(0,powerPills,true)));
		
		//add lines connecting Ms Pac-Man and the power pills
//		for(int i=0;i<powerPills.length;i++)
//			GameView.addLines(game,Color.CYAN,current,powerPills[i]);
		
		//add lines to the ghosts (if not in lair) - green if edible, red otherwise
		for(int i=0;i<G.NUM_GHOSTS;i++)										
			if(game.getLairTime(i)==0)
				if(game.isEdible(i))
					GameView.addLines(game,Color.GREEN,current,game.getCurGhostLoc(i));
				else
					GameView.addLines(game,Color.RED,current,game.getCurGhostLoc(i));
		
		//adds the paths the ghost would need to follow to reach Ms Pac-Man
//		Color[] colors={Color.RED,Color.BLUE,Color.MAGENTA,Color.ORANGE};
		
//		for(int i=0;i<G.NUM_GHOSTS;i++)	
//			if(game.getLairTime(i)==0)
//				GameView.addPoints(game,colors[i],game.getGhostPath(i,current));
			
		pacman.set(game.getNextPacManDir(nearest,true,Game.DM.PATH));		
	}
	
	public static void main(String[] args) {
		while (true) {
			PacManSimulator.play(new NearestPillPacManVS(), new GameGhosts(false));
			GameView.lastInstance.setVisible(false);
		}
	}
}