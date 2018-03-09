import java.util.Random;

public class APSpec1 {
	
	public static void main(String[] args) {
		int gridX = 20;
		int gridY = 20;
		Position[][] pos = new Position[gridY][gridX];
		for(int i=0; i< gridY; i++) {
			for(int j=0; j< gridX; j++) {
				pos[i][j]= new Position(j,i);
			}
		}
		int nThreads = 50;
		Thread[] carThreads = new Thread[nThreads];
		Car[] cars = new Car[nThreads];
		GridDrawer grid = new GridDrawer(pos, cars);
		Thread gridThread = new Thread(grid);
		gridThread.start();
		for(int i = 0; i < nThreads; i++) {
			cars[i]= new Car();
			Random random = new Random();
			int direction = random.nextInt(2);
			Car car = new Car(pos, direction);
			carThreads[i] = new Thread(car);
			carThreads[i].start();
			cars[i] = car;
			grid.updateCars(cars);
		}
	}
}
