import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL1, 0);  
        insertEvent(MEASURE, 5);
        
		actState.arrivalTime = 1; //1, 2, 5

        // The main simulation loop
    	while (time < 5000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}

    	// Printing the result of the simulation, in this case a mean value
		System.out.println("no of measurments: " + actState.noMeasurements);
		System.out.println("no of arrivals: " + actState.arrivals);
    	System.out.println("mean number q2 " + 1.0*actState.accumulated/actState.noMeasurements);
    	System.out.println("norejected: " +actState.noRejected);
    	System.out.println("PROB: " + 1.0*actState.noRejected/actState.arrivals);
		System.out.println("time is: " + actState.time);
		System.out.println("queue 1: " + actState.numberInQueue1);
		System.out.println("queue 2: " + actState.numberInQueue2);
		System.out.println("accumulated: "+actState.accumulated);
		
    }
}