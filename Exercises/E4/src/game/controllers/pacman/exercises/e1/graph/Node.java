package game.controllers.pacman.exercises.e1.graph;

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
	
	@Override
	public String toString() {
		return "Node[index=" + index + ",type=" + type + "]";
	}
	
}
