package tests;

import org.junit.jupiter.api.Test;

import code.src.Parser;

/**
 * These test only run the code, to check if the tests passed check the output
 * @author Karl Bennett
 *
 */
class Stage2Tests {

	@Test
	void testPasses() {
		Parser.main(new String[] {"data/s2_simple.prog","data/s2_full.prog"});
	}
	
	@Test
	void testFails() {
		Parser.main(new String[] {"data/s2_bad1.prog","data/s2_bad2.prog","data/s2_bad3.prog","data/s2_bad4.prog","data/s2_bad5.prog","data/s2_bad6.prog","data/s2_bad7.prog"});
	}

}
