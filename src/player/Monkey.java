package player;

import game.TimeTrial;

public class Monkey {
	
	private TimeTrial timeTrial;				// for access to maze object
	
	public int currentRow;						// x position in rows
	public int currentColumn;					// y position in rows
	public int nextRow;							// x position in rows
	public int nextColumn;						// y position in rows
	
	public Monkey(TimeTrial timeTrial) {
		this.timeTrial = timeTrial;
		currentRow = 1;
		currentColumn = 1;
	}
 	
	public void moveUp() {						// POST: move character up
		this.nextRow = currentRow - 1;
		moveRow();
	}

	public void moveDown() {					// POST: move character down
		this.nextRow = currentRow + 1;
		moveRow();
	}

	public void moveLeft() {					// POST: move character left
		this.nextColumn = currentColumn - 1;
		moveColumn();
	}

	public void moveRight() {					// POST: move character right
		this.nextColumn = currentColumn + 1;
		moveColumn();
	}
	
	private void moveRow() {
		if (timeTrial.m.isAvailableBlock(timeTrial.m.maze[nextRow][currentColumn])) {	// if block is movable
			if (timeTrial.m.maze[nextRow][currentColumn] == 2) {						// if block was already moved over
				timeTrial.m.maze[currentRow][currentColumn] = 4;						// set to gray
			}
			currentRow = nextRow;														// move character position
			timeTrial.m.maze[nextRow][currentColumn] = 2;								// set to green
			timeTrial.repaint();
		}
		if (timeTrial.reachedEndOfMaze()) {												// if character is in bottom right square
			timeTrial.nextMaze();														// finish and get next maze
		}
	}
	
	private void moveColumn() {
		if (timeTrial.m.isAvailableBlock(timeTrial.m.maze[currentRow][nextColumn])) {	// if block is movable
			if (timeTrial.m.maze[currentRow][nextColumn] == 2) {						// if block was already moved over
				timeTrial.m.maze[currentRow][currentColumn] = 4;						// set to gray
			}
			currentColumn = nextColumn;													// move character position
			timeTrial.m.maze[currentRow][nextColumn] = 2;								// set to green
			timeTrial.repaint();
		}
		if (timeTrial.reachedEndOfMaze()) {												// if character is in bottom right square
			timeTrial.nextMaze();														// finish and get next maze
		}
	}
}