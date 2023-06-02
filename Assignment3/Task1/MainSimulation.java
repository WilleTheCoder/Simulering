import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

	public static Config config;
	public static FileWriter fw;
	public static Random slump = new Random();
	public static Gateway gateway = null;

	public static void main(String[] args) throws IOException {

		int m = 200;
		boolean save = true;
		if (save)
			fw = new FileWriter("res.txt");

		config = Config.get_config();
		config.load_config();
		int nl = config.n_arr_i.length;

		Double intervals[][] = new Double[m][3];

		// ------------A---------------
		// for (int n : config.n_arr_i) {
		// double loss_rate = run(9000, 7, save);
		// System.out.println(loss_rate);
		// }
		// ---------------------------

		// ----------B------------
		// boolean overlap = true;
		// while (overlap) {
		// int idx = 0;
		// for (int n : config.n_arr_i) {
		// List<Double> list_res = new ArrayList<>();
		// for (int i = 0; i < 100; i++) {
		// double res = run(n, 7, save);
		// list_res.add(res);
		// }
		// System.out.println("Running with Network Load: " + n);
		// double[] inter = confidenceInterval(list_res);
		// intervals[idx][0] = inter[0]; // lower interval
		// intervals[idx][1] = inter[1]; // upper interval
		// intervals[idx][2] = inter[2]; // mean value
		// idx++;
		// System.out.println("conf for run " + idx + ": " + inter[0] + " " + inter[1] +
		// " " + inter[2]);
		// }

		// overlap = false;
		// }

		// for (int i = 0; i < config.n_arr_i.length; i++) {
		// if (save)
		// fw.write(config.n_arr_i[i] + " " + intervals[i][2] + "\n");
		// }
		// ------------------------

		// ------------C---------------
		int idx = 0;
		for (int r : config.r_arr_i) {
			List<Double> list_res = new ArrayList<>();
			for (int i = 0; i < 30; i++) {
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
			if (save)
				fw.write(config.r_arr_i[i] + " " + intervals[i][2] + "\n");
		}

		// int ra = 20;
		// double res = run(2000, ra, save);
		// System.out.println("throughput: " + res + " - radius: " + ra);
		

		// ---------------------------

		if (save)
			fw.close();
	}

	private static double run(int n, int r, boolean save) throws IOException {
		gateway = new Gateway();
		time = 0;
		gateway.r = r;
		// System.out.println(gateway.r);
		Signal actSignal = null;
		new SignalList();
		List<Point> coordinates = new ArrayList<>();

		Sensor[] sensors = new Sensor[n];

		for (int j = 0; j < sensors.length; j++) {
			sensors[j] = new Sensor();
			Point point = null;
			// generate point until the point doesnt already exist
			do {
				point = new Point(10 * slump.nextDouble(), 10 * slump.nextDouble());
			} while (coordinates.contains(point));
			coordinates.add(point);
			sensors[j].point = point;
			sensors[j].gateway = gateway;
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
		// System.out.println("act crashes: " + gateway.no_crashes);
		// double x = gateway.no_transmissions - gateway.no_success;
		// System.out.println("Crashes: " + x);

		// ---------------A---------------

		// double lambda = gateway.no_transmissions / time;
		// double throughput2 = lambda * config.tp * Math.exp(-2 * lambda * config.tp);
		double throughput = (double) gateway.no_success / n;
		// if (save)
		// fw.write(n + " " + throughput + "\n");
		// ---------------B---------------
		// double loss_rate = (double) (gateway.no_crashes) / gateway.no_transmissions;
		return throughput;
		// ---------------C---------------
		// if (save)
		// fw.write(r + " " + throughput + "\n");

		// System.out.println(r + " " + throughput);
		// System.out.println("th: " + throughput);

		// return throughput;
	}

	public static double[] confidenceInterval(List<Double> values) {
		int n = values.size();
		double[] result = new double[3];

		// Calculate mean
		double sum = 0;
		for (double value : values) {
			sum += value;
		}
		double mean = sum / n;
		result[0] = mean; // Mean value

		// Calculate standard deviation
		double squaredSum = 0;
		for (double value : values) {
			squaredSum += Math.pow(value - mean, 2);
		}
		double standardDeviation = Math.sqrt(squaredSum / (n - 1));

		// Calculate confidence interval (using z-value for 95% confidence level)
		double z = 1.96; // Z-value for 95% confidence level
		double marginOfError = z * (standardDeviation / Math.sqrt(n));
		result[1] = mean - marginOfError; // Lower confidence interval
		result[2] = mean + marginOfError; // Upper confidence interval

		return result;
	}
}
