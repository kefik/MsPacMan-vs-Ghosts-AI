package game.controllers.pacman.exercises.e2.path.informed;

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
 */
public interface IGraphView<NODE> {
	
	/**
	 * This method may return new nodes which are not present in 'mapNeighbors' (as returned by {@link IPFMap#getNeighbors(Object)}).
	 * Such nodes are then exclusively accessible to your particular agent, that is, this methods is adding nodes that can be accessed 
	 * by the agent but are not part of your general map description.
	 * <p><p>
	 * "node" MUST NOT BE ADDED TO "mapNeighbors"!
	 * <p><p>
	 * Returned collection must not contain multiple references to a single neighbor (multi-graph is forbidden).
	 * <p><p>
	 * Returned collection must not contain any node from "mapNeighbors".
	 * 
	 * @param node
	 * @param mapNeighbors neighbors of the "node" as returned by {@link IPFMap#getNeighbors(Object)}, may return null
	 */
	public Collection<NODE> getExtraNeighbors(NODE node, Collection<NODE> mapNeighbors);
	
	/**
	 * Method defining extra-node cost, that is a cost that is added to {@link IPFMap#getNodeCost(Object)}.
	 * <p><p>
	 * This allows you to provide "customization" to the graph nodes, simply, it is a way of telling "this node is cool to have in path" (negative cost)
	 * or "this node is bad to have in path" (positive cost).
	 * 
	 * @param node
	 * @param mapCost cost of the node as returned by underlying {@link IPFMap#getNodeCost(Object)}
	 * 
	 * @return
	 */
	public int getNodeExtraCost(NODE node, int mapCost);
	
	/**
	 * Method defining extra-arc cost, that is a cost that is added to {@link IPFMap#getArcCost(Object, Object)}.
	 * <p><p>
	 * This allows you to
	 * provide "customization" to the graph arc costs. It allows you to say "this is a cool arc to use for travel" (negative extra cost)
	 * or "this edge is hard to cross" (positive extra cost).
	 * <p><p>
	 * NOTE THAT YOU MUST AVOID HAVING NEGATIVELY-VALUED ARCs ({@link IPFMap#getArcCost(Object, Object)} + {@link #getArcExtraCost(Object, Object, int)} &lt; 0)!
	 * <p> 
	 * Such arcs might lead to negative-valued circles which will make exploratory
	 * algorithms to endlessly walk in circles.
	 * 
	 * @param nodeFrom
	 * @param nodeTo
	 * @param mapCost cost of the arc as returned by underlying {@link IPFMap#getArcCost(Object, Object)}
	 * 
	 * @return
	 */
	public int getLinkExtraCost(NODE nodeFrom, NODE nodeTo, int mapCost);

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
	 * Arcs filter. Method defining which "arc" (oriented links between nodes) can be used for the purpose of path-planning. It can be used
	 * to "forbid" usage of some arc, that is you can rule out some arc you do not want your agent to be able to travel.
	 * 
	 * @param nodeFrom
	 * @param nodeTo
	 * 
	 * @return
	 */
	public boolean isArcOpened(NODE nodeFrom, NODE nodeTo);

	/**
	 * Default view does not impose any specific view on the map... all nodes/arcs are opened, no extra cost/nodes defined.
	 * @author Jimmy
	 */
	public class DefaultView<NODE> implements IGraphView<NODE> {

		@Override
		public Collection<NODE> getExtraNeighbors(NODE node, Collection<NODE> mapNeighbors) {
			return null;
		}

		@Override
		public int getLinkExtraCost(NODE nodeFrom, NODE nodeTo, int mapCost) {
			return 0;
		}

		@Override
		public int getNodeExtraCost(NODE node, int mapCost) {
			return 0;
		}

		@Override
		public boolean isArcOpened(NODE nodeFrom, NODE nodeTo) {
			return true;
		}

		@Override
		public boolean isNodeOpened(NODE node) {
			return true;
		}

	}
	

}
