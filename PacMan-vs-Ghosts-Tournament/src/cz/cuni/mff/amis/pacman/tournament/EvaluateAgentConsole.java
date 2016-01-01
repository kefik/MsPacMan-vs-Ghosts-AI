package cz.cuni.mff.amis.pacman.tournament;

import game.SimulatorConfig;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.IPacManController;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Iterator;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

public class EvaluateAgentConsole {
	
	private static final char ARG_SEED_SHORT = 's';
	
	private static final String ARG_SEED_LONG = "seed";
	
	private static final char ARG_SIMULATOR_OPTIONS_SHORT = 'o';
	
	private static final String ARG_SIMULATOR_OPTIONS_LONG = "simulator-options";
	
	private static final char ARG_RUNS_COUNT_SHORT = 'c';
	
	private static final String ARG_RUNS_COUNT_LONG = "runs-count";
	
	private static final char ARG_ONE_RUN_REPETITIONS_SHORT = 'r';
	
	private static final String ARG_ONE_RUN_REPETITIONS_LONG = "one-run-repetitions";
	
	private static final char ARG_AGENT_FQCN_SHORT = 'p';
	
	private static final String ARG_AGENT_FQCN_LONG = "pacman-fqcn";
	
	private static final char ARG_AGENT_ID_SHORT = 'i';
	
	private static final String ARG_AGNET_ID_LONG = "agent-id";
	
	private static final char ARG_RESULT_DIR_SHORT = 'd';
	
	private static final String ARG_RESULT_DIR_LONG = "result-dir";
	
	private static JSAP jsap;

	private static int seed = 0;

	private static String simulatorOptions;
	
	private static int runCount;
	
	private static int oneLevelRepetitions;
	
	private static String agentFQCN;
	
	private static Class<?> agentClass;
	
	private static IPacManController agent;
	
	private static String agentId;
	
	private static String resultDir;
	
	private static File resultDirFile;

	private static boolean headerOutput = false;

	private static JSAPResult config;

		
	
	private static void fail(String errorMessage) {
		fail(errorMessage, null);
	}

	private static void fail(String errorMessage, Throwable e) {
		header();
		System.out.println("ERROR: " + errorMessage);
		System.out.println();
		if (e != null) {
			e.printStackTrace();
			System.out.println("");
		}		
        System.out.println("Usage: java -jar pacman-vs-ghosts-tournament.jar ");
        System.out.println("                " + jsap.getUsage());
        System.out.println();
        System.out.println(jsap.getHelp());
        System.out.println();
        throw new RuntimeException("FAILURE: " + errorMessage);
	}

	private static void header() {
		if (headerOutput) return;
		System.out.println();
		System.out.println("================================");
		System.out.println("PacMan-vs-Ghosts Agent Evaluator");
		System.out.println("================================");
		System.out.println();
		headerOutput = true;
	}
		
	private static void initJSAP() throws JSAPException {
		jsap = new JSAP();
		    	
    	FlaggedOption opt1 = new FlaggedOption(ARG_AGENT_FQCN_LONG)
        	.setStringParser(JSAP.STRING_PARSER)
        	.setRequired(true) 
        	.setShortFlag(ARG_AGENT_FQCN_SHORT)
        	.setLongFlag(ARG_AGENT_FQCN_LONG);    
        opt1.setHelp("Agent fully-qualified class name to evaluate. Must be present on classpath.");
        
        jsap.registerParameter(opt1);
        
        FlaggedOption opt2 = new FlaggedOption(ARG_AGNET_ID_LONG)
	    	.setStringParser(JSAP.STRING_PARSER)
	    	.setRequired(true) 
	    	.setShortFlag(ARG_AGENT_ID_SHORT)
	    	.setLongFlag(ARG_AGNET_ID_LONG);    
	    opt2.setHelp("Id of the agent that will serve as identificator within results file.");
	
	    jsap.registerParameter(opt2);
	    
	    FlaggedOption opt3 = new FlaggedOption(ARG_ONE_RUN_REPETITIONS_LONG)
	    	.setStringParser(JSAP.INTEGER_PARSER)
	    	.setRequired(false)
	    	.setDefault("10")
	    	.setShortFlag(ARG_ONE_RUN_REPETITIONS_SHORT)
	    	.setLongFlag(ARG_ONE_RUN_REPETITIONS_LONG);    
	    opt3.setHelp("How many times should be one PacMan game configuration be repeated (in order to gain statistically sound data).");
	    
	    jsap.registerParameter(opt3);
	    
	    FlaggedOption opt31 = new FlaggedOption(ARG_SIMULATOR_OPTIONS_LONG)
	    	.setStringParser(JSAP.STRING_PARSER)
	    	.setRequired(true) 
	    	.setShortFlag(ARG_SIMULATOR_OPTIONS_SHORT)
	    	.setLongFlag(ARG_SIMULATOR_OPTIONS_LONG);    
	    opt31.setHelp("List of simulator options.");
	
	    jsap.registerParameter(opt31);
	    
	    FlaggedOption opt32 = new FlaggedOption(ARG_RESULT_DIR_LONG)
	    	.setStringParser(JSAP.STRING_PARSER)
	    	.setRequired(false)
	    	.setDefault("./results")
	    	.setShortFlag(ARG_RESULT_DIR_SHORT)
	    	.setLongFlag(ARG_RESULT_DIR_LONG);    
	    opt32.setHelp("Directory where to output results, will be created if not exist.");
	    
	    jsap.registerParameter(opt32);
	    
	    FlaggedOption opt33 = new FlaggedOption(ARG_RUNS_COUNT_LONG)
	    	.setStringParser(JSAP.INTEGER_PARSER)
	    	.setRequired(false)
	    	.setDefault("100")
	    	.setShortFlag(ARG_RUNS_COUNT_SHORT)
	    	.setLongFlag(ARG_RUNS_COUNT_LONG);    
	    opt33.setHelp("How many different levels should an agent be evaluated in.");
	
	    jsap.registerParameter(opt33);
    
	    FlaggedOption opt6 = new FlaggedOption(ARG_SEED_LONG)
	    	.setStringParser(JSAP.INTEGER_PARSER)
	    	.setRequired(false)
	    	.setDefault("0")
	    	.setShortFlag(ARG_SEED_SHORT)
	    	.setLongFlag(ARG_SEED_LONG);    
	    opt6.setHelp("Seed to be used when generating seeds for respective levels.");
	
	    jsap.registerParameter(opt6);
   	}

	private static void readConfig(String[] args) {
		System.out.println("Parsing command arguments.");
		
		try {
	    	config = jsap.parse(args);
	    } catch (Exception e) {
	    	fail(e.getMessage());
	    	System.out.println("");
	    	e.printStackTrace();
	    	throw new RuntimeException("FAILURE!");
	    }
		
		if (!config.success()) {
			String error = "Invalid arguments specified.";
			Iterator errorIter = config.getErrorMessageIterator();
			if (!errorIter.hasNext()) {
				error += "\n-- No details given.";
			} else {
				while (errorIter.hasNext()) {
					error += "\n-- " + errorIter.next();
				}
			}
			fail(error);
    	}

		seed = config.getInt(ARG_SEED_LONG);

		simulatorOptions = config.getString(ARG_SIMULATOR_OPTIONS_LONG);
		
		runCount = config.getInt(ARG_RUNS_COUNT_LONG);
		
		oneLevelRepetitions = config.getInt(ARG_ONE_RUN_REPETITIONS_LONG);
		
		agentFQCN = config.getString(ARG_AGENT_FQCN_LONG);
		
		agentId = config.getString(ARG_AGNET_ID_LONG);
		
		resultDir = config.getString(ARG_RESULT_DIR_LONG);
	    
	}
	
	private static void sanityChecks() {
		System.out.println("Sanity checks...");
		
		System.out.println("-- seed: " + seed);
		System.out.println("-- level options: " + simulatorOptions);
		System.out.println("-- run count: " + runCount);
		System.out.println("-- single level repetitions: " + oneLevelRepetitions);
		
		resultDirFile = new File(resultDir);
		System.out.println("-- result dir: " + resultDir + " --> " + resultDirFile.getAbsolutePath());
		
		if (!resultDirFile.exists()) {
			System.out.println("---- result dir does not exist, creating!");
			resultDirFile.mkdirs();
		}
		if (!resultDirFile.exists()) {
			fail("Result dir does not exists. Parsed as: " + resultDir + " --> " + resultDirFile.getAbsolutePath());
		}
		if (!resultDirFile.isDirectory()) {
			fail("Result dir is not a directory. Parsed as: " + resultDir + " --> " + resultDirFile.getAbsolutePath());
		}
		System.out.println("---- result directory exists, ok");
		
		System.out.println("-- resolving agent FQCN: " + agentFQCN);
		try {
			agentClass = Class.forName(agentFQCN);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (agentClass == null) {
			fail("Failed to find agent class: " + agentFQCN);
		}
		System.out.println("---- agent class found");
		Constructor agentCtor = null;
		try {
			agentCtor = agentClass.getConstructor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (agentCtor == null) {
			fail("Failed to locate parameterless constructor for agent class: " + agentClass.getName());
		}
		System.out.println("---- agent parameterless constructor found");
		try {
			agent = (IPacManController) agentCtor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (agent == null) {
			fail("Failed to construct the agent instance.");
		}
		
		System.out.println("---- agent instantiated");
		
	    System.out.println("Sanity checks OK!");
	}
	
	private static void evaluateAgent() {
		SimulatorConfig config = SimulatorConfig.fromOptions(simulatorOptions);
		EvaluateAgent evaluate = new EvaluateAgent(seed, config, runCount, oneLevelRepetitions, resultDirFile);
		evaluate.evaluateAgent(agentId, agentFQCN);		
	}
		
	// ==============
	// TEST ARGUMENTS
	// ==============
	public static String[] getTestArgs() {
		return new String[] {
				  "-s", "20" // "seed"
				, "-o", "-pp false -tp 0.5 -gc 0 -lc 1 -v false -2x false -p false -tt 40 -r true"   // prototype-options";
				, "-c", "5"  // run-count
				, "-r", "5"  // one-run-repetitions
				, "-p", "game.controllers.pacman.examples.NearestPillPacMan" // agent-fqcn ... requires MarioAI4J-Agents on classpath!
				, "-i", "NearestPill"   // agent-id
				, "-d", "./results" // result-dir"	
		};
	}
	
	public static void main(String[] args) throws JSAPException {
		// -----------
		// FOR TESTING
		// -----------
		//args = getTestArgs();		
		
		// --------------
		// IMPLEMENTATION
		// --------------
		
		initJSAP();
	    
	    header();
	    
	    readConfig(args);
	    
	    sanityChecks();
	    
	    evaluateAgent();
	    
	    System.out.println("---// FINISHED //---");
	    
	    System.exit(0);
	}

}
