import java.util.concurrent.locks.*;

public class Position {

	private int x;
	private int y;
	public boolean isOccupied = false;
	private ReentrantLock positionLock = new ReentrantLock();
	private Condition lockCondition = positionLock.newCondition();
	private String carAvatar;

	//constructor just establishes location of Position object within array.
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
		
	   public void passCar() { //car leaving position
	        positionLock.lock();
	        try {
	            isOccupied = false;
	            lockCondition.signal();
	        } finally {
	            positionLock.unlock();
	        }
	    }

	    public void receiveCar() { //car arriving at position
	        positionLock.lock();
	    try {
	        while (isOccupied == true) {
	            try {
	                lockCondition.await();
	            } catch (InterruptedException e) { }
	        }
	        isOccupied = true;
	        } finally {
	            positionLock.unlock();
	        }
	    }

	public void setAvatar(Car car) {
		int carDir = car.getDirection();
		switch (carDir) {
		case 0: carAvatar = ">";
			break;
		case 1: carAvatar = "v";
			break;
		case 2: carAvatar = "<";
			break;
		case 3: carAvatar = "^";
			break;
		default: carAvatar = " ";
			break;
		}
	}

	//accessor methods
	public int getX() {return x;}
	public int getY() {return y;}
	public String getCarAvatar() {return carAvatar;}

}
