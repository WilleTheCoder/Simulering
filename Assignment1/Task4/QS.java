
import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc {
	public int numberInQueue = 0, totalNumberInQueueS = 0, accumulated, noMeasurements, numberOfGoingN = 0,
			numberOfGoingS = 0, noArrivals = 0;
	public Proc sendTo;
	Random slump = new Random();

	public double x1 = 4, percentage = 0.5;

	public double timeInTotalN = 0;
	public double timeInTotalS = 0;
	LinkedList<Double> arrivalTimeListN = new LinkedList<>();
	LinkedList<Double> arrivalTimeListS = new LinkedList<>();

	// public ArrayList<Double> timeListNStart = new ArrayList<Double>();
	// public ArrayList<Double> timeListNEnd = new ArrayList<Double>();
	// public ArrayList<Double> timeListSStart = new ArrayList<Double>();
	// public ArrayList<Double> timeListSEnd = new ArrayList<Double>();

	public void TreatSignal(Signal x) {
		switch (x.signalType) {

			case ARRIVAL: {
				noArrivals++;

				if (this.isSkipper(percentage)) {
					arrivalTimeListS.addLast(time);
				} else {
					arrivalTimeListN.addLast(time);
				}

				if (numberInQueue == 0) {
					SignalList.SendSignal(READY, this, time + exp(x1));
				}
				numberInQueue++;
			}
				break;

			case READY: {
				if (arrivalTimeListS.size() > 0) {
					timeInTotalS += time - arrivalTimeListS.poll();
					numberOfGoingS++;
				} else if (arrivalTimeListN.size() > 0) {
					timeInTotalN += time - arrivalTimeListN.poll();
					numberOfGoingN++;
				}
				numberInQueue--;

				if (numberInQueue > 0) {
					SignalList.SendSignal(READY, this, time + exp(x1));
				}
			}
				break;

			case MEASURE: {
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 2 * slump.nextDouble());
			}
				break;
		}
	}

	public double exp(double time) {
		double lambda = 1 / time;
		return Math.log(1 - slump.nextDouble()) / (-lambda);
	}

	private boolean isSkipper(double percentage) {
		int chance = (int) (percentage * 10);
		return slump.nextInt(10) < chance;
	}

}