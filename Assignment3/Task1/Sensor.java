import java.util.Map;
import java.util.Random;

class Sensor extends Proc {
	public Gateway gateway;
	public Point point;
	Random slump = new Random();

	public void TreatSignal(Signal x) {
		switch (x.signalType) {

			case TRANSMISSION: {
				gateway.no_transmissions++;
				SignalList.SendSignal(TRANSMISSION, gateway, time, this);
				SignalList.SendSignal(TRANSMISSION, this, time + exp(Config.get_config().ts), this);
			}
				break;

			case TRANSMISSION2: {
				boolean flag = true;
				cp: for (Map.Entry<Sensor, Boolean> other_sensor : Gateway.active_sensors.entrySet()) {
					if (this.isSameArea(this, other_sensor.getKey(), r)) {
						flag = false;
						SignalList.SendSignal(TRANSMISSION, gateway, time + sleep_time(), this);
						break cp;
					}
				}

				if (flag) {
					SignalList.SendSignal(TRANSMISSION, gateway, time, this);
				}

				SignalList.SendSignal(TRANSMISSION2, this, time + exp(Config.get_config().ts), this);

			}
				break;
		}

	}

	public double sleep_time() {
		return Global.lb + (Global.ub - Global.lb) * slump.nextDouble();
	}

	private boolean isSameArea(Sensor s1, Sensor s2, int r) {
		double distance = Math
				.sqrt(Math.pow(s2.point.getX() - s1.point.getX(), 2) + Math.pow(s2.point.getY() - s1.point.getY(), 2));
		return distance < r;
	}

}