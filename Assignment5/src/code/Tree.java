package code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Tree<T> {

	private int ID = -1;
	private T value = null;
	private int occurances;
	private Tree<T> left = null;
	private Tree<T> right = null;
	private Tree<T> parent = null;

	public Tree(Bag<T> occurances) {
		// for the root
		ArrayList<Tree<T>> trees = new ArrayList<Tree<T>>();
		int IDs = 0;
		for (int i = 0; i < occurances.size(); i++) {
			trees.add(new Tree<T>(occurances.getKey(i), occurances.getCount(i), IDs++));
		}
		if (trees.size() == 1) {
			Tree<T> tree = trees.get(0);
			this.value = tree.value;
			return;
		}
		
		while (trees.size() != 2) {
			Tree<T> left = trees.remove(this.indexOfMinTree(trees));
			Tree<T> right = trees.remove(this.indexOfMinTree(trees));
			trees.add(new Tree<T>(IDs++, left.occurances + right.occurances, left, right));
		}
		this.left = trees.get(0);
		this.right = trees.get(1);
		this.left.parent = this;
		this.right.parent = this;
		this.occurances = left.occurances + right.occurances;
		this.ID = IDs++;
	}

	private Tree(int ID, int occurances, Tree<T> left, Tree<T> right) {
		// for intermediate nodes
		this.ID = ID;
		this.occurances = occurances;
		this.left = left;
		this.right = right;
		this.left.parent = this;
		this.right.parent = this;
	}

	private Tree(T value, int occurances, int ID) {
		// for leaf nodes
		this.occurances = occurances;
		this.value = value;
		this.ID = ID;
	}

	private int indexOfMinTree(List<Tree<T>> trees) {
		int min = Integer.MAX_VALUE;
		int minTree = -1;
		for (int i = 0; i < trees.size(); i++) {
			Tree<T> tree = trees.get(i);
			if (tree.occurances < min) {
				min = tree.occurances;
				minTree = i;
			}
		}
		return minTree;
	}

	public Map<T, String> getCodeMap() {
		Map<Tree<T>, String> allCodes = new HashMap<Tree<T>, String>();
		Map<T, String> codes = new HashMap<T, String>();
		Queue<Tree<T>> queue = new ArrayDeque<Tree<T>>();
		allCodes.put(this, "");
		queue.offer(this.left);
		queue.offer(this.right);
		while (!queue.isEmpty()) {
			Tree<T> node = queue.poll();
			if (node.parent.left == node) {
				allCodes.put(node, allCodes.get(node.parent) + "0");
			}else {
				allCodes.put(node, allCodes.get(node.parent) + "1");
			}
			
			if (node.left != null) {
				queue.offer(node.left);
			}
			if (node.right != null) {
				queue.offer(node.right);
			}
		}
		for(Tree<T> node : allCodes.keySet()) {
			if(node.value != null) {
				codes.put(node.value, allCodes.get(node));
			}
		}
		return codes;
	}

	@Override
	public String toString() {
		if (value != null) {
			return "Value: " + value + " Occurances: " + occurances;
			//return this.value.toString();
		}
		//return ""+this.occurances;
		return "Value: " + ID + " Occurances: " + occurances + " {" + left.toString() + "," + right.toString() + "}";
	}
}
