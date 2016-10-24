package game.controllers.pacman.exercises.e1.path.uninformed;

import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.path.uninformed.DLS.DLSConfig;
import game.controllers.pacman.exercises.e1.path.uninformed.base.SearchTreeNode;
import game.controllers.pacman.exercises.e1.path.uninformed.base.UninformedGraphSearch;

/**
 * DEPTH-LIMITED-SEARCH
 * 
 * TODO:
 * 
 * To make this work you need to:
 * 1) implement {@link DFS},
 * 2) which means to implement {@link UninformedGraphSearch#step()}.
 */
public class DLS extends DFS<DLSConfig> {
	
	private boolean depthLimitHit = false;
	
	public static class DLSConfig {
		
		public int depthLimit;

		public DLSConfig(int depthLimit) {
			this.depthLimit = depthLimit;
		}
				
	}
	
	@Override
	public String getName() {
		return "DLS[limit=" + (config == null ? "null" : config.depthLimit) + "," + getSteps() + "]";
	}
	
	@Override
	public void init(Graph graph, Node start, Node goal, DLSConfig config) {
		super.init(graph, start, goal, config);
		depthLimitHit = false;
	}
	
	@Override
	protected SearchTreeNode makeSearchNode(Node node, int pathCost, SearchTreeNode parent) {
		if (parent != null && config != null && parent.level + 1 > config.depthLimit) {
			depthLimitHit = true;
			return null;			
		}
		return super.makeSearchNode(node, pathCost, parent);
	}

	public boolean isDepthLimitHit() {
		return depthLimitHit;
	}
	
}
