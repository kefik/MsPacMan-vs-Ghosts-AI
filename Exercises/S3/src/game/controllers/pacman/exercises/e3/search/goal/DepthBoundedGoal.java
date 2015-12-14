package game.controllers.pacman.exercises.e3.search.goal;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.ISearchGoal;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;
import game.controllers.pacman.exercises.e3.search.nodes.InformedNode;
import game.controllers.pacman.exercises.e3.search.nodes.NodeWithPills;
import game.core.Game;

public class DepthBoundedGoal implements ISearchGoal<NodeWithPills> {

	private Game game;
	private Node start;
	private int depth;
	
	public DepthBoundedGoal(Game game, Node start, int depth) {
		this.game = game;
		this.start = start;
		this.depth = depth;
	}
	
	@Override
	public Node getStart() {
		return start;
	}

	@Override
	public boolean isGoal(InformedSearch<NodeWithPills> search, NodeWithPills node) {		
		return node.nodeLevel >= depth || node.pills.length == 0;
	}

	@Override
	public int estimate(InformedSearch<NodeWithPills> search, Node node, Link link, NodeWithPills parent) {
		return 0;
	}

}
