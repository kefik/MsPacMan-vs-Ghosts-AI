package game.controllers.pacman.exercises.e1.graph;

import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;
import game.core.GameView;

import java.awt.Color;

public class Link {
	
	public final Node n1;
	public final Node n2;
	
	public final MazeNode[] mazeNodes;
	
	public final int distance;
	
	public Link(Node n1, Node n2, MazeNode... mazeNodes) {
		this.n1 = n1;
		this.n2 = n2;
		this.mazeNodes = mazeNodes;
		distance = mazeNodes.length-1;
	}
	
	public Node getOtherEnd(Node node) {
		if (node == n1) return n2;
		if (node == n2) return n1;
		return null;
	}
	
	public void debugDrawLink(Game game, Color linkColor, Color nodeColor, boolean drawDistance) {
		GameView.addLines(game, linkColor, n1.index, mazeNodes[0].index);				
		int i = 0;
		int step = 4;
		for (i = step; i < mazeNodes.length-1; i += step) {
			GameView.addLines(game, linkColor, mazeNodes[i-step].index, mazeNodes[i].index);
		}				
		if (i >= mazeNodes.length) {
			GameView.addLines(game, linkColor, mazeNodes[i-step].index, mazeNodes[mazeNodes.length-1].index);
		}
		GameView.addLines(game, linkColor, mazeNodes[mazeNodes.length-1].index, n2.index);
		if (drawDistance) {
			GameView.addText(game, mazeNodes[mazeNodes.length/2].index, Color.YELLOW, String.valueOf(distance));			
		}
		GameView.addPoints(game, nodeColor, n1.index, n2.index);
	}

}
