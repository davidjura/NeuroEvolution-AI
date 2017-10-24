package neuroevolution;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
    		final GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
    		
    		JFrame frame;
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		final GamePanel panel = new GamePanel();
		NetworkInterface netInterface = new NetworkInterface(){
			public void getPlayerInputs(int[] players, double obstacleX, double obstacleY) {
				for(int i = 0; i< players.length;i++) {
					if(geneticAlgorithm.shouldJump(new double[] {(double)players[i],obstacleX,obstacleY},i))
						panel.playerJump(i);
				}
			}

			public void allDead(int[] players, double fitness[]) {
				geneticAlgorithm.evolvePopulation(fitness);
				panel.resetGame();
			}

		};
		panel.setInterface(netInterface);
		panel.setBounds(0, 0, 800, 500);
		frame.getContentPane().add(panel);
		frame.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				
			}

			public void keyPressed(KeyEvent e) {
				panel.switchSpeed();
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
    }
}
