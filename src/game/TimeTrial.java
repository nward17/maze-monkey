package game;

import eventhandler.Keys;
import main.TimerPanel;
import maze.MazeMonkey;
import player.Monkey;

@SuppressWarnings("serial")
public class TimeTrial extends MazeMonkey implements Runnable {

	public static int gameResult = -1; 			// -1 means game still running, 0 is lost, 1 is won

	private TimerPanel timerPanel; 				// timer panel so we can start and stop timer
	private Monkey monkey; 						// our character for the time trial
	private Keys keyListener; 					// the keylistener for the maze
	public int size; 							// current size of maze

	private int mazesCompleted = 0;				// number of mazes completed

	private Thread lossCheck;					// allows us to check if player has run out of time

	public TimeTrial(int size, TimerPanel timerPanel) {
		super(size);
		this.size = size;
		this.timerPanel = timerPanel;

		setTrialData();
		new Thread(this).start();
	}

	private void setTrialData() {								// set some data for the timer
																// time limits and number of mazes
																// based off numerous tests
		if (size == 1) {
			timerPanel.remaining = 60;
			timerPanel.mazes = 6;
		} else if (size == 2) {
			timerPanel.remaining = 45;
			timerPanel.mazes = 3;
		} else if (size == 4) {
			timerPanel.remaining = 60;
			timerPanel.mazes = 1;
		} else if (size == 8) {
			timerPanel.remaining = 120;
			timerPanel.mazes = 1;
		}

		lossCheck = new Thread(new Runnable() {					// check if player has run out of time
			public void run() {
				while (true) {
					try { Thread.sleep(10); } catch (InterruptedException ignore) {}
					if (timerPanel.remaining == 0) {			// player ran out of time
						timerPanel.stopTimer();					// stop the timer
						gameResult = 0; 						// set value to lost
						repaint();								// show lost screen
						break;
					}
				}
			}
		});
		lossCheck.start();
	}

	public boolean reachedEndOfMaze() {							// true if current block is bottom right, path has been found
		if (monkey.currentRow == m.rows - 2 && monkey.currentColumn == m.columns - 2) return true;
		return false;
	}

	public void nextMaze() {
		removeKeyListener(keyListener); 						// remove old key listener
		mazesCompleted++;										// increment mazes completed
		timerPanel.mazes--;										// decrement number of mazes left
		if (timerPanel.mazes == 0) {
			lossCheck.interrupt();								// stop checking to see if the player lost
			timerPanel.stopTimer();								// stop the timer!
			timerPanel.repaint(); 								// just so the mazes "mazes left" number updates
			gameResult = 1; 									// user won the game
			repaint();
		} else {
			Thread.currentThread().interrupt(); 				// stop thread for old maze
			new Thread(this).start(); 							// start thread for new maze
		}
	}

	@Override
	public void run() {
		init(size);												// initiate a new maze

		try { Thread.sleep(500); 								// pause so user can look before it starts generating
		} catch (InterruptedException e) {}

		monkey = new Monkey(this);

		m.maze = new int[m.rows][m.columns]; 					// make a new maze
		create(); 												// generate the maze
		m.maze[monkey.currentRow][monkey.currentColumn] = 2; 	// add player to start of maze
		m.maze[m.rows - 2][m.columns - 2] = 2; 					// designate end of maze
		
		repaint();

		keyListener = new Keys(monkey); 						// new key listener with new monkey
		addKeyListener(keyListener); 							// add key listener

		if (mazesCompleted == 0) timerPanel.startTimer(); 		// only start time once if we just started game
	}
}
