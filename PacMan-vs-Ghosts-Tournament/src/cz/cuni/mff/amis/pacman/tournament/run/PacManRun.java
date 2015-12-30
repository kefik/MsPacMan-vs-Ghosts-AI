package cz.cuni.mff.amis.pacman.tournament.run;

import cz.cuni.mff.amis.pacman.tournament.PacManConfig;
import game.PacManSimulator;
import game.SimulatorConfig;
import game.controllers.pacman.IPacManController;
import game.core.Game;

public class PacManRun {
	
	private PacManConfig config;

	public PacManRun(PacManConfig config) {
		this.config = config;
	}
	
	public synchronized PacManRunResult run(IPacManController pacMan) {
		PacManRunResult result = new PacManRunResult(config);
		SimulatorConfig simulatorConfig = config.config;
		for (int i = 0; i < config.repetitions; ++i) {
			simulatorConfig.pacManController = pacMan;
			PacManSimulator simulator = new PacManSimulator();
			Game info = simulator.play(simulatorConfig);
			result.addResult(info);
		}
		return result;		
	}

}
