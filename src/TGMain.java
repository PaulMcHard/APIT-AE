import java.io.*;
import java.util.Random;

public class TGMain {

	public static void main (String[] args) {

		int gridX = 20;
		int gridY = 10;

		Position[][] pos = new Position[gridY][gridX];
		for(int i=0; i< gridY; i++) {
			for(int j=0; j< gridX; j++) {
				pos[i][j]= new Position(j,i);
			}
		}
		int nThreads = 200;

		TrafficGenerator mainGen = new TrafficGenerator(pos, 190, 0);
		mainGen.setTenTwenty();
		TrafficGenerator rowGen = new TrafficGenerator(pos, 10, 1, 3, 0);
		TrafficGenerator[] generators = new TrafficGenerator[] {mainGen, rowGen};

		GridDrawer grid = new GridDrawer(pos, generators);
		Thread gridThread = new Thread(grid);
		gridThread.start();
		//grid.updateCars(mainGen.getCarList());
		for (TrafficGenerator gen : generators) {
			gen.start();
		}
		try {
			for (TrafficGenerator gen : generators) {
				gen.join();
			}
			gridThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Statistics stat = new Statistics(generators);
		String textOut = stat.createReport();
		BufferedWriter output = null;
		try {
			File file = new File("output.txt");
			output = new BufferedWriter(new FileWriter(file));
			output.write(textOut);
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		} 
	}

}
