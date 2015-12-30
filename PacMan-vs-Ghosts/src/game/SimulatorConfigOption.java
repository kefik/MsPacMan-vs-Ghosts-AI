package game;

public enum SimulatorConfigOption {

	GAME_SEED("-s"),
	
	GAME_POWER_PILLS("-pp"),
	
	GAME_TOTAL_PILLS("-tp"),
	
	GAME_LEVELS_TO_PLAY("-lc"),
	
	GAME_GHOST_COUNT("-gc"),
	
	SIM_VISUALIZE("-v"),
	
	SIM_VIS_SCALE_2X("-2x"),
	
	SIM_CAN_BE_PAUSED("-p"),
	
	SIM_THINK_TIME_MILLIS("-tt"),
	
	SIM_REPLAY("-r"),
	
	SIM_REPLAY_FILE("-rf");
	
	public final String option;
	
	private SimulatorConfigOption(String option) {
		this.option = option;
	}

}
