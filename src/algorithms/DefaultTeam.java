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
  
  public ArrayList<Point> circleCheck(ArrayList<Point> points){
	  return null;
  }
  
}
