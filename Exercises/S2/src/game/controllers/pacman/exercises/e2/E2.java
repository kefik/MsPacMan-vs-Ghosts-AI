package game.controllers.pacman.exercises.e2;

import game.PacManSimulator;
import game.controllers.Direction;
import game.controllers.pacman.PacManHijackController;
import game.controllers.pacman.exercises.e2.graph.Graph;
import game.controllers.pacman.exercises.e2.graph.Link;
import game.controllers.pacman.exercises.e2.graph.Node;
import game.controllers.pacman.exercises.e2.path.IPathFinder;
import game.controllers.pacman.exercises.e2.path.informed.AStar;
import game.controllers.pacman.exercises.e2.path.informed.IGraphView;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;
import game.core.GameView;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class E2 extends PacManHijackController
{
	private Graph graph;
	
	private AStar aStar;
	
	private IPathFinder pathFinder = null;
	
	private List<Node> path = null;
	
	private boolean navigationRequest = true;
	
	private MazeNode previousNode = null;
	
	/**
	 * Called once at the beginning of the game.
	 */
	@Override
	public void reset(Game game) {
		super.reset(game);
		resetForLevel(game);
	}
	
	/**
	 * Called once at the beginning of the next level.
	 */
	@Override
	public void nextLevel(Game game) {
		super.nextLevel(game);
		resetForLevel(game);
	}
	
	private void resetForLevel(Game game) {		
		aStar = new AStar(game);

		System.out.println("BUILDING GRAPH");
		
		// THIS WILL CREATE A GRAPH OUT OF MAZE WITHIN THE CONSTRUCTOR
		graph = new Graph(maze);		
		
		System.out.println("GRAPH #NODES: " + graph.getNodes().size());
		System.out.println("GRAPH #LINKS: " + graph.getLinks().size());
		System.out.println("GRAPH BUILT");
	}
	
	/**
	 * Periodically called.
	 */
	@Override
	public void tick(Game game, long timeDue) {
		if (navigationRequest) {
			navigationRequest = false;
			newNavigationRequest();					
		}
		
		if (path != null) {
			Node node = path.get(0);
			if (maze.getPacManLocation() == maze.getNode(node.index)) {
				path.remove(0);
				if (path.size() > 0) {
					Node next = path.get(0);					
					Link nextLink = node.getLink(next);
					if (nextLink == null) nextLink = next.getLink(node);					
					Direction goToDir = nextLink.getDirectionFrom(node);
					pacman.set(goToDir);
				}
			}
			if (path.size() == 0) {
				path = null;
				navigationRequest = true;
			} else {			
				// HANDLE CORNERS
				MazeNode pacManNode = maze.getPacManLocation();
				if (pacManNode.link(pacman.get()) == null) {
					MazeNode nextNode = pacManNode.getRandomLink(previousNode);
					Direction goToDir = pacManNode.direction(nextNode);
					pacman.set(goToDir);
				}
				
				previousNode = pacManNode;
			}
		}
		
		if (pathFinder != null) {
			if (pathFinder.isRunning()) {
				if (!pacman.pauseSimulation) {
					pathFinder.step();
				}
// OPTIONAL STUFF TO SLOW DOWN PATH FINDING...				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}				
		}
		
		
		
		debugDraw(game);
	}

	private void newNavigationRequest() {
		// RESET NAVIGATION
		
		// DECIDE ON THE PATH END POINTS
		MazeNode mazeNodeFrom = maze.getPacManLocation();
		
		MazeNode mazeNodeTo   = maze.getRandomNode();
//		Node nodeTo = graph.getRandomNode();
//		MazeNode mazeNodeTo = maze.getNode(nodeTo.index);		
		
		// INITIALIZE CUSTOM GRAPH VIEW
		SimpleGraphView view = new SimpleGraphView(mazeNodeFrom, mazeNodeTo);
		
		// PERFORM PATH-FINDING
		aStar.init(graph, view.nodeFrom, view.nodeTo, view);
		while (aStar.isRunning()) aStar.step();
		
		// PATH FOUND?
		if (aStar.isPathFound()) {
			// INIT THE NAVIGATION
			path = new ArrayList<Node>();
			for (Node node : aStar.getPath().path) {
				path.add(node);
			}
		} else {
			path = null;
		}
		
		pathFinder = aStar;
		
	}
	
	private class SimpleGraphView extends IGraphView.DefaultView<Node, Link> {

		private Map<Node, List<Link>> newLinks = new HashMap<Node, List<Link>>();
		
		public Node nodeFrom = null;
		public Node nodeTo = null;
		
		public SimpleGraphView(MazeNode mazeNodeFrom, MazeNode mazeNodeTo) {
			nodeFrom = graph.getNode(mazeNodeFrom.index);
			nodeTo = graph.getNode(mazeNodeTo.index);
			
			if (nodeFrom == null) {
				nodeFrom = newNode(mazeNodeFrom);
			}
			if (nodeTo == null) {
				nodeTo = newNode(mazeNodeTo);
			}
		}

		private Node newNode(MazeNode mazeNode) {
			Link link = graph.getLink(mazeNode.index);
			Node newNode = link.split(mazeNode);
			
			// WRITE DOWN NEW LINKS
			for (Link newLink : newNode.links.values()) {
				Node origNode = newLink.getOtherEnd(newNode);
				List<Link> links = newLinks.get(origNode);
				if (links == null) {
					links = new ArrayList<Link>();
					newLinks.put(origNode, links);
				}
				links.add(newLink);
			}
			
			return newNode;
		}

		@Override
		public Collection<Link> getExtraLinks(Node node, Collection<Link> nodeLinks) {
			return newLinks.get(node);
		}
				
	}
	
	// ===============
	// DEBUGGING STUFF
	// ===============
	
	private boolean drawGraph = false;
	private boolean drawPathFinder = true;
	
	/**
	 * @param args
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_G) {
			drawGraph = !drawGraph;
		}
		if (key == KeyEvent.VK_F) {
			drawPathFinder = !drawPathFinder;
		}
		if (key == KeyEvent.VK_N && pacman.pauseSimulation) {
			if (pathFinder != null && pathFinder.isRunning()) pathFinder.step();
		}
		if (key == KeyEvent.VK_1) {
			pathFinder = aStar;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode());
		}
		if (key == KeyEvent.VK_2) {
			pathFinder = aStar;
			navigationRequest = true;				
		}
	}
	
	private void debugDraw(Game game) {
		debugDrawGraph(game);
		debugDrawPathFinder(game);
	}
	
	private void debugDrawGraph(Game game) {
		if (!drawGraph) return;
		for (Link link : graph.getLinks()) {
			link.debugDrawLink(game, Color.LIGHT_GRAY, Color.DARK_GRAY, true);
		}
	}
	
	private void debugDrawPathFinder(Game game) {
		if (!drawPathFinder) return;
		try {
			debugDrawPathFinder(game, pathFinder);
		} catch (Exception e) {			
		}
	}
	
	private void debugDrawPathFinder(Game game, IPathFinder pathFinder) {
		if (!drawPathFinder) return;
		if (pathFinder == null) return;
		
		GameView.addText(0, 0, Color.YELLOW, pathFinder.getName());
		
		for (Node node : pathFinder.getClosedList()) {
			GameView.addPoints(game, Color.LIGHT_GRAY, node.index);
			Node parent = pathFinder.getParent(node);
			if (parent != null) debugDrawLine(game, node, parent, Color.LIGHT_GRAY);
		}
		for (Node node : pathFinder.getOpenList()) {
			GameView.addPoints(game, Color.WHITE, node.index);
			Node parent = pathFinder.getParent(node);
			if (parent != null) debugDrawLine(game, node, parent, Color.LIGHT_GRAY);
		}
		
		GameView.addPoints(game, Color.RED, pathFinder.getStart().index);
		GameView.addPoints(game, Color.GREEN, pathFinder.getGoal().index);
		
		if (pathFinder.isPathFound()) {
			Node node = pathFinder.getGoal();
			while (node != null) {
				Node parent = pathFinder.getParent(node);
				if (parent != null) debugDrawLine(game, node, parent, Color.YELLOW);
				node = parent;
			}
		}
	}
	
	private void debugDrawLine(Game game, Node from, Node to, Color color) {
		GameView.addLinesPath(game, color, from.index, to.index);		
	}

	// ===========
	// MAIN METHOD
	// ===========

	public static void main(String[] args) {
		PacManSimulator.play(new E2());
	}

}