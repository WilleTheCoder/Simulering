import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

	public static Config config;
	public static FileWriter fw;
	public static Random slump = new Random();

	public static void main(String[] args) throws IOException {

		int m = 200;
		boolean save = false;
		if (save)
			fw = new FileWriter("stra2_lossratex.txt");

		config = Config.get_config();
		config.load_config();
		int nl = config.n_arr_i.length;

		List<Double> list_res = new ArrayList<>();

		boolean overlap = true;

		Double intervals[][] = new Double[m][3];

		// ------------A---------------
		// for (int n : config.n_arr_i) {
		// double loss_rate = run(n, 7, save);
		// loss_rates.add(loss_rate);
		// System.out.println(loss_rate);
		// }
		// ---------------------------

		// ----------B------------
		// while (overlap) {
		// 	int idx = 0;
		// 	for (int n : config.n_arr_i) {
		// 		for (int i = 0; i < m; i++) {
		// 			double res = run(n, 7, save);
		// 			list_res.add(res);
		// 		}
		// 		System.out.println("Running with Network Load: " + n);
		// 		double[] inter = confidenceInterval(list_res);
		// 		intervals[idx][0] = inter[0]; // lower interval
		// 		intervals[idx][1] = inter[1]; // upper interval
		// 		intervals[idx][2] = inter[2]; // mean value
		// 		idx++;
		// 		System.out.println("conf for run " + idx + ": " + inter[0] + " " + inter[1] +
		// 				" " + inter[2]);
		// 	}

		// 	// check if no overlap
		// 	overlap = false;
		// 	for (int i = 0; i < nl; i++) {
		// 		for (int j = i + 1; j < nl; j++) {
		// 			if (intervals[i][1] > intervals[j][0] && intervals[j][1] > intervals[i][0]) {
		// 				overlap = true;
		// 				break;
		// 			}
		// 		}
		// 	}
		// }

		// for (int i = 0; i < config.n_arr_i.length; i++) {
		// 	if (save)
		// 		fw.write(config.n_arr_i[i] + " " + intervals[i][2] + "\n");
		// }
		// ------------------------

		// ------------C---------------
		int idx = 0;
		for (int r : config.r_arr_i) {
			for (int i = 0; i < m; i++) {
				double res = run(2000, r, save);
				list_res.add(res);
			}
			double[] inter = confidenceInterval(list_res);
			intervals[idx][0] = inter[0]; // lower interval
			intervals[idx][1] = inter[1]; // upper interval
			intervals[idx][2] = inter[2]; // mean value
			idx++;
		}

		for (int i = 0; i < config.r_arr_i.length; i++) {
			System.out.println(config.r_arr_i[i] + " " + intervals[i][2]);
			if (save)
				fw.write(config.r_arr_i[i] + " " + intervals[i][2] + "\n");
		}

		// ---------------------------

		if (save)
			fw.close();
	}

	private static double run(int n, int r, boolean save) throws IOException {

		time = 0;
		Gateway gateway = Gateway.getInstance();
		gateway.setInstance();
		gateway.r = r;
		Signal actSignal = null;
		new SignalList();
		List<Point> coordinates = new ArrayList<>();

		Sensor[] sensors = new Sensor[n];

		for (int j = 0; j < sensors.length; j++) {
			sensors[j] = new Sensor();
			// sensors[j].gateway = gateway;
			Point point = null;
			// generate point until the point doesnt already exist
			do {
				point = new Point(10 * slump.nextDouble(), 10 * slump.nextDouble());
			} while (coordinates.contains(point));
			coordinates.add(point);
			sensors[j].point = point;
		}

		gateway.sensors = sensors;
		SignalList.SendSignal(START, gateway, time, null);

		while (time < 1000) {
			actSignal = SignalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
		}

		// System.out.println("Success: " + gateway.no_success);
		// System.out.println("Transmissions: " + gateway.no_transmissions);
		// double x = gateway.no_transmissions - gateway.no_success;
		// System.out.println("Crashes: " + x);

		// ---------------A---------------

		// double lambda = gateway.no_transmissions/time;
		// double throughput= lambda * config.tp * Math.exp(-2 * lambda * config.tp);

		double throughput = (double) gateway.no_success / n;
		if (save)
			fw.write(n + " " + throughput + "\n");
		// ---------------B---------------
		// double loss_rate = (double) (gateway.no_transmissions-gateway.no_success) /
		// gateway.no_transmissions;
		// return throughput;
		// ---------------C---------------
		if (save)
			fw.write(r + " " + throughput + "\n");

		// System.out.println(r + " " + throughput);

		return throughput;
	}

	public static double[] confidenceInterval(List<Double> list) {
		double mean = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
		double standardDeviation = Math
				.sqrt(list.stream().mapToDouble(num -> Math.pow(num - mean, 2)).average().orElse(0.0));
		double confidenceLevel = 1.96;
		double temp = confidenceLevel * standardDeviation / Math.sqrt(list.size());
		return new double[] { mean - temp, mean + temp, mean };
	}

}