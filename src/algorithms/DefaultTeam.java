package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

public class DefaultTeam {

	public ArrayList<Point> calculFVS(ArrayList<Point> points) {
		return bafnaFVS(points);
	}

	public ArrayList<Point> bafnaFVS(ArrayList<Point> points) {
		HashMap<Point, Node> nodes = initializeNodes(points);
		ArrayList<Point> cycle = null, fvs = new ArrayList<Point>();
		Stack<Point> stack = new Stack<>();
		Point[] temps;
		Point minNode;

		double lambda;

		cleanup(nodes);	
		
		while (!nodes.keySet().isEmpty()) {
			
			cycle = cycleCheck(nodes);
			
			if (cycle != null) {
				lambda = min(cycle, nodes);
				for (Point p : cycle) {
					nodes.get(p).setWeight(nodes.get(p).getWeight() - lambda);
				}
			} else {
				lambda = min(nodes);
				for (Point p : nodes.keySet()) {
					nodes.get(p).setWeight(nodes.get(p).getWeight() - lambda * (nodes.get(p).getDegree() - 1));
				}

			}

			temps = nodes.keySet().toArray(new Point[0]);

			for (int i = 0; i < temps.length; ++i) {
				minNode = temps[i];

				if (nodes.get(minNode).getWeight() == 0) {
					fvs.add(minNode);
					stack.push(minNode);
					removeMinWeightedNode(minNode, nodes);
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

	private double min(ArrayList<Point> cycle, HashMap<Point, Node> clones) {
		Double lambda = Double.MAX_VALUE;
		double tempWeight;

		for (Point p : cycle) {
			tempWeight = clones.get(p).getWeight();
			if (tempWeight < lambda) {
				lambda = tempWeight;
			}
		}

		return lambda;
	}


	private double min(HashMap<Point, Node> nodes) {
		Double lambda = Double.MAX_VALUE;
		double tempWeight;
		for (Node node : nodes.values()) {
			tempWeight = node.getWeight() / (node.getDegree() - 1);
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
			
			for (Entry<Point,Node> e : nodes.entrySet()) {
				Point p = e.getKey();
				Node n = e.getValue();

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

	private HashMap<Point, Node> initializeNodes(ArrayList<Point> points) {
		HashMap<Point, Node> nodes = new HashMap<>();

		for (Point p : points) {
			nodes.put(p, new Node(p, Evaluation.neighbor(p, points)));
		}

		return nodes;
	}

	public ArrayList<Point> cycleCheck(HashMap<Point, Node> nodes) {
		ArrayList<Node> vertices = new ArrayList<>(nodes.values()); // nodes
																	// remaining
		ArrayList<Node> green = new ArrayList<>(); // cycle acceptable nodes to
													// visit next
		ArrayList<Node> black = new ArrayList<>(); // already visited nodes
		ArrayList<Node> white = new ArrayList<>(); // waiting jokers

		for (Node n : vertices)
			n.resetTag(); // reset tags

		int maxTag = -1;
		Node current_joker;

		while (!white.isEmpty() || !vertices.isEmpty()) {
			current_joker = null;
			if (!white.isEmpty()) { // prefer waiting jokers over random points
				Node joker = white.remove(0);
				green.add(joker);
				current_joker = joker;
			} else { // remaining random points are actually in a different
						// subgraph (or first iteration)
				Node origin = vertices.remove(0);
				green.add(origin);
				maxTag++;
				origin.setTag(maxTag);
				if (origin.getDegree() > 2) // and may not be jokers
					current_joker = origin;
			}
			
			while (!green.isEmpty()) {
				Node current_node = green.remove(0);

				for (Point p : current_node.getNeighbors()) {
					Node current_neighbor = nodes.get(p);

					if (black.contains(current_neighbor))
						continue;

					if (current_neighbor.isTagged()) {
						if (current_neighbor.getTag() == current_node.getTag()) {
							// Backtrack and return cycle
							ArrayList<Point> cycle = new ArrayList<Point>();
							backtrack(current_node, cycle, nodes);
							return cycle;
						} // else joker already added to white, nothing to do
					} else {
						if (current_neighbor.getDegree() > 2) { // found a new
																// joker
							if (current_joker == null) { // No current joker,
															// use the new one
								current_joker = current_neighbor;
								current_neighbor.setTag(current_node.getTag());
								green.add(current_neighbor);
							} else { // Already a current joker, add the new one
										// to the waiting jokers list
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

	public void backtrack(Node n, ArrayList<Point> p, HashMap<Point, Node> nodes) {
		for (Point neighbor_point : n.getNeighbors()) {
			if (!p.contains(neighbor_point)) {
				p.add(neighbor_point);
				Node neighbor = nodes.get(neighbor_point);
				if (neighbor.getDegree() == 2)
					backtrack(neighbor, p, nodes);
			}

		}
	}

	private void removeMinWeightedNode(Point point, HashMap<Point, Node> nodes) {
//		nodes.remove(node);
//		
//		for (Point p: nodes.keySet()) {
//			if (p.equals(node)) {
//				continue;
//			}
//			
//			if(nodes.get(p).getNeighbors().contains(node)) {
//				nodes.get(p).getNeighbors().remove(node);
//			}
//		}
		Node node = nodes.get(point);
		for(Point neighbor_point : node.getNeighbors()){
			nodes.get(neighbor_point).removeNeighbor(point);
		}
		nodes.remove(point);
	}
}
