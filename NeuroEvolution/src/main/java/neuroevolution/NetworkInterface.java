package neuroevolution;

public interface NetworkInterface {
	public void getPlayerInputs(int[] players ,double obstacleX, double obstacleY);
	public void allDead(int[] players, double fitness[]);
}
