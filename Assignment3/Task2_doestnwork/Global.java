public class Global {
	public static final int MOVE = 1, ACQUAINT = 2, START = 3, STOPACQUAINT = 4;
	public static double time = 0;
	public double acquaint_time = 60;
	public double walk_time = 1 / 2;
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

	public static void hall_to_string() {
		for (int i = 0; i < hall.length; i++) {
			for (int j = 0; j < hall[i].length; j++) {
				System.out.print(hall[i][j].status + "\t");
			}
			System.out.println();
		}
	}
}
