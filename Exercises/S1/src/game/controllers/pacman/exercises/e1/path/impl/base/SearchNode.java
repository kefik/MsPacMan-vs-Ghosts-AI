package game.controllers.pacman.exercises.e1.path.impl.base;

import game.controllers.pacman.exercises.e1.graph.Node;

public class SearchNode {

	/**
	 * Wrapped node.
	 */
	public Node node;
	
	/**
	 * Current path cost to this node.
	 */
	public int pathCost;
	
	/**
	 * Parent node on the path to this node.
	 */
	public SearchNode parent;
	
	public SearchNode(Node node, int pathCost, SearchNode parent) {
		this.node = node;
		this.pathCost = pathCost;
		this.parent = parent;
	}
	
	public int hashCode() {
		return node.hashCode();
	}
	
	public boolean equals(Object node) {
		if (node == null) return false;
		if (!(node instanceof SearchNode)) return false;
		return this.node == ((SearchNode)node).node;
	}	
	
}
