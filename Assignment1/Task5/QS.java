

import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int accumulated, noMeasurements;

	public int numberInQueue = 0;
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

				numberInQueue--;
				if (numberInQueue > 0){
					SignalList.SendSignal(READY, this, time + exp(x1));
				}
			} break;
			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 1);
			} break;
		}
	}
 
	public double exp(double time){
		double lambda = 1/time;
		return Math.log(1-slump.nextDouble())/(-lambda);
	}

	public static double uni(double mean) {
		double mid = mean / 2;
		return 2 * mid * Math.random() + mean - mid;
	}
}