import java.util.*;
import java.awt.Point;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Minion extends Proc {

	protected Point point;
	protected int dir = 0, no_steps = 0, i = 0;
	protected double social_time = 0;
	protected Set<Minion> minion_set;

	Random slump = new Random();

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case MOVE: {
				// System.out.println("Sensor id: " + this.i);
				// System.out.println("Minions met: " + this.minion_set.size());
				// System.out.println();
				// hall_to_string();
				// make the move and update
				Box old = hall[this.point.x][this.point.y];				
				old.remove(this);

				int[] new_pos = make_a_move(this.point.x, this.point.y);
				this.point.x = new_pos[0];
				this.point.y = new_pos[1];
	
				Box current = hall[this.point.x][this.point.y];
				int skip = current.add(this);
				// System.out.println();
				// hall_to_string();

				no_steps--;
				if (no_steps == 0){
					dir = slump.nextInt(8);
					no_steps = 1 + slump.nextInt(10);
				}

				if (skip == 3) { // 2 ppl talk, dont interrupt
					SignalList.SendSignal(MOVE, this, time + walk_time);
				}
				// next step
				else if (current.status == 2) {// oh an alone person, go keep them company.
					SignalList.SendSignal(ACQUAINT, this, time);
					Minion other = hall[this.point.x][this.point.y].get_other(this);
					SignalList.SendSignal(ACQUAINT, other, time);
				} else if (current.status == 1) { // no one home
					SignalList.SendSignal(MOVE, this, time + walk_time);
				}
			}
				break;

			case ACQUAINT: {
				Minion other = hall[this.point.x][this.point.y].get_other(this);
				social_time += acquaint_time;
				minion_set.add(other);
				SignalList.SendSignal(MOVE, this, time + acquaint_time);
			}
				break;
		}
	}

	public int[] make_a_move(int x, int y) {
		int[][] directions = {
				{ -1, -1 }, // upleft
				{ 0, -1 }, // up
				{ 1, -1 }, // upright
				{ 1, 0 }, // right
				{ 1, 1 }, // lowright
				{ 0, 1 }, // down
				{ -1, 1 }, // lowleft
				{ -1, 0 } // left
		};

		Random random = new Random();
		List<Integer> validDirs = new ArrayList<>();

		if (no_steps != 0) {
			// check if we can walk in same direction
			if (x + directions[dir][0] >= 0 && x + directions[dir][0] < grid_size &&
					y + directions[dir][1] >= 0 && y + directions[dir][1] < grid_size) {
				int[] coords = { x + directions[dir][0], y + directions[dir][1] };
				return coords;
			}
		}
		// see which dirs are valid and randomly choose one.
		for (int i = 0; i < directions.length; i++) {
			int newX = x + directions[i][0];
			int newY = y + directions[i][1];
			if (newX >= 0 && newX < grid_size && newY >= 0 && newY < grid_size) {
				validDirs.add(i);
			}
		}

		int dirIdx = random.nextInt(validDirs.size());
		int newDir = validDirs.get(dirIdx);
		this.dir = newDir; //new direction 
		this.no_steps = 1 + slump.nextInt(10); //steps in that direction

		int[] coords = { x + directions[newDir][0], y + directions[newDir][1] };
		return coords;
	}

}