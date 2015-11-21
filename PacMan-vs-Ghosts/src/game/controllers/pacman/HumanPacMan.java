package game.controllers.pacman;

import game.core.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Allows a human player to play the game using the arrow key of the keyboard.
 * 
 * Press 'P' to pause the simulation.
 */
public final class HumanPacMan implements IPacManController, KeyListener
{
	private PacManAction input = new PacManAction();
	
    @Override
	public void reset(Game game) {
    	input.reset();		
	}
    
    @Override
	public void nextLevel(Game game) {		
	}
    
    @Override
    public void tick(Game game, long timeDue) {
    }
    
    @Override
    public void killed() {
    }

    @Override
    public PacManAction getAction() {
    	return input;
    }
    
    public void keyPressed(KeyEvent e) 
    {
        int key = e.getKeyCode();
        
        if      (key == KeyEvent.VK_UP)    input.up();
        else if (key == KeyEvent.VK_RIGHT) input.right();
        else if (key == KeyEvent.VK_DOWN)  input.down();
        else if (key == KeyEvent.VK_LEFT)  input.left();
        else if (key == KeyEvent.VK_P)     input.togglePause();
        else if (key == KeyEvent.VK_N && input.pauseSimulation) input.nextFrame = true;
    }

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	
}