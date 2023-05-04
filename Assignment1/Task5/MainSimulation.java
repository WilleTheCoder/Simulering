
import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		// Signallistan startas och actSignal deklareras. actSignal �r den senast
		// utplockade signalen i huvudloopen nedan.
		// The signal list is started and actSignal is declaree. actSignal is the latest
		// signal that has been fetched from the
		// signal list in the main loop below.

		Signal actSignal;
		new SignalList();

		// H�r nedan skapas de processinstanser som beh�vs och parametrar i dem ges
		// v�rden.
		// Here process instances are created (two queues and one generator) and their
		// parameters are given values.

		Gen Generator = new Gen();

		// 0.11, 0.15 and 2.00 seconds
		Generator.lambda = 2.0; // Generator ska generera nio kunder per sekund //Generator shall generate 9
											// customers per second
		// Generator.sendTo = Q[0]; //De genererade kunderna ska skickas till k�systemet
		// QS // The generated customers shall be sent to Q1

		QS[] queues = new QS[5];
		for (int i = 0; i < queues.length; i++) {
			queues[i] = new QS();
			queues[i].sendTo = null;
			Generator.queues.add(queues[i]);
			SignalList.SendSignal(MEASURE, queues[i], time);
		}

		// H�r nedan skickas de f�rsta signalerna f�r att simuleringen ska komma ig�ng.
		// To start the simulation the first signals are put in the signal list

		SignalList.SendSignal(READY, Generator, time);
		// SignalList.SendSignal(MEASURE, Generator, time);

		// Detta �r simuleringsloopen:
		// This is the main loop

		while (time < 100000) {
			actSignal = SignalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
		}

		// Slutligen skrivs resultatet av simuleringen ut nedan:
		// Finally the result of the simulation is printed below:

		System.out.println("--------TASK5--------\n");
		// Find the mean number of jobs in the queuing systems for all the algorithms
		// and the following mean
		// arrival times to the dispatcher: 0.11, 0.15 and 2.00 seconds. Which is the
		// best algorithm?
		double sum = 0;
		for (QS qs : queues) {

			sum += 1.0*qs.accumulated / qs.noMeasurements;
			System.out.println("mean number queue: " + 1.0*qs.accumulated / qs.noMeasurements);
		}
		System.out.println(sum);

			// System.out.println(1.0*q);

		// System.out.println(Generator.totalNumberInQueues);

		// System.out.println("Mean number of jobs in the queuing systems: "+ 1.0 * Generator.totalNumberInQueues / Generator.noMeasurements);

		// for (int i = 0; i < 20; i++) {
		// 	Generator.randomDistribution();
		// }
		System.out.println("\n----------END----------");
	}

}