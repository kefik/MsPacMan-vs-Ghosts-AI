package game.controllers.pacman.exercises.e4.graph;

import game.controllers.Direction;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.controllers.pacman.modules.Maze.NodeType;

import java.util.HashMap;
import java.util.Map;

/**
 * Maze graph node (a junction / crossroad on the PacMan map).
 */
public class Node {
	
	/**
	 * Unique index of the node.
	 */
	public final int index;
	
	/**
	 * Type of sub-node within the Pac-Man maze.
	 */
	public final NodeType type;
	
	/**
	 * Direction -> Link
	 * 
	 * Node that link is never stored for {@link Direction#NONE}.
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
