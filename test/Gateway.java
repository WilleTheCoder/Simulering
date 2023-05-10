import java.util.*;
import java.io.*;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gateway extends Proc {

	// Slumptalsgeneratorn startas:
	// The random number generator is started:
	Random slump = new Random();

	// Generatorn har tv� parametrar:
	// There are two parameters:
	public double lambda; // Hur m�nga per sekund som ska generas //How many to generate per second
	public Sensor[] sensors;
	public int transmissions = 0;
	public int crashes = 0;
	public int count = 0;

	public ArrayList<Double> transmission_time = new ArrayList<>(null);

	// H�r nedan anger man vad som ska g�ras n�r en signal kommer //What to do when
	// a signal arrives
	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case START: {
				for (int i = 0; i < 1; i++) {
					SignalList.SendSignal(IDLE, sensors[i], time, this);
				}
			}
				break;
			case REPORT: {
		


				loop: for (double t : controlMap.keySet()) {
					
					
					

					// if ((t-) <= 1) {
					// 	crashes+=2;
					// 	SignalList.SendSignal(IDLE, controlMap.get(t), time, this);
					// 	SignalList.SendSignal(IDLE, x.from, time, this);
					// 	break loop;
					}
					// SignalList.SendSignal(IDLE, controlMap.get(t), time+1, this);
				}
				

			}
				
		}
	}
