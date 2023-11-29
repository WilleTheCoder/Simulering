import java.util.Map;
import java.util.Random;

public class Sensor extends Proc{
    private double lambda, x, y, radius, T_p, ub, lb;
    private Random rand = new Random();
    public Gateway gateway;

    // For strategy 1
    public Sensor(double x, double y, double T_p, double ts, double r, Gateway gateway) {
        this.x = x;
        this.y = y;
        this.T_p = T_p;
        this.lambda = ts;
        this.radius = r;
        this.gateway = gateway;
    }

    // For strategy 2
    public Sensor(double x, double y, double radius, double lambda, double messageTime, Gateway gateway, double lb, double ub) {
        this.x = x;
        this.y = y;
        this.T_p = messageTime;
        this.lambda = lambda;
        this.radius = radius;
        this.gateway = gateway;
        this.lb = lb;
        this.ub = ub;
    }

    public double getX()  {
        return x;
    }

    public double getY()  {
        return y;
    }

    public double getR()  {
        return radius;
    }

    public double getTp() {
        return T_p;
    }

    public double expDistribution() {
        return - lambda * Math.log(1-rand.nextDouble());
	}

    public double sleep() {
        return lb + (ub - lb)*rand.nextDouble();
	}

    public boolean checkChannel(Sensor other) {
        double delta_x = Math.pow(this.getX() - other.getX(), 2);
        double delta_y = Math.pow(this.getY() - other.getY(), 2);
        double dist = Math.sqrt(delta_x + delta_y);
        return dist < other.getR();
    }

    public void TreatSignal(Signal x) {
        switch(x.signalType) {
            case TRANSMIT_STRAT1:
                SignalList.SendSignal(RECEIVING, this, gateway, time);
                SignalList.SendSignal(TRANSMIT_STRAT1, this, this, time + T_p + expDistribution());
                break;
            case SENSE_CHANNEL:
                for (Map.Entry<Sensor, Boolean> s : Gateway.activeSensors.entrySet()) {
                    if(this.checkChannel(s.getKey())) {
                        SignalList.SendSignal(SENSE_CHANNEL, this, this, time + sleep());
                        return;
                    }
                }
                SignalList.SendSignal(RECEIVING, this, gateway, time);
                SignalList.SendSignal(SENSE_CHANNEL, this, this, time + T_p + expDistribution());
                break;
        }
    }
}