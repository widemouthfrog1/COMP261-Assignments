
public class AStarNode implements Comparable<AStarNode>{
	public Node node, previous;
	private double costFromStart, estimatedCostToGoal;
	public AStarNode(Node node, Node previous, double costFromStart, double estimatedCostToGoal) {
		this.node = node;
		this.previous = previous;
		this.setCostFromStart(costFromStart);
		this.estimatedCostToGoal = estimatedCostToGoal;
	}
	@Override
	public int compareTo(AStarNode node) {
		if(this.estimatedCostToGoal < node.estimatedCostToGoal) {
			return -1;
		}
		if(this.estimatedCostToGoal > node.estimatedCostToGoal) {
			return 1;
		}
		return 0;//(int)(this.estimatedCostToGoal-node.estimatedCostToGoal);
	}
	public double getCostFromStart() {
		return costFromStart;
	}
	public void setCostFromStart(double costFromStart) {
		this.costFromStart = costFromStart;
	}
	public double getEstimatedCostToGoal() {
		return estimatedCostToGoal;
	}
	
}
