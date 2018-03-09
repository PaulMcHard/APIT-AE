import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Car extends Thread {
	
	private long startTime;
	private int maxSpeed = 100;
	private int minSpeed = 1000;
	private long elapsedTime;
	private Position[][] grid;
	private int startIndex;
	private Position currentPos;
	private int speed;
	private int direction;
	private int gridHeight;
	private int gridWidth;
	public boolean exists;

	public Car(Position[][] gridIn, int direction) {
		this.grid = gridIn;
		this.gridHeight= grid.length;
		this.gridWidth = grid[0].length;
		this.direction = direction;
		initStartIndex(); 
		currentPos = setStart();
		currentPos.receiveCar();
		speed = ThreadLocalRandom.current().nextInt(maxSpeed, minSpeed);// (speed is between 1000 and 5000)
		this.exists = true;
	}

	public Car() {
		//empty constructor to fill Car[] in main with cars that don't yet exist.
		this.exists = false;
	}

	public void initStartIndex() {
		Random random = new Random();
		if (this.direction == 0 || this.direction ==2 ) {//for a car moving horizontally
			this.startIndex = random.nextInt(gridHeight); // start index bound by the number of rows
		}
		else { //for a car moving vertically
			this.startIndex = random.nextInt(gridWidth); // start index bound by number of columns
		}
	}

	private Position setStart() {
		Position startPos;
		switch (direction) {
		case 0: startPos = new Position(0,startIndex); // start on leftmost column and given row
		break;
		case 1: startPos = new Position(startIndex, 0);// start in given column and topmost row.
		break;
		case 2: startPos = new Position(gridWidth,startIndex); // start on leftmost column and given row
		break;
		case 3: startPos = new Position(startIndex, gridHeight);// start in given column and topmost row.
		break;
		default: startPos = new Position(0,0); //included so program doesn't fall over.
		break;
		}
		return startPos;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		Position newPos;
		try {
			//EastWest movement
			if (direction == 0 || direction == 2) {
				for( int i = 0; i < gridWidth; i++ ) {//spans horizontal length 
					if (direction == 0) {
						newPos = grid[startIndex][i];
					}
					else {
						newPos = grid[startIndex][gridWidth-(i+1)];
					}
					Thread.sleep(speed);//waits in its current spot for speed
					newPos.receiveCar();
					currentPos.passCar();
					currentPos = newPos;
				}
			}
			else {
				for( int i = 0; i < gridHeight; i++) {//spans vertical length
					if(direction == 1) {
						newPos = grid[i][startIndex];
					}
					else {
						newPos = grid[gridHeight-(i+1)][startIndex];
					}
					Thread.sleep(speed);//waits in its current spot for speed
					newPos.receiveCar();
					currentPos.passCar();
					currentPos = newPos;
				}
			}
			Thread.sleep(speed);
			currentPos.passCar();
			this.exists = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		elapsedTime = System.currentTimeMillis() - startTime;
	}
	
	//Accessor Methods
	public Position getPos() {	return currentPos;	}
	public int getDirection() {	return direction;	}
	public int getStartIndex() {	return startIndex;	}
	public long getElapsedTime() {	return elapsedTime;	}
	public int getMaxSpeed() 	{	return maxSpeed;	}
	public int getMinSpeed()	{	return minSpeed;	}
	public int getSpeed()		{	return speed;		}
	public int getGridHeight()	{	return gridHeight;	}
	public int getGridWidth()	{	return gridWidth;	}

	//Mutator Methods
	public void setStartIndex(int index) {
		this.startIndex = index;
	}
	public void setDirection(int dir) {
		this.direction = dir;
	}
	public void setSpeed(int spd) {
		this.speed = spd;
	}
}
