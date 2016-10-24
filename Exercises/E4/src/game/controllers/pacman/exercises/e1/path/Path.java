package game.controllers.pacman.exercises.e1.path;

import java.util.Collection;

import game.controllers.pacman.exercises.e1.graph.Link;
import game.controllers.pacman.exercises.e1.graph.Node;


public class Path {

	public final Node[] path;
	
	public Path(Node... path) {
		this.path = path;
	}
	
	public Path(Collection<Node> path) {
		this.path = path.toArray(new Node[path.size()]);		
	}

	public void reverse() {
		Node[] pathReverse = new Node[path.length];
		for (int i = path.length - 1; i >= 0; --i) {
			pathReverse[path.length - 1 - i] = path[i];
		}
	}
	
	public int computeCost() {
		int result = 0;
		for (int i = 1; i < path.length; ++i) {
			Node n1 = path[i-1];
			Node n2 = path[i];
			for (Link link : n1.links.values()) {
				if (link.n1 == n2 || link.n2 == n2) {
					result += link.distance;
					break;
				}
			}
		}
		return result;
	}
	
}
