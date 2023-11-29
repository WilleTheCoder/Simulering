import java.util.Random;

public class Global{
	public static final int TRANSMISSION = 1, START = 2, FINISHED = 3,TRANSMISSION2 = 4;
	public static int r;
	public static double time = 0;
	public static int lb = 1000;
	public static int ub = 10000;

	Random slump = new Random();

	
	public double exp(double lambda){
		return Math.log(1 - slump.nextDouble())*-lambda;
	}

}
