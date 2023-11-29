import java.util.*;
import java.io.*;

public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {
		// ============================================================ Task a - c
		// String fileName = "";
		// List<double[]> results = new ArrayList<double[]>();
		// for(int i = 0; i < 10; i++) {
		// fileName = "configs_task_a_c/task1_" + (i + 1) + "k.config.out";
		// results.add(simulate(fileName));
		// }

		// ============================================================ Task d
		String fileName = "";
		List<double[]> results = new ArrayList<double[]>();

        fileName = "configs_task_a_c/task1_1k.config";
        results.add(simulate(fileName));
	}

	private static double[] simulate(String file) {
		try {
			Scanner scan = new Scanner(new File(file));

			time = 0;
			transmissions = 0;
			numberOfInterruptedTransmissions = 0;
			numberOfSuccessfulTransmissions = 0;

			int n = 0;
			double tp = 0;
			double ts = 0;
			int r = 0;
			NodeProc[] nodes = new NodeProc[0];

			Signal actSignal;
			new SignalList();
			GatewayProc gateway = new GatewayProc();

			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] s = line.split("=");

				if (s[0].equals("n")) {
					n = Integer.parseInt(s[1]);
					nodes = new NodeProc[n];
				} else if (s[0].equals("tp")) {
					tp = Double.parseDouble(s[1]);
				} else if (s[0].equals("ts")) {
					ts = Double.parseDouble(s[1]);
				} else if (s[0].equals("r")) {
					r = Integer.parseInt(s[1]);
				} else if (s[0].equals("node")) {
					String[] data = s[1].split(",");
					int id = Integer.parseInt(data[0]);
					int[] neighbours = new int[data.length - 3];
					for (int i = 3; i < data.length; i++) {
						neighbours[i - 3] = Integer.parseInt(data[i]);
					}

					nodes[id] = new NodeProc(gateway, tp, ts, neighbours, 1, 1, 1);
				}
			}
			scan.close();

			// Set neighbours and calculate mean number of neighbours
			int totalNeighbours = 0;
			for (NodeProc node : nodes) {
				node.setNeighbours(nodes);
				totalNeighbours += node.neighbours.length;
			}
			double meanNeighbours = totalNeighbours / (double) nodes.length;

			System.out.println("\n############################");
			System.out.println("Config file read successfully");
			System.out.println("Number of nodes: " + n);
			System.out.println("Mean number of neighbours: " + meanNeighbours);
			// System.out.println("Transmission probability: " + tp);
			// System.out.println("Transmission speed: " + ts);
			// System.out.println("Radius: " + r);

			// Set up simulation

			System.out.println("Running simulation...");

			while (time < 30000) {
				actSignal = SignalList.FetchSignal();
				time = actSignal.arrivalTime;
				actSignal.destination.TreatSignal(actSignal);
			}

			System.out.println("Simulation done\n");
			double load = (transmissions / time);
			double actual = (numberOfSuccessfulTransmissions / time);
			double t = numberOfSuccessfulTransmissions / time;
			double normalizedThroughput = (actual * Math.exp(-2 * actual));
			double z = 1.96 * 2;
			double p = (1 - (double) numberOfSuccessfulTransmissions / transmissions);
			double interval = z * Math.sqrt(p * (1 - p) / transmissions);
			double packetLossP = (1 - (double) numberOfSuccessfulTransmissions / transmissions);

			double[] results = new double[] { load, t, interval, packetLossP };
			System.out.println("Load:\t\t" + results[0]);
			System.out.println("Throughput:\t" + results[1]);
			System.out.println("Error:\t\t" + results[2]);
			System.out.println("PLP.:\t\t" + results[3]);
			System.out.println("Number of interrupted transmissions: " + numberOfInterruptedTransmissions);

			System.out.println("############################\n");

			return results;

		} catch (Exception e) {
			System.out.println("Error reading file");
			e.printStackTrace();
		}
		return null;
	}

	private static NodeProc[] getNeighbors(NodeProc[] nodes, int[] ids) {
		NodeProc[] neighbours = new NodeProc[ids.length];
		for (int i = 0; i < ids.length; i++) {
			neighbours[i] = nodes[ids[i]];
		}
		return neighbours;
	}

}