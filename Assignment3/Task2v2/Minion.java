import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.Point;

public class Minion extends GlobalSimulation {
	protected Point point;
	protected int dir = 0, no_steps = 0, i = 0;
	protected double social_time = 0;
	protected Set<Minion> minion_set;
	protected double time_til_move = 0;
	Random rand = new Random();

	Minion() {
		this.no_steps = 1 + rand.nextInt(9);
		this.dir = rand.nextInt(8);
		this.minion_set = new HashSet<>();
	}

	public int[] step(int x, int y) {

		List<Integer> valid_dirs = new ArrayList<>();

		if (no_steps == 0) {
			dir = rand.nextInt(8);
			no_steps = 1 + rand.nextInt(9);
		}

		// check if we can walk in same direction
		if (x + directions[dir][0] >= 0 && x + directions[dir][0] < grid_size &&
				y + directions[dir][1] >= 0 && y + directions[dir][1] < grid_size) {

			no_steps--;
			int[] coords = { x + directions[dir][0], y + directions[dir][1] };
			return coords;
		}

		// see which dirs are valid and randomly choose one.
		for (int i = 0; i < directions.length; i++) {
			int new_x = x + directions[i][0];
			int new_y = y + directions[i][1];
			if (new_x >= 0 && new_x < grid_size && new_y >= 0 && new_y < grid_size) {
				valid_dirs.add(i);
			}
		}

		int dir_idx = rand.nextInt(valid_dirs.size());
		int new_dir = valid_dirs.get(dir_idx);
		this.dir = new_dir; // new direction
		this.no_steps = 1 + rand.nextInt(10); // steps in that direction

		int[] coords = { x + directions[new_dir][0], y + directions[new_dir][1] };
		return coords;
	}

}
