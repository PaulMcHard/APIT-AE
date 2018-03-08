import java.io.IOException;
import java.util.ArrayList;

public class GridDrawer extends Thread {
	private Position[][] positions;
	private Car[] cars;

	public GridDrawer(Position[][] positions, Car[] cars) {
		this.positions = positions;
		this.cars = cars;
	}
	
	//Overridden constructor for Spec 2 allowing use of multiple traffic generators.
	public GridDrawer(Position[][] positions, TrafficGenerator[] generators) {
		this.positions = positions;
		ArrayList<Car> carsIn = new ArrayList<Car>();
		for( TrafficGenerator t : generators) {
			for(Car c : t.getCarList()) {
				carsIn.add(c);
			}
		}
		this.cars = new Car[carsIn.size()];
		for(int i=0; i < carsIn.size(); i++ ) {
			cars[i] = carsIn.get(i);
		}
	}

	private String drawGrid() {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < positions[0].length; i++) {
			output.append("===");
		}
		output.append(System.lineSeparator());
		for(int i = 0; i < positions.length; i++) {
			for(int j = 0; j < positions[i].length; j++) {
				for(int k=0; k < cars.length; k++) {
					if(positions[i][j]==cars[k].getPos() && cars[k].getPos().isOccupied) {	
						positions[i][j].isOccupied = true;
						positions[i][j].setAvatar(cars[k]);
					}
					
				}
				if(positions[i][j].isOccupied) {
					output.append("["+positions[i][j].getCarAvatar()+"]");
				}
				else {
					output.append("[ ]");
				}
			}
			output.append(System.lineSeparator());
		}
		for(int i = 0; i < positions[0].length; i++) {
			output.append("===");
		}
		return output.toString();
	}

	public void updateCars(Car[] cars) {
		//Updates the list of cars when more are added to the system.
		this.cars = cars;
	}

	public void run() {
			for (int i = 0; i < 2000; i++) {
				try {
					Thread.sleep(20); // time taken to prepare a course
					System.out.println("\n\n\n\n\n");
					System.out.println(drawGrid());
				}catch(InterruptedException e) {
					e.printStackTrace();
				} 
			}
	}
}
