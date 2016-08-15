package eventhandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import player.Monkey;

public class Keys implements KeyListener {
	
	private Monkey monkey;					// character object so we can move the player
	
	public Keys(Monkey monkey) {
		this.monkey = monkey;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {	// move character based on what key was pressed
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			monkey.moveUp();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			monkey.moveLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			monkey.moveDown();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			monkey.moveRight();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
