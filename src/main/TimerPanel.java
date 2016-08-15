package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TimerPanel extends JPanel {
	
	private JLabel timeLeft = new JLabel();		// message for time left in top panel
	public int remaining = 0;					// actual remaining time left
	
	private JLabel mazesLeft = new JLabel();	// message for mazes left in top panel
	public int mazes = 0;						// actual number of mazes left
	
	private Timer timer;						// reference to timer so we can start and stop it

	public void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {		// run timer and decrease timeLeft by 1 every second	
            @Override
            public void run() {
                remaining--;
                repaint();
            }   
        },0, 1000);
	}
	public void stopTimer() {					// stop the timer
		timer.cancel();
		timer.purge();
	}

	public TimerPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		timeLeft = new JLabel();
		mazesLeft = new JLabel();
		
		timeLeft.setText("<html><font color='white' style='font-size: 16pt;'>Time left: "+ remaining +"</font></html>");
		mazesLeft.setText("<html><font color='white' style='font-size: 16pt;'>Mazes left: " + mazes + "</font></html>");
		
		add(Box.createRigidArea(new Dimension(80, 0)));		// add some spacing between the labels
		add(timeLeft);
		add(mazesLeft);
		
		setBackground(Color.GRAY);
	}

	public void paintComponent(Graphics g) {				// repaint with new values for remaining time and mazes
		super.paintComponent(g);
		timeLeft.setText("<html><font color='white' style='font-size: 16pt;'>Time left: "+ remaining +"</font></html>");
		mazesLeft.setText("<html><font color='white' style='font-size: 16pt;'>Mazes left: " + mazes + "</font></html>");
	}
}
