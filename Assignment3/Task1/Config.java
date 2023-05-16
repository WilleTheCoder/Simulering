import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    
private static Config instance = null;

public static Config get_config(){
    if (instance == null){
        instance = new Config();
    } 
    return instance;
}

public int tp, ts, r;
public int[] n_arr_i;

public void load_config() throws IOException{

    FileReader fr = new FileReader("config.cfg");
    BufferedReader br = new BufferedReader(fr);

    String line = br.readLine();

    while(line != null){
        String[] pair = line.split("=");
        pair[0] = pair[0].strip();
        pair[1] = pair[1].strip();
    
        line =  br.readLine();
        switch (pair[0]) {
            case "n":
                String[] n_arr = pair[1].split(",");
                n_arr_i = new int[n_arr.length]; 
                for (int i = 0; i < n_arr_i.length; i++) {
                    n_arr_i[i] = Integer.parseInt(n_arr[i].trim()); 
                }
                break;
            case "tp":
                tp = Integer.parseInt(pair[1]);
                break;
            case "ts":
                ts = Integer.parseInt(pair[1]);
                break;
            case "r":
                r = Integer.parseInt(pair[1]);
                break;
            default:
                break;
        }
    }
}

    
}
