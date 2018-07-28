import java.util.ArrayList;
import java.util.Stack;

public class Trie<T> {
	private TrieNode<T> head;
	Trie(){
		head = new TrieNode<T>('\0');
	}
	
	public void add(String name, T object) {
		TrieNode<T> pointer = head;
		for(int i = 0; i < name.length(); i++) {
			char letter = name.charAt(i);
			if(!pointer.contains(letter)) {
				pointer.addChild(letter);
			}
			pointer = pointer.getChild(letter);
		}
		pointer.add(object);
	}
	
	public ArrayList<T> get(String input){
		TrieNode<T> pointer = head;
		for(int i = 0; i < input.length(); i++) {
			char letter = input.charAt(i);
			if(!pointer.contains(letter)) {
				return null;
			}
			pointer = pointer.getChild(letter);
		}
		ArrayList<T> matches = new ArrayList<T>();
		if(!pointer.getObjects().isEmpty()) {//if there is an exact match
			matches.addAll(pointer.getObjects());
			return matches;
		}
		
		//Depth first traversal:
		Stack<TrieNode<T>> stack = new Stack<TrieNode<T>>();
		ArrayList<TrieNode<T>> children = pointer.getChildren();
		for(int i = children.size()-1; i >= 0; i--) {//add to stack in reverse order to go down left-most side of trie
			stack.add(children.get(i));
		}
		while(!stack.isEmpty()) {
			TrieNode<T> node = stack.pop();
			
			//add objects of current node to matches
			matches.addAll(node.getObjects());
			
			//add to stack in reverse order to go down left-most side of trie
			children = node.getChildren();
			for(int i = children.size()-1; i >= 0; i--) {
				stack.add(children.get(i));
			}
		}
		return matches;
	}

	public void clear() {
		head = new TrieNode<T>('\0');
	}
}
