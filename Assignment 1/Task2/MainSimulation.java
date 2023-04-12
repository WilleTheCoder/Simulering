package Task2;

import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
		System.out.println("lets go");
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

		// System.out.println(actState.exp(1));

		// 1. Find the mean number of jobs in the buffer for the system above:
    	System.out.println(1.0*actState.accumulated/actState.noMeasurements);
		// 89.1939

		// 2. Let the delay distribution be exponential instead of always having the same value, but let its 
		// mean still be 1 s. What is now the mean number of jobs in the buffer?
		// 3.724

		// 3. Let the distribution be of constant length = 1 s again. Change the priorities so that jobs of 
		// type A have the higher priority. What is now the mean number of jobs in the buffer.
		// 1.494

		

    }
}