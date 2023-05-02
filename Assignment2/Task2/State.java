import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue = 0, accumulated = 0, noMeasurements = 0, nbrofGoing = 0, numberofArrivals = 0;
	public int lambda = 0;
	public boolean doneForDay = false;
	Random slump = new Random(); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL:
				arrival();
				break;
			case READY:
				ready();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrival(){
		numberofArrivals++;
		if (numberInQueue == 0)
			insertEvent(READY, time + uni(10, 20));
		numberInQueue++;

		if(time < 8*60){
		// System.out.println("time: "+ time);
		insertEvent(ARRIVAL, time + pois(lambda));
		}
	}
	
	private void ready(){
		numberInQueue--;
		nbrofGoing++;
		if (numberInQueue > 0){
			insertEvent(READY, time + uni(10, 20));
		}
	}
	
	private void measure(){
		accumulated = accumulated + numberInQueue;
		if(time > 8*60 && numberInQueue == 0){
			doneForDay = true;
		} 
		noMeasurements++;
		insertEvent(MEASURE, time + 5);

	}

	public double uni(int a, int b){
		return a + slump.nextDouble()*b;
	}

	public double pois(double mean) {
		double u = slump.nextDouble();
		return Math.log(1-u)*-mean;
	}

	public double[] confidenceInterval(ArrayList<Double> givenNumbers) {

		// calculate the mean value (= average)
		double sum = 0.0;
		for (double num : givenNumbers) {
			sum += num;
		}
		double mean = sum / givenNumbers.size();
	
		// calculate standard deviation
		double squaredDifferenceSum = 0.0;
		for (double num : givenNumbers) {
			squaredDifferenceSum += (num - mean) * (num - mean);
		}
		double variance = squaredDifferenceSum / givenNumbers.size();
		double standardDeviation = Math.sqrt(variance);
	
		// value for 95% confidence interval, source: https://en.wikipedia.org/wiki/Confidence_interval#Basic_Steps
		double confidenceLevel = 1.96;
		double temp = confidenceLevel * standardDeviation / Math.sqrt(givenNumbers.size());
		return new double[]{mean - temp, mean + temp};
	}


}