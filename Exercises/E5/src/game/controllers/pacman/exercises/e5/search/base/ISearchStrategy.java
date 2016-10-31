package game.controllers.pacman.exercises.e5.search.base;

import java.util.Collection;

import game.controllers.pacman.exercises.e5.graph.ILink;
import game.controllers.pacman.exercises.e5.graph.INode;
import game.controllers.pacman.exercises.e5.search.InformedNode;
import game.controllers.pacman.exercises.e5.search.InformedSearch;

public interface ISearchStrategy<NODE extends INode, LINK extends ILink<NODE>, SEARCH_NODE extends InformedNode> {
	
	// --------------------
	// DEPENDENCY INJECTION
	// --------------------
	
	/** 
	 * Binds the object with concrete SEARCH instance.
	 * @param search
	 */
	public void setContext(InformedSearch search);
	
	// ---------------
	// NODE MANAGEMENT
	// ---------------
	
	/**
	 * Returns a SEARCH_NODE for given maze NODE.
	 * @param node
	 * @return
	 */
	public SEARCH_NODE getNode(NODE node);	
	
	/**
	 * Creates an initial node from the start 'node'.
	 * @param node
	 * @return
	 */
	public SEARCH_NODE makeInitialNode(NODE node);
	
	/**
	 * Creates new SEARCH_NODE given detail information about its cost.
	 * @param node
	 * @param link
	 * @param parent
	 * @param nodeGraphCost
	 * @param nodeExtraCost
	 * @param linkGraphCost
	 * @param linkExtraCost
	 * @param estimateToGoal
	 * @return
	 */
	public SEARCH_NODE makeNode(NODE node, LINK link, SEARCH_NODE parent, int nodeGraphCost, int nodeExtraCost, int linkGraphCost, int linkExtraCost, int estimateToGoal);
	
	// ----------------------------
	// OPEN / CLOSED LIST FACTORIES
	// ----------------------------
	
	public Collection<SEARCH_NODE> createCloseList();
	public Collection<SEARCH_NODE> createOpenList();
	
	// -------------------------------------
	// SEARCH STRATEGY - WHAT TO PROBE NEXT?
	// -------------------------------------
	
	/**
	 * Implementation of search strategy - which node to expand next?
	 * @param openList
	 * @return
	 */
	public SEARCH_NODE selectNextNode(Collection<SEARCH_NODE> openList);
	
}
