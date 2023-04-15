import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
		double totTime = 0;
    	// Some events must be put in the event list at the beginning
        // insertEvent(ARRIVALS, 0);  
        
		// 1000 runs of the system
		for (int i = 0; i < 1000; i++) {
				insertEvent(ARRIVALS, 0);
				insertEvent(MEASURE, 5);
	
		        // The main simulation loop
				while (actState.breakDownFlag != true){
					actEvent = eventList.fetchEvent();
					time = actEvent.eventTime;
					actState.treatEvent(actEvent);
				}
	
				totTime += GlobalSimulation.time;
				GlobalSimulation.time = 0;
				GlobalSimulation.eventList = new EventListClass();
				actEvent = null;
				actState = new State();
			}

		System.out.println(totTime/1000);
    	
    	// Printing the result of the simulation, in this case a mean value
    	// System.out.println(1.0*actState.accumulated/actState.noMeasurements);
    }
}