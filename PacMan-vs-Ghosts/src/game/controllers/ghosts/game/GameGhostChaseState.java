package game.controllers.ghosts.game;

import game.core.Game;

public class GameGhostChaseState implements GameGhostAI {
	int X = 0;
	int Y = 1;
	int pinkDist = 16;
	int tileDist = 4;
	int ClydeCornerNode = 1195;
	int ClydeMaintainDistance = 8;
	int InkyPacDistance = 2;
		
	@Override
	public int[] execute(int ghostType, Game game, long timeDue) {
		int[] target = {0,0};
		if(ghostType == BLINKY){
			target[X] = game.getX(game.getCurPacManLoc());
			target[Y] = game.getY(game.getCurPacManLoc());		
		}
		if(ghostType == PINKY){
			target[X] = game.getX(game.getCurPacManLoc());
			target[Y] = game.getY(game.getCurPacManLoc());	
			int pacLastDirection = game.getCurPacManDir();
			if (pacLastDirection == Game.UP){
				target[X] -= pinkDist;
				target[Y] -= pinkDist;
			}
			else if(pacLastDirection == Game.LEFT){
				target[X] -= pinkDist;
			}
			else if(pacLastDirection == Game.RIGHT){
				target[X] += pinkDist;
			}
			else if(pacLastDirection == Game.DOWN){
				target[Y] += pinkDist;
			}	
		}
		if(ghostType == CLYDE){
			double distanceFromPac = GameGhosts.getEuclideanDistance(game.getX(game.getCurGhostLoc(ghostType)), game.getY(game.getCurGhostLoc(ghostType)),  game.getX(game.getCurPacManLoc()), game.getY(game.getCurPacManLoc()));
			if (distanceFromPac >= ClydeMaintainDistance*tileDist){
				target[X] = game.getX(game.getCurPacManLoc());
				target[Y] = game.getY(game.getCurPacManLoc());	
			}
			else{
				target[X] = game.getX(ClydeCornerNode);
				target[Y] = game.getY(ClydeCornerNode);
			}
				
		}
		if(ghostType == INKY){
			int pacFrontX = game.getX(game.getCurPacManLoc());
			int pacFrontY = game.getY(game.getCurPacManLoc());	
			int pacLastDirection = game.getCurPacManDir();
			if (pacLastDirection == Game.UP){
				pacFrontX -= tileDist * InkyPacDistance;
				pacFrontY -= tileDist * InkyPacDistance;
			}
			else if(pacLastDirection == Game.LEFT){
				pacFrontX -= tileDist * InkyPacDistance;
			}
			else if(pacLastDirection == Game.RIGHT){
				pacFrontX += tileDist * InkyPacDistance;
			}
			else if(pacLastDirection == Game.DOWN){
				pacFrontY += tileDist * InkyPacDistance;
			}	
			
			int blinkyLocX = game.getX(game.getCurGhostLoc(BLINKY));
			int blinkyLocY = game.getY(game.getCurGhostLoc(BLINKY));
			int vectorX = pacFrontX - blinkyLocX;
			int vectorY = pacFrontY - blinkyLocY;
			target[X]= blinkyLocX + vectorX*2;
			target[Y]= blinkyLocY + vectorY*2;
		
				
		}
		return target;
	}

}
