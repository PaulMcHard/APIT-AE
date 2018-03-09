import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class APSpec2 {

	public static void main (String[] args) {

		int gridX = 20;
		int gridY = 10;

		Position[][] pos = new Position[gridY][gridX];
		for(int i=0; i< gridY; i++) {
			for(int j=0; j< gridX; j++) {
				pos[i][j]= new Position(j,i);
			}
		}
		
		//TrafficGenerator(Position[][] posIn, int nCars, int type, int index, int dir)
		TrafficGenerator mainGen = new TrafficGenerator(pos, 190, 5);
		
		TrafficGenerator fastLane = new TrafficGenerator(pos, 40, 2, 6, 1);
		TrafficGenerator slowLane = new TrafficGenerator(pos, 10, 3, 7, 1 );
		TrafficGenerator oneCarGap = new TrafficGenerator(pos, 45, 4, 5, 1 );
		TrafficGenerator[] generators = new TrafficGenerator[] {mainGen, fastLane, slowLane, oneCarGap};

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
