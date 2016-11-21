package game.controllers.pacman.exercises.e4.path.uninformed;

import java.util.Collection;

import game.controllers.pacman.exercises.e4.graph.Graph;
import game.controllers.pacman.exercises.e4.graph.Node;
import game.controllers.pacman.exercises.e4.path.Path;
import game.controllers.pacman.exercises.e4.path.PathFinderState;
import game.controllers.pacman.exercises.e4.path.uninformed.base.SearchTreeNode;
import game.controllers.pacman.exercises.e4.path.uninformed.base.UninformedGraphSearch;
import game.controllers.pacman.exercises.e4.path.uninformed.base.UninformedGraphSearchBase;

/**
 * ITERATIVE DEEPENING SEARCH
 * 
 * TODO: implement me!
 * 
 * To make this work you need to:
 * 1) implement {@link DLS},
 * 2) which means to implement {@link DFS},
 * 3) which means to implement {@link UninformedGraphSearch#step()}.
 */
public class IDS extends UninformedGraphSearchBase<Object> {

	private int currentLevel = 0;
	
	private DLS dls = new DLS();
	
	@Override
	public String getName() {
		return "IDS[l= " + currentLevel + ",steps=" + steps + "]";
	}

	@Override
	public void reset() {
		super.reset();
		currentLevel = 0;
		dls.reset();
	}
	
	@Override
	public void init(Graph graph, Node start, Node goal, Object config) {
		super.init(graph, start, goal, config);
		currentLevel = 1;
		dls.init(graph, start, end, new DLS.DLSConfig(currentLevel));
	}

	@Override
	public PathFinderState step() {
		if (state != PathFinderState.RUNNING) return state;
		
		++steps;
		
		// TODO: implement me!
		
		return state;		
	}

	@Override
	public Path getPath() {
		return dls != null ? dls.getPath() : null;
	}

	@Override
	public Node getParent(Node node) {
		return dls != null ? dls.getParent(node) : null;
	}
	
	@Override
	public SearchTreeNode getNodeInfo(Node node) {
		return dls != null ? dls.getNodeInfo(node) : null;
	}

	@Override
	public Collection<Node> getClosedList() {
		return dls != null ? dls.getClosedList() : null;
	}

	@Override
	public Collection<Node> getOpenList() {
		return dls != null ? dls.getOpenList() : null;
	}

}
