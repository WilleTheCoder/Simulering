import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

	public static Config config;
	public static FileWriter fw;

	public static void main(String[] args) throws IOException {
		int m = 200;
		fw = new FileWriter("res1.txt");

		config = Config.get_config();
		config.load_config();
		int nl = config.n_arr_i.length;

		List<Double> loss_rates = new ArrayList<>();

		boolean overlap = true;

		Double intervals[][] = new Double[m][3];

		while (overlap) {
			int idx = 0;
			for (int n : config.n_arr_i) {
				for (int i = 0; i < m; i++) {
					double loss_rate = run(n);
					loss_rates.add(loss_rate);
				}
				System.out.println("Running with Network Load: " + n);
				double[] inter = confidenceInterval(loss_rates);
				intervals[idx][0] = inter[0];
				intervals[idx][1] = inter[1];
				idx++;
				System.out.println("conf for run " + idx + ": " + inter[0] + " " + inter[1]);
			}
			overlap = false;
			for (int i = 0; i < nl; i++) {
				for (int j = i + 1; j < nl; j++) {
					if (intervals[i][1] > intervals[j][0] && intervals[j][1] > intervals[i][0]) {
						overlap = true;
						break;
					}
				}
			}

			
		}
		
		for (int nx : config.n_arr_i) {
			fw.write(nx + " " + intervals[nx][3] + "\n");
		}
		fw.close();
	}

	private static double run(int n) throws IOException {

		time = 0;
		Gateway gateway = new Gateway();
		Signal actSignal = null;
		new SignalList();

		Sensor[] sensors = new Sensor[n];

		for (int j = 0; j < sensors.length; j++) {
			sensors[j] = new Sensor();
			sensors[j].gateway = gateway;
		}

		gateway.sensors = sensors;
		SignalList.SendSignal(START, gateway, time, null);

		while (time < 1000) {
			actSignal = SignalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
		}

		// ---------------A---------------

		// double lambda = gateway.no_transmissions/time;
		// double throughput= lambda * config.tp * Math.exp(-2 * lambda * config.tp);
		// System.out.println("Throughput: " + throughput);
		// fw.write(n + " " + throughput + "\n");

		// ---------------B---------------
		double loss_rate = (double) gateway.no_collisions / gateway.no_transmissions;
		return loss_rate;
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