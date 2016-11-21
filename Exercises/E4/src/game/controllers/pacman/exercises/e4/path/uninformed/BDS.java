package game.controllers.pacman.exercises.e4.path.uninformed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import game.controllers.pacman.exercises.e4.graph.Graph;
import game.controllers.pacman.exercises.e4.graph.Node;
import game.controllers.pacman.exercises.e4.path.Path;
import game.controllers.pacman.exercises.e4.path.PathFinderState;
import game.controllers.pacman.exercises.e4.path.uninformed.base.SearchTreeNode;
import game.controllers.pacman.exercises.e4.path.uninformed.base.UninformedGraphSearch;
import game.controllers.pacman.exercises.e4.path.uninformed.base.UninformedGraphSearchBase;

/**
 * BIDIRECTIONAL SEARCH
 * 
 * TODO: implement me! 
 * 
 * To make this work you need to:
 * 1) implement {@link BFS},
 * 2) which means to implement {@link UninformedGraphSearch#step()}.
 */
public class BDS extends UninformedGraphSearchBase<Object> {

	private BFS bfs1 = new BFS();
	private BFS bfs2 = new BFS();
	
	private Path path;
	
	@Override
	public String getName() {
		return "BDS[" + steps + "]";
	}
	
	@Override
	public void reset() {
		super.reset();
		path = null;
		bfs1.reset();
		bfs2.reset();
	}
	
	@Override
	public void init(Graph graph, Node start, Node goal, Object config) {
		super.init(graph, start, goal, config);
		
		bfs1.init(graph, start, goal, config);
		bfs2.init(graph, goal, start, config);
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
		return this.path;
	}

	@Override
	public Node getParent(Node node) {
		Node result;
		result = bfs1.getParent(node);
		if (result != null) return result;
		result = bfs2.getParent(node);
		if (result != null) return result;
		return null;
	}

	@Override
	public SearchTreeNode getNodeInfo(Node node) {
		SearchTreeNode result;
		result = bfs1.getNodeInfo(node);
		if (result != null) return result;
		result = bfs2.getNodeInfo(node);
		if (result != null) return result;
		return null;
	}

	@Override
	public Collection<Node> getClosedList() {
		List<Node> result = new ArrayList<Node>();
		result.addAll(bfs1.getClosedList());
		result.addAll(bfs2.getClosedList());
		return result;
	}

	@Override
	public Collection<Node> getOpenList() {
		List<Node> result = new ArrayList<Node>();
		result.addAll(bfs1.getOpenList());
		result.addAll(bfs2.getOpenList());
		return result;
	}

}
