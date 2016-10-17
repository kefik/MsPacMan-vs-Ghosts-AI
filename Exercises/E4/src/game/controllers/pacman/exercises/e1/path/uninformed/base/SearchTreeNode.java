package game.controllers.pacman.exercises.e1.path.uninformed.base;

import game.controllers.pacman.exercises.e1.graph.Node;

public class SearchTreeNode {

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
	public SearchTreeNode parent;
	
	/**
	 * What is the level of the node within the search tree (how far in terms of node-count are we from the start).
	 */
	public int level;
	
	public SearchTreeNode(Node node) {
		this(node, 0, null, 0);
	}
	
	public SearchTreeNode(Node node, int pathCost, SearchTreeNode parent) {
		this(node, pathCost, parent, (parent == null ? 0 : parent.level + 1));
	}
	
	public SearchTreeNode(Node node, int pathCost, SearchTreeNode parent, int level) {
		this.node = node;
		this.pathCost = pathCost;
		this.parent = parent;
		this.level = level;
	}
	
	public int hashCode() {
		return node.hashCode();
	}
	
	public boolean equals(Object node) {
		if (node == null) return false;
		if (!(node instanceof SearchTreeNode)) return false;
		return this.node == ((SearchTreeNode)node).node;
	}	
	
	@Override
	public String toString() {
		return "SearchTreeNode[index=" + node.index + ",level=" + level + ",pathCost=" + pathCost + "]";
	}
	
}
