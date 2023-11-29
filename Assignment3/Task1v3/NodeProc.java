import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NodeProc extends Proc {
  
  private int strategy = 0;
  private double lb = 0, ub = 0;
  private GatewayProc gateway;
  private double tp = 0, ts = 0;
  private boolean transmitting = false;

  public int[] neighbourIds;
  public NodeProc[] neighbours;

  private static Random rand = new Random();

  public NodeProc(GatewayProc gateway, double tp, double ts, int[] neighbourIds, int strategy, double lb, double ub) {
    this.gateway = gateway;
    this.tp = tp;
    this.ts = ts;
    this.neighbourIds = neighbourIds;
    this.strategy = strategy;
    this.lb = lb;
    this.ub = ub;

    // Send first signal
    SignalList.SendSignal(TRANSMISSION_CHECK, this, time + exp(ts), this);
  }

  public void setNeighbours(NodeProc[] nodes) {
    neighbours = new NodeProc[neighbourIds.length];
    for(int i = 0; i < neighbourIds.length; i++) {
      neighbours[i] = nodes[neighbourIds[i]];
    }
  }

  @Override
  public void TreatSignal(Signal x) {
    switch(x.signalType) {
      case TRANSMISSION_STARTED: 
        transmissions++;
        transmitting = true;
        SignalList.SendSignal(TRANSMISSION_STARTED, gateway, time, this);
        SignalList.SendSignal(TRANSMISSION_ENDED, this, time + tp, this);
        break;
      case TRANSMISSION_CHECK:
        // Check if any neighbour is transmitting
        boolean neighbourTransmitting = false;
        for(NodeProc n : neighbours) {
          if(n.transmitting) {
            neighbourTransmitting = true;
            break;
          }
        }

        if(neighbourTransmitting) {
          // Wait for transmission to end
          double wait = rand.nextDouble() * (ub - lb) + lb;
          SignalList.SendSignal(TRANSMISSION_STARTED, this, time + wait, this);
        } else {
          // Start transmission
          SignalList.SendSignal(TRANSMISSION_STARTED, this, time, this);
        }
        break;
      case TRANSMISSION_ENDED:
        transmitting = false;
        SignalList.SendSignal(TRANSMISSION_ENDED, gateway, time, this);

        if(strategy == 0) {
          SignalList.SendSignal(TRANSMISSION_STARTED, this, time + exp(ts), this);
        } else {
          SignalList.SendSignal(TRANSMISSION_CHECK, this, time + exp(ts), this);
        }
        break;

    }
  }

  private double exp(double mean) {
		return -mean * Math.log(rand.nextDouble());
	}

}
