package code;

import java.util.HashMap;
import java.util.Map;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {
	Map<Character, String> codes = new HashMap<Character, String>();
	Map<String, Character> characters = new HashMap<String, Character>();
	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		fillHashMap(text);
	}
	
	public void fillHashMap(String text) {
		Bag<Character> occurances = new Bag<Character>();
		for(int i = 0; i < text.length(); i++) {
			occurances.add(text.charAt(i));
		}
		
		Tree<Character> tree = new Tree<Character>(occurances);
		System.out.println(tree.toString());
		this.codes = tree.getCodeMap();
		for(Character character : this.codes.keySet()) {
			this.characters.put(this.codes.get(character), character);
		}
		
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		char[] output = new char[text.length()*8];
		int k = 0;
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			for(int j = 0; j < codes.get(c).length(); j++) {
				String code = this.codes.get(c);
				output[k] = code.charAt(j);
				k++;
			}
		}
		return new String(output).trim();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		char[] text = new char[encoded.length()*8];
		char[] code = new char[21];
		int j = 0;
		int k = 0;
		for(int i = 0; i < encoded.length(); i++) {
			code[j] = encoded.charAt(i);
			j++;
			if(characters.containsKey(new String(code).trim())) {
				text[k++] = characters.get(new String(code).trim());
				j = 0;
				code = new char[21];
			}
		}
		return new String(text).trim();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}
}
