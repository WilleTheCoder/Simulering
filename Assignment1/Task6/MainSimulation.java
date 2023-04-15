import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
		double totTime = 0;
		int arrivals = 1000;
    	// Some events must be put in the event list at the beginning
        // insertEvent(ARRIVALS, 0);  
        
		// 1000 runs of the system
		for (int i = 0; i < arrivals; i++) {
				insertEvent(ARRIVALS, 0);
				// insertEvent(MEASURE, 5);
	
		        // The main simulation loop
				while (actState.isBreakDown() != true){
					actEvent = eventList.fetchEvent();
					time = actEvent.eventTime;
					actState.treatEvent(actEvent);
				}
	
				//reset it
				GlobalSimulation.eventList = new EventListClass();
				totTime += time;
				GlobalSimulation.time = 0;
				actState = new State();
				actEvent = null;
			}

		System.out.println("--------TASK4--------\n");
			
		System.out.println(totTime/arrivals);

		System.out.println("\n----------END----------");
    }
}