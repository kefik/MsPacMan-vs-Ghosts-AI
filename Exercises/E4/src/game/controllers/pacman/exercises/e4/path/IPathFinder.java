package game.controllers.pacman.exercises.e4.path;

import java.util.Collection;

import game.controllers.pacman.exercises.e4.graph.Graph;
import game.controllers.pacman.exercises.e4.graph.Node;
import game.controllers.pacman.exercises.e4.path.uninformed.base.SearchTreeNode;

/**
 * Incremental path-finder.
 * 
 * The idea is to have a path-finder whose execution can be spread between multiple game frames.
 * 
 * Therefore we do not have single method that performs the path-finding but we have a stateful object.
 * 
 * Path finder always needs to be {@link #init(Graph, Node, Node)}ed first, then you can
 * call {@link #step()} until path-finder reaches one of its terminal {@link PathFinderState} that
 * is  {@link PathFinderState#PATH_FOUND} or {@link PathFinderState#PATH_NOT_FOUND}.
 *  
 * @author Jimmy
 */
public interface IPathFinder<PATH_FINDER_CONFIG> {
	
	/**
	 * Shorthand of the Path-Finder name (e.g. DFS, BFS, UCS, ...)
	 * @return
	 */
	public String getName();
	
	// ==============
	// INITIALIZATION
	// ==============
	
	/**
	 * Wipes {@link IPathFinder} internal data.
	 * 
	 * Reset {@link #getState()} to {@link PathFinderState#INIT}.
	 */
	public void reset();
	
	/**
	 * Initialize {@link IPathFinder} to use {@link Graph} and prepare to search from 'start' towards 'goal' node.
	 * 
	 * Changes state of {@link #getState()} to {@link PathFinderState#RUNNING}.
	 * 
	 * @param graph
	 * @param start
	 * @param goal
	 * @param path-finder specific config
	 */
	public void init(Graph graph, Node start, Node goal, PATH_FINDER_CONFIG config);
		
	// =========
	// EXECUTION
	// =========
	
	/**
	 * What state the path-finder is in.
	 * @return
	 */
	public PathFinderState getState();
	
	/**
	 * Perform another step of "IPathFinder".
	 * 
	 * Can be called if and only if {@link #getState()} == {@link PathFinderState#RUNNING}.
	 * 
	 * @return what state the path-finder is in after the method finishes
	 */
	public PathFinderState step();
	
	// =======
	// RESULTS
	// =======
	
	/**
	 * Returns found path that has 'start' node as the first element and 'goal' as the last element of the path.
	 * 
	 * Can be called if and only if {@link #getState()} == {@link PathFinderState#PATH_FOUND}.
	 *  
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
	 * Returns GOAL node.
	 * @return
	 */
	public Node getGoal();
	
	/**
	 * Returns current parent-on-the-path-from START
	 * @param node
	 * @return
	 */
	public Node getParent(Node node);
	
	/**
	 * Returns extra information about the search node.
	 * @param node
	 * @return
	 */
	public SearchTreeNode getNodeInfo(Node node);
	
	/**
	 * Already expanded nodes that were discussed.
	 * @return
	 */
	public Collection<Node> getClosedList();
	
	/**
	 * Containing "leaf nodes", also known as FRINGE.
	 * @return
	 */
	public Collection<Node> getOpenList();
	
	// =========
	// PROFILING
	// =========
	
	/**
	 * How many number of iterations we have performed so-far.
	 * How many times {@link #step()} was called.
	 * @return
	 */
	public int getSteps();
	
}
