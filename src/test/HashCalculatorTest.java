package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import serialisation.Expose;
import chat.checksum.HashCalculator;

/**
 * Tests für HashCalculator
 * 
 * @author Daniel Abrecht
 */
public class HashCalculatorTest {

	/**
	 * Checksummenberechnung testen
	 */
	@Test
	public void test() {
		Object a = new Object(){
			@Expose(position=0)
			Integer a = 123;
			@Expose(position=1)
			Integer b = 345;
		};
		Object b = new Object(){
			@Expose(position=0)
			Integer a = 543;
			@Expose(position=1)
			Integer b = 345;
		};
		assertTrue(HashCalculator.calcHash(12345)==HashCalculator.calcHash(12345));
		assertFalse(HashCalculator.calcHash(12345)==HashCalculator.calcHash(54321));
		assertTrue(HashCalculator.calcHash(a)==HashCalculator.calcHash(a));
		assertFalse(HashCalculator.calcHash(a)==HashCalculator.calcHash(b));
	}

}
