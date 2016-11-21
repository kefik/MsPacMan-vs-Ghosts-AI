package game.controllers.pacman.exercises.e4;

import java.awt.Color;
import java.awt.event.KeyEvent;

import game.PacManSimulator;
import game.controllers.pacman.PacManHijackController;
import game.controllers.pacman.exercises.e4.graph.Graph;
import game.controllers.pacman.exercises.e4.graph.Link;
import game.controllers.pacman.exercises.e4.graph.Node;
import game.controllers.pacman.exercises.e4.path.IPathFinder;
import game.controllers.pacman.exercises.e4.path.Path;
import game.controllers.pacman.exercises.e4.path.PathFinderState;
import game.controllers.pacman.exercises.e4.path.uninformed.BDS;
import game.controllers.pacman.exercises.e4.path.uninformed.BFS;
import game.controllers.pacman.exercises.e4.path.uninformed.DFS;
import game.controllers.pacman.exercises.e4.path.uninformed.DLS;
import game.controllers.pacman.exercises.e4.path.uninformed.IDS;
import game.controllers.pacman.exercises.e4.path.uninformed.UCS;
import game.core.Game;
import game.core.GameView;

public final class E4 extends PacManHijackController
{
	private Graph graph;
	
	private Node startNode;
	private Node goalNode;
	
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
	
	private boolean comparePathFinders = false;
	
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
		if (key == KeyEvent.VK_C) {
			comparePathFinders = !comparePathFinders;
			if (comparePathFinders) {
				solveAll();
			}
		}
		
		if (!comparePathFinders) {
			if (key == KeyEvent.VK_1 || key == KeyEvent.VK_NUMPAD1 || key == KeyEvent.VK_F1) {
				pathFinder = dfs;
				reinitPathFinder();
			}
			if (key == KeyEvent.VK_2 || key == KeyEvent.VK_NUMPAD2 || key == KeyEvent.VK_F2) {
				pathFinder = bfs;
				reinitPathFinder();
			}
			if (key == KeyEvent.VK_3 || key == KeyEvent.VK_NUMPAD3 || key == KeyEvent.VK_F3) {
				pathFinder = ucs;
				reinitPathFinder();
			}
			if (key == KeyEvent.VK_4 || key == KeyEvent.VK_NUMPAD4 || key == KeyEvent.VK_F4) {
				pathFinder = dls;
				reinitPathFinder();
			}
			if (key == KeyEvent.VK_5 || key == KeyEvent.VK_NUMPAD5 || key == KeyEvent.VK_F5) {
				pathFinder = ids;
				reinitPathFinder();
			}
			if (key == KeyEvent.VK_6 || key == KeyEvent.VK_NUMPAD6 || key == KeyEvent.VK_F6) {
				pathFinder = bds;
				reinitPathFinder();
			}
		}
	}
	
	private void generateNewStartGoal() {
		startNode = graph.getRandomNode();
		goalNode = graph.getRandomNode();
	}
	
	private void reinitPathFinder() {
		pathFinder.reset();
		generateNewStartGoal();
		
		if (pathFinder == dls) {
			pathFinder.init(graph, startNode, goalNode, new DLS.DLSConfig(3));
		} else {
			pathFinder.init(graph, startNode, goalNode, null);
		}
	}
	
	private void solveOne(IPathFinder pathFinder) {
		if (startNode == null) generateNewStartGoal();
		pathFinder.init(graph, startNode, goalNode, null);
		while (pathFinder.getState() == PathFinderState.RUNNING) {
			pathFinder.step();
		}
	}
	
	private void solveAll() {
		generateNewStartGoal();

		solveOne(dfs);
		solveOne(bfs);
		solveOne(ucs);
		solveOne(ids);
		solveOne(bds);
	}
	
	private void debugDraw(Game game) {
		debugDrawGraph(game);
		if (comparePathFinders) {
			debugDrawComparePathFinders(game);
		} else {
			debugDrawPathFinder(game, pathFinder);
		}
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
	
	private void debugDrawComparePathFinders(Game game) {
		if (!drawPathFinder) return;
		if (startNode == null) return; 
		
		GameView.addText(0, 0, Color.YELLOW, "COMPARE");
		
		GameView.addPoints(game, Color.RED, startNode.index);
		GameView.addPoints(game, Color.GREEN, goalNode.index);
		
		IPathFinder[] pathFinders = new IPathFinder[]{ dfs, bfs, ucs, ids, bds };
		Color[] colors = new Color[]{ Color.red, Color.blue, Color.green, Color.yellow, Color.magenta };
		
		for (int i = 0; i < pathFinders.length; ++i) {
			IPathFinder pathFinder = pathFinders[i];
			Color color = (i >= 0 && i < colors.length ? colors[i] : Color.WHITE);
			if (pathFinder.getState() == PathFinderState.PATH_FOUND) {
				Path path = pathFinder.getPath();
				if (path != null) {
					for (int j = 1; j < path.path.length; ++j) {
						debugDrawLine(game, path.path[j-1], path.path[j], color);
					}			
				}
				GameView.addText(0, (i+1)*6, color, pathFinder.getName() + ": path = " + pathFinder.getPath().computeCost());
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