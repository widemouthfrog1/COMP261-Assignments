
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

	public void clear() {
		head = new TrieNode<T>('\0');
	}
}
