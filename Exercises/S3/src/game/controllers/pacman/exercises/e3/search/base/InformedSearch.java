package game.controllers.pacman.exercises.e3.search.base;

import game.controllers.pacman.exercises.e3.graph.Graph;
import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.IGraphView;
import game.controllers.pacman.exercises.e3.search.IPathFinder;
import game.controllers.pacman.exercises.e3.search.ISearchGoal;
import game.controllers.pacman.exercises.e3.search.ISearchStrategy;
import game.controllers.pacman.exercises.e3.search.nodes.InformedNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InformedSearch<WRAPPER extends InformedNode> implements IPathFinder<WRAPPER> {
	
	protected SearchState state = SearchState.NONE;
	
	protected Graph graph;
	
	protected ISearchGoal<WRAPPER> goal;
	
	protected ISearchStrategy<WRAPPER> strategy;
	
	protected IGraphView<Node, Link, WRAPPER> view;
	
	protected int steps = 0;
	
	protected Node startNode;
	
	protected Node goalNode;
	
	protected Collection<WRAPPER> closed;
	
	protected Collection<WRAPPER> opened;
	
	protected Path path;
	
	/**
	 * Determines COST of the given node within the graph.
	 * @param node
	 * @param parent
	 * @return
	 */
	protected int getNodeGraphCost(Node node, WRAPPER parent) {
		return 0;
	}
	
	/**
	 * Determines EXTRA COST of the given node.
	 * @param node
	 * @param parent
	 * @return
	 */
	protected int getNodeViewCost(Node node, WRAPPER parent) {
		return (view == null ? 0 : view.getNodeExtraCost(node, 0, parent));
	}
	
	/**
	 * Determines LENGTH of the link.
	 * @param node
	 * @param link
	 * @return
	 */
	protected int getLinkGraphCost(Node node, Link link) {
		return link.distance;
	}
	
	/**
	 * Determines EXTRA COST of the given LINK that leads between 'parent' -> 'node'.
	 * @param node
	 * @param link
	 * @param parent
	 * @return
	 */
	protected int getLinkViewCost(Node node, Link link, WRAPPER parent) {
		return (view == null ? 0 : view.getLinkExtraCost(node, link, getLinkGraphCost(node, link), parent));
	}
		
	/**
	 * Gathers all LINKS we can travel through from 'searchNode'.
	 * @param graph
	 * @param searchNode
	 * @return
	 */
	protected Collection<Link> getLinks(Graph graph, WRAPPER searchNode) {
		Node node = searchNode.node;
		Collection<Link> origLinks = node.links.values();
		
		if (view == null) {
			return origLinks;
		}
		
		Node from = searchNode.node;		
		
		Collection<Link> extraLinks = view.getExtraLinks(searchNode, origLinks);
		
		List<Link> result = new ArrayList<Link>(origLinks.size() + (extraLinks == null ? 0 : extraLinks.size()));
		
		if (origLinks != null) {
			for (Link link : origLinks) {
				if (view == null || !view.isLinkOpened(from, link)) continue;				
				// LINK IS OPENED
				result.add(link);
			}
		}
		
		if (extraLinks != null) {
			for (Link link : extraLinks) {
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
		state = SearchState.NONE;
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
	public void init(Graph graph, ISearchGoal<WRAPPER> goal, ISearchStrategy<WRAPPER> strategy, IGraphView<Node, Link, WRAPPER> view) {
		reset();
			
		this.graph = graph;
		this.goal = goal;
		this.strategy = strategy;
		this.view = view;
		this.startNode = goal.getStart();
		
		closed = strategy.createCloseList();
		opened = strategy.createOpenList();
		 
		opened.add(strategy.makeFirstNode(startNode));
		
		state = SearchState.RUNNING;
	}

	@Override
	public void step() {
		if (state != SearchState.RUNNING) return;
		
		++steps;
		
		if (opened.size() == 0) {
			// NOWHERE TO CONTINUE
			this.state = SearchState.FAILED;
			return;
		}
		
		WRAPPER expanding = strategy.selectNextNode(this, opened);

		if (goal.isGoal(this, expanding)) {
			// VICTORY
			this.state = SearchState.PATH_FOUND;
			
			this.goalNode = expanding.node;
			
			List<Link> path = new ArrayList<Link>();
			InformedNode node = expanding;
			while (node.node != startNode && node.link != null) {
				path.add(node.link);
				InformedNode parent = node.parent;
				if (parent == null) break;
				node = parent;
			}
			Collections.reverse(path);
			this.path = new Path(startNode, path.toArray(new Link[path.size()]));
			return;
		}

		opened.remove(expanding);
		closed.add(expanding);

		Collection<Link> links = getLinks(graph, expanding);

		for (Link link : links) {
			
			if (view != null && !view.isLinkOpened(expanding.node, link)) continue;

			Node currentNode = expanding.node;
			Node nextNode = link.getOtherEnd(currentNode);
			
			if (view != null && !view.isNodeOpened(expanding.node)) continue;

			int nextNodeGraphCost = getNodeGraphCost(nextNode, expanding);
			int nextNodeExtraCost = getNodeViewCost(nextNode, expanding);
			int linkGraphCost = getLinkGraphCost(nextNode, link);
			int linkExtraCost = getLinkViewCost(currentNode, link, expanding);
			
			int estimate = goal.estimate(this, nextNode, link, expanding);

			// CREATE OR GET FOR 'nextNode'
			WRAPPER next = strategy.makeNode(nextNode, link, expanding, nextNodeGraphCost, nextNodeExtraCost, linkGraphCost, linkExtraCost, estimate);
			
			if (!view.isNodeOpened(next)) {
				continue;
			}
			
			int newPathCost = expanding.getPathCost() + nextNodeGraphCost + nextNodeExtraCost + linkGraphCost + linkExtraCost;

			if (closed.contains(next)) {
				// WE ALREADY HAVE THE NEXT NODE IN CLOSED LIST!
				if (next.getPathCost() > newPathCost) {
					// BUT WE HAVE FOUND A BETTER PATH
					closed.remove(next);

					// UPDATE
					next.updateParent(expanding, link, nextNodeGraphCost, nextNodeExtraCost, linkGraphCost, linkExtraCost, estimate);

					// ADD TO OPEN
					opened.add(next);
				}
			} else if (opened.contains(next)) {
				// WE ALREADY HAVE THE NEXT NODE IN OPENED LIST!
				if (next.getPathCost() > newPathCost) {
					// BUT WE HAVE FOUND A BETTER PATH
					opened.remove(next);

					// UPDATE
					next.updateParent(expanding, link, nextNodeGraphCost, nextNodeExtraCost, linkGraphCost, linkExtraCost, estimate);

					// ADD TO OPEN
					opened.add(next);
				}
			} else {
				// NEW NODE
				opened.add(next);
			}
		}
	}
		
	@Override
	public Path getPath() {
		return path;
	}
	
	@Override
	public Node getStart() {
		return startNode;
	}
	
	@Override
	public Node getGoal() {
		return goalNode;
	}
	
	@Override
	public WRAPPER getParent(Node node) {
		if (strategy == null) return null;
		WRAPPER wrapper = strategy.getNode(node);
		if (wrapper == null) return null;
		return (WRAPPER)wrapper.parent;
	}
	
	@Override
	public Collection<WRAPPER> getClosedList() {
		return closed;
	}
	
	@Override
	public Collection<WRAPPER> getOpenList() {		
		return opened;
	}
	
	@Override
	public int getSteps() {
		return steps;
	}


	@Override
	public String getName() {
		return "SEARCH[" + steps + "]";
	}

	@Override
	public SearchState getState() {
		return state;
	}
	
}
