
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
    	while (time < 5000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}

    	// Printing the result of the simulation, in this case a mean value

		System.out.println("--------TASK1--------\n");

		System.out.println(actState.noMeasurements);

		System.out.println("Mean number of customers in Q2: " + (1.0*actState.totalNumberInQueue2)/actState.noMeasurements);

		System.out.println("Probability that a customer is rejected: " + 1.0*actState.noRejected/actState.arrivals);

		System.out.println("\n----------END----------");
    }
}