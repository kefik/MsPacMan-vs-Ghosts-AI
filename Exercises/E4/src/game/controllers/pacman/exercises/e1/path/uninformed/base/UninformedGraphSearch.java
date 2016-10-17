package game.controllers.pacman.exercises.e1.path.uninformed.base;

import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Link;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.path.IPathFinder;
import game.controllers.pacman.exercises.e1.path.Path;
import game.controllers.pacman.exercises.e1.path.PathFinderState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UninformedGraphSearch<PATH_FINDER_CONFIG> extends UninformedGraphSearchBase<PATH_FINDER_CONFIG> {
	
	// RUNTIME
	
	protected Map<Node, SearchTreeNode> nodes;
	protected Collection<SearchTreeNode> closed;
	protected Collection<SearchTreeNode> opened;
	
	// RESULT
	
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
	 * @param openList
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
	
	@Override
	public Node getParent(Node node) {
		SearchTreeNode search = nodes.get(node);
		return search == null || search.parent == null ? null : search.parent.node;
	}
	
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
