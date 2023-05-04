
import java.util.*;
import java.io.*;

public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL1, 0);  
        insertEvent(MEASURE, 5);

        // The main simulation loop
    	while (time < 50000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}

    	// Printing the result of the simulation, in this case a mean value
		System.out.println("--------TASK3--------\n");
		System.out.println(actState.noMeasurements);
    	System.out.println("Observed mean number of customers: " + 1.0*actState.accumulated/actState.noMeasurements);
    	System.out.println("Calculated mean number of customers: " + 2/(actState.a-1));

    	System.out.println("Observed mean time a customers spends: " + 1.0*actState.timeInTotal/actState.numberOfGoing);
    	System.out.println("Calculated mean time a customers spends: " + (2*actState.a)/(actState.a-1));
		
		System.out.println("\n----------END----------");
    }
}