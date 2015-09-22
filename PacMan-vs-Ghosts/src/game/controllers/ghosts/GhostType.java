package game.controllers.ghosts;

import java.util.HashMap;
import java.util.Map;

public enum GhostType {
	BLINKY(0), PINKY(1), CLYDE(2), INKY(3);
	
	public final int index;
	
	private static Map<Integer, GhostType> types = null;
	
	private GhostType(int index) {
		this.index = index;		
	}
	
	public static GhostType forIndex(int index) {
		if (types == null) {
			types = new HashMap<Integer, GhostType>();
			for (GhostType type : values()) {
				types.put(type.index, type);
			}
		}
		return types.get(index);
	}
}
