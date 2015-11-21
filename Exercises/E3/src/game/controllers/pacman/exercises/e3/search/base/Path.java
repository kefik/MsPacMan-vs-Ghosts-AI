package game.controllers.pacman.exercises.e3.search.base;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class Path {

	public final Node[] nodes;
	
	public final Link[] links;
	
	public Path(Node start, Link... path) {
		this.links = path;
		
		this.nodes = new Node[links.length+1];
		
		this.nodes[0] = start;
		
		for (int i = 0; i < links.length; ++i) {
			this.nodes[i+1] = links[i].getOtherEnd(this.nodes[i]);
		}
	}
	
	public List<Node> copyNodes() {
		List<Node> result = new ArrayList<Node>(nodes.length);
		for (Node node : nodes) {
			result.add(node);
		}
		return result;
	}

	public List<Link> copyLinks() {
		List<Link> result = new ArrayList<Link>(links.length);
		for (Link link : links) {
			result.add(link);
		}
		return result;
	}
	
}
