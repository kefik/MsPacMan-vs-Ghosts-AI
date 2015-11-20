package game.controllers.pacman.exercises.e1.path.impl;

import game.controllers.pacman.exercises.e1.path.impl.base.UninformedNode;
import game.controllers.pacman.exercises.e1.path.impl.base.UninformedGraphSearch;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * UNIFORM-COST-SEARCH
 *  
 * @author Jimmy
 */
public class UCS extends UninformedGraphSearch {

	@Override
	public String getName() {
		return "UCS[" + getSteps() + "]";
	}

	@Override
	public void reset() {
		super.reset();
	}
	
	@Override
	protected Collection<UninformedNode> createCloseList() {
		return new HashSet<UninformedNode>();
	}

	@Override
	protected Collection<UninformedNode> createOpenList() {		
		return new PriorityQueue<UninformedNode>(20, new Comparator<UninformedNode>() {

			@Override
			public int compare(UninformedNode o1, UninformedNode o2) {
				return o1.pathCost - o2.pathCost;
			}
			
		});
	}

	@Override
	protected UninformedNode selectNextNode(Collection<UninformedNode> openList) {
		return ((PriorityQueue<UninformedNode>)openList).peek();
	}

}
