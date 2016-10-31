package game.controllers.pacman.exercises.e5.search.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import game.controllers.pacman.exercises.e5.graph.ILink;
import game.controllers.pacman.exercises.e5.graph.INode;

/**
 * Path represented as the list of links.
 * 
 * @author Jimmy
 *
 * @param <NODE>
 * @param <LINK>
 */
public class Path<NODE extends INode, LINK extends ILink<NODE>> {

	public int cost;
	
	public NODE start;
	
	public NODE end;
	
	public LINK[] path;
	
	public NODE[] nodes;
	
	public Path(NODE start, LINK... path) {
		this.start = start;
		this.path = path;
		this.end = computeEnd();
		this.nodes = computeNodes();
		this.cost = computeCost();		
	}
		
	public Path(NODE start, Collection<LINK> path) {
		this(start, path.toArray((LINK[]) new ILink[path.size()]));
	}
	
	public void reverse() {
		LINK[] pathReverse = ((LINK[])new ILink[path.length]);
		for (int i = path.length - 1; i >= 0; --i) {
			pathReverse[path.length - 1 - i] = path[i];
		}
		NODE temp = end;
		this.end = start;
		this.start = temp;
		this.nodes = computeNodes();
	}
	
	protected int computeCost() {
		int result = 0;
		for (int i = 0; i < path.length; ++i) {
			result += path[i].getCost();
		}
		return result;
	}
	
	protected NODE[] computeNodes() {
		List<NODE> nodes = new ArrayList<NODE>();		
		NODE node = this.start;
		
		nodes.add(node);
		for (LINK link : path) {
			node = link.getOther(node);
			nodes.add(node);
		}
	
		return nodes.toArray((NODE[])new INode[nodes.size()]);
	}
	
	protected NODE computeEnd() {
		NODE node = start;
		for (int i = 0; i < path.length; ++i) {
			node = (NODE)path[i].getOther(node);
		}
		if (node == null) node = start;
		return node;
	}

	public List<LINK> copyPath() {
		List<LINK> result = new ArrayList<LINK>();
		Collections.addAll(result, path);
		return result;
	}
	
}
