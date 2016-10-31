package game.controllers.pacman.exercises.e5.graph.maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import game.controllers.Direction;
import game.controllers.pacman.exercises.e5.graph.IGraph;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;

public class MazeGraph implements IGraph<MazeGraphNode, MazeLink>{

	private Maze maze;
	
	/**
	 * Node index -> Node
	 */
	private Map<Integer, MazeGraphNode> nodes = new HashMap<Integer, MazeGraphNode>();
	
	/**
	 * Set of all existing links.
	 */
	private Set<MazeLink> links = new HashSet<MazeLink>();
	
	/**
	 * Node index -> Link
	 */
	private Map<Integer, MazeLink> node2Link = new HashMap<Integer, MazeLink>();
	
	public MazeGraph(Maze maze) {
		this.maze = maze;
		build();
	}
	
	private void build() {
		// we will go through all the maze nodes looking for "crossroads"
		for (MazeNode node : maze.getNodes()) {
			// now we are testing if 'node' is a cross road
			if (!node.junction()) continue;
			
			// then we will query of the directions we can take from that crossroad
			for (Direction dir : Direction.arrows()) {
				// and if some path in 'dir' exists
				if (!node.hasLink(dir)) continue;
				
				// then we will be building a list for a new LINK
				
				List<MazeNode> newLink = new ArrayList<MazeNode>();
				
				newLink.add(node);
				
				MazeNode next = null;
				MazeNode current = node;
				MazeNode previous = null;
				
				previous = current;
				current = node.link(dir);
				
				while (!current.junction()) {
					newLink.add(current);
					
					for (Direction nextDir : Direction.arrows()) {
						if (current.hasLink(nextDir) && current.link(nextDir) != previous) {
							next = current.link(nextDir);
							break;
						}
					}					
					previous = current;
					current = next;
					next = null;
				}				
				newLink.add(current);
				
				makeLink(newLink.toArray(new MazeNode[0]));
			}
		}
		
		
	}
	
	/**
	 * Get or make-add-return a node for 'mazeNode'.
	 * @param mazeNode
	 * @return
	 */
	public MazeGraphNode makeNode(MazeNode mazeNode) {
		if (mazeNode == null) return null;
		MazeGraphNode node = getNode(mazeNode.index);
		if (node != null) return node;
		node = new MazeGraphNode(mazeNode);
		nodes.put(node.index, node);
		return node;
	}
	
	/**
	 * Get or make-add-return a link running between 'from' / 'to'.
	 * @param from
	 * @param to
	 * @return
	 */
	public MazeLink makeLink(MazeNode[] mazeNodes) {
		if (mazeNodes == null || mazeNodes.length < 2) return null;
		MazeGraphNode nodeFrom = makeNode(mazeNodes[0]);
		MazeGraphNode nodeTo = makeNode(mazeNodes[mazeNodes.length-1]);
		
		Direction dirFromTo = mazeNodes[0].direction(mazeNodes[1]);
		
		MazeLink link = nodeFrom.links.get(dirFromTo);
		if (link != null) return link;
		
		MazeNode[] between = Arrays.copyOfRange(mazeNodes, 1, mazeNodes.length-1);
		
		link = new MazeLink(nodeFrom, nodeTo, between);
		
		for (MazeNode mazeNode : mazeNodes) {
			node2Link.put(mazeNode.index, link);
		}
		
		nodeFrom.links.put(dirFromTo, link);		
		
		Direction dirToFrom = mazeNodes[mazeNodes.length-1].direction(mazeNodes[mazeNodes.length-2]);
		
		nodeTo.links.put(dirToFrom, link);
		
		links.add(link);
				
		if (link.distance == 0) {
			System.out.println("Wierd link: [" + nodeFrom.index + "]-" + dirFromTo + "<--(" + link.distance + ")-->" + dirToFrom + "[" + nodeTo.index + "]");						
		}
		
		return link;
	}
	
	public Collection<MazeGraphNode> getNodes() {
		return nodes.values();
	}
	
	public MazeGraphNode getNode(int index) {
		return nodes.get(index);
	}
	
	public MazeGraphNode getRandomNode() {
		int rnd = new Random().nextInt(nodes.size());
		if (rnd < 0) return null;
		for (MazeGraphNode node : nodes.values()) {
			if (rnd <= 0) return node;
			--rnd;
		}
		return null;
	}
	
	public Set<MazeLink> getLinks() {
		return links;
	}
	
	public MazeLink getLink(int index) {
		return node2Link.get(index);
	}
	
}
