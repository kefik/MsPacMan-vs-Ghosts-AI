package game.controllers.pacman.exercises.e5.search.strategy;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import game.controllers.pacman.exercises.e5.graph.maze.MazeGraphNode;
import game.controllers.pacman.exercises.e5.graph.maze.MazeLink;
import game.controllers.pacman.exercises.e5.search.InformedNode;
import game.controllers.pacman.exercises.e5.search.InformedSearch;
import game.controllers.pacman.exercises.e5.search.base.ISearchStrategy;

public class AStarStrategy<SEARCH_NODE extends InformedNode> implements ISearchStrategy<MazeGraphNode, MazeLink, SEARCH_NODE> {
	
	protected InformedSearch search;
	
	protected Map<MazeGraphNode, SEARCH_NODE> nodes = new HashMap<MazeGraphNode, SEARCH_NODE>();
	
	@Override
	public void setContext(InformedSearch search) {
		this.search = search;
	}
	
	@Override
	public SEARCH_NODE getNode(MazeGraphNode node) {
		return nodes.get(node);
	}
	
	@Override
	public SEARCH_NODE makeInitialNode(MazeGraphNode node) {
		return (SEARCH_NODE)new InformedNode(node);
	}
	
	@Override
	public SEARCH_NODE makeNode(MazeGraphNode node, MazeLink link, SEARCH_NODE parent, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal) {
		SEARCH_NODE result = nodes.get(node);
		if (result != null) {
			return result;
		}
		result = (SEARCH_NODE)new InformedNode(node, link, parent, nodeGraphCost, nodeExtraCost, linkGraphCost, linkExtraCost, estimateToGoal);
		nodes.put(node, result);
		return (SEARCH_NODE)result;
	}
	
	@Override
	public Collection<SEARCH_NODE> createCloseList() {
		return new HashSet<SEARCH_NODE>();
	}

	@Override
	public Collection<SEARCH_NODE> createOpenList() {	
		return new PriorityQueue<SEARCH_NODE>(20, 
			new Comparator<SEARCH_NODE>() {
				@Override
				public int compare(SEARCH_NODE o1, SEARCH_NODE o2) {
					return o1.getTotalCost() - o2.getTotalCost();
				}
			});
	}

	@Override
	public SEARCH_NODE selectNextNode(Collection<SEARCH_NODE> openList) {
		return ((PriorityQueue<SEARCH_NODE>)openList).peek();
	}

}
