import java.util.Random;

public class NodeProcess extends Proc {

  private int id;
  private int[] neighbours;
  private Proc[] nodes;
  private GatewayProc gateway;
  private double tp = 0, ts = 0;

  private Random rand = new Random();

  private boolean transmitting = false;

  public NodeProcess(int id, int[] neighbours, Proc[] nodes, GatewayProc gateway, double tp, double ts) {
    this.id = id;
    this.neighbours = neighbours;
    this.nodes = nodes;
    this.gateway = gateway;
    this.tp = tp;
    this.ts = ts;

    // Send first signal
    SignalList.SendSignal(READY, this, time + exp(ts), null);
  }

  @Override
  public void TreatSignal(Signal x) {
    switch(x.signalType) {
      case READY:
        // Check if transmission is possible
        for(int i = 0; i < neighbours.length; i++) {
          int id = neighbours[i];
          if(gateway.transmittingNodes.contains(id)) {
            // Transmission is not possible
            SignalList.SendSignal(TRANSMISSION_INTERRUPTED, nodes[id], time, null);
            SignalList.SendSignal(READY, this, time + exp(ts), null);

            numberOfInterruptedTransmissions++;
            return;
          }
        }
        
        // Transmission is possible
        // SignalList.SendSignal(TRANSMISSION_STARTED, gateway, time);
        SignalList.SendSignal(TRANSMISSION_ENDED, this, time + tp, null);
        gateway.transmittingNodes.add(this.id);
        this.transmitting = true;
        break;
      case TRANSMISSION_INTERRUPTED:
        gateway.transmittingNodes.remove(gateway.transmittingNodes.indexOf(this.id));
        this.transmitting = false;
        numberOfInterruptedTransmissions++;
        SignalList.SendSignal(READY, this, time + exp(ts), null);
        break;
      case TRANSMISSION_ENDED:
        if(this.transmitting) {
          gateway.transmittingNodes.remove(gateway.transmittingNodes.indexOf(this.id));
          this.transmitting = false;
          numberOfSuccessfulTransmissions++;
          SignalList.SendSignal(READY, this, time + exp(ts), null);
        }
        break;
    }
  }

  private double exp(double mean) {
		return -mean * Math.log(1.0 - rand.nextDouble());
	}
  
}
