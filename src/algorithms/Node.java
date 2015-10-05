package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class Node {
	private Point p;
	private ArrayList<Point> neighbors;
	private double weight;
	
	public Node(Point p, ArrayList<Point> neighbors){
		this.p = p;
		this.neighbors = neighbors;
		this.weight = 1;
	}
	
	public Point getPoint(){
		return p;
	}
	
	public ArrayList<Point> getNeighbors(){
		return neighbors;
	}
	
	public int getDegree(){
		return neighbors.size();
	}
	
	public double getWeight(){
		return weight;
	}
	
	public void removeNeighbor(Point p){
		neighbors.remove(p);
	}
	
	public void setWeight(double weight){
		this.weight = weight;
	}
	
}
