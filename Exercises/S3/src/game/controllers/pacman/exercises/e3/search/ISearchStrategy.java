package game.controllers.pacman.exercises.e3.search;

import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.search.base.InformedNode;
import game.controllers.pacman.exercises.e3.search.base.InformedSearch;

import java.util.Collection;

public interface ISearchStrategy<WRAPPER extends InformedNode> {
	
	public WRAPPER getNode(Node node);
	
	public WRAPPER makeFirstNode(Node node);
	public WRAPPER makeNode(Node node, Link link, WRAPPER parent, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal);
	
	public Collection<WRAPPER> createCloseList();
	public Collection<WRAPPER> createOpenList();
	
	public WRAPPER selectNextNode(InformedSearch<WRAPPER> search, Collection<WRAPPER> openList);
	
}
