import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue = 0, accumulated = 0, noMeasurements = 0, nbrofGoing = 0, numberofArrivals = 0;
	public int lambda = 0;
	public boolean doneForDay = false;
	public ArrayList<Double> presc_arrival_times = new ArrayList<>();
	public ArrayList<Double> presc_times = new ArrayList<>();


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
		presc_arrival_times.add(time);
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
		presc_times.add(time - presc_arrival_times.remove(presc_arrival_times.size()-1));
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

	public double uni(double a, double b) {
		return a + slump.nextDouble() * (b - a);
	}
	
	

	public double pois(double mean) {
		double u = slump.nextDouble();
		return Math.log(1-u)*-mean;
	}


}