import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Global{
	public static final int TRANSMISSION = 1, IDLE = 2, START = 3, MEASURE = 4, REPORT = 5, CHECK = 6;
	public static double time = 0;
	public Map<Double, Sensor> controlMap = new LinkedHashMap <>();
}
