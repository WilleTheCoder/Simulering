// import java.util.*;
import java.io.*;
import java.util.Random;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Sensor extends Proc {
	public Gateway gateway;

	public Point point;
	Random slump = new Random();

	public void TreatSignal(Signal x) {
		switch (x.signalType) {

			case IDLE: {
				SignalList.SendSignal(TRANSMISSION, this, time + exp(Config.get_config().ts), this);
			}
				break;

			case TRANSMISSION: {
				SignalList.SendSignal(CHECK, gateway, time, this);
				// SignalList.SendSignal(CHECK2, gateway, time, this);
			}
				break;
		}
	}

	public double exp(double lambda){
		return Math.log(1 - slump.nextDouble())*-lambda;
	}
}