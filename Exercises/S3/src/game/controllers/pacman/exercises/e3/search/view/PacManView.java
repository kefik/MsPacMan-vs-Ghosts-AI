package game.controllers.pacman.exercises.e3.search.view;

import game.controllers.pacman.exercises.e3.graph.Graph;
import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.IGraphView;
import game.controllers.pacman.exercises.e3.search.base.InformedNode;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacManView extends IGraphView.DefaultView<Node, Link, InformedNode> {

	protected Map<Node, List<Link>> newLinks = new HashMap<Node, List<Link>>();
	
	protected Game game;

	protected Maze maze;

	protected Graph graph;
	
	protected Node[] extraNodes;
	
	public PacManView(Maze maze, Graph graph, MazeNode... extraNodes) {
		this.game = maze.getGame();
		this.maze = maze;
		this.graph = graph;
		
		if (extraNodes == null) {
			this.extraNodes = new Node[0];			
		} else {
			this.extraNodes = new Node[extraNodes.length];
			
			int i = 0;
			for (MazeNode node : extraNodes) {
				this.extraNodes[i] = newNode(node);
				++i;
			}
		}
	}

	private Node newNode(MazeNode mazeNode) {
		if (mazeNode == null) return null;
		
		Link link = graph.getLink(mazeNode.index);
		Node newNode = link.split(mazeNode);
		
		// WRITE DOWN NEW LINKS
		for (Link newLink : newNode.links.values()) {
			Node origNode = newLink.getOtherEnd(newNode);
			List<Link> links = newLinks.get(origNode);
			if (links == null) {
				links = new ArrayList<Link>();
				newLinks.put(origNode, links);
			}
			links.add(newLink);
		}
		
		return newNode;
	}
	
	public Node[] getExtraNodes() {
		return extraNodes;
	}
	
	@Override
	public Collection<Link> getExtraLinks(InformedNode node, Collection<Link> nodeLinks) {
		return newLinks.get(node.node);
	}

}
