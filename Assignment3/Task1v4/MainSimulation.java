import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

    public static Random slump = new Random();

    public static void main(String[] args) throws IOException {

        // Signallistan startas och actSignal deklareras. actSignal �r den senast
        // utplockade signalen i huvudloopen nedan.
        // The signal list is started and actSignal is declaree. actSignal is the latest
        // signal that has been fetched from the
        // signal list in the main loop below.

        Signal actSignal;
        new SignalList();

        Properties conf = new Properties();
        try {
            String path = "configs/1000.conf";
            FileInputStream file = new FileInputStream(path);
            conf.load(file);
            file.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int n = 10000;
        Double T_p = 1.0;
        Double t_s = 4000.0;
        Double r = 7 * 1000.0;

        // Used for uniform sleep in Strat2
        double lb = 1000, ub = 10000;

        Gateway gateway = new Gateway();

        // Sensor[] sensors = new Sensor[n];
        // for (int i = 0; i < n; i++) {
        //     String[] point = conf.getProperty("point_" + Integer.toString(i)).split(" ");
        //     Sensor sensor = new Sensor(Double.parseDouble(point[0]), Double.parseDouble(point[0]), r, t_s, T_p, gateway,
        //             lb, ub);
        //     SignalList.SendSignal(SENSE_CHANNEL, null, sensor, time + sensor.expDistribution());
        //     sensors[i] = sensor;
        // }

        List<Point> coordinates = new ArrayList<>();

		// init sensors
		Sensor[] sensors = new Sensor[n];
		// Create sensor coordinates
		for (int j = 0; j < n; j++) {
            Point point = null;
            
			// generate point until the point doesnt already exist
			do {
                double x = slump.nextDouble()*10000;
                double y = slump.nextDouble()*10000;
				point = new Point(x, y);
			} while (coordinates.contains(point));
			coordinates.add(point);
            sensors[j] = new Sensor(point.getX(), point.getY(), r, t_s, T_p, gateway, lb, ub);
     
            SignalList.SendSignal(SENSE_CHANNEL, null, sensors[j], time + sensors[j].expDistribution());
		}


        SignalList.SendSignal(MEASURE, null, gateway, time + 10);

        System.out.println("lb = " + lb + ", ub = " + ub);
        System.out.println("n = " + n + ", T_p = " + T_p + ", t_s = " + t_s + ", Radius = " + r + "\n");

        while (time < 10000) {
            actSignal = SignalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        String throughput = String.format("%.4f", gateway.succSignals / time);
        System.out.println("Throuhgput: " + throughput);

        double lambda = (gateway.totSignals / time);
        double T_put = 1 * lambda * Math.exp(-2 * lambda);

        double packetLoss = 1 - (gateway.succSignals / gateway.totSignals);

        System.out.println("T_put = " + String.format("%.4f", T_put));
        System.out.println("Packet loss: " + String.format("%.4f", packetLoss));
    
    }
}