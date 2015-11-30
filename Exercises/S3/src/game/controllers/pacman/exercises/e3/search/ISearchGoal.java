package game.controllers.pacman.exercises.e3.search;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;
import game.controllers.pacman.exercises.e3.search.nodes.InformedNode;

public interface ISearchGoal<WRAPPER extends InformedNode> {
	
	public Node getStart();
	
	public boolean isGoal(InformedSearch<WRAPPER> search, WRAPPER node);
	
	public int estimate(InformedSearch<WRAPPER> search, Node node, Link link, WRAPPER parent);

}
