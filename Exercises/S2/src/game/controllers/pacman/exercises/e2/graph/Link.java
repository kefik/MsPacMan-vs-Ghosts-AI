package game.controllers.pacman.exercises.e2.graph;

import game.controllers.Direction;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;
import game.core.GameView;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Link {
	
	public final Node n1;
	public final Node n2;
	
	public final MazeNode[] mazeNodes;
	
	public final int distance;
	
	public Link(Node n1, Node n2, List<MazeNode> mazeNodes) {
		this(n1, n2, mazeNodes.toArray(new MazeNode[mazeNodes.size()]));
	}
	
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
	
	public Direction getDirectionFrom(Node node) {
		if (n1 == node) {
			return mazeNodes[1].direction(n1.index).opposite();
		}
		if (n2 == node) {
			return mazeNodes[mazeNodes.length-2].direction(n2.index).opposite();
		}
		return Direction.NONE;
	}
	
	public void debugDrawLink(Game game, Color linkColor, Color nodeColor, boolean drawDistance) {
		GameView.addLinesPath(game, linkColor, n1.index, mazeNodes[0].index);				
		int i = 0;
		int step = 4;
		for (i = step; i < mazeNodes.length-1; i += step) {
			GameView.addLinesPath(game, linkColor, mazeNodes[i-step].index, mazeNodes[i].index);
		}				
		if (i >= mazeNodes.length) {
			GameView.addLinesPath(game, linkColor, mazeNodes[i-step].index, mazeNodes[mazeNodes.length-1].index);
		}
		GameView.addLinesPath(game, linkColor, mazeNodes[mazeNodes.length-1].index, n2.index);
		if (drawDistance) {
			GameView.addText(game, mazeNodes[mazeNodes.length/2].index, Color.YELLOW, String.valueOf(distance));			
		}
		GameView.addPoints(game, nodeColor, n1.index, n2.index);
	}

	/**
	 * Creates a new {@link Node} containing two new {@link Link}s leading to n1 and n2.
	 * 
	 * New links are added neither to 'n1' nor 'n2' (ends of this link).
	 * 
	 * @param splitPoint part of 'mazeNodes'
	 * @return
	 */
	public Node split(MazeNode splitPoint) {
		
		if (splitPoint.index == n1.index) return n1;
		if (splitPoint.index == n2.index) return n2;
		
		Node result = new Node(splitPoint);
		
		List<MazeNode> link1Nodes = new ArrayList<MazeNode>();

		// LINK BETWEEN 'n1' -> 'splitPoint'
		
		int i = 0;
		while (i < mazeNodes.length && mazeNodes[i] != splitPoint) {
			link1Nodes.add(mazeNodes[i]);
			++i;
		}
		
		if (i >= mazeNodes.length) {
 			// mazeNode cannot be found within 'mazeNodes'
			throw new RuntimeException("Cannot split the link, provided 'mazeNode' does not lie inside the link.");
		}
		
		// add splitPoint as well
		link1Nodes.add(mazeNodes[i]);
		
		Direction dir1 = splitPoint.direction(link1Nodes.get(link1Nodes.size()-2));
		if (dir1 == Direction.NONE) throw new RuntimeException("Invalid");
		
		Link link1 = new Link(n1, result, link1Nodes);
		
		result.links.put(dir1, link1);
		
		// LINK BETWEEN 'splitPoint' -> 'n2'

		List<MazeNode> link2Nodes = new ArrayList<MazeNode>();
		
		// begin with splitPoint
		link2Nodes.add(mazeNodes[i]);
		
		++i; // walk over 'splitPoint'
		while (i < mazeNodes.length) {
			link2Nodes.add(mazeNodes[i]);
			++i;
		}
		
		Direction dir2 = splitPoint.direction(link2Nodes.get(1));
		if (dir2 == Direction.NONE) throw new RuntimeException("Invalid");
		
		Link link2 = new Link(result, n2, link2Nodes);
		
		result.links.put(dir2, link2);
		
		return result;
	}

}
