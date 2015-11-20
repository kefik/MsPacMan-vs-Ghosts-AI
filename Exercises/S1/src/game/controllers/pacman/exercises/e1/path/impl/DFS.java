package game.controllers.pacman.exercises.e1.path.impl;

import game.controllers.pacman.exercises.e1.path.impl.base.UninformedNode;
import game.controllers.pacman.exercises.e1.path.impl.base.UninformedGraphSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * DEAPTH-FIRST-SEARCH
 *
 * @author Jimmy
 */
public class DFS extends UninformedGraphSearch {

	@Override
	public String getName() {
		return "DFS[" + getSteps() + "]";
	}

	@Override
	protected Collection<UninformedNode> createCloseList() {
		return new HashSet<UninformedNode>();
	}

	@Override
	protected Collection<UninformedNode> createOpenList() {		
		return new ArrayList<UninformedNode>();
	}

	@Override
	protected UninformedNode selectNextNode(Collection<UninformedNode> openList) {
		return ((List<UninformedNode>)openList).get(openList.size()-1);
	}


}
