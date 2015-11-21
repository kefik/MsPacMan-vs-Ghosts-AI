package game.controllers.pacman.exercises.e3.search;

import game.controllers.pacman.exercises.e3.graph.Graph;
import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.base.InformedNode;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;
import game.controllers.pacman.exercises.e3.search.base.Path;
import game.controllers.pacman.exercises.e3.search.base.SearchState;
import game.controllers.pacman.exercises.e3.search.goal.NodeGoal;
import game.controllers.pacman.exercises.e3.search.strategy.AStarStrategy;
import game.controllers.pacman.exercises.e3.search.view.PacManView;
import game.controllers.pacman.modules.Maze;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;

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
		PacManView view = new PacManView(maze, graph, from, to);
		AStarStrategy strategy = new AStarStrategy();
		NodeGoal goal = new NodeGoal(game, view.getExtraNodes()[0], view.getExtraNodes()[1]);
		
		search.init(graph, goal, strategy, view);
		
		while (search.getState() == SearchState.RUNNING) search.step();
		
		return search.getPath();		
	}

	public IPathFinder getPathFinder() {
		return search;
	}
	
}
