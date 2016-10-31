package game.controllers.pacman.exercises.e5.search;

import java.util.Collection;

import game.controllers.pacman.exercises.e5.graph.ILink;
import game.controllers.pacman.exercises.e5.graph.INode;
import game.controllers.pacman.exercises.e5.search.base.IGraphView;

public class InformedNode<NODE extends INode, LINK extends ILink<NODE>> implements INode {

	/**
	 * Wrapped node.
	 */
	public NODE node;
	
	/**
	 * Link we have traveled to get to this node.
	 */
	public LINK link;
	
	/**
	 * Parent node on the path from START to this node.
	 */
	public InformedNode<NODE, LINK> parent;
	
	/**
	 * Cost for the path to parent.
	 */
	public int parentPathCost;
	
	/**
	 * Original NODE COST from the graph.
	 */
	public int nodeGraphCost;
	
	/**
	 * MazeNode EXTRA COST as determined by {@link IGraphView}.
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
	
	public InformedNode(NODE node) {
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
	
	public InformedNode(NODE node, LINK link, InformedNode parent, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal) {
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
	
	@Override
	public Collection<LINK> getLinks() {
		return (Collection)node.getLinks();
	}		
	
	public void updateParent(InformedNode parent, LINK link, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal) {
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
