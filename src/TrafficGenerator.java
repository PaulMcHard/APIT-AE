import java.util.ArrayList;
import java.util.Random;

public class TrafficGenerator extends Thread {

	private int genType; //dictates which type of traffic generator will be run
	private int genIndex; //indicates which row // column will be affected
	private Position[][] pos; //NxM matrix of positions passed from main
	private Car[] carList; // Variable sized array of Car Objects
	private Thread[] threadList; // Variable sized array of Threads for aforementioned cars
	private Long[] timeList; // Variable sized array of elapsed times for each car's run.

	public TrafficGenerator(Position[][] posIn, int nCars, int type) {
		genType = type;
		pos = posIn;
		carList = new Car[nCars];
		threadList = new Thread[nCars];
		timeList = new Long[nCars];
		generateN(nCars);
	}

	//Override Constructor allowing a row or column index and direction to be specified for a traffic generator.
	public TrafficGenerator(Position[][] posIn, int nCars, int type, int index, int dir) {
		genType = type;
		genIndex = index;
		pos = posIn;
		carList = new Car[nCars];
		threadList = new Thread[nCars];
		timeList = new Long[nCars];
		generateN(nCars, dir);
	}

	//Starts each thread in the list. Written as a test to check operation worked fine.
	public void run() {
		switch (genType) {
		case 0: genStandard();
		break;
		case 1: genOnePath();
		break;
		case 2: genFastLane();
		break;
		case 3: genSlowLane();
		break;
		case 4: genOneCarGap();
		break;
		case 5: genTenTwenty();
		break;
		default: genStandard();
		break;
		}
	}

	public void genStandard() {
		for(int i=0; i < carList.length; i++) {
			threadList[i] = new Thread(carList[i]);
		}
		for(int i=0; i < carList.length; i++) {
			threadList[i].start();
		}
		for(int i=0; i< carList.length; i++) {
			try {
				threadList[i].join();
			} catch (InterruptedException e) {	e.printStackTrace();		}
		}
		for(int i = 0; i < carList.length; i++) {
			timeList[i] = new Long(carList[i].getElapsedTime());
		}	
	}

	public void genOnePath() {
		for( Car c : carList ) {
			if(c.getDirection() == 0 || c.getDirection() == 2 ) {
				if(genIndex > 0 && genIndex < c.getGridHeight()) {
					c.setStartIndex(genIndex);
				}
			}
			else if(c.getDirection() == 1 || c.getDirection() == 3) {
				if(genIndex > 0 && genIndex < c.getGridWidth()) {
					c.setStartIndex(genIndex);
				}
			}
		}
		genStandard();
	}

	public void genFastLane() {
		for( Car c : carList ) {
			if(c.getDirection() == 0 || c.getDirection() == 2 ) {
				if(genIndex > 0 && genIndex < c.getGridHeight()) {
					c.setStartIndex(genIndex);
				}
			}
			else if(c.getDirection() == 1 || c.getDirection() == 3) {
				if(genIndex > 0 && genIndex < c.getGridWidth()) {
					c.setStartIndex(genIndex);
				}
			}
			c.setSpeed(c.getMaxSpeed());
		}
		genStandard();
	}

	public void genSlowLane() {
		for( Car c : carList ) {
			if(c.getDirection() == 0 || c.getDirection() == 2 ) {
				if(genIndex > 0 && genIndex < c.getGridHeight()) {
					c.setStartIndex(genIndex);
				}
			}
			else if(c.getDirection() == 1 || c.getDirection() == 3) {
				if(genIndex > 0 && genIndex < c.getGridWidth()) {
					c.setStartIndex(genIndex);
				}
			}
			c.setSpeed(c.getMinSpeed());
		}
		genStandard();
	}
	
	public void genOneCarGap() {
		genOnePath();
		for(int i=0; i < carList.length; i++) {
			threadList[i] = new Thread(carList[i]);
		}
		for(int i=0; i < carList.length; i++) {
			threadList[i].start();
			try {
				this.sleep(carList[i].getSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int i=0; i< carList.length; i++) {
			try {
				threadList[i].join();
			} catch (InterruptedException e) {	e.printStackTrace();		}
		}
		for(int i = 0; i < carList.length; i++) {
			timeList[i] = new Long(carList[i].getElapsedTime());
		}	
	}
	
	public void genTenTwenty() {
		for(int i = 0; i < carList.length; i++) {
			Car car = carList[i];
			int dirIn = car.getDirection();
			int indexIn = car.getStartIndex();
			int dirOut;
			if (dirIn == 0 || dirIn == 2) {
				if(indexIn < 5) {//set top 5 rows to left only for generated horizontal cars.
					dirOut = 0;
				}
				else {
					dirOut = 2;
				}
			}
			else if (dirIn == 1 || dirIn == 3) {
				if(indexIn < 10) {//set first 10 columns to be down only for generated vertical cars.
					dirOut = 1;
				}
				else {
					dirOut = 3;
				}
			}
			else {
				dirOut = dirIn; //Shouldn't reach this case, but have it as a failsafe.
			}
			car.setDirection(dirOut);
		}
		genStandard();
	}


	//Starts the most recent addition to the array. Facilitates operation as per Spec1.
	public void startLast() {
		threadList[threadList.length-1].start();
	}

	public void generateN(int n) { 
		//basic version of the generateN method which generates n cars with random direction as per spec 1
		for(int i= 0; i < n; i++) {
			Random random = new Random();
			int direction = random.nextInt(4);
			Car car = new Car(pos, direction);
			carList[i] = car;
		}
	}

	public void generateN(int n, int dir) { 
		//overridden method which allows direction to be dictated.
		for(int i= 0; i < n; i++) {
			int direction = dir;
			Car car = new Car(pos, direction);
			carList[i] = car;
		}
	}

	public void generateN(int n, int dir, int speed) { 
		//overridden method which allows direction and speed to be dictated.
		for(int i= 0; i < n; i++) {
			int direction = dir;
			Car car = new Car(pos, direction);
			car.setSpeed(speed);
			carList[i] = car;
		}
	}


	//Accessor Methods
	public Car[] getCarList() { return carList;	}
	public Long[] getTimeList() { return timeList;	}

	/**Implementing the specific case of the "Ten Twenty Example" (bullet point 2 in spec level 2) as a static method.
	 * This is done to allow it to be called in the main without implementing a traffic generator object.
	 * This method is not necessarily a function of traffic generator but it was cohesive to have it in the traffic generator class.
	 * 
	 * @param car
	 * @return int
	 */
	public static int setTenTwenty(Car car) {
		int dirIn = car.getDirection();
		int indexIn = car.getStartIndex();
		int dirOut;
		if (dirIn == 0 || dirIn == 2) {
			if(indexIn < 5) {//set top 5 rows to left only for generated horizontal cars.
				dirOut = 0;
			}
			else {
				dirOut = 2;
			}
		}
		else if (dirIn == 1 || dirIn == 3) {
			if(indexIn < 10) {//set first 10 columns to be down only for generated vertical cars.
				dirOut = 1;
			}
			else {
				dirOut = 3;
			}
		}
		else {
			dirOut = dirIn; //Shouldn't reach this case, but have it as a failsafe.
		}
		return dirOut;
	}

}
