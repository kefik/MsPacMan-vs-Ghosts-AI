package game.controllers.pacman.exercises.e2.path.informed.base;

import game.controllers.pacman.exercises.e2.graph.Node;

public class InformedNode {

	/**
	 * Wrapped node.
	 */
	public Node node;
	
	/**
	 * Current path cost to this node.
	 */
	public int pathCost;
	
	/**
	 * Heuristic cost of the path between this NODE and the GOAL node.
	 */
	public int estimate;
	
	/**
	 * Parent node on the path to this node.
	 */
	public InformedNode parent;
	
	public InformedNode(Node node, int pathCost, InformedNode parent, int estimate) {
		this.node = node;
		this.pathCost = pathCost;
		this.parent = parent;
		this.estimate = estimate;
	}
	
	public int hashCode() {
		return node.hashCode();
	}
	
	public boolean equals(Object node) {
		if (node == null) return false;
		if (!(node instanceof InformedNode)) return false;
		return this.node == ((InformedNode)node).node;
	}	
	
}
