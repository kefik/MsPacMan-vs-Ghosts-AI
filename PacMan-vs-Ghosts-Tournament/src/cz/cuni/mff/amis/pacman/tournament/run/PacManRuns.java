package cz.cuni.mff.amis.pacman.tournament.run;

import cz.cuni.mff.amis.pacman.tournament.PacManConfig;
import game.controllers.pacman.IPacManController;

public class PacManRuns {
	
	public PacManConfig[] configs;
	
	public PacManRuns(PacManConfig[] configs) {
		this.configs = configs;
	}
	
	public synchronized PacManResults run(IPacManController pacMan) {
		PacManResults results = new PacManResults();
		for (PacManConfig config : configs) {
			PacManRun run = new PacManRun(config);
			PacManRunResult result = run.run(pacMan);
			results.addRunResults(result);
		}
		return results;
	}

}
