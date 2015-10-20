package game.controllers.pacman.exercises.e1;

import game.PacManSimulator;
import game.controllers.pacman.PacManHijackController;
import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Link;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.path.IPathFinder;
import game.controllers.pacman.exercises.e1.path.impl.BFS;
import game.controllers.pacman.exercises.e1.path.impl.DFS;
import game.controllers.pacman.exercises.e1.path.impl.UCS;
import game.controllers.pacman.modules.Maze.MazeNode;
import game.core.Game;
import game.core.GameView;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Date;


public final class E1 extends PacManHijackController
{
	private Graph graph;
	
	private DFS dfs = new DFS();
	private BFS bfs = new BFS();	
	private UCS ucs = new UCS();
	
	private IPathFinder pathFinder = dfs;
	
	/**
	 * Called once at the beginning of the level.
	 */
	public void reset(Game game) {
		super.reset(game);
		
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
		if (pathFinder != null) {
			if (pathFinder.isRunning()) {
				if (!pacman.pauseSimulation) {
					pathFinder.step();
				}
// OPTIONAL STUFF TO SLOW DOWN PATH FINDING...				
//				try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//				}
			}				
		}
		
		debugDraw(game);
	}
	
	// ===============
	// DEBUGGING STUFF
	// ===============
	
	private boolean drawGraph = true;
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
			pathFinder = dfs;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode());
		}
		if (key == KeyEvent.VK_2) {
			pathFinder = bfs;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode());
		}
		if (key == KeyEvent.VK_3) {
			pathFinder = ucs;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode());
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
			GameView.addPoints(game, Color.DARK_GRAY, node.index);
			Node parent = pathFinder.getParent(node);
			if (parent != null) GameView.addLines(game, Color.LIGHT_GRAY, node.index, parent.index);
		}
		for (Node node : pathFinder.getOpenList()) {
			GameView.addPoints(game, Color.LIGHT_GRAY, node.index);
			Node parent = pathFinder.getParent(node);
			if (parent != null) GameView.addLines(game, Color.LIGHT_GRAY, node.index, parent.index);
		}
		
		GameView.addPoints(game, Color.RED, pathFinder.getStart().index);
		GameView.addPoints(game, Color.GREEN, pathFinder.getGoal().index);
		
		if (pathFinder.isPathFound()) {
			Node node = pathFinder.getGoal();
			while (node != null) {
				Node parent = pathFinder.getParent(node);
				if (parent != null) GameView.addLines(game, Color.YELLOW, node.index, parent.index);
				node = parent;
			}
		}
	}

	// ============
	// PATH FINDERS
	// ============
	
	public BFS getBfs() {
		return bfs;
	}

	public DFS getDfs() {
		return dfs;
	}

	public UCS getUcs() {
		return ucs;
	}
	
	// ===========
	// MAIN METHOD
	// ===========

	public static void main(String[] args) {
		PacManSimulator.play(new E1());
	}

}