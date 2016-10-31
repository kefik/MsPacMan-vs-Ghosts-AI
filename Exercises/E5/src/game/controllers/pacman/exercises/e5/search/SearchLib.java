package game.controllers.pacman.exercises.e5.search;

import game.controllers.pacman.exercises.e5.graph.maze.MazeGraph;
import game.controllers.pacman.exercises.e5.graph.maze.MazeGraphNode;
import game.controllers.pacman.exercises.e5.graph.maze.MazeLink;
import game.controllers.pacman.exercises.e5.search.base.IPathFinder;
import game.controllers.pacman.exercises.e5.search.base.Path;
import game.controllers.pacman.exercises.e5.search.base.PathFinderState;
import game.controllers.pacman.exercises.e5.search.goal.NodeGoal;
import game.controllers.pacman.exercises.e5.search.strategy.AStarStrategy;
import game.controllers.pacman.exercises.e5.search.view.PacManView;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;

@SuppressWarnings("rawtypes")
public class SearchLib {

	private Game game;
	
	private Maze maze;
	
	private MazeGraph graph;
	
	private InformedSearch search;

	public SearchLib(Maze maze, MazeGraph graph) {
		this.game = maze.getGame();
		this.maze = maze;
		this.graph = graph;
		this.search = new InformedSearch();
	}
	
	@SuppressWarnings("unchecked")
	public Path<MazeGraphNode, MazeLink> shortestPath(MazeNode from, MazeNode to) {
		PacManView view        = new PacManView(maze, graph, from, to);
		AStarStrategy strategy = new AStarStrategy();
		NodeGoal goal          = new NodeGoal(game, view.getExtraNodes()[0], view.getExtraNodes()[1]);
		
		search.init(graph, goal, strategy, view, null);
		
		while (search.getState() == PathFinderState.RUNNING) search.step();
				
		if (search.getState() != PathFinderState.PATH_FOUND) return null;
		
		Path<MazeGraphNode, MazeLink> result = search.getPath();
		
		if (from.index != result.start.index) {
			throw new RuntimeException("BAD PATH FOUND, start nodes mismatch.");
		}
		
		return search.getPath();		
	}

	public IPathFinder getPathFinder() {
		return search;
	}
	
}
