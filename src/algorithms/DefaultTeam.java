package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class DefaultTeam {

	public ArrayList<Point> calculFVS(ArrayList<Point> points) {
		/*ArrayList<Point> fvs = new ArrayList<Point>();

		for (int i = 0; i < 5 * points.size() / 6; i++) {
			fvs.add(points.get(i));
		}

		return fvs;
*/	
		return bafnaFVS(points);

		}

	public ArrayList<Point> bafnaFVS(ArrayList<Point> points) {
		HashMap<Point, Node> nodes = initializeNodes(points);
		ArrayList<Point> cycle, fvs = new ArrayList<Point>();
		Stack<Point> stack = new Stack<>();
		Point[] temps;
		Point temp;
		
		double lambda;

		cleanup(nodes);

		while (!nodes.keySet().isEmpty()) {

			cycle = circleCheck(points); // TODO
			if (cycle.size() > 2) {
				lambda = min(cycle, nodes, true);
				for (Point p : cycle) {
					nodes.get(p).setWeight(nodes.get(p).getWeight() - lambda);
				}
			} else {
				lambda = min(cycle, nodes, false);

				for (Point p : cycle) {
					nodes.get(p).setWeight(nodes.get(p).getWeight() - lambda * (nodes.get(p).getDegree() - 1));
				}

			}

			temps = nodes.keySet().toArray(new Point[0]);

			for (int i = 0; i < temps.length; ++i) {
				temp = temps[i];

				if (nodes.get(temp).getWeight() == 0) {
					fvs.add((Point) temp.clone());
					stack.push((Point) temp.clone());
					nodes.remove(temp);
				}

			}

			cleanup(nodes);
		}

		removeInvalid(stack, fvs, points);

		return fvs;
	}

	private void removeInvalid(Stack<Point> stack, ArrayList<Point> fvs, ArrayList<Point> originalPoints) {
		Point p = null;

		while (!stack.isEmpty()) {
			p = stack.pop();
			fvs.remove(p);

			if (!Evaluation.isValide(originalPoints, fvs)) {
				fvs.add(p);
			}

		}

	}

	private double min(ArrayList<Point> cycle, HashMap<Point, Node> clones, boolean cycleDetected) {
		Double lambda = Double.MAX_VALUE;
		double tempWeight;

		if (cycleDetected) {
			for (Point p : cycle) {
				tempWeight = clones.get(p).getWeight();
				if (tempWeight < lambda) {
					lambda = tempWeight;
				}
			}

			return lambda;
		}

		for (Point p : cycle) {
			tempWeight = clones.get(p).getWeight() / (clones.get(p).getDegree() - 1);
			if (tempWeight < lambda) {
				lambda = tempWeight;
			}
		}

		return lambda;
	}

	public void cleanup(HashMap<Point, Node> nodes) {
		boolean clean;
		do {
			clean = true;

			for (Point p : nodes.keySet()) {

				Node n = nodes.get(p);

				if (n.getDegree() <= 1) {

					for (Point neighbor : n.getNeighbors()) {
						nodes.get(neighbor).removeNeighbor(p);
					}

					nodes.remove(p);
					clean = false;

					break;
				}
			}

		} while (!clean);
	}

	public ArrayList<Point> circleCheck(ArrayList<Point> points) {
		return new ArrayList<>();
	}

	private HashMap<Point, Node> initializeNodes(ArrayList<Point> points) {
		HashMap<Point, Node> nodes = new HashMap<>();

		for (Point p : points) {
			nodes.put(p, new Node(p, Evaluation.neighbor(p, points)));
		}

		return nodes;
	}
}
