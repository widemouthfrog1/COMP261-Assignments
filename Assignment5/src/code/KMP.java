package code;

import java.util.ArrayList;
import java.util.List;

/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {
	public List<Integer> matchTable = new ArrayList<Integer>();
	public int index = -1;
	public int steps = 0;
	public int stepsWithoutMap = 0;

	public KMP(String pattern, String text) {
		fillMap(pattern);
		index = search(pattern, text);
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		int i = 0;
		while(i < text.length()) {
			for(int j = 0; j < pattern.length(); j++) {
				steps += 2;
				stepsWithoutMap += 2;
				if(i+j >= text.length()) {
					return -1;
				}
				steps++;
				stepsWithoutMap++;
				if(text.charAt(i+j) != pattern.charAt(j)) {
					i = i+j-matchTable.get(j);
					break;
				}
				steps++;
				stepsWithoutMap++;
				if(j == pattern.length()-1) {
					return i;
				}
			}
			stepsWithoutMap++;
			steps++;
		}
		return -1;
	}

	public void fillMap(String pattern) {
		if (pattern == null || pattern.length() == 0) {
			return;
		}
		//these values are always the same
		matchTable.add(-1);
		matchTable.add(0);
		steps += 4; //for two comparisons and 2 additions
		
		for (int i = 1; i < pattern.length()-1; i++) {
			char[] prefix;
			int matchValue = 0;
			char[] suffix;
			for (int j = 0; j < (i + 1) / 2; j++) {
				//empty the arrays and make them have just enough size to hold the suffix/prefix of the current length
				suffix = new char[j+1];
				prefix = new char[j+1];
				for(int k = 0; k <= j; k++) {
					//fill prefix
					prefix[k] = pattern.charAt(k);
					steps++;
				}
				for (int k = 0; k <= j; k++) {
					//fill suffix
					suffix[k] = pattern.charAt(i - j + k);
					steps++;
				}
				if (java.util.Arrays.equals(prefix, suffix)) {
					//set the value to go into the matchTable to be the length of the suffix/prefix
					matchValue = j+1;
				}
				steps += j + 3; //one for each assignment and j+1 for the equals method

			}
			matchTable.add(matchValue);
			steps++; //for the add
		}
	}

}
