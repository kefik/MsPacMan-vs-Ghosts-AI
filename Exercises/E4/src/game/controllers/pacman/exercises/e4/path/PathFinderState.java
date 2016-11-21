package game.controllers.pacman.exercises.e4.path;

public enum PathFinderState {

	/**
	 * Path-finder is not working, ready to be {@link IPathFinder#init(game.controllers.pacman.exercises.e1.graph.Graph, game.controllers.pacman.exercises.e1.graph.Node, game.controllers.pacman.exercises.e1.graph.Node, Object)}ed.
	 */
	INIT,
	
	/**
	 * Path-finder was inited and is working; it does not found the solution
	 * yet.
	 */
	RUNNING,
	
	/**
	 * Path-finder has finished, it found the path between start-goal nodes.
	 */
	PATH_FOUND,
	
	/**
	 * Path-finder has failed to find a path given the initial settings.
	 */
	PATH_NOT_FOUND
	
}
