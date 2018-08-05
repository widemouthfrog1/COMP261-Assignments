import java.awt.Graphics;

public class QuadTree<T> {
	QuadTreeNode<T> head;
	QuadTree(){}
	
	public void setSize(Location BL, Location TR) {
		head = new QuadTreeNode<T>(null, BL, TR);
	}
	
	public void add(T object, Location loc) {
		head.add(object, loc);
	}
	
	public void draw(Graphics g, Location origin, double scale) {
		head.draw(g, origin, scale);
	}
	
	public boolean isEmpty() {
		return head == null;
	}
	
	public T getNearest(Location loc) {
		if(head.BL == null) {
			return head.object;
		}
		return head.getNearest(loc);
	}
}
