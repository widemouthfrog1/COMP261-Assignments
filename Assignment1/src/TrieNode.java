import java.util.ArrayList;

public class TrieNode<T> {
	private ArrayList<T> objects = new ArrayList<T>();
	public char letter;
	private ArrayList<TrieNode<T>> children = new ArrayList<TrieNode<T>>();
	TrieNode(char letter){
		this.letter = letter;
	}
	
	public void add(T object) {
		objects.add(object);
	}
	
	public void addChild(char letter) {
		children.add(new TrieNode<T>(letter));
	}
	
	public TrieNode<T> getChild(char letter){
		for(TrieNode<T> node : children) {
			if(node.letter == letter) {
				return node;
			}
		}
		return null;
	}
	
	public boolean contains(char letter) {
		for(TrieNode<T> node : children) {
			if(node.letter == letter) {
				return true;
			}
		}
		return false;
	}
}
