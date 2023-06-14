import java.util.Random;

public class Global{
	public static final int TRANSMISSION = 1, IDLE = 2, MEASURE = 3, START = 4, FINISHED = 5, CHECK = 6, CHECK2 = 7, WAIT = 8;
	public static double time = 0;
	Random slump = new Random();

	
	public double exp(double lambda){
		return Math.log(1 - slump.nextDouble())*-lambda;
	}

}
