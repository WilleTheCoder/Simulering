import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue = 0, accumulated = 0, noMeasurements = 0, arrivalTime = 0;

	Random slump = new Random(); // This is just a random number generator
	
	
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
				// arrival2();
				break;
			case READY2:
				// ready2();
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
		if(numberInQueue < 10) {
			if (numberInQueue == 0)
				insertEvent(READY1, time + Math.exp(2.1));
			numberInQueue++;
			insertEvent(ARRIVAL1, time + arrivalTime);
		}
	}
	
	private void ready1(){
		numberInQueue--;
		if (numberInQueue > 0)
			insertEvent(READY1, time + 2*slump.nextDouble());
	}
	
	// private void arrival2(){
	// 	if(numberInQueue < 10) {
	// 		if (numberInQueue == 0)
	// 			insertEvent(READY1, time + Math.exp(2.1));
	// 		numberInQueue++;
	// 		insertEvent(ARRIVAL1, time + arrivalTime);
	// 	}
	// }
	
	// private void ready2(){
	// 	numberInQueue--;
	// 	if (numberInQueue > 0)
	// 		insertEvent(READY, time + 2*slump.nextDouble());
	// }
	
	private void measure(){
		accumulated = accumulated + numberInQueue;
		noMeasurements++;
		insertEvent(MEASURE, time + slump.nextDouble()*10);
	}
}