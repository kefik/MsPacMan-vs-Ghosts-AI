package game.controllers.pacman.exercises.e3.search.strategy;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.ISearchStrategy;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;
import game.controllers.pacman.exercises.e3.search.nodes.InformedNode;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarTreeSearch implements ISearchStrategy<InformedNode> {
	
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
		return new Collection<InformedNode>() {
			
			@Override
			public <T> T[] toArray(T[] a) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object[] toArray() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public boolean retainAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean removeAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean remove(Object o) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Iterator<InformedNode> iterator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean containsAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean contains(Object o) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean addAll(Collection<? extends InformedNode> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean add(InformedNode e) {
				// TODO Auto-generated method stub
				return false;
			}
		};
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
