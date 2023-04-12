package Task2;

import java.util.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue_A = 0, numberInQueue_B = 0, accumulated = 0, noMeasurements = 0;

	// 𝜆 = 150 𝑠-1, 𝑥A =0.002 𝑠, 𝑥B =0.004 𝑠 and 𝑑 = 1 𝑠.
	private double a = 1.0/150, xA = 0.002, xB = 0.004;
	Random slump = new Random(); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVAL_A:
				arrivalA();
				break;
			case ARRIVAL_B:
				arrivalB();
				break;
			case READY_A:
				ready('A');
				break;
			case READY_B:
				ready('B');
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	

	//High priority jobs B teardown QUEUE
	// arrival intensity poisson process labmda
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.

	private void arrivalA(){
		// if (numberInQueue_A + numberInQueue_B == 0){ //B priority
		// 	insertEvent(READY_A, time + xA);
		// }
		if (numberInQueue_A == 0){  //A priority
			insertEvent(READY_A, time + xA);
		}

		numberInQueue_A++;
		insertEvent(ARRIVAL_A, time + exp(a));
	}
	private void arrivalB(){
		// if (numberInQueue_B == 0){ //B priority
		// 	insertEvent(READY_B, time + xB);
		// }
		if (numberInQueue_A + numberInQueue_B == 0){ //A priority
			insertEvent(READY_B, time + xB);
		}
		numberInQueue_B++;
	}
	
	private void ready(char job){
		// if(numberInQueue_B > 0){ //B priority
		// 	insertEvent(READY_B, time + xB);
		// 	numberInQueue_B--;
		// } else if(numberInQueue_A > 0){
		// 	insertEvent(READY_A, time + xA);
		// 	numberInQueue_A--;
		// }

		if(numberInQueue_A > 0){ //A priority
			insertEvent(READY_A, time + xA);
			numberInQueue_A--;
		} else if(numberInQueue_B > 0){
			insertEvent(READY_B, time + xB);
			numberInQueue_B--;
		}

		if(job == 'A'){
			insertEvent(ARRIVAL_B, time + 1); //change value of delay d
		}
	}
	
	private void measure(){
		accumulated = accumulated + numberInQueue_A+numberInQueue_B;
		noMeasurements++;
		insertEvent(MEASURE, time + 0.1);
		// System.out.println("Number in queue: " + numberInQueue_A + " : " + numberInQueue_B);
	}

	public double exp(double time){
		double lambda = 1.0 / time;
		return Math.log(1 - slump.nextDouble()) / (-lambda);
	}
}