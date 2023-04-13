
import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int numberInQueue = 0, numberInQueueN = 0, numberInQueueS = 0,  totalNumberInQueueS = 0, accumulated, noMeasurements;
	public Proc sendTo;
	Random slump = new Random();

	public double a = 300, x1 = 240, percentage = 0.1;

	public ArrayList<Double> timeListNStart = new ArrayList<Double>();
	public ArrayList<Double> timeListNEnd = new ArrayList<Double>();
	public ArrayList<Double> timeListSStart = new ArrayList<Double>();
	public ArrayList<Double> timeListSEnd = new ArrayList<Double>();

	public void TreatSignal(Signal x){
		switch (x.signalType){
			
			case ARRIVAL:{
				
				if (this.isSkipper(percentage)){
					numberInQueueS++;
					timeListSStart.add(time);
				} else{
					numberInQueueN++;
					timeListNStart.add(time);
				}

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

					if(numberInQueueS > 0){
						numberInQueueS--;
						timeListSEnd.add(time);
					} else{
						numberInQueueN--;
						timeListNEnd.add(time);
					}

					SignalList.SendSignal(READY, this, time + exp(x1));
				}
			} break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 2*slump.nextDouble());
			} break;
		}
	}

	public double exp(double time){
		double lambda = 1/time;
		return Math.log(1-slump.nextDouble())/(-lambda);
	}

	private boolean isSkipper(double percentage){
		int chance =  (int)(percentage*10);
		return slump.nextInt(10) < chance;
	}


}