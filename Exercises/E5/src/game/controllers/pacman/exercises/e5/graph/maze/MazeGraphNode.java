package game.controllers.pacman.exercises.e5.graph.maze;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import game.controllers.Direction;
import game.controllers.pacman.exercises.e5.graph.INode;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.controllers.pacman.modules.Maze.NodeType;

public class MazeGraphNode implements INode {
	
	public final int index;
	public final NodeType type;
	public final MazeNode mazeNode;
	
	/**
	 * Direction -> Link
	 */
	public Map<Direction, MazeLink> links = new HashMap<Direction, MazeLink>();
		
	public MazeGraphNode(MazeNode node) {
		this.index = node.index;
		this.type = node.nodeType();
		this.mazeNode = node;
	}
	
	@Override
	public String toString() {
		return "MazeGraphNode[index=" + index + ",type=" + type + "]";
	}

	@Override
	public Collection<MazeLink> getLinks() {
		return links.values();		
	}
}
