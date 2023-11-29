public class Global {
	public static final int ARRIVAL = 1, READY = 2, MEASURE = 3;
	public static final int TRANSMISSION_STARTED = 4, TRANSMISSION_ENDED = 5, TRANSMISSION_INTERRUPTED = 6, TRANSMISSION_CHECK = 7;
	public static double time = 0;

	public static int transmissions = 0;
	public static int numberOfInterruptedTransmissions = 0;
	public static int numberOfSuccessfulTransmissions = 0;
}
