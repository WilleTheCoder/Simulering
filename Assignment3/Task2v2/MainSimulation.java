import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.awt.Point;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		// Some events must be put in the event list at the beginning
		Random rand = new Random();
		List<Point> coordinates = new ArrayList<>();
		Minion[] minions = new Minion[n_minions];
		FileWriter fw = new FileWriter("res.txt");

		ArrayList<Double> timeList = new ArrayList<>();
		double ci_len = 0;
		int x = 0;
		while (!(ci_len > 0.2 && ci_len < 0.3)) {
			x++;
			GlobalSimulation.time = 0;
			GlobalSimulation.eventList = new EventListClass();
			Event actEvent;
			State actState = new State(); // The state that should be used

			// Create Hall
			hall = new Box[grid_size][grid_size];
			for (int i = 0; i < grid_size; i++) {
				for (int j = 0; j < grid_size; j++) {
					hall[i][j] = new Box();
				}
			}

			// Init minions
			for (int j = 0; j < n_minions; j++) {
				minions[j] = new Minion();
				minions[j].i = j;
				Point point = null;
				do {
					point = new Point(rand.nextInt(grid_size), rand.nextInt(grid_size));
				} while (coordinates.contains(point));
				minions[j].point = point;
				coordinates.add(point);
				Box b = hall[minions[j].point.x][minions[j].point.y];
				b.add(minions[j]);
			}

			actState.minions = minions;

			insertEvent(MOVE, 0);

			// The main simulation loop
			while (!actState.isDone()) {
				// System.out.println("ppl met: " + minions[0].minion_set.size());
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}

			double t = time / (60 * 60); // in hours
			timeList.add(t);
			double[] ci = confidenceInterval(timeList);
			ci_len = ci[1] - ci[0];
			// System.out.println(ci_len);
		}
		System.out.println(x);

		double[] ci = confidenceInterval(timeList);
		ci_len = ci[1] - ci[0];

		System.out.println("confidence interval:" + ci[0] + " : " + ci[1]);
		System.out.println("interval length: " + ci_len);

		// get a minion
		Collection<Double> times = minions[0].minion_map.values();
		ArrayList<Double> times_sorted = new ArrayList<>(times);
		Collections.sort(times_sorted);
		System.out.println();

		// print out time spent with other minions in order of time
		// for (Double m_time : times_sorted) {
		// 	System.out.println(m_time);
		// 	// fw.write(m_time/60 + " " + share_hold + "\n");
		// }

		Map<Double, Integer> h = new TreeMap<>();
		for (int i = 0; i < times_sorted.size(); i++) {
			if (h.containsKey(times_sorted.get(i))) {
				h.put(times_sorted.get(i), h.get(times_sorted.get(i)) + 1);
			} else {
				h.put(times_sorted.get(i), 1);
			}
		}

		for (Entry<Double, Integer> entry: h.entrySet()) {
			fw.write(entry.getKey() + " " + entry.getValue() + "\n");
		}

		fw.close();
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