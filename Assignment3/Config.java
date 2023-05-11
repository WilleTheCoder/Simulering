import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    
public int n, tp, ts, r;

public void load_config() throws IOException{

    FileReader fr = new FileReader("config.cfg");
    BufferedReader br = new BufferedReader(fr);

    String line = br.readLine();

    while(line != null){
        String[] arr = line.split("=");
        arr[0] = arr[0].strip();
        arr[1] = arr[1].strip();
    
        line =  br.readLine();
        switch (arr[0]) {
            case "n":
                n = Integer.parseInt(arr[1]);
                break;
            case "tp":
                tp = Integer.parseInt(arr[1]);
                break;
            case "ts":
                ts = Integer.parseInt(arr[1]);
                break;
            case "r":
                r = Integer.parseInt(arr[1]);
                break;
            default:
                break;
        }

    }


}

    
}
