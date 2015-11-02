package game.controllers.pacman.exercises.e1.path.impl;

import game.controllers.pacman.exercises.e1.path.impl.base.SearchNode;
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
	protected Collection<SearchNode> createCloseList() {
		return new HashSet<SearchNode>();
	}

	@Override
	protected Collection<SearchNode> createOpenList() {		
		return new ArrayList<SearchNode>();
	}

	@Override
	protected SearchNode selectNextNode(Collection<SearchNode> openList) {
		return ((List<SearchNode>)openList).get(openList.size()-1);
	}


}
