package cz.cuni.mff.amis.pacman.tournament;

import game.SimulatorConfig;
import game.controllers.pacman.IPacManController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

import cz.cuni.mff.amis.pacman.tournament.run.PacManResults;
import cz.cuni.mff.amis.pacman.tournament.run.PacManRun;
import cz.cuni.mff.amis.pacman.tournament.run.PacManRunResult;
import cz.cuni.mff.amis.pacman.tournament.run.PacManRunsGenerator;
import cz.cuni.mff.amis.pacman.tournament.utils.Sanitize;

public class EvaluateAgent {
	
	private int seed = 0;

	private SimulatorConfig prototypeConfig;
	
	private int runCount;
	
	private int oneRunRepetitions;
	
	private File resultDirFile;
	
	public EvaluateAgent(int seed, SimulatorConfig prototypeConfig, int runCount, int oneRunRepetitions, File resultDirFile) {
		this.seed = seed;
		this.prototypeConfig = prototypeConfig;
		this.runCount = runCount;
		this.oneRunRepetitions = oneRunRepetitions;
		this.resultDirFile = resultDirFile;
	}
	
	private void log(String agentId, String msg) {
		System.out.println("[" + agentId + "] " + msg);
	}
	
	public void evaluateAgent(String agentId, String agentFQCN) {
		agentId = Sanitize.idify(agentId);
		
		log(agentId, "EVALUATING AGENT IN " + runCount + " RUNS with " + oneRunRepetitions + " repetition, TOTAL " + (runCount * oneRunRepetitions) + " SIMULATIONS!");
		
		PacManRun[] runs = PacManRunsGenerator.generateRunList(seed, prototypeConfig, runCount, oneRunRepetitions);
		
		PacManResults results = new PacManResults();
		
		resultDirFile.mkdirs();
		File replayDir = new File(resultDirFile, "replays");
		replayDir.mkdirs();
						
		for (int i = 0; i < runs.length; ++i) {
			long start = System.currentTimeMillis();
			
			log(agentId, "RUN " + (i+1) + " / " + runs.length + " (" + oneRunRepetitions + " repetitions)");
			
			if (runs[i].getConfig().config.replay) {
				if (runs[i].getConfig().config.replayFile == null) {
					runs[i].getConfig().config.replayFile = new File(replayDir, agentId + "-Run-" + i + ".replay");
				} else {
					String file = runs[i].getConfig().config.replayFile.getName();
					int index = file.lastIndexOf(".");
					String newFile = file.substring(0, index) + "-Run-" + i + "." + file.substring(index+1);
					runs[i].getConfig().config.replayFile = new File(runs[i].getConfig().config.replayFile.getParentFile(), newFile);
				}
			}
			
			PacManRunResult result = runs[i].run(agentFQCN);
			
			log(agentId, "LEVEL " + (i+1) + " / " + runs.length + " SIMULATIONS FINISHED: " + result.toString());
			
			results.addRunResults(result);
			
			log(agentId, "TIME: " + (System.currentTimeMillis() - start) + "ms");
		}
		
		log(agentId, "EVALUATION FINISHED!");
		log(agentId, results.toString());
		
		outputResults(agentId, results);		
	}

	private void outputResults(String agentId, PacManResults results) {		
		resultDirFile.mkdirs();
		
		//outputAgentResults(agentId, results);
		outputAgentAvgs(agentId, results);
		outputAgentGlobalAvgs(agentId, results);
	}

	
//	private void outputAgentResults(String agentId, PacManResults results) {
//		File file = new File(resultDirFile, agentId + ".runs.csv");
//		System.out.println("[" + agentId + "] Outputing runs into: " + file.getAbsolutePath());
//		
//		PrintWriter writer = null;
//		try {
//			writer = new PrintWriter(new FileOutputStream(file));
//			
//			writer.println("agentId;configNumber;simulationNumber;" + results.getRunResults().get(0).getCSVHeader());
//			int simulationNumber = 0;
//			int configNumber = 0;
//			for (PacManConfig config : results.getConfigs()) {
//				++configNumber;
//				for (int i = 0; i < config.repetitions; ++i) {
//					++simulationNumber;
//					writer.print(agentId);
//					writer.print(";" + configNumber);
//					writer.print(";" + simulationNumber);
//					PacManRunResult info = results.getRunResults().get(simulationNumber-1);
//					writer.println(";" + info.getCSV());
//				}
//			}
//			
//		} catch (Exception e) {
//			throw new RuntimeException("Failed to write results into: " + file.getAbsolutePath());
//		} finally {
//			if (writer != null) writer.close();
//		}
//		
//	}
	
	private void outputAgentAvgs(String agentId, PacManResults results) {
		File file = new File(resultDirFile, agentId + ".runs.avgs.csv");
		System.out.println("[" + agentId + "] Outputing runs avgs into: " + file.getAbsolutePath());
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(file));
			
			writer.println("agentId;runNumber;" + results.getRunResults().get(0).getCSVHeader());
			int configNumber = 0;
			for (PacManRunResult run : results.getRunResults()) {
				++configNumber;
				writer.print(agentId);
				writer.print(";" + configNumber);
				writer.println(";" + run.getCSV());				
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to write results into: " + file.getAbsolutePath());
		} finally {
			if (writer != null) writer.close();
		}
	}
	
	private void outputAgentGlobalAvgs(String agentId, PacManResults results) {
		File file = new File(resultDirFile, "results.csv");		
		System.out.println("[" + agentId + "] Outputing total avgs into: " + file.getAbsolutePath());
		
		PrintWriter writer = null;
		try {
			boolean outputHeaders = !file.exists();
			writer = new PrintWriter(new FileOutputStream(file, true));
			if (outputHeaders) {
				writer.println("agentId;configSeed;" + results.getCSVHeader());
			}
			writer.print(agentId + ";");
			writer.print(seed + ";");
			writer.println(results.getCSV());
		} catch (Exception e) {
			throw new RuntimeException("Failed to write results into: " + file.getAbsolutePath());
		} finally {
			if (writer != null) writer.close();
		}
	}
	
	



}
