import java.util.*;
import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		int N = 1000; //number of workdays
		// Event actEvent;
		// State actState = new State(); // The state that shoud be used
		// actState.lambda = 15;

		ArrayList<Double> timeList = new ArrayList<>();
		ArrayList<Double> presc_times = new ArrayList<>();

		State actState = null;
		// The main simulation loop
		double meanTime = 0;
		int noOfDays = 0;
		double prevTime = 0;
		
		while(noOfDays < N){
			// Some events must be put in the event list at the beginning
			insertEvent(ARRIVAL, 0);
			insertEvent(MEASURE, 5);
			Event actEvent;
			actState = new State();
			actState.lambda = 15;

			while (actState.doneForDay == false) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}
			noOfDays++;
			timeList.add(time);
			meanTime += time;
			presc_times.addAll(actState.presc_times);
		}

		double[] ci = confidenceInterval(timeList);
		System.out.println("confidence interval:" + ci[0] / 60 + " : " + ci[1] / 60);

		System.out.println("meantime work: " + (meanTime/N) / 60); // in hours

		double[] ci2 = confidenceInterval(presc_times);
		System.out.println("confidence interval:" + ci2[0] + " : " + ci2[1]);
	}

	public static double[] confidenceInterval(ArrayList<Double> list) {
		double mean = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
		double standardDeviation = Math.sqrt(list.stream().mapToDouble(num -> Math.pow(num - mean, 2)).average().orElse(0.0));
		double confidenceLevel = 1.96;
		double temp = confidenceLevel * standardDeviation / Math.sqrt(list.size());
		return new double[]{mean - temp, mean + temp};
	}
	

}