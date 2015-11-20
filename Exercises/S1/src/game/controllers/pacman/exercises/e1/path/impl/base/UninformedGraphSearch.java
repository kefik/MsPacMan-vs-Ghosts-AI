package game.controllers.pacman.exercises.e1.path.impl.base;

import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Link;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.path.IPathFinder;
import game.controllers.pacman.exercises.e1.path.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UninformedGraphSearch implements IPathFinder {
	
	protected Graph graph;
	protected Node start;
	protected Node end;
	
	protected int steps = 0;
	protected Map<Node, UninformedNode> nodes;
	protected Collection<UninformedNode> closed;
	protected Collection<UninformedNode> opened;
	
	protected Path path;
	
	protected abstract Collection<UninformedNode> createCloseList();
	protected abstract Collection<UninformedNode> createOpenList();
	
	protected abstract UninformedNode selectNextNode(Collection<UninformedNode> openList);
	
	protected Collection<Link> getOptions(Graph graph, UninformedNode searchNode) {
		Node node = searchNode.node;
		return node.links.values();
	}	
	
	protected UninformedNode makeSearchNode(Node node) {
		return makeSearchNode(node, 0, null);
		
	}
	
	public UninformedNode makeSearchNode(Node node, int pathCost, UninformedNode parent) {
		UninformedNode result = nodes.get(node);
		if (result != null) {
			return result;
		}
		result = new UninformedNode(node, pathCost, parent);
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
		
		nodes = new HashMap<Node, UninformedNode>();
		closed = createCloseList();
		opened = createOpenList();
		 
		opened.add(makeSearchNode(start));
	}

	@Override
	public void step() {
		if (!isRunning()) return;
		
		++steps;
		
		UninformedNode expanding = selectNextNode(opened);

		if (expanding.node == end) {
			// VICTORY
			List<Node> path = new ArrayList<Node>();
			Node node = end;
			while (node != null) {
				path.add(node);
				UninformedNode parent = nodes.get(node).parent;
				if (parent == null) break;
				node = parent.node;
			}
			Collections.reverse(path);
			this.path = new Path(path.toArray(new Node[0]));
			return;
		}

		opened.remove(expanding);
		closed.add(expanding);

		Collection<Link> options = getOptions(graph, expanding);

		for (Link option : options) {

			Node nextNode = option.getOtherEnd(expanding.node);

			int linkCost = option.distance;
			int newPathCost = expanding.pathCost + linkCost;

			// CREATE OR GET FOR 'nextNode'
			UninformedNode next = makeSearchNode(nextNode, newPathCost, expanding);

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
				if (next.pathCost > newPathCost) {
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
		UninformedNode search = nodes.get(node);
		return search == null || search.parent == null ? null : search.parent.node;
	}
	
	@Override
	public Collection<Node> getClosedList() {
		List<Node> result = new ArrayList<Node>(closed.size());
		for (UninformedNode node : closed) {
			result.add(node.node);
		}
		return result;
	}
	
	@Override
	public Collection<Node> getOpenList() {
		List<Node> result = new ArrayList<Node>(opened.size());
		for (UninformedNode node : opened) {
			result.add(node.node);
		}
		return result;
	}
	
	@Override
	public int getSteps() {
		return steps;
	}
	
}
