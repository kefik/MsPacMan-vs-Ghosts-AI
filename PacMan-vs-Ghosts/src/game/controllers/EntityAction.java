package game.controllers;

public class EntityAction {

	protected Direction direction;
	
	public void left() {
		direction = Direction.LEFT;
	}
	
	public void right() {
		direction = Direction.RIGHT;
	}
	
	public void up() {
		direction = Direction.UP;
	}
	
	public void down() {
		direction = Direction.DOWN;
	}
	
	public void set(Direction dir) {
		direction = dir;
	}
	
	public void set(int directionIndex) {
		direction = Direction.forIndex(directionIndex);
	}
	
	public Direction get() {
		return direction;
	}
	
	public EntityAction clone() {
		EntityAction result = new EntityAction();
		
		result.direction = direction;
		
		return result;
	}
	
	public void reset() {
		direction = Direction.NONE;		
	}
	
}
