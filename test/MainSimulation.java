import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	//Signallistan startas och actSignal deklareras. actSignal �r den senast utplockade signalen i huvudloopen nedan.
    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the 
    	// signal list in the main loop below.
		
		Config config = new Config();
		config.load_config();

    	Signal actSignal;
    	new SignalList();

    	//H�r nedan skapas de processinstanser som beh�vs och parametrar i dem ges v�rden.
    	// Here process instances are created (two queues and one generator) and their parameters are given values. 

    	// Q1.sendTo = null;

		Random slump = new Random();

		Sensor[] sensors = new Sensor[config.n];

		Gateway Gateway = new Gateway();
		Gateway.sensors = sensors;

		for (int i = 0; i < 1; i++) {
			sensors[i] = new Sensor();
			sensors[i].sendTo = null;
			sensors[i].config = config;
			sensors[i].Gateway = Gateway;
			// SignalList.SendSignal(IDLE, sensors[i], time);
		}

    	// Gateway.lambda = 9; //Generator ska generera nio kunder per sekund  //Generator shall generate 9 customers per second
    	// Gateway.sendTo = sensors[0]; //De genererade kunderna ska skickas till k�systemet QS  // The generated customers shall be sent to Q1

    	//H�r nedan skickas de f�rsta signalerna f�r att simuleringen ska komma ig�ng.
    	//To start the simulation the first signals are put in the signal list

    	SignalList.SendSignal(START, Gateway, time, null);
    	// SignalList.SendSignal(MEASURE, Q1, time);


    	// Detta �r simuleringsloopen:
    	// This is the main loop

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	//Slutligen skrivs resultatet av simuleringen ut nedan:
    	//Finally the result of the simulation is printed below:

    	// System.out.println("Mean number of customers in queuing system: " + 1.0*Q1.accumulated/Q1.noMeasurements);

    }
}