package game.controllers.pacman.exercises.e5.search.goal;

import game.controllers.pacman.exercises.e5.graph.maze.MazeGraphNode;
import game.controllers.pacman.exercises.e5.graph.maze.MazeLink;
import game.controllers.pacman.exercises.e5.search.InformedNode;
import game.controllers.pacman.exercises.e5.search.InformedSearch;
import game.controllers.pacman.exercises.e5.search.base.ISearchGoal;
import game.core.Game;

public class NodeGoal<SEARCH_NODE extends InformedNode> implements ISearchGoal<MazeGraphNode, MazeLink, SEARCH_NODE> {

	private Game game;
	
	private InformedSearch search;
	
	private MazeGraphNode start;
	private MazeGraphNode end;
	
	public NodeGoal(Game game, MazeGraphNode start, MazeGraphNode end) {
		this.game = game;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void setContext(InformedSearch search) {
		this.search = search;
	}
	
	@Override
	public MazeGraphNode getStart() {
		return start;
	}

	@Override
	public boolean isGoal(SEARCH_NODE node) {		
		return node.node == end;
	}

	@Override
	public int estimate(SEARCH_NODE parent, MazeLink link, MazeGraphNode node) {
		MazeGraphNode from = node;
		MazeGraphNode to = end;
		
		int width = 20*4;
		
		int fromX = game.getX(from.index);
		int fromY = game.getY(from.index);
		int toX   = game.getX(to.index);
		int toY   = game.getY(to.index);
		
		int diffX1 = Math.abs(fromX - toX);
		int diffX2 = Math.abs(width - Math.abs(fromX - toX));
		
		int distance = Math.min(diffX1, diffX2) + Math.abs(fromY - toY);
		
		return distance;
	}

}
