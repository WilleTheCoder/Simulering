import java.util.*;
import java.io.*;

class Controller extends Proc {
	Random slump = new Random();
	public Minion[] minions;
	public boolean is_done = false;

	// H�r nedan anger man vad som ska g�ras n�r en signal kommer //What to do when
	// a signal arrives
	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case START: {
				// start moving minions
				for (int i = 0; i < minions.length; i++) {
					SignalList.SendSignal(MOVE, minions[i], time);
				}
				break;
			}
			// case MEASURE: {
			// 	is_done = isDone(minions);
			// 	SignalList.SendSignal(MEASURE, this, time + 120);
			// 	System.out.println("is it done? " + is_done);
			// }
		}
	}

	public boolean isDone(Minion[] minions) {

		for (Minion m : minions) {
			if (m.minion_set.size() != n_minions - 1) { // you cant meet yourself sad -1
				return false;
			}
		}
		return true;
	}
}