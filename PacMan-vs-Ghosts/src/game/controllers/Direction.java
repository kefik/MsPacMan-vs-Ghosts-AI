package game.controllers;

import game.core.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Direction {
	
	NONE(-1, 0, 0), UP(Game.UP, 0, -1), RIGHT(Game.RIGHT, 1, 0), DOWN(Game.DOWN, 0, 1), LEFT(Game.LEFT, -1, 0);
	
	private static Direction[] arrows = new Direction[]{UP, RIGHT, DOWN, LEFT};
	
	private static List<Direction> arrowsList = null;
	
	private static Map<Integer, Direction> indices = null;
	
	public final int index;
	public final int dX;
	public final int dY;

	private Direction(int directionIndex, int dX, int dY) {
		this.index = directionIndex;
		this.dX = dX;
		this.dY = dY;
	}
	
	public Direction opposite() {
		switch(this) {
		case NONE: return NONE;
		case DOWN: return UP;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		case UP: return DOWN;
		}
		return null;
	}
	
	public Direction cw() {
		switch(this) {
		case NONE: return NONE;
		case DOWN: return LEFT;
		case LEFT: return UP;
		case RIGHT: return DOWN;
		case UP: return RIGHT;
		}
		return null;
	}
	
	public Direction ccw() {
		switch(this) {
		case NONE: return NONE;
		case DOWN: return RIGHT;
		case LEFT: return DOWN;
		case RIGHT: return UP;
		case UP: return LEFT;
		}
		return null;
	}
	
	public static Direction forIndex(int directionIndex) {
		if (indices == null) {
			indices = new HashMap<Integer, Direction>();
			for (Direction dir : values()) {
				indices.put(dir.index, dir);
			}
		}
		Direction dir = indices.get(directionIndex);
		if (dir == null) return NONE;
		return dir;
	}
	
	/**
	 * UP, RIGHT, DOWN, LEFT
	 * @return
	 */
	public static Direction[] arrows() {
		return arrows;
	}
	
	/**
	 * UP, RIGHT, DOWN, LEFT
	 * @return
	 */
	public static List<Direction> arrowsList() {
		if (arrowsList == null) {
			arrowsList = new ArrayList<Direction>(4);
			for (Direction d : arrows) {
				arrowsList.add(d);
			}
		}
		return arrowsList;
	}
	

}
