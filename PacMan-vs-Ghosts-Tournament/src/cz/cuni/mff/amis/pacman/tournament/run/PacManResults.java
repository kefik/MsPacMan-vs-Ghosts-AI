package cz.cuni.mff.amis.pacman.tournament.run;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.amis.pacman.tournament.EvaluationInfos;
import cz.cuni.mff.amis.pacman.tournament.PacManConfig;

public class PacManResults extends EvaluationInfos {
	
	private List<PacManConfig> configs = new ArrayList<PacManConfig>();
	private List<PacManRunResult> runResults = new ArrayList<PacManRunResult>();
	
	public void addRunResults(PacManRunResult... results) {
		for (PacManRunResult result : results) {
			runResults.add(result);
			configs.add(result.getConfig());
			addResults(result.getResults());
		}
	}
	
	public void addRunResults(List<PacManRunResult> results) {
		for (PacManRunResult result : results) {
			runResults.add(result);
			configs.add(result.getConfig());
			addResults(result.getResults());
		}
	}

	public List<PacManRunResult> getRunResults() {
		return runResults;
	}

	public List<PacManConfig> getConfigs() {
		return configs;
	}
	
}
