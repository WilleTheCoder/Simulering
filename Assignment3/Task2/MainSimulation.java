import java.util.*;
import java.io.*;
import java.awt.Point;


//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {
		Random rand = new Random();

		Signal actSignal;
		new SignalList();
		List<Point> coordinates = new ArrayList<>();
		Minion[] minions = new Minion[n_minions];

		hall = new Box[grid_size][grid_size];
		for (int i = 0; i < grid_size; i++) {
			for (int j = 0; j < grid_size; j++) {
				hall[i][j] = new Box();
			}
		}

		for (int j = 0; j < n_minions; j++) {
			minions[j] = new Minion();
			minions[j].i = j;
			Point point = null;
			do{
				point = new Point(rand.nextInt(grid_size), rand.nextInt(grid_size));
			} while(coordinates.contains(point));
			minions[j].point = point;
			coordinates.add(point);

			minions[j].no_steps = 1 + rand.nextInt(10);
			minions[j].dir = rand.nextInt(8);
			minions[j].minion_set = new HashSet<>();
			Box b = hall[minions[j].point.x][minions[j].point.y];
			b.add(minions[j]);
		}

		Controller controller = new Controller();
		controller.minions = minions;

		hall_to_string();

		SignalList.SendSignal(START, controller, time);

		// This is the main loop
		while (!controller.isDone(minions)) {
			// System.out.println("ppl met: " + controller.minions[1].minion_set.size());
			actSignal = SignalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
		}

		// Finally the result of the simulation is printed below:
	
	}
}