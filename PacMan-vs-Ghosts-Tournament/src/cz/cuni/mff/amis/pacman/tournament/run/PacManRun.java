package cz.cuni.mff.amis.pacman.tournament.run;

import game.PacManSimulator;
import game.SimulatorConfig;
import game.controllers.pacman.IPacManController;
import game.core.Game;

import java.io.File;
import java.lang.reflect.Constructor;

import cz.cuni.mff.amis.pacman.tournament.PacManConfig;

public class PacManRun {
	
	private PacManConfig config;
	
	private File origReplayFile;

	public PacManRun(PacManConfig config) {
		this.config = config;
	}
	
	/**
	 * TODO: param should be PacMan provider/factory really... 
	 * @param pacManFQCN
	 * @return
	 */
	public synchronized PacManRunResult run(String pacManFQCN) {		
		SimulatorConfig simulatorConfig = config.config;
		if (simulatorConfig.replay) {
			origReplayFile = simulatorConfig.replayFile;
		}
		
		PacManRunResult result = new PacManRunResult(config);
		
		for (int i = 0; i < config.repetitions; ++i) {
			
			System.out.println("ITERATION " + (i+1) + " / " + config.repetitions);
			
			IPacManController pacMan = constructAgent(pacManFQCN);	
			simulatorConfig.pacManController = pacMan;
			
			if (simulatorConfig.replay) {
				String file = origReplayFile.getName();
				int index = file.lastIndexOf(".");
				String newFile = file.substring(0, index) + "-Iter-" + i + "." + file.substring(index+1);
				simulatorConfig.replayFile = new File(origReplayFile.getParentFile(), newFile);
			}
			
			PacManSimulator simulator = new PacManSimulator();
			
			Game info = simulator.play(simulatorConfig);
			result.addResult(info);
			
			System.out.println("GAME FINISHED - Score: " + info.getScore() + ", Time: " + info.getTotalTime() + ", " + (info.getLivesRemaining() > 0 ? "WIN" : "LOSE"));
		}
		return result;		
	}

	public PacManConfig getConfig() {
		return config;
	}
	
	private IPacManController constructAgent(String pacManFQCN) {
		try {
			Class agentClass = Class.forName(pacManFQCN);
			Constructor agentCtor = agentCtor = agentClass.getConstructor();
			return (IPacManController) agentCtor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to instantiate PacMan agent: " + pacManFQCN, e);
		}
	}
	
}
