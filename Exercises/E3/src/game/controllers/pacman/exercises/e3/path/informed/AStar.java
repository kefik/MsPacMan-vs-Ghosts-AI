package game.controllers.pacman.exercises.e3.path.informed;

import game.controllers.pacman.exercises.e3.graph.Graph;
import game.controllers.pacman.exercises.e3.graph.Link;
import game.controllers.pacman.exercises.e3.graph.Node;
import game.controllers.pacman.exercises.e3.path.informed.base.InformedGraphSearch;
import game.controllers.pacman.exercises.e3.path.informed.base.InformedNode;
import game.core.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * TASK1: turned implementation of UNIFORM-COST-SEARCH into A-Star
 * TASK2: included IGraphView, see {@link #init(Graph, Node, Node, IGraphView)}, {@link #getNodeCost(Node)}, {@link #getLinkCost(Node, Link)}, {@link #getOptions(Graph, InformedNode)}
 *  
 * @author Jimmy
 */
public class AStar extends InformedGraphSearch {

	private Game game;

	private IGraphView<Node, Link> view;
	
	public AStar(Game game) {
		this.game = game;
	}
	
	@Override
	public String getName() {
		return "AStar[" + getSteps() + "]";
	}

	@Override
	public void reset() {
		super.reset();
		this.view = null;
	}
	
	public void init(Graph graph, Node start, Node goal, IGraphView<Node, Link> view) {		
		super.init(graph, start, goal);
		this.view = view;
	}
	
	@Override
	protected Collection<InformedNode> createCloseList() {
		return new HashSet<InformedNode>();
	}

	@Override
	protected Collection<InformedNode> createOpenList() {	
		return new PriorityQueue<InformedNode>(20, 
			new Comparator<InformedNode>() {
				@Override
				public int compare(InformedNode o1, InformedNode o2) {
					return o1.getTotalCost() - o2.getTotalCost();
				}
			});
	}

	@Override
	protected InformedNode selectNextNode(Collection<InformedNode> openList) {
		return ((PriorityQueue<InformedNode>)openList).peek();
	}

	@Override
	protected int estimatePathCostToGoal(Graph graph, Node from) {		
		Node to = getGoal();
		
		int width = 20*4;
		
		int fromX = game.getX(from.index);
		int fromY = game.getY(from.index);
		int toX   = game.getX(to.index);
		int toY   = game.getY(to.index);
		
		int diffX1 = Math.abs(fromX - toX);
		int diffX2 = Math.abs(width - Math.abs(fromX - toX));
		
		int distance = Math.min(diffX1, diffX2) + Math.abs(fromY - toY);
		
		return distance;
	}
	
	@Override
	protected int getNodeCost(Node node) {
		if (view == null) return super.getNodeCost(node);
		int origNodeCost = super.getNodeCost(node); 
		return origNodeCost + view.getNodeExtraCost(node, origNodeCost);
	}
	
	@Override
	protected int getLinkCost(Node node, Link link) {
		if (view == null) return super.getLinkCost(node, link);
		int origLinkCost = super.getLinkCost(node, link);
		return origLinkCost + view.getLinkExtraCost(node, link, origLinkCost);
	}
		
	@Override
	protected Collection<Link> getOptions(Graph graph, InformedNode searchNode) {
		if (view == null) return super.getOptions(graph, searchNode);
		
		Node from = searchNode.node;		
		Collection<Link> origLinks = super.getOptions(graph, searchNode);
		Collection<Link> extraLinks = view.getExtraLinks(searchNode.node, origLinks);
		
		List<Link> result = new ArrayList<Link>(origLinks.size() + (extraLinks == null ? 0 : extraLinks.size()));
		
		if (origLinks != null) {
			for (Link link : origLinks) {
				if (!isLinkOpened(from, link)) continue;				
				// LINK IS OPENED
				result.add(link);
			}
		}
		
		if (extraLinks != null) {
			for (Link link : extraLinks) {
				if (!isLinkOpened(from, link)) continue;				
				// LINK IS OPENED
				result.add(link);
			}
		}
				
		return result;
	}

	private boolean isLinkOpened(Node from, Link link) {
		if (!view.isLinkOpened(from, link)) return false;
		
		Node to = link.getOtherEnd(from);					
		if (!view.isNodeOpened(to)) return false;
		
		return true;
	}
	
}
