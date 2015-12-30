package cz.cuni.mff.amis.pacman.tournament;

import game.SimulatorConfig;
import game.controllers.pacman.IPacManController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import cz.cuni.mff.amis.pacman.tournament.run.PacManResults;
import cz.cuni.mff.amis.pacman.tournament.run.PacManRun;
import cz.cuni.mff.amis.pacman.tournament.run.PacManRunResult;
import cz.cuni.mff.amis.pacman.tournament.run.PacManRunsGenerator;
import cz.cuni.mff.amis.pacman.tournament.utils.Sanitize;

public class EvaluateAgent {
	
	private int seed = 0;

	private SimulatorConfig prototypeConfig;
	
	private int runCount;
	
	private int oneLevelRepetitions;
	
	private File resultDirFile;
	
	public EvaluateAgent(int seed, SimulatorConfig prototypeConfig, int runCount, int oneLevelRepetitions, File resultDirFile) {
		this.seed = seed;
		this.prototypeConfig = prototypeConfig;
		this.runCount = runCount;
		this.oneLevelRepetitions = oneLevelRepetitions;
		this.resultDirFile = resultDirFile;
	}
	
	private void log(String agentId, String msg) {
		System.out.println("[" + agentId + "] " + msg);
	}
	
	public void evaluateAgent(String agentId, IPacManController agent) {
		agentId = Sanitize.idify(agentId);
		
		log(agentId, "EVALUATING AGENT IN " + runCount + " LEVELS with " + oneLevelRepetitions + " level-repetition, TOTAL " + (runCount * oneLevelRepetitions) + " SIMULATIONS!");
		
		PacManRun[] runs = PacManRunsGenerator.generateRunList(seed, prototypeConfig, runCount, oneLevelRepetitions);
		
		PacManResults results = new PacManResults();
		
		for (int i = 0; i < runs.length; ++i) {
			log(agentId, "LEVEL " + (i+1) + " / " + runs.length + " (" + oneLevelRepetitions + " repetitions)");
			
			PacManRunResult result = runs[i].run(agent);
			
			log(agentId, "LEVEL " + (i+1) + " / " + runs.length + " SIMULATIONS FINISHED: " + result.toString());
			
			results.addRunResults(result);			
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
			
			writer.println("agentId;configNumber;" + results.getRunResults().get(0).getCSVHeader());
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
