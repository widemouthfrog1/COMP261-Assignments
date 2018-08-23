
public class AStarNode implements Comparable<AStarNode>{
	public Node node, previous;
	public double costFromStart, estimatedCostToGoal;
	public AStarNode(Node node, Node previous, double costFromStart, double estimatedCostToGoal) {
		this.node = node;
		this.previous = previous;
		this.costFromStart = costFromStart;
		this.estimatedCostToGoal = estimatedCostToGoal;
	}
	@Override
	public int compareTo(AStarNode node) {
		return (int)(this.estimatedCostToGoal -node.estimatedCostToGoal);
	}
	
}
