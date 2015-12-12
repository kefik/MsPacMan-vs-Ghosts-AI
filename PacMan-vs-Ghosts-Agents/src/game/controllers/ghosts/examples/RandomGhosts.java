package game.controllers.ghosts.examples;

import game.controllers.ghosts.GhostsControllerBase;
import game.core.G;
import game.core.Game;

public final class RandomGhosts extends GhostsControllerBase
{	
	
	public RandomGhosts() {
		super(Game.NUM_GHOSTS);
	}

	@Override
	public void tick(Game game, long timeDue) {
		int[] directions=new int[Game.NUM_GHOSTS];
		
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i=0;i<directions.length;i++)
			if(game.ghostRequiresAction(i))
			{			
				int[] possibleDirs=game.getPossibleGhostDirs(i);			
				directions[i]=possibleDirs[G.rnd.nextInt(possibleDirs.length)];
			}
		
		input.set(directions);
	}
}