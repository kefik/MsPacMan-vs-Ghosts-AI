package game.controllers.pacman.exercises.e5.graph;

public interface ILink<NODE extends INode> {

	public NODE getFirst();
	public NODE getSecond();
	
	public int getCost();
	
	public NODE getOther(NODE node);
	
}
