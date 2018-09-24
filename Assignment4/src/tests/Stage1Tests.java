package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code.src.Parser;

class Stage1Tests {

	@Test
	void testPasses() {
		Parser.main(new String[] {"data/s1_simple.prog","data/s1_full.prog"});
	}
	
	@Test
	void testFails() {
		Parser.main(new String[] {"data/s1_bad1.prog","data/s1_bad2.prog","data/s1_bad3.prog","data/s1_bad4.prog","data/s1_bad5.prog"});
	}

}
