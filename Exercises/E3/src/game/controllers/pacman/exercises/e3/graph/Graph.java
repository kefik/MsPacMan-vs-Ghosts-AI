package game.controllers.pacman.exercises.e3.graph;

import game.controllers.Direction;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Graph {

	private Maze maze;
	
	/**
	 * Node index -> Node
	 */
	private Map<Integer, Node> nodes = new HashMap<Integer, Node>();
	
	/**
	 * Set of all existing links.
	 */
	private Set<Link> links = new HashSet<Link>();
	
	/**
	 * Node index -> Link
	 */
	private Map<Integer, Link> node2Link = new HashMap<Integer, Link>();
	
	public Graph(Maze maze) {
		this.maze = maze;
		build();
	}
	
	private void build() {
		// TODO: delete following example...
		
		// GRAPH IS BUILD LIKE THIS
		// 1: obtain an array of MazeNodes 
		
//		MazeNode[] mazeNodes = new MazeNode[20];
//		mazeNodes[0] = maze.getNodes()[maze.getNodes().length / 4];
//		for (int i = 1; i < mazeNodes.length; ++i) {
//			mazeNodes[i] = mazeNodes[i-1].getRandomLink(i > 1 ? mazeNodes[i-2] : null);
//		}
//		
//		// 2: make a link out of them (auto create Node(s) if required)
//		makeLink(mazeNodes);		
		
		
		// we will go through all the mazenodes looking for "crossroads"
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
	public Node makeNode(MazeNode mazeNode) {
		if (mazeNode == null) return null;
		Node node = getNode(mazeNode.index);
		if (node != null) return node;
		node = new Node(mazeNode);
		nodes.put(node.index, node);
		return node;
	}
	
	/**
	 * Get or make-add-return a link running between 'from' / 'to'.
	 * @param from
	 * @param to
	 * @return
	 */
	public Link makeLink(MazeNode[] mazeNodes) {
		if (mazeNodes == null || mazeNodes.length < 2) return null;
		Node nodeFrom = makeNode(mazeNodes[0]);
		Node nodeTo = makeNode(mazeNodes[mazeNodes.length-1]);
		
		Direction dirFromTo = mazeNodes[0].direction(mazeNodes[1]);
		
		Link link = nodeFrom.links.get(dirFromTo);
		if (link != null) return link;
		
		MazeNode[] between = Arrays.copyOfRange(mazeNodes, 0, mazeNodes.length);
		
		link = new Link(nodeFrom, nodeTo, between);
		
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
	
	public Collection<Node> getNodes() {
		return nodes.values();
	}
	
	public Node getNode(int index) {
		return nodes.get(index);
	}
	
	public Node getRandomNode() {
		int rnd = new Random().nextInt(nodes.size());
		if (rnd < 0) return null;
		for (Node node : nodes.values()) {
			if (rnd <= 0) return node;
			--rnd;
		}
		return null;
	}
	
	public Set<Link> getLinks() {
		return links;
	}
	
	public Link getLink(int index) {
		return node2Link.get(index);
	}
	
}
