package game.controllers.pacman.exercises.e2.graph;

import game.controllers.Direction;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.controllers.pacman.modules.Maze.NodeType;

import java.util.HashMap;
import java.util.Map;

public class Node {
	
	public final int index;
	public final NodeType type;
	
	/**
	 * Direction -> Link
	 */
	public Map<Direction, Link> links = new HashMap<Direction, Link>();
	
	public Node(int index, NodeType type) {
		this.index = index;
		this.type = type;
	}
	
	public Node(MazeNode node) {
		this.index = node.index;
		this.type = node.nodeType();
	}

	public Link getLink(Node next) {
		for (Link link : links.values()) {
			if (link.getOtherEnd(this) == next) return link;
		}
		return null;
	}
	
}
