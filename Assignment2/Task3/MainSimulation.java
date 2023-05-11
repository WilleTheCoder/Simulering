import java.util.*;
import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		ArrayList<Double> timeList = new ArrayList<>();
		double ci_len = 0;
		while (!(ci_len < 2.0 && ci_len > 1.8)) {
			GlobalSimulation.time = 0;
            GlobalSimulation.eventList = new EventListClass();
			FileWriter fw = new FileWriter("res.txt");
			Event actEvent;
		 	actState = new State(); // The state that shoud be used
			actState.fw = fw;
			actState.moneyGoal = 2000000;
			actState.monthly_investment = 5000;
			actState.monthly_growth = 1 + actState.from_yearly_to_montly_rate(0.3);

			// Some events must be put in the event list at the beginning
			insertEvent(INVESTMENT, 0);
			insertEvent(DISTURBANCE, actState.slump.nextInt(96));
			// The main simulation loop
			while (!actState.isRich()) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
				// System.out.println(actState.share_hold);
			}
			double[] ci = confidenceInterval(timeList);
			ci_len = ci[1] - ci[0];
			timeList.add(time);
			fw.close();
		}
		
		// Printing the result of the simulation
		
		double[] ci = confidenceInterval(timeList);
		ci_len = ci[1] - ci[0];

		System.out.println("confidence interval:" + ci[0] + " : " + ci[1]);
		System.out.println("interval length: " + ci_len);
		System.out.println(time);
		System.out.println("disturb counter: " + actState.distCount);
	}

	public static double[] confidenceInterval(ArrayList<Double> list) {
		double mean = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
		double standardDeviation = Math
				.sqrt(list.stream().mapToDouble(num -> Math.pow(num - mean, 2)).average().orElse(0.0));
		double confidenceLevel = 1.96;
		double temp = confidenceLevel * standardDeviation / Math.sqrt(list.size());
		return new double[] { mean - temp, mean + temp };
	}
}