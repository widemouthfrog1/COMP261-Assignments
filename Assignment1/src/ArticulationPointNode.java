import java.util.ArrayList;

public class ArticulationPointNode {
	public Node node;
	public ArticulationPointNode previous;
	public Integer count, reachBack;
	public ArrayList<Node> children = null;
	public ArticulationPointNode(Node node, Integer count, ArticulationPointNode previous) {
		this.node = node;
		this.count = count;
		this.previous = previous;
		//this.setChildren();
	}
	
	public Integer count() {
		return this.count;
	}
	
	public ArrayList<Node> getNeighbours() {
		return node.getNeighbours();
	}

	public void setChildren() {
		children = getNeighbours();
		if(previous != null)
			children.remove(previous.node);
	}
}
