package game.controllers.pacman.exercises.e1;

import java.awt.Color;
import java.awt.event.KeyEvent;

import game.PacManSimulator;
import game.controllers.pacman.PacManHijackController;
import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Link;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.path.IPathFinder;
import game.controllers.pacman.exercises.e1.path.Path;
import game.controllers.pacman.exercises.e1.path.PathFinderState;
import game.controllers.pacman.exercises.e1.path.uninformed.BDS;
import game.controllers.pacman.exercises.e1.path.uninformed.BFS;
import game.controllers.pacman.exercises.e1.path.uninformed.DFS;
import game.controllers.pacman.exercises.e1.path.uninformed.DLS;
import game.controllers.pacman.exercises.e1.path.uninformed.IDS;
import game.controllers.pacman.exercises.e1.path.uninformed.UCS;
import game.core.Game;
import game.core.GameView;


public final class E4 extends PacManHijackController
{
	private Graph graph;
	
	private DFS<Object> dfs = new DFS<Object>();
	private BFS bfs = new BFS();	
	private UCS ucs = new UCS();
	private DLS dls = new DLS();
	private IDS ids = new IDS();
	private BDS bds = new BDS();
	
	private IPathFinder pathFinder = null;
	
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
			if (pathFinder.getState() == PathFinderState.RUNNING) {
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
			if (pathFinder != null && pathFinder.getState() == PathFinderState.RUNNING) pathFinder.step();
		}
		if (key == KeyEvent.VK_1) {
			pathFinder = dfs;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode(), null);
		}
		if (key == KeyEvent.VK_2) {
			pathFinder = bfs;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode(), null);
		}
		if (key == KeyEvent.VK_3) {
			pathFinder = ucs;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode(), null);
		}
		if (key == KeyEvent.VK_4) {
			pathFinder = dls;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode(), new DLS.DLSConfig(3));
		}
		if (key == KeyEvent.VK_5) {
			pathFinder = ids;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode(), null);
		}
		if (key == KeyEvent.VK_6) {
			pathFinder = bds;
			pathFinder.reset();
			pathFinder.init(graph, graph.getRandomNode(), graph.getRandomNode(), null);
		}
	}
	
	private void debugDraw(Game game) {
		debugDrawGraph(game);
		debugDrawPathFinder(game, pathFinder);
	}
	
	private void debugDrawGraph(Game game) {
		if (!drawGraph) return;
		for (Link link : graph.getLinks()) {
			link.debugDrawLink(game, Color.LIGHT_GRAY, Color.DARK_GRAY, true);
		}
	}
	
	private void debugDrawPathFinder(Game game, IPathFinder<?> pathFinder) {
		if (!drawPathFinder) return;
		if (pathFinder == null) return;
		
		GameView.addText(0, 0, Color.YELLOW, pathFinder.getName());
		
		if (pathFinder.getClosedList() != null) {
			for (Node node : pathFinder.getClosedList()) {
				GameView.addPoints(game, Color.LIGHT_GRAY, node.index);
				Node parent = pathFinder.getParent(node);
				if (parent != null) debugDrawLine(game, node, parent, Color.LIGHT_GRAY);
			}
		}
		if (pathFinder.getOpenList() != null) {
			for (Node node : pathFinder.getOpenList()) {
				GameView.addPoints(game, Color.WHITE, node.index);
				Node parent = pathFinder.getParent(node);
				if (parent != null) debugDrawLine(game, node, parent, Color.LIGHT_GRAY);
				
				GameView.addText(game.getX(node.index) - 3, game.getY(node.index) - 3, Color.YELLOW, String.valueOf(pathFinder.getNodeInfo(node).pathCost));
			}
		}
		
		if (pathFinder.getStart() != null) GameView.addPoints(game, Color.RED, pathFinder.getStart().index);
		if (pathFinder.getGoal() != null) GameView.addPoints(game, Color.GREEN, pathFinder.getGoal().index);
		
		if (pathFinder.getState() == PathFinderState.PATH_FOUND) {
			Path path = pathFinder.getPath();
			if (path != null) {
				for (int i = 1; i < path.path.length; ++i) {
					debugDrawLine(game, path.path[i-1], path.path[i], Color.YELLOW);
				}			
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
		PacManSimulator.play(new E4());
	}

}