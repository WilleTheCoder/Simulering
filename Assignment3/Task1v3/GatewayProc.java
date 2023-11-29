import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GatewayProc extends Proc {

  public List<Integer> transmittingNodes = new ArrayList<>();

  public NodeProc busyNode;
  // public List<NodeProc> busyNodes = new ArrayList<>();
  // public List<NodeProc> failedTransmissions = new ArrayList<>();
  public LinkedList<Boolean> transmissions = new LinkedList<>();

  @Override
  public void TreatSignal(Signal x) {
    
    switch(x.signalType) { 
      case TRANSMISSION_STARTED:
        if(transmissions.isEmpty()) {
          transmissions.add(true);
        } else {
          // Mark transmissions as failed
          numberOfInterruptedTransmissions++;
          for(int i = 0; i < transmissions.size(); i++) {
            transmissions.set(i, false);
          }
          transmissions.add(false);
        }
        break;
      case TRANSMISSION_ENDED:
        boolean status = transmissions.pop();
        if(status) {
          numberOfSuccessfulTransmissions++;
        }
        break;
    }
  }
  
}
