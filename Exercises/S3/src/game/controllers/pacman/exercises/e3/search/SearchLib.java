package game.controllers.pacman.exercises.e3.search;

import game.controllers.pacman.exercises.e3.graph.Graph;
import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;
import game.controllers.pacman.exercises.e3.search.base.Path;
import game.controllers.pacman.exercises.e3.search.base.SearchState;
import game.controllers.pacman.exercises.e3.search.goal.DepthBoundedGoal;
import game.controllers.pacman.exercises.e3.search.goal.NodeGoal;
import game.controllers.pacman.exercises.e3.search.nodes.InformedNode;
import game.controllers.pacman.exercises.e3.search.nodes.NodeWithPills;
import game.controllers.pacman.exercises.e3.search.strategy.AStarStrategy;
import game.controllers.pacman.exercises.e3.search.strategy.AStarStrategyPills;
import game.controllers.pacman.exercises.e3.search.strategy.BFSStrategy;
import game.controllers.pacman.exercises.e3.search.view.PacManView;
import game.controllers.pacman.exercises.e3.search.view.PacManViewNoGhosts;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class SearchLib {

	private Game game;
	
	private Maze maze;
	
	private Graph graph;
	
	private InformedSearch search;

	public SearchLib(Maze maze, Graph graph) {
		this.game = maze.getGame();
		this.maze = maze;
		this.graph = graph;
		this.search = new InformedSearch();
	}
	
	@SuppressWarnings("unchecked")
	public Path shortestPath(MazeNode from, MazeNode to) {
		PacManView view        = new PacManView(maze, graph, from, to);
		AStarStrategy strategy = new AStarStrategy();
		NodeGoal goal          = new NodeGoal(game, view.getExtraNodes()[0], view.getExtraNodes()[1]);
		
		search.init(graph, goal, strategy, view);
		
		while (search.getState() == SearchState.RUNNING) search.step();
		
		return search.getPath();		
	}
	
	@SuppressWarnings("unchecked")
	public Path shortestPathNoGhosts(MazeNode from, MazeNode to) {
		PacManView view        = new PacManViewNoGhosts(maze, graph, from, to);
		AStarStrategy strategy = new AStarStrategy();
		NodeGoal goal          = new NodeGoal(game, view.getExtraNodes()[0], view.getExtraNodes()[1]);
		
		search.init(graph, goal, strategy, view);
		
		while (search.getState() == SearchState.RUNNING) search.step();
		
		return search.getPath();		
	}
	
	@SuppressWarnings("unchecked")
	public Path pathThroughPills(MazeNode from, MazeNode to) {
		PacManView view             = new PacManViewNoGhosts(maze, graph, from, to);
		AStarStrategyPills strategy = new AStarStrategyPills(maze);
		NodeGoal goal               = new NodeGoal(game, view.getExtraNodes()[0], view.getExtraNodes()[1]);
		
		search.init(graph, goal, strategy, view);
		
		while (search.getState() == SearchState.RUNNING) {
			search.step();
		}
		
		return search.getPath();	
	}
	
	/**
	 * NOT WORKING PROPERLY!
	 * @param from
	 * @return
	 */
	public Path bfsPills(MazeNode from) {
		PacManView view             = new PacManViewNoGhosts(maze, graph, from);
		BFSStrategy strategy 		= new BFSStrategy(maze);
		
		int depth = 0;
		
		while (depth < 10) {
			
			depth += 2;
			
			DepthBoundedGoal goal = new DepthBoundedGoal(game, view.getExtraNodes()[0], depth);
			
			search.init(graph, goal, strategy, view);
		
			while (search.getState() == SearchState.RUNNING) {
				search.step();
			}
			
			NodeWithPills node = strategy.getNode(search.getGoal());
			if (node != null) {
				if (node.pills.length == 0) {
					return search.getPath();	
				}
			}			
		}
		
		NodeWithPills best = null;
		
		// GET THE BEST PATH
		for (Object obj : search.getOpenList()) {
			NodeWithPills node = (NodeWithPills)obj;
			if (best == null) {
				best = node;				
			}
			if (node.pills.length < best.pills.length) {
				best = node;
			}
		}
		
		List<Link> path = new ArrayList<Link>();
		
		InformedNode node = best;
		
		Set<InformedNode> nodeOnPath = new HashSet<InformedNode>();
		nodeOnPath.add(node);
		
		Node start = null;
		
		while (node != null) {
			Link link = node.link;
			if (link != null) {
				start = link.getOtherEnd(node.node);
				path.add(link);
				if (nodeOnPath.contains(node.parent)) break;
				nodeOnPath.add(node.parent);
			} else {
				break;
			}
		}
		
		Collections.reverse(path);
		
		return new Path(path.get(0).n1, path.toArray(new Link[0]));	
	}

	public IPathFinder getPathFinder() {
		return search;
	}
	
}
