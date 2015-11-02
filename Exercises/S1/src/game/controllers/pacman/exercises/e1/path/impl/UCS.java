package game.controllers.pacman.exercises.e1.path.impl;

import game.controllers.pacman.exercises.e1.path.impl.base.SearchNode;
import game.controllers.pacman.exercises.e1.path.impl.base.UninformedGraphSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * UNIFORM-COST-SEARCH
 *  
 * @author Jimmy
 */
public class UCS extends UninformedGraphSearch {

	private SearchNode best;
	
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
	protected Collection<SearchNode> createCloseList() {
		return new HashSet<SearchNode>();
	}

	@Override
	protected Collection<SearchNode> createOpenList() {		
		return new ArrayList<SearchNode>() {
			
			@Override
			public boolean add(SearchNode e) {
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
	protected SearchNode selectNextNode(Collection<SearchNode> openList) {
		return best;
	}

}
