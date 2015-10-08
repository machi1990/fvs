package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultTeam {

  public ArrayList<Point> calculFVS(ArrayList<Point> points) {
    ArrayList<Point> fvs = new ArrayList<Point>();

    
    for(int i=0;i<5*points.size()/6;i++){
      fvs.add(points.get(i));
    }

    return fvs;
  }
  
  public ArrayList<Point> bafnaFVS (ArrayList<Point> points) {
	  
	  return null;
  }
  
  public void cleanup(HashMap<Point,Node> nodes){
	  boolean clean;
	  do{
		  clean = true;
		  
		  for(Point p : nodes.keySet()){
			  
			  Node n = nodes.get(p);
			  
			  if(n.getDegree() <= 1 ){
				  
				  for(Point neighbor : n.getNeighbors() ){
					  nodes.get(neighbor).removeNeighbor(p);
				  }
				  
				  nodes.remove(p);
				  clean = false;
				  
				  break;
			  }
		  }
		  
	  }while(!clean);
  }
  
  public ArrayList<Point> cycleCheck(HashMap<Point,Node> nodes){
	  ArrayList<Node> vertices = new ArrayList<>(nodes.values()); // nodes remaining
	  ArrayList<Node> green = new ArrayList<>(); // cycle acceptable nodes to visit next
	  ArrayList<Node> black = new ArrayList<>(); // already visited nodes
	  ArrayList<Node> white = new ArrayList<>(); // waiting jokers
	  
	  
	  for( Node n : vertices ) n.resetTag(); // reset tags
	  
	  int maxTag = -1;
	  Node current_joker;
	  
	  while( !white.isEmpty() || !vertices.isEmpty()){
		  current_joker = null;
		  if(!white.isEmpty()){ // prefer waiting jokers over random points
			  Node joker = white.remove(0);
			  green.add(joker);
			  current_joker = joker;
		  } else { // remaining random points are actually in a different subgraph (or first iteration)
			  Node origin = vertices.remove(0); 
			  green.add(origin);
			  maxTag++;
			  origin.setTag(maxTag);
			  if(origin.getDegree() > 2) // and may not be jokers
				  current_joker = origin;		  
		  }
		  while(!green.isEmpty()){
			  Node current_node = green.remove(0);
			  for(Point p : current_node.getNeighbors()){
				  Node current_neighbor = nodes.get(p);
				  
				  if(black.contains(current_neighbor))
					  continue;
				  
				  if(current_neighbor.isTagged()){
					  if(current_neighbor.getTag() == current_node.getTag()){
						  //Backtrack and return cycle
						  ArrayList<Point> cycle = new ArrayList<Point>();
						  backtrack(current_node,cycle,nodes);
						  return cycle;
					  } // else joker already added to white, nothing to do
				  } else {
					  if(current_neighbor.getDegree() > 2){ // found a new joker
						  if(current_joker == null){ // No current joker, use the new one
							  current_joker = current_neighbor;
							  current_neighbor.setTag(current_node.getTag());
							  green.add(current_neighbor);
						  } else { // Already a current joker, add the new one to the waiting jokers list
							  maxTag++;
							  current_neighbor.setTag(maxTag);
							  white.add(current_neighbor);
						  }
					  } else { // Found a new cycle acceptable node
						  current_neighbor.setTag(current_node.getTag());
						  green.add(current_neighbor);
					  }
				  }
				  vertices.remove(current_neighbor);
			  }
			  black.add(current_node);
		  }
	  }
	  
	  return null;
  }
  
  public void backtrack(Node n, ArrayList<Point> p, HashMap<Point,Node> nodes){
	 for(Point neighbor_point : n.getNeighbors()){
		 if(!p.contains(neighbor_point)){
			 p.add(neighbor_point);
			 Node neighbor = nodes.get(neighbor_point);
			 if(neighbor.getDegree() == 2)
				 backtrack(neighbor,p,nodes);
		 }
			 
	 }
  }
  
}
