package config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConfigPopulator {
  
  public static void main(String[] args) {
    String fileName = "configs_task_d/task1d_r11.config";

    // Read json file
    try {
      Scanner scan = new Scanner(new File(fileName));
      
      int n = 0;
      double tp = 0;
      double ts = 0;
      int r = 0;

      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        String[] s = line.split("=");

        if(s[0].equals("n")) {
          n = Integer.parseInt(s[1]);
        } else if(s[0].equals("tp")) {
          tp = Double.parseDouble(s[1]);
        } else if(s[0].equals("ts")) {
          ts = Double.parseDouble(s[1]);
        } else if(s[0].equals("r")) {
          r = Integer.parseInt(s[1]);
        }
      }

      // Print loaded values
      System.out.println("n: " + n);
      System.out.println("tp: " + tp);
      System.out.println("ts: " + ts);
      System.out.println("r: " + r);

      // Create the nodes
      Random rand = new Random();
      List<Node> nodes = new ArrayList<Node>();
      
      System.out.println("Creating nodes");
      for(int i = 0; i < n; i++) {
        nodes.add(new Node(i, rand.nextDouble() * 10, rand.nextDouble() * 10, r, nodes));
      }
      System.out.println("Nodes created");

      // Create output config file
      System.out.println("Creating output file");

      String outputFileName = fileName + ".out";
      File outputFile = new File(outputFileName);
      outputFile.createNewFile();

      // Write to output config file
      FileWriter writer = new FileWriter(outputFile);
      writer.write("n=" + n + "\n");
      writer.write("tp=" + tp + "\n");
      writer.write("ts=" + ts + "\n");
      writer.write("r=" + r + "\n");
      writer.write("gateway=5,5\n");
      
      for(Node node : nodes) {
        writer.write("node=" + node.getId() + "," + node.getX() + "," + node.getY() + node.getNeighborsIdsString() + "\n");
        if(node.getId() % 100 == 0) System.out.println("Node " + node.getId() + " written");
      }

      writer.close();

      System.out.println("Output file created");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
