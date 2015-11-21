package game.controllers.ghosts;

import game.core.Game;

public abstract class GhostsControllerBase implements IGhostsController {

	protected int ghostCount;
	
	protected GhostsActions input;	

	public GhostsControllerBase(int ghostCount) {
		this.ghostCount = ghostCount;
		if (this.ghostCount < 0) ghostCount = 0;
		if (this.ghostCount > 4) ghostCount = 4;
		input = new GhostsActions(ghostCount);
	}
	
	@Override
	public int getGhostCount() {
		return ghostCount;
	}
	
	@Override
	public void reset(Game game) {
		input.reset();
	}
	
	@Override
	public void nextLevel(Game game) {		
	}

	@Override
	public abstract void tick(Game game, long timeDue);

	@Override
	public GhostsActions getActions() {
		return input;
	}
	
	
	
}
