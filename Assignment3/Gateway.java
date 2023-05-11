import java.util.*;
import java.io.*;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gateway extends Proc {
	private Double lastTime;
	private Sensor lastSensor;
	public int no_collisions = 0;
	public int no_success = 0;
	public int no_transmissions = 0;

	private Gateway() {}

	private static Gateway instance = null;

	public static Gateway getInstance() {
		if (instance == null) {
			instance = new Gateway();
		}
		return instance;
	}

	// Slumptalsgeneratorn startas:
	// The random number generator is started:
	Random slump = new Random();

	// Generatorn har tv� parametrar:
	// There are two parameters:
	public Proc sendTo; // Anger till vilken process de genererade kunderna ska skickas //Where to send
						// customers
	public double lambda; // Hur m�nga per sekund som ska generas //How many to generate per second

	public Sensor[] sensors;

	// H�r nedan anger man vad som ska g�ras n�r en signal kommer //What to do when
	// a signal arrives
	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case START: {
				// boot sensors
				for (int i = 0; i < sensors.length; i++) {
					SignalList.SendSignal(IDLE, sensors[i], time, null);
				}
				break;
			}
			case MEASURMENT_REPORT: {
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
				no_transmissions++;
				// there is a prev transmission sent from a sensor
				br: if (lastSensor != null) {
					// the time of the prev transmission is less than 1 = crash
					if (time - lastTime < 1) {
						no_collisions+=2; // increment crashes
						// set both transmissions to idle
						SignalList.SendSignal(IDLE, x.from, time, null);
						SignalList.SendSignal(IDLE, lastSensor, time, null);
						lastSensor = null;
						lastTime = null;
						break br;
					} else {
						lastSensor = x.from;
						lastTime = time;
						SignalList.SendSignal(MEASURMENT_REPORT, this, time + 1, x.from);
					}
				} else{
					lastSensor = x.from;
					lastTime = time;
					SignalList.SendSignal(MEASURMENT_REPORT, this, time + 1, x.from);
				}

				break;
			}
		}
	}
}