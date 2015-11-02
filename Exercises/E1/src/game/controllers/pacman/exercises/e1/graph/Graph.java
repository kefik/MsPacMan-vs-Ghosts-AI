package game.controllers.pacman.exercises.e1.graph;

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
		for (MazeNode node : maze.getNodes()) {
			if (!node.junction()) continue;
			// IT IS A JUNCTION!
			for (Direction dir : Direction.arrows()) {
				if (!node.hasLink(dir)) continue;
				// Here we are in the situation when
				// -- we're probing a node that is a junction
				// -- there is a possibility to go from the junction in direction 'dir'
				
				List<MazeNode> linkNodes = new ArrayList<MazeNode>();
				
				// TODO: find a link leading from 'node' in direction 'dir' to another junction
				//       and create a link out of that
								
				// MAKE A LINK!
				makeLink(linkNodes.toArray(new MazeNode[0]));
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
		
		MazeNode[] between = Arrays.copyOfRange(mazeNodes, 1, mazeNodes.length-1);
		
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
	
	public Node getNode(int nodeIndex) {
		return nodes.get(nodeIndex);
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
	
	public Link getLink(int nodeIndex) {
		return node2Link.get(nodeIndex);
	}
	
}
