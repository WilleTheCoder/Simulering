import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
		actState.lambda = 15;
		int N = 1000;

		ArrayList<Double> timeList = new ArrayList<>();

    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, 5);

        // The main simulation loop
		double meanTime = 0;
		for (int i = 0; i < N; i++) {
			
			while (actState.doneForDay == false){
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}
			timeList.add(time);
			meanTime += time;
		}

		double[] ci = actState.confidenceInterval(timeList);
		System.out.println("confidence interval:" +  ci[0]/60 + " : " + ci[1]/60);


		System.out.println("arrivals" + actState.numberofArrivals);
		System.out.println("going: "+actState.nbrofGoing);


		System.out.println("meantime work: " + (meanTime/1000)/60); //in hours
 
    }
}