import java.awt.Graphics;

import javax.swing.JTextArea;

public class QuadTree<T> {
	QuadTreeNode<T> head;
	QuadTree(){}
	
	public void setSize(Location BL, Location TR) {
		head = new QuadTreeNode<T>(null, BL, TR);
	}
	
	public void add(T object, Location loc) {
		head.add(object, loc);
	}
	
	public void draw(Graphics g, Location origin, double scale, JTextArea jTextArea) {
		head.draw(g, origin, scale, jTextArea);
		head.highlightNode(jTextArea);
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
