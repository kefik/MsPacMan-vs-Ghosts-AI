package game.controllers.pacman.exercises.e1.path;

import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Node;

import java.util.Collection;

public interface IPathFinder {
	
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
	public void init(Graph graph, Node start, Node goal);
		
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
	 * Whether {@link IPathFinder} has been initialized and is currently searching for a path.
	 * @return
	 */
	public boolean isRunning();
	
	/**
	 * Whether we already finished (succeeded or failed).
	 * @return
	 */
	public boolean isFinished();
	
	/**
	 * Whether we have already found a path.
	 * @return
	 */
	public boolean isPathFound();
	
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
	 * Already expanded nodes.
	 * @return
	 */
	public Collection<Node> getClosedList();
	
	/**
	 * Containing "leaf nodes", also known as FRINGE.
	 * @return
	 */
	public Collection<Node> getOpenList();
	
	/**
	 * How many number of iterations we have performed so-far.
	 * @return
	 */
	public int getSteps();
	
}
