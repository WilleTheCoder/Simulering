import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
		//set vars
		actState.N = 100;
		actState.M = 4000;
		actState.T = 4;
		actState.lambda = 4;
		actState.x = 10; 
		List<Boolean> ready_states = new ArrayList<>(Collections.nCopies(actState.N, false));
		actState.ready_states = ready_states;

		ArrayList<Integer>NoiSList = new ArrayList<>();
		actState.NoiSList = NoiSList;
		FileWriter fw = new FileWriter("res1d.txt");
		actState.fw = fw;

    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0, 0);  
        insertEvent(MEASURE, 5, 0);
        
		// System.out.println(ready_states.get(0));

        // The main simulation loop
    	while (actState.noMeasurements < actState.M){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
		fw.close();
    	
		double[] ci = actState.confidenceInterval(NoiSList);
		System.out.println("confidence interval: " + ci[0] + " : " + ci[1]);

    	// Printing the result of the simulation, in this case a mean value
    	System.out.println(1.0*actState.accumulated/actState.noMeasurements);
    }
}
