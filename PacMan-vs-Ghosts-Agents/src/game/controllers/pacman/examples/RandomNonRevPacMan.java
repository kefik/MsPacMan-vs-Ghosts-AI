package game.controllers.pacman.examples;

import game.PacManSimulator;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.PacManHijackController;
import game.core.G;
import game.core.Game;

public final class RandomNonRevPacMan extends PacManHijackController
{	
	@Override
	public void tick(Game game, long timeDue) {
		int[] directions=game.getPossiblePacManDirs(false);		//set flag as false to prevent reversals	
		pacman.set(directions[G.rnd.nextInt(directions.length)]);		
	}
		
	public static void main(String[] args) {
		PacManSimulator.play(new RandomNonRevPacMan(), new GameGhosts(false));
	}
}