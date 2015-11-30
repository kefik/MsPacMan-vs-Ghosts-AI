package game.controllers.pacman.exercises.e2.path.informed;


import game.controllers.pacman.exercises.e2.graph.Node;
import game.controllers.pacman.exercises.e2.path.informed.base.InformedGraphSearch;
import game.controllers.pacman.exercises.e2.path.informed.base.InformedNode;
import game.core.Game;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * TODO: 
 * TASK1: turn implementation of UNIFORM-COST-SEARCH into A-Star
 * TASK2: include IGraphView
 * TASK3: move MS PacMan along the path
 *  
 * @author Jimmy
 */
public class AStar extends InformedGraphSearch {

	private Game game;

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
	}
	
	@Override
	protected Collection<InformedNode> createCloseList() {
		return new HashSet<InformedNode>();
	}

	@Override
	protected Collection<InformedNode> createOpenList() {		
		return new PriorityQueue<InformedNode>(20, new Comparator<InformedNode>() {

			@Override
			public int compare(InformedNode o1, InformedNode o2) {
				return (o1.pathCost + o1.estimate) - (o2.pathCost + o2.pathCost);
			}
			
		});
	}

	@Override
	protected InformedNode selectNextNode(Collection<InformedNode> openList) {
		return ((PriorityQueue<InformedNode>)openList).peek();
	}

	@Override
	protected int getEstimateToGoal(Node from, Node to) {
		int fromX = game.getX(from.index);
		int fromY = game.getY(from.index);
		int toX = game.getX(to.index);
		int toY = game.getY(to.index);
		
		// TORUS => X axis is a circle  
		int dx1 = Math.abs(fromX - toX);		
		int dx2 = 120 - dx1;
		
		int delta = Math.min(dx1, dx2) + Math.abs(fromY - toY);
		
		return delta;
	}

}
