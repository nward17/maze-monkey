package maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.TimeTrial;
import main.GUI;

@SuppressWarnings("serial")
public class MazeMonkey extends JPanel {
	
	public Maze m;											// maze object
	
	public MazeMonkey(int size) {
		init(size);
    }
	
	protected void init(int size) {
		this.m = new Maze(size);
		setPreferredSize(new Dimension(m.width, m.height));
        setBackground(Color.BLACK);
	}
	
	public void create() {									// randomly generate a maze
        int emptyBlocks = 0; 								// count of empty blocks
        int walls = 0;  									// count of walls
        int[] wallsRow = new int[(m.rows*m.columns)/2];  	// temporary wall positions (half the total maze size)
        int[] wallsColumn = new int[(m.rows*m.columns)/2];
        
        for (int i = 0; i < m.rows; i++)  					// for each row
            for (int j = 0; j < m.columns; j++)				// for each column (each block in a row)
            	m.maze[i][j] = 1;							// make the block a wall
        													// the maze is all walls
        
        for (int i = 1; i < m.rows - 1; i += 2)  			// loop over every other block
            for (int j = 1; j < m.columns - 1; j += 2) {
            	m.maze[i][j] = -emptyBlocks;  				// make every other block an empty block
            	emptyBlocks++;								// so increment our empty blocks
                if (i < m.rows - 2) {  						// if there is a block below this room
                	wallsRow[walls] = i + 1;				// set this block to be a wall in the temporary wall array
                	wallsColumn[walls] = j;
                    walls++;								// increment because we added a wall to the maze
                }
                if (j < m.columns - 2) {  					// if there is a block below this room
                	wallsRow[walls] = i;					// set this block to be a wall in the temporary wall array
                	wallsColumn[walls] = j + 1;
                    walls++;								// increment because we added a wall to the maze
                }
            }
        repaint();
        for (int i = walls - 1; i > 0; i--) {				// loop over the number of walls generated (half the total maze size)
            int r = (int)(Math.random() * i);  				// choose a random wall
            removeWall(wallsRow[r], wallsColumn[r]);		// remove the wall if it doesn't form a loop
            wallsRow[r] = wallsRow[i];
            wallsColumn[r] = wallsColumn[i];
        }
        for (int i = 1; i < m.rows - 1; i++)  				// for each row
            for (int j = 1; j < m.columns - 1; j++)			// for each column (each block in a row)
                if (m.maze[i][j] < 0)						// set the empty blocks to finalized blocks 
                	m.maze[i][j] = 3;
    }
	
	private synchronized void removeWall(int desiredRow, int desiredColumn) {	// removes a given block (wall) if it does not form a loop
    	int topBlock = m.maze[desiredRow - 1][desiredColumn];					// block directly above the current block
    	int leftBlock = m.maze[desiredRow][desiredColumn - 1];					// block directly left of the current block
    	int bottomBlock = m.maze[desiredRow + 1][desiredColumn];				// block directly below the current block
    	int rightBlock = m.maze[desiredRow][desiredColumn + 1];					// block directly right of the current block
    	
        if (desiredRow % 2 == 1 && leftBlock != rightBlock) {					// wall is vertical
        	m.fixBlocks(desiredRow, desiredColumn - 1, leftBlock, rightBlock);	// fix all blocks so a loop is not created when the wall is removed
            m.maze[desiredRow][desiredColumn] = rightBlock;						// set current block with wall to be empty (no wall) thus removing the wall
        } else if (desiredRow % 2 == 0 && topBlock != bottomBlock) {			// wall is horizontal
        	m.fixBlocks(desiredRow - 1, desiredColumn, topBlock, bottomBlock);	// fix all blocks so a loop is not created when the wall is removed
            m.maze[desiredRow][desiredColumn] = bottomBlock;					// set current block with wall to be empty (no wall) thus removing the wall
        }
        repaint();
        try { wait(1); }
        catch (InterruptedException e) { }
    }
	
	protected boolean solve(int currentRow, int currentColumn) {						// find solution by recursively checking the current block's surrounding blocks
    	int currentBlock = m.maze[currentRow][currentColumn];							// get the current block that we are on
    	
        if (m.isBlockEmpty(currentBlock)) {												// make sure current block is empty
        	m.maze[currentRow][currentColumn] = 2;      								// add this block to the path as a potential solution
            repaint();
            
            m.blocksTraversed++;														// keep track of how many blocks were traversed over
            
            if (currentRow == m.rows-2 && currentColumn == m.columns-2) {				// current block is bottom right, path has been found
            	GUI.ta.append(m.blocksTraversed + " blocks were traversed while solving the maze.\n");
            	double percentage = (((double) m.blocksTraversed / (m.rows * m.columns))*100) * 2;
                DecimalFormat f = new DecimalFormat("##.00");
            	GUI.ta.append("This results in " + f.format(percentage) + "% of the maze being traveled.\n");
            	return true;
            }
            
            try { Thread.sleep(1); }
            catch (InterruptedException e) { }
            
            int topBlock = currentRow - 1;												// block directly above the current block
            int leftBlock = currentColumn - 1;											// block directly left of the current block
            int bottomBlock = currentRow + 1;											// block directly below the current block
            int rightBlock = currentColumn + 1;											// block directly right of the current block
            
            if (solve(topBlock, currentColumn) || solve(currentRow, leftBlock) ||		// a solution was found
            	solve(bottomBlock, currentColumn) || solve(currentRow, rightBlock))
            	return true;
            
            m.maze[currentRow][currentColumn] = 4;   									// no solution from current block so make the block visited instead
            repaint();
            
            synchronized(this) {
                try { wait(1); }
                catch (InterruptedException e) { }
            }
        }
        return false;
    }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (TimeTrial.gameResult == -1) {								// if game is in session, not won or lost
			m.display(g);												// redraw the maze because game is still going
		} else {
			BufferedImage img = null;
			
			if (TimeTrial.gameResult == 0) {							// user lost the game
				try {
	        	    img = ImageIO.read(new File("lost.jpg"));			// grab the winning image
	        	    img = resize(img, 300, 300);
	        	    JLabel picture = new JLabel(new ImageIcon(img));
	    			picture.setSize(300, 300);
	            	setBackground(Color.WHITE);
	            	add(picture);
	        	} catch (IOException e) { e.printStackTrace(); }
	        } else if (TimeTrial.gameResult == 1) {						// user won game
	        	try {
	        	    img = ImageIO.read(new File("won.jpg"));			// grab the losing image
	        	    img = resize(img, 400, 400);
	        	    JLabel picture = new JLabel(new ImageIcon(img));
	    			picture.setSize(400, 400);
	            	setBackground(Color.WHITE);
	            	add(picture);
	        	} catch (IOException e) { e.printStackTrace(); }
	        }
			TimeTrial.gameResult = -2;									// set to different value so panel doesn't repaint with maze
		}
    }
	
	private BufferedImage resize(Image image, int width, int height) {	// resizes a given buffered image
    	BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	Graphics2D g = resized.createGraphics();
    	g.drawImage(image, 0, 0, width, height, null); 
    	g.dispose();
    	return resized;
    }
}
