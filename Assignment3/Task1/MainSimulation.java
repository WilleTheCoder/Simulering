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

		double[] result;
		config = Config.get_config();
		config.load_config();

		result = run(2000, 11000);
		System.out.println("Throughput: " + result[0]);
		System.out.println("Loss rate: " + result[1]);

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
		// int idx = 0;
		// for (int r : config.r_arr_i) {
		// List<Double> list_res = new ArrayList<>();
		// for (int i = 0; i < 30; i++) {
		// double res = run(2000, r, save);
		// list_res.add(res);
		// }
		// double[] inter = confidenceInterval(list_res);
		// intervals[idx][0] = inter[0]; // lower interval
		// intervals[idx][1] = inter[1]; // upper interval
		// intervals[idx][2] = inter[2]; // mean value
		// idx++;
		// }

		// for (int i = 0; i < config.r_arr_i.length; i++) {
		// if (save)
		// fw.write(config.r_arr_i[i] + " " + intervals[i][2] + "\n");
		// }

		// int ra = 20;
		// double res = run(2000, ra, save);
		// System.out.println("throughput: " + res + " - radius: " + ra);

		// ---------------------------
	}

	private static double[] run(int n, int r) throws IOException {
		System.out.println("N: "+n);
		gateway = new Gateway();
		Global.time = 0;
		Global.r = r;
		Signal actSignal = null;
		new SignalList();

		List<Point> coordinates = new ArrayList<>();

		Sensor[] sensors = new Sensor[n];

		for (int j = 0; j < sensors.length; j++) {
			sensors[j] = new Sensor();
			Point point = null;
			// generate point until the point doesnt already exist
			do {
				point = new Point(10000 * slump.nextDouble(), 10000 * slump.nextDouble());
			} while (coordinates.contains(point));
			coordinates.add(point);
			sensors[j].point = point;
			sensors[j].gateway = gateway;
		}

		gateway.sensors = sensors;
		SignalList.SendSignal(START, gateway, time, null);

		while (time < 10000) {
			actSignal = SignalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
		}

		// double lambda_p = (1.0 * gateway.no_transmissions / time);
		// double T_put = lambda_p * Math.exp(-2 * lambda_p);

		double throughput = (1.0 * gateway.no_success) / time;
		// double loss_rate = 1.0 * (gateway.no_transmissions - gateway.no_success) / gateway.no_transmissions;
		double loss_rate = (1 - (double) gateway.no_success / gateway.no_transmissions);

		double[] result = { throughput, loss_rate };

		return result;
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
