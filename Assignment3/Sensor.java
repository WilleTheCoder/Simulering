import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Sensor extends Proc {
	public int transmissionsStatue = 0, numberOfCollisions = 0;
	public Proc sendTo;
	Random slump = new Random();

	public void TreatSignal(Signal x) {
		switch (x.signalType) {

			case IDLE: {
				SignalList.SendSignal(TRANSMISSION, this, time + exp(Config.get_config().ts), this);
			}
				break;

			case TRANSMISSION: {
				SignalList.SendSignal(CHECK, Gateway.getInstance(), time, this);
			}
				break;
		}
	}

	public double exp(double time){
		double lambda = 1/time;
		return Math.log(1-slump.nextDouble())/(-lambda);
	}
}