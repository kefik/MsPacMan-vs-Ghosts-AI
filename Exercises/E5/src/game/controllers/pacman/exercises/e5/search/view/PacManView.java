package game.controllers.pacman.exercises.e5.search.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.controllers.pacman.exercises.e5.graph.maze.MazeGraph;
import game.controllers.pacman.exercises.e5.graph.maze.MazeGraphNode;
import game.controllers.pacman.exercises.e5.graph.maze.MazeLink;
import game.controllers.pacman.exercises.e5.search.InformedNode;
import game.controllers.pacman.exercises.e5.search.base.IGraphView;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;

public class PacManView<SEARCH_NODE extends InformedNode> extends IGraphView.DefaultView<MazeGraphNode, MazeLink, SEARCH_NODE> {

	protected Map<MazeGraphNode, List<MazeLink>> newLinks = new HashMap<MazeGraphNode, List<MazeLink>>();
	
	protected Game game;

	protected Maze maze;

	protected MazeGraph graph;
	
	protected MazeGraphNode[] extraNodes;
	
	public PacManView(Maze maze, MazeGraph graph, MazeNode... extraNodes) {
		this.game = maze.getGame();
		this.maze = maze;
		this.graph = graph;
		
		if (extraNodes == null) {
			this.extraNodes = new MazeGraphNode[0];			
		} else {
			this.extraNodes = new MazeGraphNode[extraNodes.length];
			
			int i = 0;
			for (MazeNode node : extraNodes) {
				this.extraNodes[i] = newNode(node);
				++i;
			}
		}
	}

	private MazeGraphNode newNode(MazeNode mazeNode) {
		if (mazeNode == null) return null;
		
		MazeLink link = graph.getLink(mazeNode.index);
		if (link == null) {
			throw new RuntimeException("No LINK for " + mazeNode);
		}
		MazeGraphNode newNode = link.split(mazeNode);
		
		// WRITE DOWN NEW LINKS
		for (MazeLink newLink : newNode.links.values()) {
			MazeGraphNode origNode = newLink.getOther(newNode);
			List<MazeLink> links = newLinks.get(origNode);
			if (links == null) {
				links = new ArrayList<MazeLink>();
				newLinks.put(origNode, links);
			}
			links.add(newLink);
		}
		
		return newNode;
	}
	
	public MazeGraphNode[] getExtraNodes() {
		return extraNodes;
	}
	
	@Override
	public Collection<MazeLink> getExtraLinks(SEARCH_NODE node, Collection<MazeLink> nodeLinks) {
		return newLinks.get(node.node);
	}
	
	@Override
	public boolean isNodeOpened(SEARCH_NODE node) {
		return node.nodeLevel < 100;
	}

}
