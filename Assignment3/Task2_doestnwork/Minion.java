import java.util.*;
import java.awt.Point;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Minion extends Proc {

	protected Point point;
	protected int dir = 0, no_steps = 0, i = 0;
	protected double social_time = 0;
	protected Set<Minion> minion_set;
	public boolean is_in_convo;
	Random rand = new Random();

	Minion() {
		this.no_steps = 1 + rand.nextInt(9);
		this.dir = rand.nextInt(8);
		this.minion_set = new HashSet<>();
	}

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case MOVE: {
				if(this.i == 0){
					System.out.println("im moving");
				}

				if(!is_in_convo){
					System.out.println("Sensor id: " + this.i);
					// System.out.println("Minions met: " + this.minion_set.size());
					// System.out.println();
					// hall_to_string();
					
					// make the move and update
					
					hall[this.point.x][this.point.y].remove(this);
			
					int[] new_pos = step(this.point.x, this.point.y);
					this.point.x = new_pos[0];
					this.point.y = new_pos[1];
					
					Box current = hall[this.point.x][this.point.y];
					int status = current.status; // get status before move into box					
					current.add(this);
		
					// System.out.println();
					// hall_to_string();
					
					if (status == 1) { // Someone is here, keep em company
						SignalList.SendSignal(ACQUAINT, this, time);
					} else { // status 0 or 2, keep on moving
						SignalList.SendSignal(MOVE, this, time + walk_time);
					}
				}
			}
				break;

			case ACQUAINT: {
				Minion other = hall[this.point.x][this.point.y].get_other(this);
				this.is_in_convo = true;
				other.is_in_convo = true;
				this.social_time += acquaint_time;
				other.social_time += acquaint_time;
				this.minion_set.add(other);
				other.minion_set.add(other);

				SignalList.SendSignal(STOPACQUAINT, this, time + acquaint_time);
			}
				break;

			case STOPACQUAINT: {
				Minion other = hall[this.point.x][this.point.y].get_other(this);
				this.is_in_convo = false;
				other.is_in_convo = false;
				SignalList.SendSignal(MOVE, this, time + acquaint_time);
			}
				break;
		}
	}

	public int[] step(int x, int y) {

		Random random = new Random();
		List<Integer> valid_dirs = new ArrayList<>();

		if (no_steps == 0) {
			dir = rand.nextInt(8);
			no_steps = 1 + rand.nextInt(9);
		}

		if (no_steps != 0) {
			// check if we can walk in same direction
			if (x + directions[dir][0] >= 0 && x + directions[dir][0] < grid_size &&
					y + directions[dir][1] >= 0 && y + directions[dir][1] < grid_size) {

				no_steps--;

				int[] coords = {x + directions[dir][0], y + directions[dir][1]};
				return coords;
			}
		}
		// see which dirs are valid and randomly choose one.
		for (int i = 0; i < directions.length; i++) {
			int new_x = x + directions[i][0];
			int new_y = y + directions[i][1];
			if (new_x >= 0 && new_x < grid_size && new_y >= 0 && new_y < grid_size) {
				valid_dirs.add(i);
			}
		}

		int dir_idx = random.nextInt(valid_dirs.size());
		int new_dir = valid_dirs.get(dir_idx);
		this.dir = new_dir; // new direction
		this.no_steps = 1 + rand.nextInt(10); // steps in that direction

		int[] coords = { x + directions[new_dir][0], y + directions[new_dir][1] };
		return coords;
	}

}