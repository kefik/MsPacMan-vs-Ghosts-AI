package game.controllers.pacman.exercises.e3.search;

import game.controllers.pacman.exercises.e3.graph.Graph;
import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.base.Path;
import game.controllers.pacman.exercises.e3.search.base.SearchState;
import game.controllers.pacman.exercises.e3.search.nodes.InformedNode;

import java.util.Collection;

public interface IPathFinder<WRAPPER extends InformedNode> {
	
	public String getName();
	
	// ==============
	// INITIALIZATION
	// ==============
	
	/**
	 * Wipes {@link IPathFinder} internal data. I.e. making: {@link #isRunning()} false, {@link #isFinished()} false, {@link #isPathFound()} false. 
	 */
	public void reset();
	
	/**
	 * Initialize {@link IPathFinder} to use {@link Graph} and prepare to search from 'start' towards 'goal' node.
	 * 
	 * Once {@link #init(Graph, Node, Node)}ed, either {@link #isRunning()} or {@link #isFinished()} must evaluate to TRUE.
	 * 
	 * @param graph
	 * @param start
	 * @param goal
	 */
	public void init(Graph graph, ISearchGoal<WRAPPER> goal, ISearchStrategy<WRAPPER> strategy, IGraphView<Node, Link, WRAPPER> view);
	
	// =========
	// EXECUTION
	// =========
	
	/**
	 * Perform another step of "IPathFinder". Does nothing if {@link #isFinished()}.
	 */
	public void step();
	
	// =======
	// RESULTS
	// =======
	
	/**
	 * The state of the search.
	 * @return
	 */
	public SearchState getState();
		
	/**
	 * Returns found path that has 'start' node as the first element and 'goal' as the last element of the path.
	 * 
	 * Returns null if NOT {@link #isPathFound()}. 
	 * @return
	 */
	public Path getPath();
	
	// ==================
	// INTERMEDIATE STATE
	// ==================
	
	/**
	 * Returns START node.
	 * @return
	 */
	public Node getStart();
	
	/**
	 * Returns GOAL node; may be null in case of procedural goals.
	 * @return
	 */
	public Node getGoal();
	
	/**
	 * Returns current parent-on-the-path-from START
	 * @param node
	 * @return
	 */
	public WRAPPER getParent(Node node);
		
	/**
	 * Collection of already expanded nodes (if tracked).
	 * @return
	 */
	public Collection<WRAPPER> getClosedList();
	
	/**
	 * Containing "leaf nodes", also known as FRINGE.
	 * @return
	 */
	public Collection<WRAPPER> getOpenList();
	
	/**
	 * How many number of iterations we have performed so-far.
	 * @return
	 */
	public int getSteps();
	
}
