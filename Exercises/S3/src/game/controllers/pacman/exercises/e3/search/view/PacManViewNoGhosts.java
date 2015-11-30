package game.controllers.pacman.exercises.e3.search.view;

import game.controllers.pacman.exercises.e3.graph.Graph;
import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;

public class PacManViewNoGhosts extends PacManView {

	public PacManViewNoGhosts(Maze maze, Graph graph, MazeNode... extraNodes) {
		super(maze, graph, extraNodes);
	}

	@Override
	public boolean isNodeOpened(Node node) {
		MazeNode mazeNode = maze.getNode(node.index);
		return !mazeNode.ghostDanger();	
	}
	
	@Override
	public boolean isLinkOpened(Node nodeFrom, Link link) {
		for (MazeNode mazeNode : link.mazeNodes) {
			if (mazeNode.ghostDanger()) return false;
		}
		return true;
	}
}
