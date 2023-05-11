import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Sensor extends Proc {
	public int accumulated, noMeasurements;
	public Proc sendTo;
	public Proc Gateway;
	public Config config;
	Random slump = new Random();
	public boolean sendStatus = false;

	public void TreatSignal(Signal x) {
		switch (x.signalType) {

			case TRANSMISSION: {
				sendStatus = true;
				controlMap.put(time+1, this);
				SignalList.SendSignal(REPORT, Gateway, time, this);

				// SignalList.SendSignal(IDLE, this, time + 1); //no crash
			}
				break;

			case IDLE: {
				sendStatus = false;
				SignalList.SendSignal(TRANSMISSION, this, time + exp(config.ts), this);
			}
				break;

			case MEASURE: {
				noMeasurements++;
				SignalList.SendSignal(MEASURE, this, time + 2 * slump.nextDouble(), this);
			}
				break;
		}
	}

	public double exp(double time) {
		double lambda = 1 / time;
		return Math.log(1 - slump.nextDouble()) / (-lambda);
	}
}