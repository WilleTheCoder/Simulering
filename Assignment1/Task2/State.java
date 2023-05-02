import java.util.*;
import java.io.*;

class State extends GlobalSimulation {

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue_A = 0, numberInQueue_B = 0, totalCustomerInQueues = 0, noMeasurements = 0;

	// 𝜆 = 150 𝑠-1, 𝑥A =0.002 𝑠, 𝑥B =0.004 𝑠 and 𝑑 = 1 𝑠.
	private double lambda = 150, xA = 0.002, xB = 0.004, d = 1;
	Random slump = new Random();

	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case ARRIVAL_A:
				arrivalA();
				break;
			case READY_A:
				readyA();
				break;
			case MEASURE:
				measure();
				break;
			case ARRIVAL_B:
				arrivalB();
				break;
			case READY_B:
				readyB();
				break;
		}
	}

	private void arrivalA() {
		if (numberInQueue_A + numberInQueue_B == 0) {
			insertEvent(READY_A, time + xA);
		}
		numberInQueue_A++;
		insertEvent(ARRIVAL_A, time + get_poisson(lambda));
	}

	private void readyA() {
		numberInQueue_A--;
		insertEvent(ARRIVAL_B, time + exp(d)); //set exp delay
		serve_order();
	}

	private void arrivalB() {
		if (numberInQueue_A + numberInQueue_B == 0) {
			insertEvent(READY_B, time + xB);
		}
		numberInQueue_B++;
	}

	private void readyB() {
		numberInQueue_B--;
		serve_order();
	}

	private void serve_order() {
		// if (numberInQueue_A > 0) { // set priority A or B
		// 	insertEvent(READY_A, time + xA);
		// } else if (numberInQueue_B > 0) {
		// 	insertEvent(READY_B, time + xB);
		// }

		if (numberInQueue_B > 0) { // set priority A or B
			insertEvent(READY_B, time + xB);
		} else if (numberInQueue_A > 0) {
			insertEvent(READY_A, time + xA);
		}
	}

	private void measure() {
		totalCustomerInQueues = totalCustomerInQueues + numberInQueue_A + numberInQueue_B;
		noMeasurements++;
		insertEvent(MEASURE, time + 0.1);
	}

	public double exp(double lambda) {
		return Math.log(1 - slump.nextDouble()) / (-lambda);
	}

	public double get_poisson(double lambda) {
		return -Math.log(slump.nextDouble()) / lambda;
	}
}