package game.controllers.pacman.exercises.e1.path.impl;

import game.controllers.pacman.exercises.e1.path.impl.base.UninformedNode;
import game.controllers.pacman.exercises.e1.path.impl.base.UninformedGraphSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * BREADTH-FIRST-SEARCH
 * 
 * @author Jimmy
 */
public class BFS extends UninformedGraphSearch {

	@Override
	public String getName() {
		return "BFS[" + getSteps() + "]";
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
		return ((List<UninformedNode>)openList).get(0);
	}

}
