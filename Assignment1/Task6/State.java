import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int noMeasurements = 0;
	public boolean breakDownFlag = false;

	public int components[] = {1,2,3,4,5};
	public boolean compBroken[] = {false, false, false, false, false};


	Random slump = new Random(); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVALS: 
				for(int comp : components){
					insertEvent(comp, time + 1 + 4.0*slump.nextDouble());
				}
			break;
			case breakC1:
				compBroken[0] = true;
				insertEvent(breakC2, time);
				insertEvent(breakC5, time);

			break;
			case breakC2:
				compBroken[1] = true;
			break;
			case breakC3:
				compBroken[2] = true;
				insertEvent(breakC4, time);
			break;
			case breakC4:
				compBroken[3] = true;
			break;
			case breakC5:
				compBroken[4] = true;
			break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.

	public boolean isBreakDown(){
		for (boolean status : compBroken) {
			if(!status){
				return false;
			}
		}
		return true;
	}
}