package game.controllers.pacman;

import game.core.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PacManHijackController extends PacManControllerBase implements KeyListener {

	protected boolean hijacked = false;
	
	protected PacManAction human = new PacManAction();
	
	@Override
	public void reset(Game game) {
		super.reset(game);
		human.reset();
	}
	
	@Override
	public void tick(Game game, long timeDue) {
	}
	
	public PacManAction getAction() {
		return hijacked ? human : pacman;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_H) {
			hijacked = !hijacked;
			if (hijacked) human.pauseSimulation = pacman.pauseSimulation;
		} else		
		if (key == KeyEvent.VK_P) {
			if (hijacked) human.togglePause();
			else pacman.togglePause();
		} else
		if (key == KeyEvent.VK_N) {
			if (hijacked) {
				if (human.pauseSimulation) human.nextFrame = true;
			} else {
				if (pacman.pauseSimulation) pacman.nextFrame = true;
			}
		}
		else if (key == KeyEvent.VK_UP)    human.up();
		else if (key == KeyEvent.VK_RIGHT) human.right();
		else if (key == KeyEvent.VK_DOWN)  human.down();
		else if (key == KeyEvent.VK_LEFT)  human.left();		       
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

}
