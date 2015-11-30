package game.controllers.pacman.exercises.e3.search.strategy;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.ISearchStrategy;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;
import game.controllers.pacman.exercises.e3.search.nodes.NodeWithPills;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarStrategyPills implements ISearchStrategy<NodeWithPills> {
	
	public static class NodeWithPillsKey {
		// ASCENDING ORDER!
		public Integer[] pills;
		
		public Node node;

		private int hashCode;
		
		public NodeWithPillsKey(Node node, Integer[] pills) {
			this.node = node;
			this.pills = pills;
			this.hashCode = node.hashCode();
			if (pills != null) {
				for (int pill : pills) {
					hashCode += pill;
				}
			}			
		}
		
		@Override
		public int hashCode() {
			return hashCode;
		}
		
		public boolean equals(Object node) {
			if (node == null) return false;
			if (!(node instanceof NodeWithPillsKey)) return false;		
			if (this.node != ((NodeWithPillsKey)node).node) return false;
			
			NodeWithPillsKey other = (NodeWithPillsKey)node;
			
			if (other.pills.length != pills.length) return false;
			
			for (int index = 0; index < pills.length; ++index) {
				if (other.pills[index] != pills[index]) return false;
			}
			
			return true;
		}	
	}
	
	protected Maze maze;
	
	protected Map<NodeWithPillsKey, NodeWithPills> nodes = new HashMap<NodeWithPillsKey, NodeWithPills>();
	
	public AStarStrategyPills(Maze maze) {
		this.maze = maze;
	}
	
	@Override
	public NodeWithPills getNode(Node node) {
		return nodes.get(node);
	}
	
	@Override
	public NodeWithPills makeFirstNode(Node node) {
		List<Integer> pills = new ArrayList<Integer>();
		for (MazeNode mazeNode : maze.getNodes()) {
			if (mazeNode.pill()) pills.add(mazeNode.pillIndex());
		}
		return new NodeWithPills(node, null, null, 0, 0, 0, 0, 0, pills.toArray(new Integer[pills.size()]));
	}
	
	@Override
	public NodeWithPills makeNode(Node node, Link link, NodeWithPills parent, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal) {
		List<Integer> newPills = new ArrayList<Integer>();
		
		Set<Integer> linkPills = new HashSet<Integer>();
		for (MazeNode mazeNode : link.mazeNodes) {
			if (mazeNode.pill()) linkPills.add(mazeNode.pillIndex());
		}
		
		if (parent.pills != null) {
			for (int pillIndex : parent.pills) {
				if (!linkPills.contains(pillIndex)) newPills.add(pillIndex);
			}
		}
		
		Integer[] newPillsArray = newPills.toArray(new Integer[newPills.size()]);
		
		NodeWithPillsKey key = new NodeWithPillsKey(node, newPillsArray);
		
		NodeWithPills result = nodes.get(key);
		
		if (result != null) {
			return result;
		}
		
		result = new NodeWithPills(node, link, parent, nodeGraphCost, nodeExtraCost, linkGraphCost, linkExtraCost, estimateToGoal, newPillsArray);
		nodes.put(key, result);
		return result;
	}
	
	@Override
	public Collection<NodeWithPills> createCloseList() {
		return new HashSet<NodeWithPills>();
	}

	@Override
	public Collection<NodeWithPills> createOpenList() {	
		return new PriorityQueue<NodeWithPills>(20, 
			new Comparator<NodeWithPills>() {
				@Override
				public int compare(NodeWithPills o1, NodeWithPills o2) {
					return (o1.getTotalCost() + o1.pills.length * 30) - (o2.getTotalCost() + o2.pills.length * 30);
				}
			});
	}

	@Override
	public NodeWithPills selectNextNode(InformedSearch<NodeWithPills> search, Collection<NodeWithPills> openList) {
		return ((PriorityQueue<NodeWithPills>)openList).peek();
	}

}
