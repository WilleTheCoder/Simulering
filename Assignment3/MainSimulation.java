import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	//Signallistan startas och actSignal deklareras. actSignal �r den senast utplockade signalen i huvudloopen nedan.
    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the 
		// signal list in the main loop below.

		Config config = Config.get_config();
		config.load_config();

    	Signal actSignal;
    	new SignalList();

    	//H�r nedan skapas de processinstanser som beh�vs och parametrar i dem ges v�rden.
    	// Here process instances are created (two queues and one generator) and their parameters are given values. 

		Random slump = new Random();

		Sensor[] sensors = new Sensor[config.n];

		for (int i = 0; i < sensors.length; i++) {
			sensors[i] = new Sensor();
		}
		
    	Gateway gateway = Gateway.getInstance();
		gateway.sensors = sensors;

    	//H�r nedan skickas de f�rsta signalerna f�r att simuleringen ska komma ig�ng.
    	//To start the simulation the first signals are put in the signal list

    	SignalList.SendSignal(START, gateway, time, null);
    	// SignalList.SendSignal(MEASURE, Q1, time);

    	// Detta �r simuleringsloopen:
    	// This is the main loop

    	while (time < 100000) {
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	//Slutligen skrivs resultatet av simuleringen ut nedan:
    	//Finally the result of the simulation is printed below:
		System.out.println("Number of transmissions: "+ gateway.no_transmissions);   
		System.out.println("Number of collisions: "+ gateway.no_collisions);    
		System.out.println("Number of successfull transmissions: "+ gateway.no_success);   

 		double lambda = gateway.no_transmissions/time;
		double throughput = lambda * config.tp * Math.exp(-2 * lambda * config.tp);
		
		System.out.println("Throughput: " + throughput);

		double x = (double) gateway.no_success / gateway.no_transmissions;
		System.out.println(x);
	}
}