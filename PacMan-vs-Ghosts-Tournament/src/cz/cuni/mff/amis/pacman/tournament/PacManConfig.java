package cz.cuni.mff.amis.pacman.tournament;

import game.SimulatorConfig;

public class PacManConfig {
	
	public SimulatorConfig config;
	
	public int repetitions;

	@Override
	public PacManConfig clone() {
		PacManConfig result = new PacManConfig();
		
		result.config = config.clone();
		result.repetitions = repetitions;
		
		return result;
	}
	
	public String getCSVHeader() {
		return config.getCSVHeader() + ";repetitions";
	}
	
	public String getCSV() {
		return config.getCSV() + ";" + repetitions;
	}
	

}
