package code;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class Tests {

	@Test
	void testMap() {
		List<Integer> matchTable = new ArrayList<Integer>();
		matchTable.add(-1);
		matchTable.add(0);
		matchTable.add(0);
		matchTable.add(0);
		matchTable.add(1);
		matchTable.add(2);
		matchTable.add(3);
		matchTable.add(0);
		assertEquals(matchTable, new KMP("abcabcde", "").matchTable);
	}
	
	@Test
	void testMapOverlap() {
		List<Integer> matchTable = new ArrayList<Integer>();
		matchTable.add(-1);
		matchTable.add(0);
		matchTable.add(0);
		matchTable.add(1);
		matchTable.add(2);
		matchTable.add(1);
		matchTable.add(2);
		assertEquals(matchTable,new KMP("abababc", "").matchTable);
	}
	
	@Test
	void testKMPSearch() {
		assertEquals(3, new KMP("abcd", "abcabcd").index);
		assertEquals(-1, new KMP("abcd", "abcabc").index);
		assertEquals(-1, new KMP("abcd", "abcabce").index);
	}
	
	@Test
	void testBruteForceSearch() {
		assertEquals(3, new BruteForceSearch("abcd", "abcabcd").index);
		assertEquals(-1, new BruteForceSearch("abcd", "abcabc").index);
		assertEquals(-1, new BruteForceSearch("abcd", "abcabce").index);
	}
	
	@Test
	void testHuffmanEncoding() {
		String text = "aaaabbb c ef g by hi aaaaa";
		HuffmanCoding hc = new HuffmanCoding(text);
		String encoded = "111111111011011010110010011001100000100010110100100100111000011111111111";
		assertEquals(encoded,hc.encode(text));
		assertEquals(text,hc.decode(encoded));
	}

}
