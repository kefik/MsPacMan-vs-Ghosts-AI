package game.controllers.pacman.exercises.e5.search.base;

import game.controllers.pacman.exercises.e5.graph.ILink;
import game.controllers.pacman.exercises.e5.graph.INode;
import game.controllers.pacman.exercises.e5.search.InformedNode;
import game.controllers.pacman.exercises.e5.search.InformedSearch;

public interface ISearchGoal<NODE extends INode, LINK extends ILink<NODE>, SEARCH_NODE extends InformedNode> {
	
	/**
	 * Sets 'search' as the context for the goal. 
	 * Called once during {@link InformedSearch#init(game.controllers.pacman.exercises.e5.path.informed.Graph, game.controllers.pacman.exercises.e5.path.informed.ISearchGoal, game.controllers.pacman.exercises.e5.path.informed.ISearchStrategy, game.controllers.pacman.exercises.e5.path.informed.IGraphView)}.
	 * @param search
	 */
	public void setContext(InformedSearch search);
	
	/**
	 * Returns an initial node we want to start the search from.
	 * @return
	 */
	public NODE getStart();
	
	/**
	 * Represents a goal we try to achieve; here you procedurally describes whether
	 * 'node' is the 'goal node'.
	 * 
	 * @param node
	 */
	public boolean isGoal(SEARCH_NODE node);
	
	/**
	 * This represents a heuristic function that estimates how far is 'node' from the goal.
	 * 
	 * The estimation can be based on 'node' as well as 'link' we want to use to get into the 'node'. 
	 * 
	 * @param parent node we are currently expanding
	 * @param link link we are currently discussing
	 * @param node node we can get into by following 'link' from 'parent'
	 * 
	 * @return
	 */
	public int estimate(SEARCH_NODE parent, LINK link, NODE node);

}
