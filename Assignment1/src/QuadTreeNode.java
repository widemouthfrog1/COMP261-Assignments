import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JTextArea;

public class QuadTreeNode<T> {
	public QuadTreeNode<T> TR = null, TL = null, BR = null, BL = null;
	public ArrayList<QuadTreeNode<T>> children = new ArrayList<QuadTreeNode<T>>();
	public T object = null;
	public Location location;
	private QuadTreeNode<T> parent;
	private double top, bottom, left, right;
	private int stackNumber = 0;
	private boolean highlight = false;
	
	QuadTreeNode(QuadTreeNode<T> parent, Location BL, Location TR){
		this.parent = parent;
		this.bottom = BL.y;
		this.left = BL.x;
		this.top = TR.y;
		this.right = TR.x;
	}
	
	public void draw(Graphics g, Location origin, double scale, JTextArea jTextArea) {
		//jTextArea.setText("Top: " + String.valueOf(this.top) + " Bottom: " + String.valueOf(this.bottom));
		
		g.drawRect(new Location(this.left, this.top).asPoint(origin, scale).x, new Location(this.left,this.top).asPoint(origin, scale).y , this.absolute(new Location(this.right, this.bottom).asPoint(origin, scale).x - new Location(this.left, this.top).asPoint(origin, scale).x) , this.absolute((new Location(this.right, this.bottom).asPoint(origin, scale).y - new Location(this.left, this.top).asPoint(origin, scale).y)));

		for(QuadTreeNode<T> node : this.getAll()) {
			if(node.highlight) {
				g.setColor(Color.yellow);
			}
			g.drawRect(new Location(node.left, node.top).asPoint(origin, scale).x, new Location(node.left,node.top).asPoint(origin, scale).y , node.absolute(new Location(node.right, node.bottom).asPoint(origin, scale).x - new Location(node.left, node.top).asPoint(origin, scale).x) , node.absolute((new Location(node.right, node.bottom).asPoint(origin, scale).y - new Location(node.left, node.top).asPoint(origin, scale).y)));
			g.setColor(Color.black);
		}
	}
	
	private int absolute(int a) {
		if(a < 0) {
			return -a;
		}
		return a;
	}
	
	public void add(T object, Location loc) {
		if(this.TR == null) {
			if(this.object != null) {
				this.split();
				if(this.TR.within(loc)) {
					TR.add(object, loc);
				}
				if(this.TL.within(loc)) {
					TL.add(object, loc);
				}
				if(this.BR.within(loc)) {
					BR.add(object, loc);
				}
				if(this.BL.within(loc)) {
					BL.add(object, loc);
				}
				
				if(this.TR.within(this.location)) {
					TR.add(this.object, this.location);
				}
				if(this.TL.within(this.location)) {
					TL.add(this.object, this.location);
				}
				if(this.BR.within(this.location)) {
					BR.add(this.object, this.location);
				}
				if(this.BL.within(this.location)) {
					BL.add(this.object, this.location);
				}
				this.object = null;
				this.location = null;
			}else {
				this.object = object;
				this.location = loc;
			}
		}else {
			if(this.TR.within(loc)) {
				TR.add(object, loc);
			}
			if(this.TL.within(loc)) {
				TL.add(object, loc);
			}
			if(this.BR.within(loc)) {
				BR.add(object, loc);
			}
			if(this.BL.within(loc)) {
				BL.add(object, loc);
			}
		}
	}
	public void highlightNode(JTextArea jTextArea) {
		//jTextArea.setText(this.TL.BR.TL.BL.BL.TL.TL.BL.location.toString());
		this.TL.BR.TL.BL.BL.TL.TL.BL.highlight = true;
	}
	public boolean within(Location loc) {
		return loc.x >= this.left && loc.x < this.right && loc.y >= this.bottom && loc.y < this.top;//could be exactly on edge but don't want more than one node containing the object
	}
	
	public void split() {
		this.TR = new QuadTreeNode<T>(this, new Location((this.right+this.left)/2, (this.top + this.bottom)/2), new Location(this.right, this.top));
		this.TL = new QuadTreeNode<T>(this, new Location(this.left, (this.top + this.bottom)/2), new Location((this.right+this.left)/2, this.top));
		this.BR = new QuadTreeNode<T>(this, new Location((this.right+this.left)/2, this.bottom), new Location(this.right, (this.top + this.bottom)/2));
		this.BL = new QuadTreeNode<T>(this, new Location(this.left, this.bottom), new Location((this.right+this.left)/2, (this.top + this.bottom)/2));
		this.children.add(TR);
		this.children.add(TL);
		this.children.add(BR);
		this.children.add(BL);
	}
	
	private ArrayList<QuadTreeNode<T>> getAll(){
		ArrayList<QuadTreeNode<T>> nodes = new ArrayList<QuadTreeNode<T>>();
		if(this.BL == null) {
			nodes.add(this);
			return nodes;
		}
		nodes.addAll(this.BL.getAll());
		nodes.addAll(this.BR.getAll());
		nodes.addAll(this.TL.getAll());
		nodes.addAll(this.TR.getAll());
		return nodes;
	}
	
	public T getNearest(Location loc) {
		
		QuadTreeNode<T> closestNode = this;
		//find the node representing the area that was clicked
		while(true) {
			boolean nodeUnchanged = true;
			for(QuadTreeNode<T> node : closestNode.children) {
				if(node.within(loc)) {
					closestNode = node;
					nodeUnchanged = false;
				}
			}
			if(nodeUnchanged) {
				break;
			}
		}
		//find the actual closest object from the closest node and all surrounding nodes
		Double distance = null;
		if(closestNode.object != null) {
			distance = Math.sqrt(Math.pow(loc.x-closestNode.location.x, 2) + Math.pow(loc.y-closestNode.location.y, 2));
		}
		for(QuadTreeNode<T> node : closestNode.getAllSurroundingNodes()) {
			
			for(QuadTreeNode<T> child : node.getAll()) {//node is included in node.getAll()
				if(child.object != null) {
					double newDistance = Math.sqrt(Math.pow(loc.x-child.location.x, 2) + Math.pow(loc.y-child.location.y, 2));
					if(distance == null || newDistance < distance) {
						distance = newDistance;
						closestNode = child;
					}
				}
			}
		}
		return closestNode.object;
	}
	public ArrayList<QuadTreeNode<T>> getAllSurroundingNodes(){
		ArrayList<QuadTreeNode<T>> nodes = new ArrayList<QuadTreeNode<T>>();
		nodes.add(this.getBottom());
		nodes.add(this.getBottomLeft());
		nodes.add(this.getBottomRight());
		nodes.add(this.getLeft());
		nodes.add(this.getRight());
		nodes.add(this.getTop());
		nodes.add(this.getTopLeft());
		nodes.add(this.getTopRight());
		
		//all of these functions (e.g. this.getBottom()) could return null
		while(nodes.contains(null)) {
			nodes.remove(null);
		}
		
		return nodes;
	}
	
	public QuadTreeNode<T> getTop(){
		//Easy cases:
		if(this.parent == null) {
			//the head of the QuadTree has no parent and also has no node above it
			return null;
		}
		if(this.bottom()) {
			//the node above this node is stored within the same node as this one
			if(this.right()) {
				return this.parent.TR;
			}else {
				return this.parent.TL;
			}
		}
		//Hard case:
		Stack<QuadTreeNode<T>> pointerStack = new Stack<QuadTreeNode<T>>();
		QuadTreeNode<T> pointer = this;
		pointerStack.push(pointer);
		
		while(pointer.top()) {
			pointer = pointer.parent;
			if(pointer == null) {
				//we're at the top so there isn't a node above
				return null;
			}
			pointerStack.push(pointer);//so we can get back down no matter how far up we go
		}
		if(pointerStack.pop().right()) {
			pointer = pointer.parent.TR;
		}else {
			pointer = pointer.parent.TL;
		}
		while(!pointerStack.isEmpty()) {
			if(pointerStack.pop().right()) {
				if(pointer.BR == null) {
					break;
				}
				pointer = pointer.BR;
			}else {
				if(pointer.BL == null) {
					break;
				}
				pointer = pointer.BL;
			}
		}
		pointer.stackNumber = pointerStack.size();
		return pointer;
	}
	
	public QuadTreeNode<T> getBottom(){
		if(this.parent == null) {
			//the head of the QuadTree has no parent and also has no node below it
			return null;
		}
		if(this.top()) {
			//the node below this node is stored within the same node as this one
			if(this.right()) {
				return this.parent.BR;
			}else {
				return this.parent.BL;
			}
		}
		
		Stack<QuadTreeNode<T>> pointerStack = new Stack<QuadTreeNode<T>>();
		QuadTreeNode<T> pointer = this;
		pointerStack.push(pointer);
		
		while(pointer.bottom()) {
			pointer = pointer.parent;
			if(pointer == null) {
				//we're at the bottom so there isn't a node below
				return null;
			}
			pointerStack.push(pointer);//so we can get back down no matter how far up we go
		}
		if(pointerStack.pop().right()) {
			pointer = pointer.parent.BR;
		}else {
			pointer = pointer.parent.BL;
		}
		while(!pointerStack.isEmpty()) {
			if(pointerStack.pop().right()) {
				if(pointer.TR == null) {
					break;
				}
				pointer = pointer.TR;
			}else {
				if(pointer.TL == null) {
					break;
				}
				pointer = pointer.TL;
			}
		}
		pointer.stackNumber = pointerStack.size();
		return pointer;
	}
	
	public QuadTreeNode<T> getLeft(){
		if(this.parent == null) {
			//the head of the QuadTree has no parent and also has no node to the left of it
			return null;
		}
		if(this.right()) {
			//the node to the left of this node is stored within the same node as this one
			if(this.top()) {
				return this.parent.TL;
			}else {
				return this.parent.BL;
			}
		}
		
		Stack<QuadTreeNode<T>> pointerStack = new Stack<QuadTreeNode<T>>();
		QuadTreeNode<T> pointer = this;
		pointerStack.push(pointer);
		
		while(pointer.left()) {
			pointer = pointer.parent;
			if(pointer == null) {
				//we're at the left edge so there isn't a node below
				return null;
			}
			pointerStack.push(pointer);//so we can get back down no matter how far up we go
		}
		if(pointerStack.pop().top()) {
			pointer = pointer.parent.TL;
		}else {
			pointer = pointer.parent.BL;
		}
		while(!pointerStack.isEmpty()) {
			if(pointerStack.pop().top()) {
				if(pointer.TR == null) {
					break;
				}
				pointer = pointer.TR;
			}else {
				if(pointer.BR == null) {
					break;
				}
				pointer = pointer.BR;
			}
		}
		pointer.stackNumber = pointerStack.size();
		return pointer;
	}
	
	public QuadTreeNode<T> getRight(){
		if(this.parent == null) {
			//the head of the QuadTree has no parent and also has no node to the right of it
			return null;
		}
		
		if(this.left()) {
			//the node to the right of this node is stored within the same node as this one
			if(this.top()) {
				return this.parent.TR;
			}else {
				return this.parent.BR;
			}
		}
		
		Stack<QuadTreeNode<T>> pointerStack = new Stack<QuadTreeNode<T>>();
		QuadTreeNode<T> pointer = this;
		pointerStack.push(pointer);
		
		while(pointer.right()) {
			pointer = pointer.parent;
			if(pointer == null) {
				//we're at the right edge so there isn't a node below
				return null;
			}
			pointerStack.push(pointer);//so we can get back down no matter how far up we go
		}
		if(pointerStack.pop().top()) {
			pointer = pointer.parent.TR;
		}else {
			pointer = pointer.parent.BR;
		}
		while(!pointerStack.isEmpty()) {
			if(pointerStack.pop().top()) {
				if(pointer.TL == null) {
					break;
				}
				pointer = pointer.TL;
			}else {
				if(pointer.BL == null) {
					break;
				}
				pointer = pointer.BL;
			}
		}
		pointer.stackNumber = pointerStack.size();
		return pointer;
	}
	
	public QuadTreeNode<T> getTopRight(){
		if(this.parent == null || this.getTop() == null) {
			//the head of the QuadTree has no parent and also has no node to the top-right of it
			//if there is no node above this node there won't be a node to the top-right
			return null;
		}
		QuadTreeNode<T> pointer = this.getTop();
		int stackNumber = pointer.stackNumber;
		if(stackNumber == 0) {
			//if there's nothing left on the stack then getTop() returned a node on the same level as this, which is correct
			return this.getTop().getRight();
		}
		pointer = pointer.getRight();
		while(stackNumber != 0) {
			//the only time this happens is when getTop() yields a node higher in the QuadTree
			//than this and if getTop().getRight() has child nodes we need to 
			//get the bottom-left-most node going through all its children
			//(if there are no child nodes, the if statement below will keep the pointer the same)
			if(pointer.BL != null) {
				pointer = pointer.BL;
			}
			stackNumber--;
		}
		
		return pointer;
	}
	
	public QuadTreeNode<T> getTopLeft(){
		if(this.parent == null || this.getTop() == null) {
			//the head of the QuadTree has no parent and also has no node to the top-left of it
			//if there is no node above this node there won't be a node to the top-left
			return null;
		}
		QuadTreeNode<T> pointer = this.getTop();
		int stackNumber = pointer.stackNumber;
		if(stackNumber == 0) {
			return this.getTop().getLeft();
		}
		while(stackNumber != 0) {
			if(pointer.BR != null) {
				pointer = pointer.BR;
			}
			stackNumber--;
		}
		return pointer;
	}
	
	public QuadTreeNode<T> getBottomRight(){
		if(this.parent == null || this.getBottom() == null) {
			//the head of the QuadTree has no parent and also has no node to the bottom-right of it
			//if there is no node below this node there won't be a node to the bottom-right
			return null;
		}
		QuadTreeNode<T> pointer = this.getBottom();
		int stackNumber = pointer.stackNumber;
		if(stackNumber == 0) {
			return this.getBottom().getRight();
		}
		while(stackNumber != 0) {
			if(pointer.TL != null) {
				pointer = pointer.TL;
			}
			stackNumber--;
		}
		return pointer;
	}
	
	public QuadTreeNode<T> getBottomLeft(){
		if(this.parent == null || this.getBottom() == null) {
			//the head of the QuadTree has no parent and also has no node to the bottom-left of it
			//if there is no node below this node there won't be a node to the bottom-left
			return null;
		}
		QuadTreeNode<T> pointer = this.getBottom();
		int stackNumber = pointer.stackNumber;
		if(stackNumber == 0) {
			return this.getBottom().getLeft();
		}
		while(stackNumber != 0) {
			if(pointer.TR != null) {
				pointer = pointer.TR;
			}
			stackNumber--;
		}
		return pointer;
	}

	public boolean top() {
		return this.equals(this.parent.TL) || this.equals(this.parent.TR);
	}
	
	public boolean bottom() {
		return this.equals(this.parent.BL) || this.equals(this.parent.BR);
	}
	
	public boolean right() {
		return this.equals(this.parent.BR) || this.equals(this.parent.TR);
	}
	
	public boolean left() {
		return this.equals(this.parent.BL) || this.equals(this.parent.TL);
	}
}
