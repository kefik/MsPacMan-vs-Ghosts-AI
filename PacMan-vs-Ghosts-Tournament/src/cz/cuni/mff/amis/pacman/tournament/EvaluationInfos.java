package cz.cuni.mff.amis.pacman.tournament;

import game.core.Game;

import java.util.ArrayList;
import java.util.List;

public class EvaluationInfos {
	
	private List<Game> results = new ArrayList<Game>();
	
	public int totalVictories;
	public double avgVictories;
	public int totalDeaths;
	public double avgDeaths;
	public int totalScore;
	public double avgScore;
	
	public int totalTimeSpent;
	public double avgTimeSpent;
		
	public List<Game> getResults() {
		return results;
	}

	public void addResult(Game result) {
		if (result.getLivesRemaining() > 0) {
			++totalVictories;
		} else {
			++totalDeaths;
		}
		totalScore     += result.getScore();		
		totalTimeSpent += result.getTotalTime();
		
		results.add(result);
		
		avgVictories = ((double)totalVictories) / ((double)results.size());
		avgDeaths    = ((double)totalDeaths)    / ((double)results.size());
		avgScore     = ((double)totalScore)     / ((double)results.size());		
		avgTimeSpent = ((double)totalTimeSpent) / ((double)results.size());
	}
	
	public void addResults(Game... results) {
		for (Game info : results) {
			addResult(info);
		}
	}
	
	public void addResults(List<Game> results) {
		for (Game info : results) {
			addResult(info);
		}
	}
	
	public String getCSVHeader() {
		return "resultCount;totalVictories;avgVictories;totalDeaths;avgDeaths;totalScore;avgScore;totalTimeSpent;avgTimeSpent";
	}
	
	public String getCSV() {
		return results.size() 
			   + ";" + totalVictories + ";" + avgVictories
			   + ";" + totalDeaths    + ";" + avgDeaths
			   + ";" + totalScore     + ";" + avgScore
			   + ";" + totalTimeSpent + ";" + avgTimeSpent;		
	}
	
	@Override
	public String toString() {
		return "EvaluationInfos[RUNS=" + results.size() + ", AVGS: WIN=" + avgVictories + ", DEATH=" + avgDeaths + ", SCORE=" + avgScore + ", TIME=" + avgTimeSpent + "]";
	}
	
}
