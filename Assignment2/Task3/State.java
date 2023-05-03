import java.util.*;
import java.io.*;

class State extends GlobalSimulation {

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int moneyGoal = 0, monthly_investment = 0;
	public double monthly_growth = 0, share_hold = 0;

	public FileWriter fw = null;
	Random slump = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case INVESTMENT:
				investment();
				break;
			case DISTURBANCE:
				disturbance();
				break;
		}
	}

	// monthly investment
	private void investment() {
		write_to_file();
		share_hold += monthly_investment;
		share_hold = share_hold * monthly_growth;
		insertEvent(INVESTMENT, time + 1);
	}

	private void disturbance() {
		stock_disturbance();
		insertEvent(DISTURBANCE, time + slump.nextInt(96));
	}

	private void write_to_file() {
		try {
			fw.write(time + " " + share_hold + "\n");
		} catch (IOException e) {
			System.out.println("error");
		}
	}

	private void stock_disturbance() {
		int x = slump.nextInt(100);

		if (x < 10) {
			share_hold = share_hold * 0.25;
		} else if (x < 25) {
			share_hold = share_hold * 0.50;
		} else if (x < 50) {
			share_hold = share_hold * 0.60;
		} else {
			share_hold = share_hold * 0.10;
		}
	}

	public boolean isRich() {
		return share_hold > moneyGoal;
	}

	public double uni() {
		// Generate a uniformly distributed random value between -2 and 2
		double u = -2 + 4 * slump.nextDouble();

		// Convert the value to months and round to the nearest integer
		int disturbance = (int) Math.round(u * 12);

		// Make sure the disturbance is within the valid range
		if (disturbance < -96) {
			disturbance = -96;
		} else if (disturbance > 96) {
			disturbance = 96;
		}

		return disturbance;
	}

	public double from_yearly_to_montly_rate(double annualGrowthRate) {
		// (1 + r)^(1/12) - 1
		double monthlyGrowthRate = Math.pow(1 + annualGrowthRate, 1.0 / 12.0) - 1;
		return monthlyGrowthRate;
	}
}