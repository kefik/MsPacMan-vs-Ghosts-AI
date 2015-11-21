package game.controllers.pacman.exercises.e3.search.goal;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.ISearchGoal;
import game.controllers.pacman.exercises.e3.search.base.InformedNode;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;
import game.core.Game;

public class NodeGoal implements ISearchGoal<InformedNode> {

	private Game game;
	private Node start;
	private Node end;
	
	public NodeGoal(Game game, Node start, Node end) {
		this.game = game;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public Node getStart() {
		return start;
	}

	@Override
	public boolean isGoal(InformedSearch<InformedNode> search, InformedNode node) {
		return node.node == end;
	}

	@Override
	public int estimate(InformedSearch<InformedNode> search, Node node, Link link, InformedNode parent) {
		Node from = node;
		Node to = end;
		
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
