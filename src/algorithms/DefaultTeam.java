package algorithms;

import java.awt.Point;
import java.util.ArrayList;

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
}
