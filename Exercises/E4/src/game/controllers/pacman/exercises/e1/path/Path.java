package game.controllers.pacman.exercises.e1.path;

import java.util.Collection;

import game.controllers.pacman.exercises.e1.graph.Node;

/**
 * Class that represents found path.
 * 
 * Path is stored within {@link #path} list.
 */
public class Path {

	public Node[] path;
	
	public Path(Node... path) {
		this.path = path;
	}
	
	public Path(Collection<Node> path) {
		this.path = path.toArray(new Node[path.size()]);		
	}

	/**
	 * Reverses {@link #path}.
	 */
	public void reverse() {
		Node[] pathReverse = new Node[path.length];
		for (int i = path.length - 1; i >= 0; --i) {
			pathReverse[path.length - 1 - i] = path[i];
		}
		this.path = pathReverse;
	}
	
}
