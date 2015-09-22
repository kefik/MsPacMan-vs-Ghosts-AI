package game.controllers.ghosts;

import game.core.Game;

public abstract class GhostsControllerBase implements IGhostsController {

	protected GhostsActions input = new GhostsActions();

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
