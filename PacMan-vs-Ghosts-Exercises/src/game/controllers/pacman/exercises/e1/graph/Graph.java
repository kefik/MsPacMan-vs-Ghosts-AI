package game.controllers.pacman.exercises.e1.graph;

import game.controllers.Direction;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Graph {

	private Maze maze;
	
	/**
	 * Node index -> Node
	 */
	private Map<Integer, Node> nodes = new HashMap<Integer, Node>();
	
	/**
	 * Node index -> Link
	 */
	private Map<Integer, Link> links = new HashMap<Integer, Link>();
	
	public Graph(Maze maze) {
		this.maze = maze;
		build();
	}
	
	private void build() {
		// TODO: delete following example...
		
		// GRAPH IS BUILD LIKE THIS
		// 1: obtain an array of MazeNodes 
		
		MazeNode[] mazeNodes = new MazeNode[20];
		mazeNodes[0] = maze.getNodes()[maze.getNodes().length / 4];
		for (int i = 1; i < mazeNodes.length; ++i) {
			mazeNodes[i] = mazeNodes[i-1].getRandomLink(i > 1 ? mazeNodes[i-2] : null);
		}
		
		// 2: make a link out of them (auto create Node(s) if required)
		makeLink(mazeNodes);		
		
		// TODO: provide own implementation
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
			links.put(mazeNode.index, link);
		}
		
		nodeFrom.links.put(dirFromTo, link);		
		nodeTo.links.put(mazeNodes[mazeNodes.length-1].direction(mazeNodes[mazeNodes.length-2]), link);
		
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
	
	public Collection<Link> getLinks() {
		return links.values();
	}
	
	public Link getLink(int index) {
		return links.get(index);
	}
	
}
