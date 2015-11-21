package game.controllers.ghosts;

public final class GhostsActions {
	
	private static GhostAction NONE;
	
	public GhostAction[] actions = new GhostAction[4];
	
	public final int ghostCount;	
	
	public boolean pauseSimulation;
	public boolean nextFrame;
	
	public GhostsActions(int ghostCount) {
		this.ghostCount = ghostCount;
		for (int i = 0; i < actions.length; ++i) {
			actions[i] = new GhostAction();
		}
	}
	
	public GhostAction ghost(int index) {
		if (index < 0 || index > actions.length) return NONE;
		return actions[index];
	}
	
	public void set(int[] directions) {
		for (int i = 0; i < directions.length && i < actions.length; ++i) {
			actions[i].set(directions[i]);
		}
	}
	
	public GhostAction blinky() {
		return actions[GhostType.BLINKY.index];
	}
	
	public GhostAction pinky() {
		return actions[GhostType.PINKY.index];
	}
	
	public GhostAction clyde() {
		return actions[GhostType.CLYDE.index];
	}
	
	public GhostAction inky() {
		return actions[GhostType.INKY.index];
	}
	
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
		for (int i = 0; i < actions.length; ++i) {
			actions[i].reset();
		}
		pauseSimulation = false;
		nextFrame = false;
	}		
	
	public GhostsActions clone() {
		GhostsActions result = new GhostsActions(ghostCount);
		
		for (int i = 0; i < actions.length; ++i) {
			result.actions[i] = actions[i].clone();
		}
		result.pauseSimulation = pauseSimulation;
		result.nextFrame = nextFrame;
		
		return result;
	}

	

}
