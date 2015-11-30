package game.controllers.pacman.exercises.e3.search.nodes;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;

public class NodeWithPills extends InformedNode {

	protected int hashCode;
	
	// ASCENDING ORDER!
	public Integer[] pills;

	public NodeWithPills(
			Node node, Link link, InformedNode parent,
			int nodeGraphCost, int nodeExtraCost, int linkGraphCost,
			int linkExtraCost, int estimateToGoal,
			Integer[] pills
	) 
	{
		super(node, link, parent, nodeGraphCost, nodeExtraCost, linkGraphCost, linkExtraCost, estimateToGoal);
		this.pills = pills;
		
		this.hashCode = node.hashCode();
		if (pills != null) {
			for (int pill : pills) {
				hashCode += pill;
			}
		}
	}
	
	public NodeWithPills(Node node) {
		this(node, null, null, 0, 0, 0, 0, 0, null);
	}

	public int hashCode() {
		return hashCode;
	}
	
	public boolean equals(Object node) {
		if (node == null) return false;
		if (!(node instanceof NodeWithPills)) return false;		
		if (this.node != ((InformedNode)node).node) return false;
		
		NodeWithPills other = (NodeWithPills)node;
		
		if (other.pills.length != pills.length) return false;
		
		for (int index = 0; index < pills.length; ++index) {
			if (other.pills[index] != pills[index]) return false;
		}
		
		return true;
	}	

}
