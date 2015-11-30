package game.controllers.pacman.exercises.e3.search.nodes;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.IGraphView;

public class InformedNode {

	/**
	 * Wrapped node.
	 */
	public Node node;
	
	/**
	 * Link we have traveled to get to this node.
	 */
	public Link link;
	
	/**
	 * Parent node on the path from START to this node.
	 */
	public InformedNode parent;
	
	/**
	 * Cost for the path to parent.
	 */
	public int parentPathCost;
	
	/**
	 * Original NODE COST from the graph.
	 */
	public int nodeGraphCost;
	
	/**
	 * Node EXTRA COST as determined by {@link IGraphView}.
	 */
	public int nodeExtraCost;
	
	/**
	 * Original GRAPH COST as determined by {@link Link} length within the {@link Graph}.
	 */
	public int linkGraphCost;
	
	/**
	 * Link EXTRA COST as determined by {@link IGraphView}.
	 */
	public int linkExtraCost;
	
	/**
	 * How far is the goal ... as we do think...
	 */
	public int estimateToGoal;
	
	/**
	 * How far (node count) is the node from the start. Depth of the node. 
	 */
	public int nodeLevel;
	
	public InformedNode(Node node) {
		this.node = node;
		link = null;
		parent = null;
		parentPathCost = 0;
		nodeGraphCost = 0;
		nodeExtraCost = 0;
		linkGraphCost = 0;
		linkExtraCost = 0;
		estimateToGoal = 0;
		nodeLevel = 0;
	}
	
	public InformedNode(Node node, Link link, InformedNode parent, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal) {
		this.node = node;
		this.link = link;
		this.parent = parent;
		this.parentPathCost = (parent == null ? 0 : parent.getPathCost());
		this.nodeGraphCost = nodeGraphCost;
		this.nodeExtraCost = nodeExtraCost;
		this.linkGraphCost = linkGraphCost;
		this.linkExtraCost = linkExtraCost;
		this.estimateToGoal = estimateToGoal;
		this.nodeLevel = (parent == null ? 0 : parent.nodeLevel + 1);
	}
	
	public void updateParent(InformedNode parent, Link link, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal) {
		this.parent = parent;
		this.parentPathCost = (parent == null ? 0 : parent.getPathCost());
		this.link = link;
		this.nodeGraphCost = nodeGraphCost;
		this.nodeExtraCost = nodeExtraCost;
		this.linkGraphCost = linkGraphCost;
		this.linkExtraCost = linkExtraCost;
		this.estimateToGoal = estimateToGoal;
		this.nodeLevel = (parent == null ? 0 : parent.nodeLevel + 1);
	}
	
	public int getPathCost() {
		return parentPathCost + nodeGraphCost + nodeExtraCost + linkGraphCost + linkExtraCost;
	}
	
	public int getTotalCost() {
		return parentPathCost + nodeGraphCost + nodeExtraCost + linkGraphCost + linkExtraCost + estimateToGoal;
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
