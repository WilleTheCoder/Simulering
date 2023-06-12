import java.util.*;
import java.io.*;

//It inherits Proc so that we can use time and the signal names without dot notation 

class Gateway extends Proc {
	private Double lastTime;
	private Sensor lastSensor;
	public int r; // radius
	public int no_success = 0;
	public int no_crashes = 0;
	public int no_transmissions = 0;
	public double lb = 0.2, ub = 1.0;
	public int check_radius = 0;

	// Slumptalsgeneratorn startas:
	// The random number generator is started:
	Random slump = new Random();

	public Proc sendTo;
	public double lambda;
	public Sensor[] sensors;

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			
			case START: {
				// boot sensors
				for (int i = 0; i < sensors.length; i++) {
					SignalList.SendSignal(IDLE, sensors[i], time, null);
				}
				break;
			}
			case REPORT: {
				if (lastSensor == x.from) {
					no_success++;
					Sensor temp = lastSensor;
					lastSensor = null;
					lastTime = null;
					SignalList.SendSignal(IDLE, temp, time, null);
				}

				break;
			}
			case CHECK: {
				// there is a prev transmission sent from a sensor
				no_transmissions++;
				if (lastSensor != null) {
					// the time of the lastSensor is less than 1 and its transmitting in the same
					// area
					if ((time - lastTime) < 1) {
						SignalList.SendSignal(IDLE, x.from, time, null);
						SignalList.SendSignal(IDLE, lastSensor, time, null);
						no_crashes += 2;
						lastSensor = null;
						lastTime = null;
						break;
					}
				}

				lastSensor = x.from;
				lastTime = time;
				SignalList.SendSignal(REPORT, this, time + 1, x.from);
				break;
			}
			// STRATEGY 2
			case CHECK2: {
				// there is a prev transmission sent from a sensor
				no_transmissions++;
				if (lastSensor != null) {
					// the time of the lastSensor is less than 1 and its transmitting in the same
					// area

					// COLLISION
					if ((time - lastTime) < 1) {
						// Collision is found and stopped
						if (isSameArea(x.from, lastSensor, r)) {
							SignalList.SendSignal(WAIT, this, time, x.from);
							break;
						} 
						// Collision was not found in time
						else {
							SignalList.SendSignal(IDLE, x.from, time, null);
							SignalList.SendSignal(IDLE, lastSensor, time, null);
							no_crashes += 2;
							lastSensor = null;
							lastTime = null;
							break;
						}
					}
				}
				lastSensor = x.from;
				lastTime = time;
				SignalList.SendSignal(REPORT, this, time + 1, x.from);
				break;
			}

			case WAIT: {
				double sleepTime = lb + (ub - lb) * slump.nextDouble();
				SignalList.SendSignal(CHECK, this, time + sleepTime, x.from);
				break;
			}
		}
	}

	private boolean isSameArea(Sensor s1, Sensor s2, int r) {
		// pythogoras
		check_radius++;
		double distance = Math
				.sqrt(Math.pow(s2.point.getX() - s1.point.getX(), 2) + Math.pow(s2.point.getY() - s1.point.getY(), 2));
		return distance <= r;
	}

}