import java.util.*;
import java.io.*;

class State extends GlobalSimulation{

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public Minion[] minions;

	Random slump = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case MOVE:
				// insertEvent(MOVE, time + 1);
				move();
				insertEvent(MOVE, time + 1/uniform_dist()); //walk_time
				break;
		}
	}

	// The following methods defines what should be done when an event takes place.
	// This could
	// have been placed in the case in treatEvent, but often it is simpler to write
	// a method if
	// things are getting more complicated than this.

	private void move() {

		for (Minion mx : minions) {
			if (time >= mx.time_til_move) {
				// if(mx.i == 0){
				// 	System.out.println();
				// 	hall_to_string();
				// }
				hall[mx.point.x][mx.point.y].remove(mx);
				int[] new_pos = mx.step(mx.point.x, mx.point.y);
				mx.point.x = new_pos[0];
				mx.point.y = new_pos[1];

				Box current = hall[mx.point.x][mx.point.y];
				int status = current.status; // get status before move into box
				current.add(mx);
				
				// if(mx.i == 0){
				// 	System.out.println();
				// 	hall_to_string();
				// }

				if (status == 1) { // Someone is here, keep em company
					// SignalList.SendSignal(ACQUAINT, this, time);
					Minion other = hall[mx.point.x][mx.point.y].get_other(mx);

					mx.time_til_move = time + acquaint_time;
					other.time_til_move = time + acquaint_time;

					mx.social_time += acquaint_time;
					other.social_time += acquaint_time;

					if(mx.minion_map.get(other) != null){
						double time_spent = mx.minion_map.get(other) + acquaint_time;
						mx.minion_map.put(other, time_spent);
						other.minion_map.put(mx, time_spent);
					} else{
						mx.minion_map.put(other, acquaint_time);
						other.minion_map.put(mx, acquaint_time);
					}
				}
			}
		}
	}

	public boolean isDone() {

		for (Minion m : minions) {
			if (m.minion_map.size() != n_minions - 1) { // you cant meet yourself sad -1
				return false;
			}
		}
		return true;
	}

}