package game.controllers.pacman.exercises.e2.path.informed.base;

import game.controllers.pacman.exercises.e2.graph.Graph;
import game.controllers.pacman.exercises.e2.graph.Link;
import game.controllers.pacman.exercises.e2.graph.Node;
import game.controllers.pacman.exercises.e2.path.IPathFinder;
import game.controllers.pacman.exercises.e2.path.Path;
import game.controllers.pacman.exercises.e2.path.informed.IGraphView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InformedGraphSearch implements IPathFinder {
	
	protected Graph graph;
	protected Node start;
	protected Node end;
	
	protected int steps = 0;
	protected Map<Node, InformedNode> nodes;
	protected Collection<InformedNode> closed;
	protected Collection<InformedNode> opened;
	
	protected Path path;
	private IGraphView<Node, Link> view;
	
	protected abstract Collection<InformedNode> createCloseList();
	protected abstract Collection<InformedNode> createOpenList();
	
	protected abstract InformedNode selectNextNode(Collection<InformedNode> openList);
	
	protected abstract int getEstimateToGoal(Node from, Node to);
	
	protected Collection<Link> getLinks(Graph graph, InformedNode searchNode) {
		Node node = searchNode.node;
		if (view != null) {
			Collection<Link> newLinks = view.getExtraLinks(searchNode.node, node.links.values());
			if (newLinks != null && newLinks.size() != 0) {
				List<Link> result = new ArrayList<Link>(newLinks);
				result.addAll(node.links.values());			
				return result;
			}
		}
		return node.links.values();
	}	
	
	protected InformedNode makeSearchNode(Node node) {
		return makeSearchNode(node, 0, null, getEstimateToGoal(node, end));
		
	}
	
	public InformedNode makeSearchNode(Node node, int pathCost, InformedNode parent, int estimate) {
		InformedNode result = nodes.get(node);
		if (result != null) {
			return result;
		}
		result = new InformedNode(node, pathCost, parent, estimate);
		nodes.put(node, result);
		return result;
	}
	
	@Override
	public void reset() {
		start = null;
		end = null;
		path = null;
		steps = 0;
		nodes = null;
		closed = null;
		opened = null;
		view = null;
	}
	
	@Override
	public void init(Graph graph, Node start, Node goal) {
		reset();
		
		this.graph = graph;
		this.start = start;
		this.end = goal;
		
		nodes = new HashMap<Node, InformedNode>();
		closed = createCloseList();
		opened = createOpenList();
		 
		opened.add(makeSearchNode(start));
	}
	
	public void init(Graph graph, Node start, Node goal, IGraphView view) {
		reset();
		
		this.graph = graph;
		this.view = view;
		this.start = start;
		this.end = goal;
		
		nodes = new HashMap<Node, InformedNode>();
		closed = createCloseList();
		opened = createOpenList();
		 
		opened.add(makeSearchNode(start));
	}

	@Override
	public void step() {
		if (!isRunning()) return;
		
		++steps;
		
		InformedNode expanding = selectNextNode(opened);

		if (expanding.node == end) {
			// VICTORY
			List<Node> path = new ArrayList<Node>();
			Node node = end;
			while (node != null) {
				path.add(node);
				InformedNode parent = nodes.get(node).parent;
				if (parent == null) break;
				node = parent.node;
			}
			Collections.reverse(path);
			this.path = new Path(path.toArray(new Node[0]));
			return;
		}

		opened.remove(expanding);
		closed.add(expanding);

		Collection<Link> links = getLinks(graph, expanding);

		for (Link link : links) {
			
			if (view != null && !view.isLinkOpened(expanding.node, link)) continue;

			Node nextNode = link.getOtherEnd(expanding.node);
			
			if (view != null && !view.isNodeOpened(nextNode)) continue;

			int linkCost = link.distance;
			int newPathCost = expanding.pathCost + linkCost;
			int estimateCost = getEstimateToGoal(nextNode, end);

			// CREATE OR GET FOR 'nextNode'
			InformedNode next = makeSearchNode(nextNode, newPathCost, expanding, estimateCost);

			if (closed.contains(next)) {
				// WE ALREADY HAVE THE NEXT NODE IN CLOSED LIST!
				if (next.pathCost > newPathCost) {
					// BUT WE HAVE FOUND A BETTER PATH
					closed.remove(next);

					// UPDATE
					next.parent = expanding;
					next.pathCost = newPathCost;

					opened.add(next);
				}
			} else if (opened.contains(next)) {
				// WE ALREADY HAVE THE NEXT NODE IN OPENED LIST!
				if (next.pathCost + next.estimate > newPathCost) {
					// BUT WE HAVE FOUND A BETTER PATH
					opened.remove(next);

					// UPDATE
					next.parent = expanding;
					next.pathCost = newPathCost;

					opened.add(next);
				}
			} else {
				// NEW NODE
				opened.add(next);
			}
		}
	}
	
	@Override
	public boolean isRunning() {
		return start != null && opened.size() > 0 && path == null;
	}
	
	@Override
	public boolean isFinished() {
		return start != null && (opened.size() == 0 || path != null);
	}
	
	@Override
	public boolean isPathFound() {
		return path != null;
	}
	
	@Override
	public Path getPath() {
		return path;
	}
	
	@Override
	public Node getStart() {
		return start;
	}
	
	@Override
	public Node getGoal() {
		return end;
	}
	
	@Override
	public Node getParent(Node node) {
		InformedNode search = nodes.get(node);
		return search == null || search.parent == null ? null : search.parent.node;
	}
	
	@Override
	public Collection<Node> getClosedList() {
		List<Node> result = new ArrayList<Node>(closed.size());
		for (InformedNode node : closed) {
			result.add(node.node);
		}
		return result;
	}
	
	@Override
	public Collection<Node> getOpenList() {
		List<Node> result = new ArrayList<Node>(opened.size());
		for (InformedNode node : opened) {
			result.add(node.node);
		}
		return result;
	}
	
	@Override
	public int getSteps() {
		return steps;
	}
	
}
