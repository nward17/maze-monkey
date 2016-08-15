/*
 * GUI.java
 * 
 * Main driver that runs Maze Monkey and displays the maze using Swing.
 */

package main;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.RunTests;
import game.TimeTrial;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private JComboBox size; 							// defines a size x size grid based on predefined options
	private JButton test; 								// button to start testing on the maze
	private JButton timeTrial; 							// button to play time trial
	public static JTextArea ta;							// output area for the results of testing

	public GUI() {
		String[] sizes = {"Small", "Medium", "Large", "Extra Large"};
		size = new JComboBox(sizes);
		test = new JButton("Run Tests");
		timeTrial = new JButton("Time Trial");

		setContentPane(getSplash());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Maze Monkey");
		setResizable(false);
		setVisible(true);
	}

	protected JPanel getSplash() {
		JPanel splash = new JPanel();							// the splash screen for the program
		splash.setBackground(Color.BLACK);

		splash.add(new JLabel("<html><font color='white'>Size:</font></html>"));
		splash.add(size);
		splash.add(test);
		splash.add(timeTrial);

		test.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int gridSize = 0;								// grid size which is calculated based on the maze size chosen
				
				if (size.getSelectedItem().toString().equals("Small")) gridSize = 1;
				else if (size.getSelectedItem().toString().equals("Medium")) gridSize = 2;
				else if (size.getSelectedItem().toString().equals("Large")) gridSize = 4;
				else if (size.getSelectedItem().toString().equals("Extra Large")) gridSize = 8;
				
				JTabbedPane tabs = new JTabbedPane();			// tabbed panel for displaying different parts of the program
				
				ta = new JTextArea();
				
				RunTests maze = new RunTests(gridSize);			// panel for running maze tests
				
				tabs.addTab("Maze", null, maze, null);
				tabs.addTab("Results", null, getResultsPanel(), null);
				
				setContentPane(tabs);
				pack();
				validate();
				repaint();
			}
		});
		
		timeTrial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
				
				int gridSize = 0;												// grid size which is calculated based on the maze size chosen
				
				if (size.getSelectedItem().toString().equals("Small")) gridSize = 1;
				else if (size.getSelectedItem().toString().equals("Medium")) gridSize = 2;
				else if (size.getSelectedItem().toString().equals("Large")) gridSize = 4;
				else if (size.getSelectedItem().toString().equals("Extra Large")) gridSize = 8;
				
				TimerPanel timerPanel = new TimerPanel();						// panel for timer information
				TimeTrial timeTrial = new TimeTrial(gridSize, timerPanel);		// panel for the time trial mazes
				
				mainPanel.add(timerPanel);
				mainPanel.add(timeTrial);
				
				setContentPane(mainPanel);
				pack();
				validate();
				repaint();
				
				timeTrial.setFocusable(true);									// for the keylistener
				timeTrial.requestFocus();
			}
		});
		
		return splash;
	}

	protected JPanel getResultsPanel() {						// POST: return the splash screen
		JPanel resultsPanel = new JPanel(new BorderLayout());	// panel that holds the testing results
		ta.setEditable(false);
		JScrollPane scrollPanel = new JScrollPane(ta);			// the scroll panel that the text area sits inside of
		resultsPanel.add(scrollPanel, BorderLayout.CENTER);
		return resultsPanel;
	}

	public static void main(String[] args) {					// start Maze Monkey
		new GUI();
	}
}
