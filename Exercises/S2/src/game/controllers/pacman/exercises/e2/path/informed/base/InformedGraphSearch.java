package game.controllers.pacman.exercises.e2.path.informed.base;

import game.controllers.pacman.exercises.e2.graph.Graph;
import game.controllers.pacman.exercises.e2.graph.Link;
import game.controllers.pacman.exercises.e2.graph.Node;
import game.controllers.pacman.exercises.e2.path.IPathFinder;
import game.controllers.pacman.exercises.e2.path.Path;

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
	
	protected abstract Collection<InformedNode> createCloseList();
	protected abstract Collection<InformedNode> createOpenList();
	
	protected abstract InformedNode selectNextNode(Collection<InformedNode> openList);
	
	protected abstract int estimatePathCostToGoal(Graph graph, Node expanding);
	
	protected int getNodeCost(Node node) {
		return 0;
	}
	
	protected int getLinkCost(Node node, Link link) {
		return link.distance;
	}
		
	protected Collection<Link> getOptions(Graph graph, InformedNode searchNode) {
		Node node = searchNode.node;
		return node.links.values();
	}	
	
	protected InformedNode makeFirstSearchNode(Node node) {
		return makeSearchNode(node, 0, estimatePathCostToGoal(graph, node), null);		
	}
	
	public InformedNode makeSearchNode(Node node, int pathCost, int estimatedCost, InformedNode parent) {
		InformedNode result = nodes.get(node);
		if (result != null) {
			return result;
		}
		result = new InformedNode(node, pathCost, estimatedCost, parent);
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
		 
		opened.add(makeFirstSearchNode(start));
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
			this.path = new Path(path.toArray(new Node[path.size()]));
			return;
		}

		opened.remove(expanding);
		closed.add(expanding);

		Collection<Link> options = getOptions(graph, expanding);

		for (Link option : options) {

			Node currentNode = expanding.node;
			Node nextNode = option.getOtherEnd(currentNode);

			int nextNodeCost = getNodeCost(nextNode);
			int linkCost = getLinkCost(currentNode, option);
			
			int newPathCost = expanding.pathCost + nextNodeCost + linkCost;
			
			int estimatedPathToGoal = estimatePathCostToGoal(graph, nextNode);

			// CREATE OR GET FOR 'nextNode'
			InformedNode next = makeSearchNode(nextNode, newPathCost, estimatedPathToGoal, expanding);

			if (closed.contains(next)) {
				// WE ALREADY HAVE THE NEXT NODE IN CLOSED LIST!
				if (next.pathCost > newPathCost) {
					// BUT WE HAVE FOUND A BETTER PATH
					closed.remove(next);

					// UPDATE
					next.parent = expanding;
					next.pathCost = newPathCost;
					next.estimatedCostToGoal = estimatedPathToGoal;

					opened.add(next);
				}
			} else if (opened.contains(next)) {
				// WE ALREADY HAVE THE NEXT NODE IN OPENED LIST!
				if (next.pathCost > newPathCost) {
					// BUT WE HAVE FOUND A BETTER PATH
					opened.remove(next);

					// UPDATE
					next.parent = expanding;
					next.pathCost = newPathCost;
					next.estimatedCostToGoal = estimatedPathToGoal;

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
