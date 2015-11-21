package game.controllers.pacman.exercises.e3.search.strategy;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.ISearchStrategy;
import game.controllers.pacman.exercises.e3.search.base.InformedNode;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarStrategy implements ISearchStrategy<InformedNode> {
	
	protected Map<Node, InformedNode> nodes = new HashMap<Node, InformedNode>();
	
	@Override
	public InformedNode getNode(Node node) {
		return nodes.get(node);
	}
	
	@Override
	public InformedNode makeFirstNode(Node node) {
		return new InformedNode(node);
	}
	
	@Override
	public InformedNode makeNode(Node node, Link link, InformedNode parent, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal) {
		InformedNode result = nodes.get(node);
		if (result != null) {
			return result;
		}
		result = new InformedNode(node, link, parent, nodeGraphCost, nodeExtraCost, linkGraphCost, linkExtraCost, estimateToGoal);
		nodes.put(node, result);
		return result;
	}
	
	@Override
	public Collection<InformedNode> createCloseList() {
		return new HashSet<InformedNode>();
	}

	@Override
	public Collection<InformedNode> createOpenList() {	
		return new PriorityQueue<InformedNode>(20, 
			new Comparator<InformedNode>() {
				@Override
				public int compare(InformedNode o1, InformedNode o2) {
					return o1.getTotalCost() - o2.getTotalCost();
				}
			});
	}

	@Override
	public InformedNode selectNextNode(InformedSearch<InformedNode> search, Collection<InformedNode> openList) {
		return ((PriorityQueue<InformedNode>)openList).peek();
	}

}
