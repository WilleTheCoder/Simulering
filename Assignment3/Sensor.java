import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Sensor extends Proc{
	public int transmissionsStatue = 0, numberOfCollisions = 0;
	public Proc sendTo;
	Random slump = new Random();

	public void TreatSignal(Signal x){
		switch (x.signalType){

			case IDLE:{
				
				if (numberIn == 1){
					SignalList.SendSignal(READY,this, time + 0.2*slump.nextDouble());
				}
			} break;

			case TRANSMISSION:{
				transmissionsStatue++;
				if (transmissionsStatue >= 1){
					
					SignalList.SendSignal(IDLE, sendTo, time);
					transmissionsStatue = 0;
					numberOfCollisions++;
				}
				if (numberInQueue > 0){
					SignalList.SendSignal(READY, this, time + 0.2*slump.nextDouble());
				}
			} break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 2*slump.nextDouble());
			} break;
		}
	}
}