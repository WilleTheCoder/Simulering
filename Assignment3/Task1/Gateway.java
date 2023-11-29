import java.util.*;
import java.io.*;

//It inherits Proc so that we can use time and the signal names without dot notation 

class Gateway extends Proc {
	public int no_success = 0;
	public int no_transmissions = 0;

	// Slumptalsgeneratorn startas:
	// The random number generator is started:
	Random slump = new Random();

	public Proc sendTo;
	public double lambda;

	public static HashMap<Sensor, Boolean> active_sensors = new HashMap<>();
	public Sensor[] sensors;

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case START: {
				// boot sensors
				for (int i = 0; i < sensors.length; i++) {
					// set transmission strategy
					SignalList.SendSignal(TRANSMISSION2, sensors[i], time + exp(1000.0), null);
				}
				break;
			}

			case TRANSMISSION: {
				no_transmissions++;
				Sensor sender = x.from;
				active_sensors.put(sender, false);
				SignalList.SendSignal(FINISHED, this, time + Config.get_config().tp, sender);
				break;
			}

			case FINISHED: {
				Sensor sender = (Sensor) x.from;
				Boolean fail = active_sensors.remove(sender);
				if (fail == null) {
					fail = false;
				}
				for (Map.Entry<Sensor, Boolean> sensor : active_sensors.entrySet()) {
					fail = true;
					sensor.setValue(true);
				}

				if (!fail) {
					no_success++;
				}

				break;
			}
		}
	}

}