package game.controllers.pacman.exercises.e5;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import game.PacManSimulator;
import game.SimulatorConfig;
import game.controllers.Direction;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.PacManHijackController;
import game.controllers.pacman.exercises.e5.graph.maze.MazeGraph;
import game.controllers.pacman.exercises.e5.graph.maze.MazeGraphNode;
import game.controllers.pacman.exercises.e5.graph.maze.MazeLink;
import game.controllers.pacman.exercises.e5.search.SearchLib;
import game.controllers.pacman.exercises.e5.search.base.IPathFinder;
import game.controllers.pacman.exercises.e5.search.base.Path;
import game.controllers.pacman.exercises.e5.search.base.PathFinderState;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;
import game.core.GameView;

public final class E5 extends PacManHijackController
{
	private MazeGraph graph;
	
	private SearchLib search;
	
	/**
	 * Node we're going from..
	 */
	private MazeGraphNode goFrom = null;
	
	/**
	 * First link is the link we're navigating through.
	 */
	private List<MazeLink> pathLinks = null;
	
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
		System.out.println("BUILDING GRAPH");
		
		// THIS WILL CREATE A GRAPH OUT OF MAZE WITHIN THE CONSTRUCTOR
		graph = new MazeGraph(maze);		
		
		System.out.println("GRAPH #NODES: " + graph.getNodes().size());
		System.out.println("GRAPH #LINKS: " + graph.getLinks().size());
		System.out.println("GRAPH BUILT");
		
		// INIT LIBRARY
		search = new SearchLib(maze, graph);
	}
	
	/**
	 * Periodically called.
	 */
	@Override
	public void tick(Game game, long timeDue) {
		System.out.println("PACMAN at " + maze.getPacManLocation());
		
		if (navigationRequest || pathLinks == null || isDangerOnPath()) {
			navigationRequest = false;
			newNavigationRequest();					
		}
		
		if (pathLinks == null) {
			System.out.println("  -- NO pathLink");
		} else {
			System.out.println("  -- Current pathLink = " + (pathLinks == null || pathLinks.size() == 0 ? "null" : pathLinks.get(0)));
			// LINK SWITCHING
			if (maze.getPacManLocation() == goFrom.mazeNode) {
				System.out.println("  -- NAVIGATION STARTS FROM " + goFrom.mazeNode);
				// IF WE ARE AT THE BEGINNING OF THE LINK
				// => set the initial direction
				pacman.set(pathLinks.get(0).getDirectionFrom(goFrom));
			} else {
				MazeLink link = pathLinks.get(0);
				// CHECK IF OUR LOCATION IS THE SAME AS THE END OF THE LINK
				if (maze.getPacManLocation() == link.getOther(goFrom).mazeNode) {					
					// => switch to the next link
					pathLinks.remove(0);
					goFrom = link.getOther(goFrom);
					System.out.println("  -- NAVIGATION SWITCH TO: " + goFrom.mazeNode);
					if (pathLinks.size() > 0) {
						pacman.set(pathLinks.get(0).getDirectionFrom(goFrom));
					}
				}
			}
			
			// NAVIGATING THROUGH CORRIDORS
			if (pathLinks.size() == 0) {
				pathLinks = null;				
			} else {			
				// HANDLE CORNERS
				MazeNode pacManNode = maze.getPacManLocation();
				if (pacManNode.link(pacman.get()) == null) {
					System.out.println("  -- CORNER TURN");
					MazeNode nextNode = pacManNode.getRandomLink(previousNode);
					Direction goToDir = pacManNode.direction(nextNode);
					pacman.set(goToDir);
				} else {
					System.out.println("  -- Corridor...");
				}
				
				previousNode = pacManNode;
			}
		}
		
		System.out.println("  -- Pac-man direction set to " + pacman.get());
		
		debugDraw(game);
	}
	
	@Override
	public void killed() {
		navigationRequest = true;
	}

	private boolean isDangerOnPath() {
		if (pathLinks == null || pathLinks.size() == 0) return false;
		
		MazeGraphNode goFrom = this.goFrom;
		MazeNode previous = previousNode;
		MazeNode node = maze.getPacManLocation();
		
		int linkNum = 0;				
		while (linkNum < pathLinks.size()) {
			MazeLink link = pathLinks.get(linkNum);
			
			if (node.index == goFrom.index) {
				if (node.ghostDanger()) {
					return true;
				}
				previous = node;
				node = node.link(link.getDirectionFrom(goFrom));
			}
			
			MazeGraphNode target = link.getOther(goFrom);
			while (node.index != target.index) {
				if (node.ghostDanger()) {
					return true;
				}
				MazeNode temp = node;
				node = node.getRandomLink(previous);
				previous = temp;
			}
			
			goFrom = target;
			++linkNum;
		}

		return false;
	}

	private void newNavigationRequest() {
		// DECIDE ON THE PATH END POINTS
		MazeNode mazeNodeFrom = maze.getPacManLocation();
		MazeNode mazeNodeTo = maze.getRandomNode();
		
		while (graph.getLink(mazeNodeTo.index) == null) mazeNodeTo = maze.getRandomNode();
		
		System.out.println("  -- SEARCHING " + mazeNodeFrom + " -> " + mazeNodeTo);
		
		// PERFORM PATH-FINDING
		//Path path = search.pathThroughPills(mazeNodeFrom, mazeNodeTo);
		Path<MazeGraphNode, MazeLink> path = search.shortestPath(mazeNodeFrom, mazeNodeTo);
		if (path != null) {
			this.goFrom = path.start;
			this.pathLinks = path.copyPath();
			System.out.println("  -- PATH FOUND, length = " + path.cost + ", #links = " + pathLinks.size());
		} else {
			System.out.println("  -- PATH NOT FOUND");
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
		if (key == KeyEvent.VK_N || key == KeyEvent.VK_1 || key == KeyEvent.VK_NUMPAD1) {
			navigationRequest = true;				
		}
	}
	
	private void debugDraw(Game game) {
		debugDrawGraph(game);
		debugDrawPathFinder(game);
	}
	
	private void debugDrawGraph(Game game) {
		if (!drawGraph) return;
		for (MazeLink link : graph.getLinks()) {
			link.debugDrawLink(game, Color.LIGHT_GRAY, Color.DARK_GRAY, true);
		}
	}
	
	private void debugDrawPathFinder(Game game) {
		if (!drawPathFinder) return;
		try {
			debugDrawPathFinder(game, search.getPathFinder());
		} catch (Exception e) {			
		}
	}
	
	private void debugDrawPathFinder(Game game, IPathFinder pathFinder) {
		if (!drawPathFinder) return;
		if (pathFinder == null) return;
		
		GameView.addText(0, 0, Color.YELLOW, pathFinder.getName());
		
		/*
		for (InformedNode node : (Collection<InformedNode>)pathFinder.getClosedList()) {
			GameView.addPoints(game, Color.LIGHT_GRAY, node.node.index);
			Node parent = ((InformedNode)(pathFinder.getParent(node.node))).node;
			if (parent != null) debugDrawLine(game, node.node, parent, Color.LIGHT_GRAY);
		}
		for (InformedNode node : (Collection<InformedNode>)pathFinder.getOpenList()) {
			GameView.addPoints(game, Color.WHITE, node.node.index);
			Node parent = ((InformedNode)(pathFinder.getParent(node.node))).node;
			if (parent != null) debugDrawLine(game, node.node, parent, Color.LIGHT_GRAY);
		}
		*/
		
		GameView.addPoints(game, Color.RED, ((MazeGraphNode)pathFinder.getStart()).mazeNode.index);
		GameView.addPoints(game, Color.GREEN, ((MazeGraphNode)pathFinder.getGoal()).mazeNode.index);
		
		if (pathFinder.getState() == PathFinderState.PATH_FOUND) {
			Path path = pathFinder.getPath();
			for (int i = 0; i < path.nodes.length-1; ++i) {
				debugDrawLine(game, (MazeGraphNode)path.nodes[i], (MazeGraphNode)path.nodes[i+1], Color.YELLOW);
			}
		}
	}
	
	private void debugDrawLine(Game game, MazeGraphNode from, MazeGraphNode to, Color color) {
		GameView.addLinesPath(game, color, from.index, to.index);		
	}

	// ===========
	// MAIN METHOD
	// ===========

	public static void main(String[] args) {
		SimulatorConfig config = new SimulatorConfig();
		
		config.pacManController = new E5();
		config.ghostsController = new GameGhosts(0);
		
		config.replay = true;
		config.replayFile = new File("./replay.log");
		
		config.visualize = true;
		config.visualizationScale2x = true;
		
		config.game.powerPillsEnabled = false;
		config.game.totalPills = 0.5;
		config.game.levelsToPlay = 1;
		
		PacManSimulator.play(config);
	}

}