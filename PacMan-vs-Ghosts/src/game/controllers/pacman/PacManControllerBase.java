package game.controllers.pacman;

import game.controllers.pacman.modules.Maze;
import game.core.Game;

public abstract class PacManControllerBase implements IPacManController {

	protected PacManAction pacman = new PacManAction();	
	
	protected Game game;
	
	protected Maze maze = new Maze();
	
	@Override
	public void reset(Game game) {
		pacman.reset();
		this.game = game;
		maze.reset(game);
	}
	
	@Override
	public void nextLevel(Game game) {
		maze.reset(game);
	}
	
	@Override
	public abstract void tick(Game game, long timeDue);
	
	@Override
	public void killed() {
	}

	@Override
	public PacManAction getAction() {
		return pacman;
	}

}
