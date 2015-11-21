package game.controllers.pacman.exercises.e3.path.informed.base;

import game.controllers.pacman.exercises.e3.graph.Node;

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
	 * How far is the goal ... as we do think...
	 */
	public int estimatedCostToGoal;
	
	/**
	 * Parent node on the path to this node.
	 */
	public InformedNode parent;
	
	public InformedNode(Node node, int pathCost, int estimatedCostToGoal, InformedNode parent) {
		this.node = node;
		this.pathCost = pathCost;
		this.estimatedCostToGoal = estimatedCostToGoal;
		this.parent = parent;
	}
	
	public int getTotalCost() {
		return pathCost + estimatedCostToGoal;
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
