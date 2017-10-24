package neuroevolution;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	private NetworkInterface netInterface;
	private int refreshRate = 16;
	private final int NETWORK_REFRESH_RATE = 1;
	private int currentRefresh = 0;
	
	private Color[] playerColors = {
			Color.RED,
			Color.BLUE,
			Color.GREEN,
			Color.YELLOW,
			Color.PINK,
			Color.ORANGE,
			Color.MAGENTA,
			Color.GRAY,
			Color.WHITE,
			Color.lightGray
	};
	
	//players
	int[] players;
	//player movements
	int[] movements = {10,10,10,10,10,10,10,10,10,10}; 
	
	//player fitness levels
	double[] playerFitness = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	
	//deterministic column lengths
	double columnLengths[];
	int currColumn = 0;
	
	boolean[] alive = {true,true,true,true,true,true,true,true,true,true};
	
	private int columnX = 800;
	private int columnY = 200 + (int)(Math.random() * 300);
	public int currentGeneration = 1;
	Timer updateTimer;
	
	public GamePanel() {
		players = new int[10];
		for(int i = 0; i< players.length; i++) {
			players[i] = 200;
		}
		updateTimer = new Timer(refreshRate,this);
		updateTimer.start();
	}
	
	public void switchSpeed() {
		if(refreshRate == 16)
			refreshRate = 1;
		else if(refreshRate == 1)
			refreshRate = 5;
		else
			refreshRate = 16;
		updateTimer.setDelay(refreshRate);
	}
	
	public void resetGame() {
		for(int i = 0; i<10;i++) {
			players[i] = 200;
			movements[i] = 10;
			playerFitness[i] = 0;
			alive[i] = true;
		}
		currentGeneration++;
		columnX = 800;
		currColumn = 0;
		columnY = 200 + (int)(Math.random() * 300);
	}
	
	public void setInterface(NetworkInterface interf) {
		this.netInterface = interf;
	}

	
	public void playerJump(int player) {
		movements[player] = -15;
	}
	
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //set background
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 800, 500);
        
        for(int i = 0; i<10; i++) {
        		if(alive[i]) {
	    			g.setColor(playerColors[i]);
	    			g.fillRect(150,players[i], 30, 30);
        		}
        }
        
        //set 
        g.setColor(Color.BLACK);
        g.fillRect(columnX, columnY, 30, 600);
        
        g.setFont(new Font("Arial", Font.BOLD, 15)); 
        g.drawString("Generation " + currentGeneration, 20, 20);
        g.setFont(new Font("Arial", Font.PLAIN, 12)); 
        for(int i = 1; i<11;i++) {
        		g.drawString(String.valueOf("Network " + i + " fitness: " + Math.round(playerFitness[i-1] * 100)/100), 20, 40 + (20*i));
        }
        
        
    }
    
	public void drawScreen() {
		super.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		boolean isAllDead = true;
		for(int i = 0; i<players.length;i++) {
			if(players[i] >= 450 || players[i] <= 0 && alive[i]) {
				alive[i] = false;
			}
			if(columnX >= 150 && columnX <= 180 && players[i]+30 >= columnY)
				alive[i] = false;
			if(alive[i])
				isAllDead = false;
		}
		
		if(isAllDead)
			netInterface.allDead(players, playerFitness);
		if(currentRefresh == NETWORK_REFRESH_RATE) {
			netInterface.getPlayerInputs(players, columnX, columnY);
			currentRefresh = 0;
		}else
			currentRefresh++;
			
		if(columnX <= -50) {
			columnX = 810;
			columnY = 200 + (int)(Math.random() * 300);
		}
		columnX -= 10;
		for(int i = 0; i<movements.length;i++) {
			if(alive[i]) {
				players[i] += movements[i];
				if(movements[i] < 10) 
					movements[i]++;
				playerFitness[i]+=0.1;
			}
		}
		repaint();
		
	}
}
