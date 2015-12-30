package cz.cuni.mff.amis.pacman.tournament.run;

import game.PacManSimulator;
import game.SimulatorConfig;
import game.controllers.pacman.IPacManController;
import game.core.Game;

import java.io.File;

import cz.cuni.mff.amis.pacman.tournament.PacManConfig;

public class PacManRun {
	
	private PacManConfig config;
	
	private File origReplayFile;

	public PacManRun(PacManConfig config) {
		this.config = config;
	}
	
	public synchronized PacManRunResult run(IPacManController pacMan) {
		SimulatorConfig simulatorConfig = config.config;
		if (simulatorConfig.replay) {
			origReplayFile = simulatorConfig.replayFile;
		}
		
		PacManRunResult result = new PacManRunResult(config);
		
		for (int i = 0; i < config.repetitions; ++i) {
			simulatorConfig.pacManController = pacMan;
			
			if (simulatorConfig.replay) {
				String file =origReplayFile.getName();
				int index = file.lastIndexOf(".");
				String newFile = file.substring(0, index) + "-Iter-" + i + "." + file.substring(index+1);
				simulatorConfig.replayFile = new File(origReplayFile.getParentFile(), newFile);
			}
			
			PacManSimulator simulator = new PacManSimulator();
			
			Game info = simulator.play(simulatorConfig);
			result.addResult(info);
		}
		return result;		
	}

	public PacManConfig getConfig() {
		return config;
	}
	
}
