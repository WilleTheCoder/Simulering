import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int accumulated = 0, noMeasurements = 0, numberInSystem = 0;
	public List<Integer> NoiSList = null;

	public int N = 0, T= 0, M=0, lambda = 0, x = 0;

	public List<Boolean> ready_states = null;
	public FileWriter fw = null;

	// Let 𝑁 = 1000, 𝑥 = 100, 𝜆 = 8, 𝑇 = 1 and 𝑀 = 1000

	Random slump = new Random(); // This is just a random number generator
	
	

	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL:
				arrival();
				break;
			case READY:
				ready(x.i);
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
		numberInSystem++;
		int flag = 0;
		for (int i = 0; i < ready_states.size(); i++) {

			if(ready_states.get(i) == false){
				ready_states.set(i, true);
				insertEvent(READY, time + x, i);
				flag = 1;
				break;
				}
		} 

		if(flag == 0){
			numberInSystem--;
		}
			
		insertEvent(ARRIVAL, time + pois(lambda), 0);
		}

	private void ready(int server){
		ready_states.set(server, false);
		numberInSystem--;
	}
	
	private void measure(){
		accumulated = accumulated + numberInSystem;
		noMeasurements++;

		NoiSList.add(numberInSystem);

		try {
			fw.write(noMeasurements + " " + numberInSystem + "\n");
		} catch (IOException e) {
			System.out.println("error");
		}


		insertEvent(MEASURE, time + T, 0);
	}


	public double pois(double mean) {
		double u = slump.nextDouble();
		return -Math.log(1-u)/mean;
	}

	public double[] confidenceInterval(ArrayList<Integer> givenNumbers) {

		// calculate the mean value (= average)
		double sum = 0.0;
		for (int num : givenNumbers) {
			sum += num;
		}
		double mean = sum / givenNumbers.size();
	
		// calculate standard deviation
		double squaredDifferenceSum = 0.0;
		for (int num : givenNumbers) {
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