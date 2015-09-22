package game.controllers.pacman;

import game.controllers.EntityAction;

public final class PacManAction extends EntityAction {

	public boolean pauseSimulation;
	public boolean nextFrame;
	
	public void pause() {
		pauseSimulation = true;
	}
	
	public void resume() {
		pauseSimulation = false;
	}
	
	public void togglePause() {
		pauseSimulation = !pauseSimulation;
	}
	
	public void reset() {
		super.reset();
		pauseSimulation = false;
	}	
	
	public PacManAction clone() {
		PacManAction result = new PacManAction();
		
		result.direction = direction;
		result.pauseSimulation = pauseSimulation;
		result.nextFrame = nextFrame;			
		
		return result;
	}
	
	
}
