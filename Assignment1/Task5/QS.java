

import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int numberInQueue = 0, accumulated, noMeasurements;
	public Proc sendTo;
	Random slump = new Random();

	public double x1 = 0.5;

	public void TreatSignal(Signal x){
		switch (x.signalType){
			
			case ARRIVAL:{

				if (numberInQueue == 0){
					SignalList.SendSignal(READY,this, time + exp(x1));
				}
				numberInQueue++; 
			} break;

			case READY:{

				if (sendTo != null){
					SignalList.SendSignal(ARRIVAL, sendTo, time);
				}

				if (numberInQueue > 0){
					numberInQueue--;
					SignalList.SendSignal(READY, this, time + exp(x1));
				}
			} break;
		}
	}
 
	public double exp(double time){
		double lambda = 1/time;
		return Math.log(1-slump.nextDouble())/(-lambda);
	}
}