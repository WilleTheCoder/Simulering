
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Gateway extends Proc{
    
    public double totSignals = 0, succSignals = 0, nbrMeasurements = 0;
    public Proc sendTo;
    private Random rand = new Random();
    public double confInterval = 1.0;

    public static HashMap<Sensor, Boolean> activeSensors = new HashMap<>();
    public ArrayList<Double> packetLosses = new ArrayList<>();

    public double confInterval(List<Double> l) {
        int n = l.size();
        double sum = 0.0;
        for (double x : l) {
            sum += x;
        }
        double mean = sum / n;
        double var = 0.0;
        for (double x : l) {
            var += Math.pow(x-mean, 2);
        }
        var = var / n;
        double std = Math.sqrt(var);
        return 1.96 * (std / Math.sqrt(n));
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case RECEIVING:
                totSignals++;
                Sensor sender = (Sensor) x.sender;
                activeSensors.put(sender, false); 
                SignalList.SendSignal(RECEIVED, sender, this, time+sender.getTp());
                break;
            case RECEIVED:
                sender = (Sensor) x.sender;
                boolean fail = activeSensors.remove(sender);
                for (Map.Entry<Sensor, Boolean> s: activeSensors.entrySet()) {
					s.setValue(true);
					fail = true;
				} 
                succSignals = fail ? succSignals : succSignals + 1;
                break;
            case MEASURE:
                nbrMeasurements++;
                double packetLoss = 1 - (succSignals / totSignals);
                packetLosses.add(packetLoss);
                confInterval = confInterval(packetLosses);
                SignalList.SendSignal(MEASURE, this, this, time + 2*rand.nextDouble());
                break;
        }
    }
    
}