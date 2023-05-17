import java.util.Random;

public class GlobalSimulation{
	
	// This class contains the definition of the events that shall take place in the
	// simulation. It also contains the global time, the event list and also a method
	// for insertion of events in the event list. That is just for making the code in
	// MainSimulation.java and State.java simpler (no dot notation is needed).
	
	public static double time = 0; // The global time variable
	public static final int MOVE = 1, MEASURE = 3; // The events, add or remove if needed!
	public static EventListClass eventList = new EventListClass(); // The event list used in the program
	public static void insertEvent(int type, double TimeOfEvent){  // Just to be able to skip dot notation
		eventList.InsertEvent(type, TimeOfEvent);
	}
	Random rand = new Random();

	public double acquaint_time = 60;
	public double walk_time = 0.25; //change velocity v=2m/s = 0.5, v=4m/s = 0.25, 
	public static int grid_size = 20;
	public static int n_minions = 20;
	public static Box[][] hall;

	public int[][] directions = {
			{ -1, -1 }, // upleft
			{ 0, -1 }, // up
			{ 1, -1 }, // upright
			{ 1, 0 }, // right
			{ 1, 1 }, // lowright
			{ 0, 1 }, // down
			{ -1, 1 }, // lowleft
			{ -1, 0 } // left
	};

	public double uniform_dist(){
		return 1 + rand.nextInt(7);
	}

	public static void hall_to_string() {
		for (int i = 0; i < hall.length; i++) {
			for (int j = 0; j < hall[i].length; j++) {
				System.out.print(hall[i][j].status + "\t");
			}
			System.out.println();
		}
	}

}