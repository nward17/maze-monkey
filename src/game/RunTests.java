package game;

import main.GUI;
import maze.MazeMonkey;

@SuppressWarnings("serial")
public class RunTests extends MazeMonkey implements Runnable {

	public RunTests(int size) {
		super(size);
		new Thread(this).start();
	}
	
    public void run() {
        try {
        	Thread.sleep(1000);						// pause for a second so the user has a second to look before it starts generating
        } catch (InterruptedException e) {}
        
        int mazes = 1;								// keep track of how many mazes we have generated
        while (true) {
        	m.maze = new int[m.rows][m.columns];	// make a new maze
        	
        	GUI.ta.append("Maze " + mazes + " generating...\n");
        	
        	long startTime = System.nanoTime();
            create();								// generate the maze
            long endTime = System.nanoTime();
            
            GUI.ta.append("Maze " + mazes + " took " + ((endTime - startTime) / 1000000) + " miliseconds to finish generating.\n");
            
            startTime = System.nanoTime();
            solve(1,1);								// solve the maze
            endTime = System.nanoTime();
            
            GUI.ta.append("Maze " + mazes + " took " + ((endTime - startTime) / 1000000) + " miliseconds to finish solving.\n\n");
            
            m.blocksTraversed = 0;					// reset blocksTraversed
            
            synchronized(this) {					// pause for a few seconds so the user can see the final maze before the next one starts
                try { wait(3000); }
                catch (InterruptedException e) { }
            }
            mazes++;
        }
    }

}
