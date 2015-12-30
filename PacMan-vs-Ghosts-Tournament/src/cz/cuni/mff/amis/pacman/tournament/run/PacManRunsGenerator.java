package cz.cuni.mff.amis.pacman.tournament.run;

import game.SimulatorConfig;

import java.util.Random;

import cz.cuni.mff.amis.pacman.tournament.PacManConfig;

public class PacManRunsGenerator {
	
	public static int[] generateSeeds(int randomSeed, int count) {
		Random random = new Random(randomSeed);
		int[] seeds = new int[count];
		
		for (int i = 0; i < count; ++i) {
			seeds[i] = random.nextInt();
			while (seeds[i] <= 0) {
				seeds[i] += Integer.MAX_VALUE;
			}			
		}
		
		return seeds;
	}
	
	public static PacManConfig[] generateConfigs(int randomSeed, SimulatorConfig prototypeOptions, int runCount, int oneRunRepetitions) {
		
		int[] seeds = generateSeeds(randomSeed, runCount);
				
		PacManConfig[] configs = new PacManConfig[runCount];
		
		for (int i = 0; i < runCount; ++i) {
			SimulatorConfig config = prototypeOptions.clone();
			
			config.game.seed = seeds[i];
						
			PacManConfig result = new PacManConfig();
			
			result.config = config;
			result.repetitions = oneRunRepetitions;
			
			configs[i] = result;
		}
		
		return configs;
	}
	
	public static PacManRun[] generateRunList(int randomSeed, SimulatorConfig prototypeOptions, int runCount, int oneRunRepetitions) {
		PacManConfig[] configs = generateConfigs(randomSeed, prototypeOptions, runCount, oneRunRepetitions);
		PacManRun[] runs = new PacManRun[runCount];
		for (int i = 0; i < runCount; ++i) {
			runs[i] = new PacManRun(configs[i]);
		}
		return runs;
	}
	
	public static PacManRuns generateRuns(int randomSeed, SimulatorConfig prototypeOptions, int runCount, int oneRunRepetitions) {
		PacManConfig[] configs = generateConfigs(randomSeed, prototypeOptions, runCount, oneRunRepetitions);
		return new PacManRuns(configs);
	}

}
