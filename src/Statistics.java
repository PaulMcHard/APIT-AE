import java.util.ArrayList;

public class Statistics {
	
	private ArrayList<TrafficGenerator> trafficList;
	
	public Statistics(TrafficGenerator[] genIn) {
		trafficList = new ArrayList<TrafficGenerator>();
		for(TrafficGenerator i : genIn) {
			trafficList.add(i);
		}
	}
	
	public void addGenerator(TrafficGenerator tgIn) {
		trafficList.add(tgIn);
	}
	
	public String createReport() {
		String output= "\n";
		for(TrafficGenerator i : trafficList) {
			output += "Stat output for Traffic Generator: #"+(trafficList.indexOf(i)+1)+".\n";
			output += "Generator was responsible for "+i.getTimeList().length+" cars.\n";
			output += "The Maximum time taken was: "+getMax(i)+" seconds.\n";
			output += "The Minimum time taken was: "+getMin(i)+" seconds.\n";
			output += "The Mean time was: "+getMean(i)+" seconds.\n";
			output += "The Variance was: "+getVariance(i)+"\n";
			output += "And Standard Deviation was: "+getStdDev(i)+"\n\n";
			}
		return output;
	}
	
	private long getMax(TrafficGenerator thisGenerator) {
		Long[] timeList = thisGenerator.getTimeList();
		long max = timeList[0];
		for(int i = 1; i < timeList.length; i++) {
			if(timeList[i] > max) {
				max = timeList[i];
			}
		}
		return max/=1000;
	}
	
	private long getMin(TrafficGenerator thisGenerator) {
		Long[] timeList = thisGenerator.getTimeList();
		long min = timeList[0];
		for(int i = 1; i < timeList.length; i++) {
			if(timeList[i] < min) {
				min = timeList[i];
			}
		}
		return min/=1000;
	}
	
	private long getMean(TrafficGenerator thisGenerator) {
		Long[] timeList = thisGenerator.getTimeList();
		long sum = 0;
		long mean;
		for( long  i : timeList ) {
			sum+=(i/1000);
		}
		mean = sum/timeList.length;
		return mean;
	}
	
	private long getVariance(TrafficGenerator thisGenerator) {
		Long[] timeList = thisGenerator.getTimeList();
		long mean = getMean(thisGenerator);
		long variance = 0;
		for (long i : timeList ) {
			long innerCalc = ((i/1000)-mean);
			variance += innerCalc*innerCalc;
		}
		variance /= timeList.length;
		return variance;
	}
	
	private double getStdDev(TrafficGenerator thisGenerator) {
		Long[] timeList = thisGenerator.getTimeList();
		long var = getVariance(thisGenerator);
		double stdDev = Math.sqrt((double)var);
		return stdDev;
	}

}
