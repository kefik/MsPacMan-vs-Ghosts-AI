package game;

import game.PacManSimulator.GameConfig;
import game.controllers.ghosts.IGhostsController;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.IPacManController;

import java.io.File;

public class SimulatorConfig {

	public GameConfig game = new GameConfig();
	
	public boolean visualize = true;
	public boolean visualizationScale2x = true;
	
	public boolean mayBePaused = true;
	
	public IPacManController pacManController;
	public IGhostsController ghostsController;
	
	/**
	 * How long can PacMan / Ghost controller think about the game before we compute next frame.
	 * If {@ #visualize} than it also determines the speed of the game.
	 * 
	 * DEFAULT: 25 FPS
	 */
	public int thinkTimeMillis = 40;
	
	public boolean replay = false;
	public File replayFile = null;
	
	public SimulatorConfig clone() {
		SimulatorConfig result = new SimulatorConfig();
		
		result.game = game.clone();
		
		result.visualize = visualize;
		result.visualizationScale2x = visualizationScale2x;
		
		result.mayBePaused = mayBePaused;
		
		result.pacManController = pacManController;
		result.ghostsController = ghostsController;
		
		result.thinkTimeMillis = thinkTimeMillis;
				
		result.replay = replay;
		result.replayFile = replayFile;
		
		return result;
	}

	public String getCSVHeader() {
		return game.getCSVHeader() + ";thinkTimeMillis;visualize;visualizeScale2x;mayBePaused;replay;replayFile";
	}
	
	public String getCSV() {
		return game.getCSV() + ";" + thinkTimeMillis + ";" + visualize + ";" + visualizationScale2x + ";" + mayBePaused + ";" + replay + ";" + (replayFile == null ? null : replayFile.getAbsolutePath());
	}
	
	public String getOptions() {
		return   SimulatorConfigOption.GAME_LEVELS_TO_PLAY.option + " " + game.levelsToPlay 
			   + " " + SimulatorConfigOption.GAME_POWER_PILLS.option + " " + game.powerPillsEnabled
			   + " " + SimulatorConfigOption.GAME_SEED.option + " " + game.seed
			   + " " + SimulatorConfigOption.GAME_TOTAL_PILLS.option + " " + game.totalPills
			   + " " + SimulatorConfigOption.GAME_GHOST_COUNT.option + " " + (ghostsController == null ? "N/A" : ghostsController.getGhostCount())
			   + " " + SimulatorConfigOption.SIM_CAN_BE_PAUSED.option + " " + mayBePaused
			   + " " + SimulatorConfigOption.SIM_REPLAY.option + " " + replay
			   + (replayFile != null ? " " + SimulatorConfigOption.SIM_REPLAY_FILE.option + " " + replayFile.getAbsolutePath() : "")
			   + " " + SimulatorConfigOption.SIM_THINK_TIME_MILLIS.option + " " + thinkTimeMillis
			   + " " + SimulatorConfigOption.SIM_VIS_SCALE_2X.option + " " + visualizationScale2x
			   + " " + SimulatorConfigOption.SIM_VISUALIZE.option + " " + visualize;
	}
	
	public static SimulatorConfig fromOptions(String options) {
		String[] parts = options.split(" ");
		
		SimulatorConfig result = new SimulatorConfig();
		
		for (int i = 0; i < parts.length; i += 2) {
			String option = parts[i];
			String value = (i+1 < parts.length ? parts[i+1] : null);
			
			if (option.equals(SimulatorConfigOption.GAME_LEVELS_TO_PLAY.option)) {
				result.game.levelsToPlay = Integer.parseInt(value);				
			}
			if (option.equals(SimulatorConfigOption.GAME_POWER_PILLS.option)) {
				result.game.powerPillsEnabled = Boolean.parseBoolean(value);
			}
			if (option.equals(SimulatorConfigOption.GAME_SEED.option)) {
				result.game.seed = Integer.parseInt(value);
			}
			if (option.equals(SimulatorConfigOption.GAME_TOTAL_PILLS.option)) {
				result.game.totalPills = Double.parseDouble(value);
			}
			if (option.equals(SimulatorConfigOption.GAME_GHOST_COUNT.option)) {
				int ghostCount = Integer.parseInt(value);
				result.ghostsController = new GameGhosts(ghostCount);
			}
			if (option.equals(SimulatorConfigOption.SIM_CAN_BE_PAUSED.option)) {
				result.mayBePaused = Boolean.parseBoolean(value);
			}
			if (option.equals(SimulatorConfigOption.SIM_REPLAY.option)) {
				result.replay = Boolean.parseBoolean(value);
			}
			if (option.equals(SimulatorConfigOption.SIM_REPLAY_FILE.option)) {
				result.replayFile = new File(value);
			}
			if (option.equals(SimulatorConfigOption.SIM_THINK_TIME_MILLIS.option)) {
				result.thinkTimeMillis = Integer.parseInt(value);
			}
			if (option.equals(SimulatorConfigOption.SIM_VIS_SCALE_2X.option)) {
				result.visualizationScale2x = Boolean.parseBoolean(value);
			}
			if (option.equals(SimulatorConfigOption.SIM_VISUALIZE.option)) {
				result.visualize = Boolean.parseBoolean(value);
			}						
		}
		
		return result;
	}
	
}
