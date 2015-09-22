package game.controllers.ghosts.game;

import game.core.Game;

public class GhostScatterState implements GameGhostAI {
	int X = 0;
	int Y = 1;
	int[] BlinkyCorner = {0,0};
	int[] PinkyCorner = {0,0};
	int[] ClydeCorner = {0,0};
	int[] InkyCorner = {0,0};
	int BlinkyCornerNode = 0;
	int PinkyCornerNode = 76;
	int ClydeCornerNode = 1195;
	int InkyCornerNode = 1290;
	
	
	@Override
	public int[] execute(int ghostType, Game game, long timeDue) {
		int[] target = {0,0};
		if(ghostType == BLINKY){
			if(game.getNumActivePills()<20){
				target[X] = game.getX(game.getCurPacManLoc());
				target[Y] = game.getY(game.getCurPacManLoc());
			}
			else{
				target[X]= game.getX(BlinkyCornerNode);
				target[Y]= game.getY(BlinkyCornerNode);
				
			}
			
		}
		if(ghostType == PINKY){
			target[X]= game.getX(PinkyCornerNode);
			target[Y]= game.getY(PinkyCornerNode);
		}
		if(ghostType == CLYDE){
			target[X]= game.getX(ClydeCornerNode);
			target[Y]= game.getY(ClydeCornerNode);		
		}
		if(ghostType == INKY){
			target[X]= game.getX(InkyCornerNode);
			target[Y]= game.getY(InkyCornerNode);
		}
		return target;
	}
	
	

}
