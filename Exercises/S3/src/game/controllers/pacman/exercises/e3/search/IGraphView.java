package game.controllers.pacman.exercises.e3.search;


import game.controllers.pacman.exercises.e3.search.base.InformedNode;

import java.util.Collection;

/**
 * For ideas behind this interface consult the paper:
 * 
 * Implementation of generic A* algorithm, better refered to as A* Machine according to 
 * Dan Higgins, Generic A* Pathfind paper from AI Gaming Wisdom, 2002
 * 
 * @author Jimmy
 *
 * @param <NODE>
 * @param <LINK>
 */
public interface IGraphView<NODE, LINK, WRAPPER extends InformedNode> {
	
	/**
	 * This method may return new links which are not present in 'nodeLinks'.
	 * Such nodes are then exclusively accessible to your particular agent, that is, this methods is adding links that can be accessed 
	 * by the agent but are not part of your general map description.
	 * <p><p>
	 * "link" MUST NOT BE ADDED INTO TO "nodeLinks"!
	 * <p><p>
	 * Returned collection must not contain any link from "nodeLinks".
	 * 
	 * @param node
	 * @param nodeLinks neighbors of the "node", may be NULL
	 * @return extra LINKs for given 'node'
	 */
	public Collection<LINK> getExtraLinks(WRAPPER node, Collection<LINK> nodeLinks);
	
	/**
	 * Method defining extra-node cost associated with traveling through this node.
	 * <p><p>
	 * This allows you to provide "customization" to the graph nodes, simply, it is a way of telling "this node is cool to have in path" (negative cost)
	 * or "this node is bad to have in path" (positive cost).
	 * <p><p>
	 * Such nodes might lead to negative-valued circles which will make exploratory
	 * algorithms to walk in circles endlessly.
	 * 
	 * @param node
	 * @param nodeCost cost of the node within the state space
	 * 
	 * @return be careful abs(RETURN) must be &gt;= 'nodeMapCost' !
	 */
	public int getNodeExtraCost(NODE node, int nodeCost, WRAPPER parent);
	
	/**
	 * Method defining extra-link cost associated with traveling the link.
	 * <p><p>
	 * This allows you to provide "customization" to the graph link cost. It allows you to say "this is a cool link to use for travel" (negative extra cost)
	 * or "this edge is hard to cross" (positive extra cost).
	 * <p><p>
	 * NOTE THAT YOU MUST AVOID HAVING NEGATIVELY-VALUED LINKs!
	 * <p><p>
	 * Such links might lead to negative-valued circles which will make exploratory
	 * algorithms to walk in circles endlessly.
	 * 
	 * @param nodeFrom
	 * @param nodeTo
	 * @param mapCost cost of the arc as returned by underlying {@link IPFMap#getArcCost(Object, Object)}
	 * 
	 * @return
	 */
	public int getLinkExtraCost(NODE node, LINK link, int linkMapCost, WRAPPER parent);

	/**
	 * Nodes filter. Method defining which nodes are allowed to be explored / used by path finding algorithms, i.e., algorithm will never return path leading 
	 * to such nodes. May be used to define "forbidden" nodes.
	 * 
	 * @param node
	 * 
	 * @return
	 */
	public boolean isNodeOpened(NODE node);
	
	/**
	 * Links filter. Method defining which "link" (oriented links between nodes) can be used for the purpose of path-planning. It can be used
	 * to "forbid" usage of some link, that is you can rule out some link you do not want your agent to be able to travel through.
	 * 
	 * @param nodeFrom
	 * @param nodeTo
	 * 
	 * @return
	 */
	public boolean isLinkOpened(NODE nodeFrom, LINK link);

	/**
	 * Default view does not impose any specific view on the map... all nodes/links are opened, no extra cost/nodes defined.
	 * @author Jimmy
	 */
	public class DefaultView<NODE, LINK, WRAPPER extends InformedNode> implements IGraphView<NODE, LINK, WRAPPER> {

		@Override
		public Collection<LINK> getExtraLinks(WRAPPER node, Collection<LINK> nodeLinks) {
			return null;
		}

		@Override
		public int getNodeExtraCost(NODE node, int nodeCost, WRAPPER parent) {
			return 0;
		}

		@Override
		public int getLinkExtraCost(NODE node, LINK link, int linkMapCost, WRAPPER parent) {
			return 0;
		}

		@Override
		public boolean isNodeOpened(NODE node) {
			return true;
		}

		@Override
		public boolean isLinkOpened(NODE nodeFrom, LINK link) {
			return true;
		}

	}

}
