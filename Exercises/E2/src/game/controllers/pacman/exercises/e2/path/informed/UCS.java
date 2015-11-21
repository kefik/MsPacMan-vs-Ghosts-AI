package game.controllers.pacman.exercises.e2.path.informed;


import game.controllers.pacman.exercises.e2.path.informed.base.UninformedGraphSearch;
import game.controllers.pacman.exercises.e2.path.informed.base.UninformedNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * TODO: 
 * TASK1: turn implementation of UNIFORM-COST-SEARCH into A-Star
 * TASK2: include IGraphView
 *  
 * @author Jimmy
 */
public class UCS extends UninformedGraphSearch {

	private UninformedNode best;
	
	@Override
	public String getName() {
		return "UFS[" + getSteps() + "]";
	}

	@Override
	public void reset() {
		super.reset();
		best = null;
	}
	
	@Override
	protected Collection<UninformedNode> createCloseList() {
		return new HashSet<UninformedNode>();
	}

	@Override
	protected Collection<UninformedNode> createOpenList() {		
		return new ArrayList<UninformedNode>() {
			
			@Override
			public boolean add(UninformedNode e) {
				boolean result = super.add(e);
				if (!result) return false;
				if (best == null) {
					best = e;
				} else {
					if (best.pathCost > e.pathCost) {
						best = e;
					}
				}
				return true;
			}
			
			@Override
			public boolean remove(Object o) {
				boolean result = super.remove(o);				
				if (!result) return false;
				
				if (o == best) {
					if (size() == 0) best = null;
					else {
						best = get(0);
						for (int i = 1; i < size(); ++i) {
							if (get(i).pathCost < best.pathCost) {
								best = get(i);
							}
						}
					}
				}
				
				return true;
			}
			
		};
	}

	@Override
	protected UninformedNode selectNextNode(Collection<UninformedNode> openList) {
		return best;
	}

}
