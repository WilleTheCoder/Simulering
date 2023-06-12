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

		// Set save options
		boolean save = true;
		if (save)
			fw = new FileWriter("res.txt");

		// init configuration
		config = Config.get_config();
		config.load_config();
		int m = 200; // runs
		int r = 7; // radius
		int n = 2000; // number of students

		gateway = new Gateway();
		Global.time = 0;
		gateway.r = r;

		Signal actSignal = null;
		new SignalList();

		List<Point> coordinates = new ArrayList<>();

		// init sensors
		Sensor[] sensors = new Sensor[n];
		// Create sensor coordinates
		for (int j = 0; j < sensors.length; j++) {
			sensors[j] = new Sensor();
			Point point = null;

			double factor = Math.pow(10, 2);

			// generate point until the point doesnt already exist
			do {
				double x = Math.round(factor * slump.nextDouble() * 10) / factor;
				double y = Math.round(factor * slump.nextDouble() * 10) / factor;
				point = new Point(x, y);
			} while (coordinates.contains(point));
			coordinates.add(point);
			sensors[j].point = point;
			sensors[j].gateway = gateway;
		}


		gateway.sensors = sensors;

		SignalList.SendSignal(START, gateway, time, null);

		// while (time < 10000) {
		// 	actSignal = SignalList.FetchSignal();
		// 	time = actSignal.arrivalTime;
		// 	actSignal.destination.TreatSignal(actSignal);
		// }

	}
}
