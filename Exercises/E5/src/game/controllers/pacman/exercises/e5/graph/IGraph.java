package game.controllers.pacman.exercises.e5.graph;

import java.util.Collection;

public interface IGraph<NODE extends INode, LINK extends ILink> {

	public Collection<NODE> getNodes();
	public Collection<LINK> getLinks();
	
}
