package game.controllers.pacman.exercises.e1.path.impl;

import java.util.Collection;

import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.path.IPathFinder;
import game.controllers.pacman.exercises.e1.path.Path;

/**
 * TODO: Implement UNIFORM-COST-SEARCH
 *  
 * @author Jimmy
 */
public class UCS implements IPathFinder {

	@Override
	public void init(Graph graph, Node start, Node goal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPathFound() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Path getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getParent(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Node> getClosedList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Node> getOpenList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node getStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getGoal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getName() {
		return "UCS" + getSteps() + "]";
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
