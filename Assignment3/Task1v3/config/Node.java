package config;
import java.util.ArrayList;
import java.util.List;

public class Node {
  
  private int id;
  private double x;
  private double y;
  private List<Node> neighbors;
  private String neighborsIdsString;

  public Node(int id, double x, double y, int r, List<Node> nodes) {
    this.id = id;
    this.x = x;
    this.y = y;

    neighbors = new ArrayList<Node>();
    neighborsIdsString = "";

    findNeighbors(nodes, r);
  }

  public void findNeighbors(List<Node> nodes, int r) {
    for(Node node : nodes) {
      if(node != this && distance(node) <= r) {
        neighborsIdsString += "," + node.id;
        node.neighborsIdsString += "," + this.id;
      }
    }
  }

  private double distance(Node node) {
    return Math.sqrt(Math.pow(node.x - this.x, 2) + Math.pow(node.y - this.y, 2));
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public int getId() {
    return id;
  }

  public List<Node> getNeighbors() {
    return neighbors;
  }

  public String getNeighborsIdsString() {
    return neighborsIdsString;
  }

}
