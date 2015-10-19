package game.controllers.pacman.exercises.e1.graph;

import game.controllers.pacman.modules.Maze.MazeNode;

public class Link {
	
	public final Node n1;
	public final Node n2;
	
	public final MazeNode[] mazeNodes;
	
	public final int distance;
	
	public Link(Node n1, Node n2, MazeNode... mazeNodes) {
		this.n1 = n1;
		this.n2 = n2;
		this.mazeNodes = mazeNodes;
		this.distance = mazeNodes.length-1;
	}
	
	public Node getOtherEnd(Node node) {
		if (node == n1) return n2;
		if (node == n2) return n1;
		return null;
	}

}
