
import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue1 = 0, numberInQueue2 = 0, totalNumberInQueue2 = 0, noMeasurements = 0, noRejected = 0, arrivals = 0;

	Random slump = new Random(); // This is just a random number generator
	public double a = 5, x1 = 2.1, x2 = 2;
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL1:
				arrival1();
				break;
			case READY1:
				ready1();
				break;
			case ARRIVAL2:
				arrival2();
				break;
			case READY2:
				ready2();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrival1(){
		if(numberInQueue1 < 10) {
			if (numberInQueue1 == 0){
				insertEvent(READY1, time + exp(x1));
			}
			numberInQueue1++;
				
		} else {
			this.noRejected++;
		}
		arrivals++;
		insertEvent(ARRIVAL1, time + a);
	}
	
	private void ready1(){
		numberInQueue1--;
		if (numberInQueue1 > 0){
			insertEvent(READY1, time + exp(x1));
		}
		insertEvent(ARRIVAL2, time);
	}
	
	private void arrival2(){
		if (numberInQueue2 == 0)
			insertEvent(READY2, time + x2);
			
		numberInQueue2++;
	}

	private void ready2(){
		numberInQueue2--;
		if (numberInQueue2 > 0){
			insertEvent(READY2, time + x2);
		}
	}
	
	private void measure(){
		totalNumberInQueue2 = totalNumberInQueue2 + numberInQueue2;
		noMeasurements++;
		insertEvent(MEASURE, time + exp(5));
	}

	public double exp(double time){
		double lambda = 1/time;
		return Math.log(1-slump.nextDouble())/(-lambda);
	}

}