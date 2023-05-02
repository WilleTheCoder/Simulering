import java.util.*;
import java.io.*;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc{

	//Slumptalsgeneratorn startas:
	//The random number generator is started:
	Random slump = new Random();
	public int noMeasurements = 0;
	public long totalNumberInQueues = 0;
	//Generatorn har tv� parametrar:
	//There are two parameters:
	public List<QS> queues = new ArrayList<>(); 
	public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
	public double lambda;  //Hur m�nga per sekund som ska generas //How many to generate per second
	public int currentQueue = 0;

	//H�r nedan anger man vad som ska g�ras n�r en signal kommer //What to do when a signal arrives
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				//set sendTo variable
				sendTo = equalDistribution();
				SignalList.SendSignal(ARRIVAL, sendTo, time);
				SignalList.SendSignal(READY, this, time + exp(lambda)); //send new signal to get new customer
				// (2.0/lambda)*slump.nextDouble()
			} 
				break;

			case MEASURE:{
				noMeasurements++;

				for (QS qs : queues) {
					totalNumberInQueues += qs.numberInQueue;
				}
				
				SignalList.SendSignal(MEASURE, this, time + 2*slump.nextDouble());
				break;
			}
		}
	}

	public Proc randomDistribution() {
		int i = slump.nextInt(5);
		return queues.get(i);
	}

	private Proc roundRobin() {
		currentQueue = currentQueue%5;
		Proc p = queues.get(currentQueue);
		currentQueue++;
		return p;
	}

	private Proc equalDistribution() {
		int minVal = Integer.MAX_VALUE;
		int minIdx = 0;
		for (int i = 0; i < queues.size(); i++){
			int cVal = queues.get(i).numberInQueue;
			if(cVal < minVal){
				minVal = cVal;
				minIdx = i;
			}
		}
		return queues.get(minIdx);
	}

	public double exp(double time){
		double lambda = 1/time;
		return Math.log(1-slump.nextDouble())/(-lambda);
	}
}