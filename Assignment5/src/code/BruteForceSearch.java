package code;

public class BruteForceSearch {
	int index = -1;
	int steps = 0;
	public BruteForceSearch(String pattern, String text) {
		index = search(pattern, text);
	}

	public int search(String pattern, String text) {
		for(int i = 0; i < text.length(); i++) {
			for(int j = 0; j < pattern.length(); j++) {
				steps += 2;
				if(i+j >= text.length()) {
					return -1;
				}
				steps++;
				if(text.charAt(i+j) != pattern.charAt(j)) {
					break;
				}
				steps++;
				if(j == pattern.length()-1) {
					return i;
				}
			}
			steps++;
		}
		return -1;
	}
}
