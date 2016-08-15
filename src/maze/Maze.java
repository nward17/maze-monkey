package maze;

import java.awt.Color;
import java.awt.Graphics;

public class Maze {
	public int[][] maze;												// 2 dimensional maze containing blocks with a row and column
	protected int blockSize;     										// size of each block
	
    public int rows;          											// rows of blocks in maze
    public int columns;       											// columns of blocks in maze
    
    public int width;   												// width of maze
    public int height;  												// height of maze
    
    public int blocksTraversed = 0;										// blocks traversed per maze (used in solving algorithm)
    
    public Maze(int size) {
		rows = (size * 10) + 1;											// calculate number of rows in maze
		columns = rows + 10;											// calculate number of columns in maze
        
        if (size == 1) blockSize = 32;									// small maze
        else if (size == 2) blockSize = 20;								// medium maze
        else if (size == 4) blockSize = 12;								// large maze
        else if (size == 8) blockSize = 6;								// extra large maze
        
        width = blockSize * columns;									// set the width of the panel dynamically for best viewability
        height = blockSize * rows;										// set the height of the panel dynamically for best viewability
        
        maze = new int[rows][columns];									// make a new maze
    }
    
    protected boolean isBlockEmpty(int block) {							// POST: return whether block is empty
    	return block == 3;
    }
    
    public boolean isAvailableBlock(int block) {
    	return block == 3 || block == 2 || block == 4;
    }
    
    protected void display(Graphics g) {														// redraw the maze for each new step of generating/solving
    	try {
            for (int i = 0; i < columns; i++) {													// for each column
                for (int j = 0; j < rows; j++) {												// for each row (each block in each row)
                	
                	int currentBlock = maze[j][i];												// the current block we are on
                    if (currentBlock < 0 || currentBlock == 3) g.setColor(Color.WHITE);			// block is a valid traversable block (not a wall)
                    else if (currentBlock == 0 || currentBlock == 1) g.setColor(Color.BLACK);	// block is a wall
                    else if (currentBlock == 2) g.setColor(new Color(41, 150, 11));				// block was traversed and is valid for the solution
                    else if (currentBlock == 4) g.setColor(new Color(118, 122, 116));			// block was traversed but was not valid for the solution
                    
                    g.fillRect((i * blockSize), (j * blockSize), blockSize, blockSize);
                }
            }
    	} catch (NullPointerException e) {
    		// do not worry, our maze array hasn't been populated yet, but it will be
    	}
    }
    
    protected void fixBlocks(int currentRow, int currentColumn, int replaceBlock, int replaceBlockWith) {
        if (maze[currentRow][currentColumn] == replaceBlock) {								// blocks are the same (a.k.a, could be causing a loop), so we need to fix this
            maze[currentRow][currentColumn] = replaceBlockWith;								// replace the block with the empty block beside it
            fixBlocks(currentRow + 1, currentColumn, replaceBlock, replaceBlockWith);		// recursively fix all blocks below
            fixBlocks(currentRow - 1, currentColumn, replaceBlock, replaceBlockWith);		// recursively fix all blocks above
            fixBlocks(currentRow, currentColumn + 1, replaceBlock, replaceBlockWith);		// recursively fix all blocks to the right
            fixBlocks(currentRow, currentColumn - 1, replaceBlock, replaceBlockWith);		// recursively fix all blocks to the left
        }
    }
}
