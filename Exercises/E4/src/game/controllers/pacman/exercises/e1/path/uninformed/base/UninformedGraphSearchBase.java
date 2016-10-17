package game.controllers.pacman.exercises.e1.path.uninformed.base;

import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.path.IPathFinder;
import game.controllers.pacman.exercises.e1.path.PathFinderState;

public abstract class UninformedGraphSearchBase<PATH_FINDER_CONFIG> implements IPathFinder<PATH_FINDER_CONFIG> {
	
	// STATE OF THE PATH FINDER
	
	protected PathFinderState state;
	
	// PROBLEM DEFINITION
	
	protected Graph graph;
	protected Node start;
	protected Node end;
	
	protected PATH_FINDER_CONFIG config;
	
	// PROFILING
	
	protected int steps = 0;
		
	// =========================================
	// IPathFinder INITIALIZATION Implementation
	// =========================================
	
	@Override
	public void reset() {
		graph = null;
		start = null;
		end = null;
		config = null;
		steps = 0;
		state = PathFinderState.INIT;
	}
	
	@Override
	public void init(Graph graph, Node start, Node goal, PATH_FINDER_CONFIG config) {
		reset();
		
		this.graph = graph;
		this.start = start;
		this.end = goal;
		
		this.config = config;
		
		state = PathFinderState.RUNNING;
	}
	
	// ============================================================
	// IPathFinder EXECUTION Implementation ~ Search Implementation
	// ============================================================
	
	@Override
	public PathFinderState getState() {
		return state;
	}
		
	// =============================================
	// IPathFinder INTERMEDIATE STATE Implementation
	// =============================================
	
	@Override
	public Node getStart() {
		return start;
	}
	
	@Override
	public Node getGoal() {
		return end;
	}
	
	// ====================================
	// IPathFinder PROFILING Implementation
	// ====================================
	
	@Override
	public int getSteps() {
		return steps;
	}
	
}
