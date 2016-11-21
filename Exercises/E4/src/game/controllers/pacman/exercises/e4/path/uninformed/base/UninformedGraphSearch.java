package game.controllers.pacman.exercises.e4.path.uninformed.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.controllers.pacman.exercises.e4.graph.Graph;
import game.controllers.pacman.exercises.e4.graph.Link;
import game.controllers.pacman.exercises.e4.graph.Node;
import game.controllers.pacman.exercises.e4.path.IPathFinder;
import game.controllers.pacman.exercises.e4.path.Path;
import game.controllers.pacman.exercises.e4.path.PathFinderState;

public abstract class UninformedGraphSearch<PATH_FINDER_CONFIG> extends UninformedGraphSearchBase<PATH_FINDER_CONFIG> {
	
	// RUNTIME
	
	/**
	 * This is cache (map) of nodes we have touched so far.
	 * "maze node" -> "search tree node"
	 */
	protected Map<Node, SearchTreeNode> nodes;
	
	/**
	 * Nodes we have expanded.
	 */
	protected Collection<SearchTreeNode> closed;
	
	/**
	 * Nodes that makes our "fringe" we're choosing a node
	 * for expansion from.
	 */
	protected Collection<SearchTreeNode> opened;
	
	// RESULT
	
	/**
	 * Once you find a path, cache it here.
	 */
	protected Path path;
	
	// ==================
	// INTERNAL INTERFACE
	// ==================
	
	@Override
	public abstract String getName();
	
	/**
	 * Creates new CLOSED list; called every time the path-finder is {@link #init(Graph, Node, Node)}ed.
	 * @return
	 */
	protected abstract Collection<SearchTreeNode> createCloseList();
	
	/**
	 * Creates new OPENED list; called every time the path-finder is {@link #init(Graph, Node, Node)}ed.
	 * @return
	 */
	protected abstract Collection<SearchTreeNode> createOpenList();
	
	/**
	 * Selects next {@link SearchTreeNode} to evaluate next.
	 * @param openList supply {@link #opened} here
	 * @return
	 */
	protected abstract SearchTreeNode selectNextNode(Collection<SearchTreeNode> openList);
	
	// =========================================
	// IPathFinder INITIALIZATION Implementation
	// =========================================
	
	@Override
	public void reset() {
		super.reset();
		path = null;
		nodes = null;
		closed = null;
		opened = null;
	}
	
	@Override
	public void init(Graph graph, Node start, Node goal, PATH_FINDER_CONFIG config) {
		super.init(graph, start, goal, config);
		
		nodes = new HashMap<Node, SearchTreeNode>();
		closed = createCloseList();
		opened = createOpenList();
		 
		opened.add(makeSearchNode(start));
		
		state = PathFinderState.RUNNING;
	}
	
	// ============================================================
	// IPathFinder EXECUTION Implementation ~ Search Implementation
	// ============================================================
	
	/**
	 * Creates new {@link SearchTreeNode} that wraps {@link Node} of the graph maintaining information
	 * we need to track in-between {@link #step()} calls.
	 * @param node
	 * @return
	 */
	protected SearchTreeNode makeSearchNode(Node node) {
		return makeSearchNode(node, 0, null);		
	}
	
	/**
	 * Creates new {@link SearchTreeNode} that wraps {@link Node} of the graph maintaining information
	 * we need to track in-between {@link #step()} calls.
	 * @param node
	 * @param pathCost
	 * @param parent
	 * @return
	 */
	protected SearchTreeNode makeSearchNode(Node node, int pathCost, SearchTreeNode parent) {
		SearchTreeNode result = nodes.get(node);
		if (result != null) {
			return result;
		}
		result = new SearchTreeNode(node, pathCost, parent);
		nodes.put(node, result);
		return result;
	}
	
	@Override
	public PathFinderState step() {
		if (state != PathFinderState.RUNNING) return state;
		
		++steps;
		
		// TODO: implement me!
		//       This should implement SINGLE STEP of the search algorithm!

		// Graph-Search algorithm template:
		
		//  -- uninformed graph search strategy is implemented via
		//  ---- createCloseList() ~ storage for expanded nodes
		//  ---- createOpenList()  ~ tells us how fringe stores nodes
		//         +-- note that "start" node is already within the open list when step() is
		//             is called for the first time
		//  ---- selectNextNode()  ~ tells us which node to choose for the
		//                           expansion next
		//  -- use makeSearchNode(node, pathCost, parent) instead of manual new SearchTreeNode
		
		// GRAPH ALGORITHM STEP SKELETON
		// 1) fringe (aka open list) empty? => PATH NOT FOUND
		// 2) choose node for the evaluation (use selectNextNode(opened))
		// 3) is it goal? => PATH FOUND
		// 4) expand the node
		//    -- mind the fact we're searching within the graph
		//    -- be sure to handle cycles correctly
		//       -- you have to correctly update SearchTreeNode in case
		//          you're moving nodes from "closed" to "opened" list
		
		// Notes:
		//   1) if you wish indicate that the search has ended
		//      you have to set this.state, not only to return the new state
		//      e.g. do: return this.state = PathFinderState.PATH_FOUND;
		//   2) do not forget to correctly initialize this.path
		//      after you find the path, see {@link IPathFinder#getPath()}
		//      for more info how {@link Path} object should be initialized
		//      ... you will probably want to utilize this.getParent(node) for that
		//   3) SearchTreeNode links can be retrieved via searchTreeNode.node.links
		//   4) SearchTreeNode instances can be checked for equality using .equals()
		//   5) Node instances can be checked for equality using operator '==' 
		//      (.equals() will work as well)
		//   6) Start and Goal nodes are stored within this.start, this.end respectively
		//   7) for given "Node node" you can retrieve SearchTreeNode via "nodes.get(node)"
				
		return state;
	}
	
	// ==================================
	// IPathFinder RESULTS Implementation
	// ==================================
	
	@Override
	public Path getPath() {
		return path;
	}
	
	// =============================================
	// IPathFinder INTERMEDIATE STATE Implementation
	// =============================================
	
	/**
	 * Returns a 'node' parent on the path. 'node' parent is stored within {@link SearchTreeNode}.
	 * @param node
	 * @return node parent on the path
	 */
	@Override
	public Node getParent(Node node) {
		SearchTreeNode search = nodes.get(node);
		return search == null || search.parent == null ? null : search.parent.node;
	}
	
	/**
	 * Returns extra search info we store for given 'node'
	 * @param node
	 * @return extra info about 'node' search node
	 */
	@Override
	public SearchTreeNode getNodeInfo(Node node) {
		return nodes.get(node);
	}
	
	@Override
	public Collection<Node> getClosedList() {
		List<Node> result = new ArrayList<Node>(closed.size());
		for (SearchTreeNode node : closed) {
			result.add(node.node);
		}
		return result;
	}
	
	@Override
	public Collection<Node> getOpenList() {
		List<Node> result = new ArrayList<Node>(opened.size());
		for (SearchTreeNode node : opened) {
			result.add(node.node);
		}
		return result;
	}	
	
}
