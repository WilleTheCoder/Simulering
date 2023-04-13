
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL_A, 0);  
        insertEvent(MEASURE, 0.1);

        // The main simulation loop
    	while (time < 1000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}

		System.out.println("--------TASK2--------\n");

		// 1. Find the mean number of jobs in the buffer for the system above:
    	System.out.println("Mean number of customers in the buffer: " +1.0*actState.totalCustomerInQueues/actState.noMeasurements);

		// 2. Let the delay distribution be exponential instead of always having the same value, but let its 
		// mean still be 1 s. What is now the mean number of jobs in the buffer?

		// 3. Let the distribution be of constant length = 1 s again. Change the priorities so that jobs of 
		// type A have the higher priority. What is now the mean number of jobs in the buffer.

		System.out.println("\n----------END----------");



		

    }
}