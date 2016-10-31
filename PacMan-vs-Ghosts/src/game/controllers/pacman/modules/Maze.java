package game.controllers.pacman.modules;

import game.controllers.Direction;
import game.core.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Maze {

	private static Random random = new Random(System.currentTimeMillis());
	
	public static final int HEIGHT = 120;
	public static final int WIDTH = 120;
	
	public static enum NodeCategory {
		SOLITER, TURN, CORRIDOR, T_CROSS, CROSS
	}
	
	public static enum NodeType {
		// NO NEIGHBOURS
		SOLITER(NodeCategory.SOLITER),
		
		// TURNS
		TURN_UP_RIGHT(NodeCategory.TURN), 
		TURN_RIGHT_DOWN(NodeCategory.TURN), 
		TURN_DOWN_LEFT(NodeCategory.TURN), 
		TURN_LEFT_UP(NodeCategory.TURN),
		
		// CORRIDOR
		CORRIDOR_LEFT_RIGHT(NodeCategory.CORRIDOR), 
		CORRIDOR_UP_DOWN(NodeCategory.CORRIDOR),
		
		// T_CROSS JUNCTIONS
		T_URD(NodeCategory.T_CROSS),
		T_RDL(NodeCategory.T_CROSS), 
		T_DLU(NodeCategory.T_CROSS), 
		T_LUR(NodeCategory.T_CROSS),
		
		// FULL CROSS
		CROSS(NodeCategory.CROSS);
		
		public final NodeCategory category;

		private NodeType(NodeCategory category) {
			this.category = category;
		}
	}
		
	public class MazeNode {
		
		public int index;
		
		private NodeType nodeType = null;
		
		protected MazeNode[] neighbours = new MazeNode[4];
		
		public MazeNode(int index) {
			this.index = index;
		}
		
		/**
		 * X-coordinate of the node.
		 * @return
		 */
		public int x() {
			return game.getX(index);
		}
		
		/**
		 * Y-coordinate of the node.
		 * @return
		 */
		public int y() {
			return game.getY(index);
		}
		
		/**
		 * Is there a pill on this node?
		 * @return
		 */
		public boolean pill() {
			int pillIndex = game.getPillIndex(index);
			if (pillIndex < 0) return false;
			return game.checkPill(pillIndex);
		}
		
		/**
		 * Is there a power pill on this node?
		 * @return
		 */
		public boolean powerPill() {
			int pillIndex = game.getPowerPillIndex(index);
			if (pillIndex < 0) return false;
			return game.checkPowerPill(pillIndex);
		}
		
		/**
		 * Is there a Ms Pac-Man on this node? 
		 * @return
		 */
		public boolean pacman() {
			return index == game.getCurPacManLoc(); 
		}
		
		/**
		 * Is there a ghost (either edible/dangerous) on this node?
		 * @return
		 */
		public boolean ghost() {
			return getGhostLocationSet().contains(this);
		}
		
		/**
		 * Is there an edible ghost on this node?
		 * @return
		 */
		public boolean ghostEdible() {
			if (!ghost()) return false;
			for (int i = 0; i < 4; ++i) {
				if (game.getCurGhostLoc(i) == index) {
					if (game.isEdible(i)) return true;
				}
			}
			return false;
		}
		
		/**
		 * Is there a ghost that can kill Ms Pac-Man on this node?
		 * @return
		 */
		public boolean ghostDanger() {
			if (!ghost()) return false;
			for (int i = 0; i < 4; ++i) {
				if (game.getCurGhostLoc(i) == index) {
					if (!game.isEdible(i)) return true;
				}
			}
			return false;
		}
		
		/**
		 * Returns {@link NodeCategory} ... Cross / T-Cross / Corridor / Junction.
		 * @return
		 */
		public NodeCategory nodeCategory() {
			return nodeType().category;
		}
		
		/**
		 * Concrete {@link NodeType} of given {@link NodeCategory}.
		 * @return
		 */
		public NodeType nodeType() {
			if (nodeType != null) return nodeType;
			if (neighbours[0] != null && neighbours[1] != null && neighbours[2] != null && neighbours[3] != null) return nodeType = NodeType.CROSS;
			if (neighbours[0] != null && neighbours[1] != null && neighbours[2] != null && neighbours[3] == null) return nodeType = NodeType.T_URD;
			if (neighbours[0] == null && neighbours[1] != null && neighbours[2] != null && neighbours[3] != null) return nodeType = NodeType.T_RDL;
			if (neighbours[0] != null && neighbours[1] == null && neighbours[2] != null && neighbours[3] != null) return nodeType = NodeType.T_DLU;			
			if (neighbours[0] != null && neighbours[1] != null && neighbours[2] == null && neighbours[3] != null) return nodeType = NodeType.T_LUR;
			if (neighbours[0] != null && neighbours[1] != null && neighbours[2] == null && neighbours[3] == null) return nodeType = NodeType.TURN_UP_RIGHT;
			if (neighbours[0] == null && neighbours[1] != null && neighbours[2] != null && neighbours[3] == null) return nodeType = NodeType.TURN_RIGHT_DOWN;
			if (neighbours[0] == null && neighbours[1] == null && neighbours[2] != null && neighbours[3] != null) return nodeType = NodeType.TURN_DOWN_LEFT;
			if (neighbours[0] != null && neighbours[1] == null && neighbours[2] == null && neighbours[3] != null) return nodeType = NodeType.TURN_LEFT_UP;
			if (neighbours[0] != null && neighbours[1] == null && neighbours[2] != null && neighbours[3] == null) return nodeType = NodeType.CORRIDOR_UP_DOWN;
			if (neighbours[0] == null && neighbours[1] != null && neighbours[2] == null && neighbours[3] != null) return nodeType = NodeType.CORRIDOR_LEFT_RIGHT;
			if (neighbours[0] == null && neighbours[1] == null && neighbours[2] == null && neighbours[3] == null) return nodeType = NodeType.SOLITER;
			// TODO: rise an exception?
			return null;
		}
		
		/**
		 * Is this node a JUNCTION (T_CROSS or CROSS) => requires decision what to do.
		 * @return
		 */
		public boolean junction() {
			return nodeCategory() == NodeCategory.CROSS || nodeCategory() == NodeCategory.T_CROSS;
		}
				
		/**
		 * Array[UP, RIGHT, DOWN, LEFT] (as defined by {@link Direction#index}). Array may contain NULLs == wall. 
		 * 
		 * @return
		 */
		public MazeNode[] links() {
			return neighbours;
		}
		
		/**
		 * Filters {@link #links()}.
		 * 
		 * Array[UP, RIGHT, DOWN, LEFT] (as defined by {@link Direction#index}). Array may contain NULLs == wall || forbidden node. 
		 * 
		 * @return
		 */
		public MazeNode[] options(MazeNode... forbidden) {
			MazeNode[] result = new MazeNode[4];
			for (int i = 0; i < 4; ++i) {				
				MazeNode option = neighbours[i];
				if (option == null) continue;
				boolean ok = true;
				for (MazeNode forbiddenNode : forbidden) {
					if (forbiddenNode == null) continue;
					if (option.index == forbiddenNode.index) {
						ok = false;
						break;
					}
				}
				if (ok) {
					result[i] = option;
				}				
			}
			return result;
		}
		
		/**
		 * Returns a {@link MazeNode} in given 'direction' or NULL if there is a wall. {@link Direction#NONE} evaluates to 'this'.
		 * @param direction
		 * @return
		 */
		public MazeNode link(Direction direction) {
			if (direction == null) return null;
			if (direction == Direction.NONE) return this;
			return neighbours[direction.index];
		}
		
		/**
		 * If there is walkable node in given 'direction'. {@link Direction#NONE} evaluates to 'null'.
		 * @param direction
		 * @return
		 */
		public boolean hasLink(Direction direction) {
			if (direction == null) return false;
			if (direction == Direction.NONE) return false;
			return neighbours[direction.index] != null;
		}
		
		/**
		 * Returns random neighbor.
		 * @return
		 */
		public MazeNode getRandomLink() {
			List<Direction> directions = new ArrayList<Direction>(Direction.arrowsList());
			Collections.shuffle(directions);
			while (directions.size() > 0) {
				Direction dir = directions.remove(directions.size()-1);
				if (hasLink(dir)) return link(dir);
			}
			return null;
		}
		
		/**
		 * Returns random neighbor that is not within 'forbidden' list of nodes.
		 * @return
		 */
		public MazeNode getRandomLink(MazeNode... forbidden) {
			List<Direction> directions = new ArrayList<Direction>(Direction.arrowsList());
			Collections.shuffle(directions);
			MazeNode[] options = options(forbidden);
			while (directions.size() > 0) {
				Direction dir = directions.remove(directions.size()-1);
				if (options[dir.index] != null) return options[dir.index];
			}
			return null;
		}
		
		/**
		 * Returns a direction for 'neighbour' ... must be immediate neighbour!
		 * @param neighbour
		 * @return
		 */
		public Direction direction(MazeNode neighbour) {
			for (Direction dir : Direction.arrows()) {
				MazeNode node = link(dir);
				if (node != null && node == neighbour) return dir;
			}
			return Direction.NONE;
		}
		
		/**
		 * Returns a direction for 'neighbour' ... must be immediate neighbour!
		 * @param neighbour
		 * @return
		 */
		public Direction direction(int neighbour) {
			for (Direction dir : Direction.arrows()) {
				MazeNode node = link(dir);
				if (node != null && node.index == neighbour) return dir;
			}
			return Direction.NONE;
		}

		public Integer pillIndex() {
			return game.getPillIndex(index);
		}
		
		@Override
		public String toString() {
			return "MazeNode[index=" + index + "]";
		}
		
	}
	
	private Game game;
	
	private MazeNode[] nodes;
	
	public void reset(Game game) {
		this.game = game;
		// ADJUST LENGTH OF 'nodes'
		if (nodes == null || nodes.length != game.getNumberOfNodes()) {
			nodes = new MazeNode[game.getNumberOfNodes()];			
		}
		// CLEAR 'nodes'
		for (int i = 0; i < nodes.length; ++i) {
			nodes[i] = null;
		}
		// BUILD 'nodes'
		for (int nodeIndex = 0; nodeIndex < nodes.length; ++nodeIndex) {
			if (nodes[nodeIndex] == null) {
				nodes[nodeIndex] = new MazeNode(nodeIndex);
			}
			for (Direction dir : Direction.arrows()) {
				int nextNode = game.getNeighbour(nodeIndex, dir.index);
				if (nextNode >= 0) {
					if (nodes[nextNode] == null) {
						nodes[nextNode] = new MazeNode(nextNode);
					}
					nodes[nodeIndex].neighbours[dir.index] = nodes[nextNode];
				}
			}
		}
	}
	
	/**
	 * How many pills are currently within the maze?
	 * @return
	 */
	public int getNumActivePills() {
		return game.getNumActivePills();
	}
	
	/**
	 * How many power-pills are currently within the maze?
	 * @return
	 */
	public int getNumActivePowerPills() {
		return game.getNumActivePowerPills();
	}
	
	/**
	 * Returns nodes with PILLS.
	 * 
	 * Time complexity: O(n) !
	 * @return
	 */
	public MazeNode[] getPillIndicesActive() {
		int[] indices = game.getPillIndicesActive();
		MazeNode[] result = new MazeNode[indices.length];
		for (int i = 0; i < indices.length; ++i) {
			result[i] = getNode(indices[i]);
		}
		return result;
	}
	
	/**
	 * Returns nodes with POWER-PILLS.
	 * 
	 * @return
	 */
	public MazeNode[] getPowerPillIndicesActive() {
		int[] indices = game.getPowerPillIndicesActive();
		MazeNode[] result = new MazeNode[indices.length];
		for (int i = 0; i < indices.length; ++i) {
			result[i] = getNode(indices[i]);
		}
		return result;
	}
	
	/**
	 * Returns {@link MazeNode} where the Ms Pac-Man stands.
	 * @return
	 */
	public MazeNode getPacManLocation() {
		return getNode(game.getCurPacManLoc());
	}
	
	/**
	 * Returns a {@link MazeNode} for a given node index.
	 * @param nodeIndex
	 * @return
	 */
	public MazeNode getNode(int nodeIndex) {
		if (nodes == null || nodeIndex < 0 || nodeIndex > nodes.length) return null;
		return nodes[nodeIndex];
	}
	
	/**
	 * Returns all existing {@link MazeNode}s.
	 * @return
	 */
	public MazeNode[] getNodes() {
		return nodes;
	}

	/**
	 * Returns random {@link MazeNode}.
	 * @return
	 */
	public MazeNode getRandomNode() {
		return nodes[random.nextInt(nodes.length)];
	}
	
	/**
	 * Underlying game data.
	 * @return
	 */
	public Game getGame() {
		return game;
	}

	private int lastGhostTimeLocations = -1;

	private MazeNode[] ghostLocations = new MazeNode[4];
	
	private int lastGhostTimeLocationSet = -1;

	private Set<MazeNode> ghostLocationSet = new HashSet<MazeNode>();
	
	private int lastGhostTimeIndices = -1;
	
	private Set<Integer> ghostLocationIndices = new HashSet<Integer>();
	
	public Set<Integer> getGhostLocationIndices() {
		if (lastGhostTimeIndices == game.getTotalTime()) return ghostLocationIndices;
		ghostLocationIndices.clear();
		for (int i = 0; i < 4; ++i) {
			ghostLocationIndices.add(game.getCurGhostLoc(i));
		}
		lastGhostTimeIndices = game.getTotalTime();
		return ghostLocationIndices;
	}
	
	/**
	 * Returns {@link MazeNode} with ghosts.
	 * @return
	 */
	public MazeNode[] getGhostLocations() {
		if (lastGhostTimeLocations == game.getTotalTime()) return ghostLocations;
		for (int i = 0; i < 4; ++i) {
			ghostLocations[i] = getNode(game.getCurGhostLoc(i));
		}
		lastGhostTimeLocations = game.getTotalTime();
		return ghostLocations;
	}
	
	/**
	 * Returns set of {@link MazeNode} with ghosts.
	 * @return
	 */
	public Set<MazeNode> getGhostLocationSet() {
		if (lastGhostTimeLocationSet == game.getTotalTime()) return ghostLocationSet;
		ghostLocationSet.clear();
		for (int i = 0; i < 4; ++i) {
			ghostLocationSet.add(getNode(game.getCurGhostLoc(i)));
		}
		lastGhostTimeLocationSet = game.getTotalTime();
		return ghostLocationSet;
	}

}
