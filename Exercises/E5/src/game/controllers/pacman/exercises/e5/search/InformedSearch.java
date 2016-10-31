package game.controllers.pacman.exercises.e5.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import game.controllers.pacman.exercises.e5.graph.IGraph;
import game.controllers.pacman.exercises.e5.graph.ILink;
import game.controllers.pacman.exercises.e5.graph.INode;
import game.controllers.pacman.exercises.e5.search.base.IGraphView;
import game.controllers.pacman.exercises.e5.search.base.IPathFinder;
import game.controllers.pacman.exercises.e5.search.base.ISearchGoal;
import game.controllers.pacman.exercises.e5.search.base.ISearchStrategy;
import game.controllers.pacman.exercises.e5.search.base.Path;
import game.controllers.pacman.exercises.e5.search.base.PathFinderState;

public class InformedSearch<NODE extends INode, LINK extends ILink<NODE>, SEARCH_NODE extends InformedNode<NODE, LINK>> implements IPathFinder<NODE, LINK, SEARCH_NODE, Object> {
	
	protected PathFinderState state = PathFinderState.INIT;
	
	protected IGraph<NODE, LINK> graph;
	
	protected ISearchGoal<NODE, LINK, SEARCH_NODE> goal;
	
	protected ISearchStrategy<NODE, LINK, SEARCH_NODE> strategy;
	
	protected IGraphView<NODE, LINK, SEARCH_NODE> view;
	
	protected int steps = 0;
	
	protected NODE startNode;
	
	protected NODE goalNode;
	
	protected Collection<SEARCH_NODE> closed;
	
	protected Collection<SEARCH_NODE> opened;
	
	protected Path<NODE, LINK> path;
	
	/**
	 * Determines COST of the given node within the graph.
	 * @param node
	 * @param parent
	 * @return
	 */
	protected int getNodeGraphCost(NODE node, SEARCH_NODE parent) {
		return 0;
	}
	
	/**
	 * Determines EXTRA COST of the given node.
	 * @param node
	 * @param parent
	 * @return
	 */
	protected int getNodeViewCost(NODE node, SEARCH_NODE parent) {
		return (view == null ? 0 : view.getNodeExtraCost(node, 0, parent));
	}
	
	/**
	 * Determines COST of the link.
	 * @param node
	 * @param link
	 * @return
	 */
	protected int getLinkGraphCost(NODE node, LINK link) {
		return link.getCost();
	}
	
	/**
	 * Determines EXTRA COST of the given LINK that leads between 'parent' -> 'node'.
	 * @param node
	 * @param link
	 * @param parent
	 * @return
	 */
	protected int getLinkViewCost(NODE node, LINK link, SEARCH_NODE parent) {
		return (view == null ? 0 : view.getLinkExtraCost(node, link, getLinkGraphCost(node, link), parent));
	}
		
	/**
	 * Gathers all LINKS we can travel through from 'searchNode'.
	 * @param graph
	 * @param searchNode
	 * @return
	 */
	protected Collection<LINK> getLinks(SEARCH_NODE searchNode) {
		NODE node = (NODE)searchNode.node;
		Collection<LINK> origLinks = (Collection<LINK>) node.getLinks();
		
		if (view == null) {
			return origLinks;
		}
		
		NODE from = (NODE) searchNode.node;		
		
		Collection<LINK> extraLinks = view.getExtraLinks(searchNode, origLinks);
		
		List<LINK> result = new ArrayList<LINK>(origLinks.size() + (extraLinks == null ? 0 : extraLinks.size()));
		
		if (origLinks != null) {
			for (LINK link : origLinks) {
				if (view == null || !view.isLinkOpened(from, link)) continue;				
				// LINK IS OPENED
				result.add(link);
			}
		}
		
		if (extraLinks != null) {
			for (LINK link : extraLinks) {
				if (view == null || !view.isLinkOpened(from, link)) continue;				
				// LINK IS OPENED
				result.add(link);
			}
		}
				
		return result;
		
	}	
	
	// =====================
	// IPathFinder INTERFACE
	// =====================
	
	@Override
	public void reset() {
		state = PathFinderState.INIT;
		graph = null;
		goal = null;
		strategy = null;
		view = null;
		steps = 0;
		startNode = null;
		goalNode = null;
		closed = null;
		opened = null;
		path = null;
	}
	
	@Override
	public void init(IGraph<NODE, LINK> graph, ISearchGoal<NODE, LINK, SEARCH_NODE> goal,
			ISearchStrategy<NODE, LINK, SEARCH_NODE> strategy, IGraphView<NODE, LINK, SEARCH_NODE> view,
			Object config) {
		
		this.graph = graph;
		this.goal = goal;
		this.strategy = strategy;
		this.view = view;
		this.startNode = goal.getStart();
		
		this.strategy.setContext(this);
		this.goal.setContext(this);
		
		closed = strategy.createCloseList();
		opened = strategy.createOpenList();
		 
		opened.add((SEARCH_NODE)strategy.makeInitialNode(startNode));
		
		state = PathFinderState.RUNNING;
	
		
	}

	@Override
	public PathFinderState step() {
		if (state != PathFinderState.RUNNING) return state;
		
		++steps;
		
		// TODO: implement
		
		return state = PathFinderState.PATH_NOT_FOUND;
	}
	
	protected Path<NODE, LINK> makePath(SEARCH_NODE end) {
		List<LINK> path = new ArrayList<LINK>();
		SEARCH_NODE node = end;
		while (node.node != startNode && node.link != null) {
			path.add(node.link);
			SEARCH_NODE parent = (SEARCH_NODE)node.parent;
			if (parent == null) break;
			node = parent;
		}
		Collections.reverse(path);
		return this.path = new Path<NODE, LINK>(node.node, path);
	}
	
	// ===============
	// UTILITY GETTERS
	// ===============
	
	@Override
	public String getName() {
		return "SEARCH[" + steps + "]";
	}
	
	@Override
	public PathFinderState getState() {
		return state;
	}
	
	@Override
	public Path<NODE, LINK> getPath() {
		return path;
	}
	
	@Override
	public NODE getStart() {
		return startNode;
	}
	
	@Override
	public NODE getGoal() {
		return goalNode;
	}
	
	@Override
	public SEARCH_NODE getSearchNode(NODE node) {
		return strategy.getNode(node);
	}
	
	@Override
	public NODE getParent(NODE node) {
		if (strategy == null) return null;
		SEARCH_NODE wrapper = strategy.getNode(node);
		if (wrapper == null) return null;
		return wrapper.parent.node;
	}
	
	@Override
	public Collection<SEARCH_NODE> getClosedList() {
		return closed;
	}
	
	@Override
	public Collection<SEARCH_NODE> getOpenList() {		
		return opened;
	}
	
	@Override
	public int getSteps() {
		return steps;
	}	
	
}
