package game.controllers.pacman.exercises.e5.graph.maze;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.controllers.Direction;
import game.controllers.pacman.exercises.e5.graph.ILink;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;
import game.core.GameView;

public class MazeLink implements ILink<MazeGraphNode> {
	
	public final MazeGraphNode n1;
	public final MazeGraphNode n2;
	
	public final MazeNode[] linkInternalNodes;
	
	public final int distance;
	
	public MazeLink(MazeGraphNode n1, MazeGraphNode n2, List<MazeNode> linkInternalNodes) {
		this(n1, n2, linkInternalNodes.toArray(new MazeNode[linkInternalNodes.size()]));
	}
	
	/**
	 * N1 -> linkInternalNodes -> N2
	 * @param n1
	 * @param n2
	 * @param linkInternalNodes
	 */
	public MazeLink(MazeGraphNode n1, MazeGraphNode n2, MazeNode... linkInternalNodes) {
		this.n1 = n1;
		this.n2 = n2;
		this.linkInternalNodes = linkInternalNodes;
		distance = linkInternalNodes.length+1;
	}
	
	@Override
	public MazeGraphNode getFirst() {
		return n1;
	}

	@Override
	public MazeGraphNode getSecond() {
		// TODO Auto-generated method stub
		return n2;
	}
	
	@Override
	public int getCost() {
		return distance;
	}
	
	@Override
	public MazeGraphNode getOther(MazeGraphNode node) {
		if (node == n1) return n2;
		if (node == n2) return n1;
		return null;
	}
	
	// ---------
	// DEBUGGING
	// ---------
	
	public void debugDrawLink(Game game, Color linkColor, Color nodeColor, boolean drawDistance) {
		GameView.addLines(game, linkColor, n1.index, linkInternalNodes[0].index);				
		int i = 0;
		int step = 4;
		for (i = step; i < linkInternalNodes.length-1; i += step) {
			GameView.addLines(game, linkColor, linkInternalNodes[i-step].index, linkInternalNodes[i].index);
		}				
		if (i >= linkInternalNodes.length) {
			GameView.addLines(game, linkColor, linkInternalNodes[i-step].index, linkInternalNodes[linkInternalNodes.length-1].index);
		}
		GameView.addLines(game, linkColor, linkInternalNodes[linkInternalNodes.length-1].index, n2.index);
		if (drawDistance) {
			GameView.addText(game, linkInternalNodes[linkInternalNodes.length/2].index, Color.YELLOW, String.valueOf(distance));			
		}
		GameView.addPoints(game, nodeColor, n1.index, n2.index);
	}
	
	// --------------
	// PACMAN SENSORS
	// --------------
	
	/**
	 * Returns direction you should take from 'node' to begin traveling through this link.
	 * @param node
	 * @return
	 */
	public Direction getDirectionFrom(MazeGraphNode node) {
		if (n1 == node) {
			if (linkInternalNodes.length == 0) {
				return n1.mazeNode.direction(n2.index);
			}
			return linkInternalNodes[0].direction(n1.index).opposite();
		}
		if (n2 == node) {
			if (linkInternalNodes.length == 0) {
				return n2.mazeNode.direction(n1.index);
			}
			return linkInternalNodes[linkInternalNodes.length-1].direction(n2.index).opposite();
		}
		return Direction.NONE;
	}

	/**
	 * Is there a ghost somewhere on this link? (Including boundary nodes.)
	 * @param maze
	 * @return
	 */
	public boolean isGhost() {
		for (MazeNode node : linkInternalNodes) {
			if (node.ghost()) return true;
		}
		return false;
	}
	
	/**
	 * Is there a ghost that can eat Ms PacMan somewhere on this link? (Including boundary nodes.)
	 * @param maze
	 * @return
	 */
	public boolean isGhostDanger() {
		for (MazeNode node : linkInternalNodes) {
			if (node.ghostDanger()) return true;
		}
		return false;
	}
	
	/**
	 * Is there a ghost that can be eaten by Ms PacMan somewhere on this link? (Including boundary nodes.)
	 * @param maze
	 * @return
	 */
	public boolean isGhostEdible() {
		for (MazeNode node : linkInternalNodes) {
			if (node.ghostEdible()) return true;
		}
		return false;
	}
	
	/**
	 * Is there a power pill somewhere on this link?
	 * @return
	 */
	public boolean isPowerPill() {
		for (MazeNode node : linkInternalNodes) {
			if (node.powerPill()) return true;
		}	
		return false;
	}
	
	/**
	 * How many pills are on this link.
	 * @return
	 */
	public int getPillCount() {
		int result = 0;
		for (MazeNode node : linkInternalNodes) {
			if (node.pill()) ++result;
		}
		return result;
	}
	
	// ---------
	// UTILITIES
	// ---------
	
	/**
	 * Creates a new {@link MazeGraphNode} containing two new {@link Link}s leading to n1 and n2.
	 * 
	 * New links are added neither to 'n1' nor 'n2' (ends of this link).
	 * 
	 * @param splitPoint part of 'mazeNodes'
	 * @return
	 */
	public MazeGraphNode split(MazeNode splitPoint) {
		
		if (splitPoint.index == n1.index) return n1;
		if (splitPoint.index == n2.index) return n2;
		
		MazeGraphNode result = new MazeGraphNode(splitPoint);
		
		List<MazeNode> link1Nodes = new ArrayList<MazeNode>();

		// LINK BETWEEN 'n1' -> 'splitPoint'
		
		int i = 0;
		while (i < linkInternalNodes.length && linkInternalNodes[i] != splitPoint) {
			link1Nodes.add(linkInternalNodes[i]);
			++i;
		}
		
		if (i >= linkInternalNodes.length) {
 			// mazeNode cannot be found within 'mazeNodes'
			throw new RuntimeException("Cannot split the link, provided 'mazeNode' does not lie inside the link.");
		}
		
		// DO NOT ADD SPLIT POINT
		MazeNode previousToSplitPoint = (link1Nodes.size() > 0 ? link1Nodes.get(link1Nodes.size()-1) : n1.mazeNode);
		
		Direction dir1 = splitPoint.direction(previousToSplitPoint);
		if (dir1 == Direction.NONE) throw new RuntimeException("Invalid");
		
		MazeLink link1 = new MazeLink(n1, result, link1Nodes);
		
		result.links.put(dir1, link1);
		
		// LINK BETWEEN 'splitPoint' -> 'n2'

		List<MazeNode> link2Nodes = new ArrayList<MazeNode>();
		
		// DO NOT BEGIN WITH SPLIT POINT
		//link2Nodes.add(linkInternalNodes[i]);
		
		++i; // walk over 'splitPoint'
		while (i < linkInternalNodes.length) {
			link2Nodes.add(linkInternalNodes[i]);
			++i;
		}
		
		MazeNode nextToSplitPoint = (link2Nodes.size() > 0 ? link2Nodes.get(0) : n2.mazeNode);
		Direction dir2 = splitPoint.direction(nextToSplitPoint);
		if (dir2 == Direction.NONE) throw new RuntimeException("Invalid");
		
		MazeLink link2 = new MazeLink(result, n2, link2Nodes);
		
		result.links.put(dir2, link2);
		
		return result;
	}

	@Override
	public String toString() {
		return "MazeLink[" + n1.index + " <-> " + n2.index + "]";
	}
	

}
