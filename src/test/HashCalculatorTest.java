package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import serialisation.Expose;
import chat.resources.HashCalculator;

public class HashCalculatorTest {

	private HashCalculator hc;
	
	@Before
	public void setUp() throws Exception {
		hc = new HashCalculator();
	}

	@After
	public void tearDown() throws Exception {
		hc = null;
	}

	@Test
	public void test() {
		Object a = new Object(){
			@Expose
			Integer a = 123;
			@Expose
			Integer b = 345;
		};
		Object b = new Object(){
			@Expose
			Integer a = 543;
			@Expose
			Integer b = 345;
		};
		assertTrue(Arrays.equals(hc.calcHash(12345), hc.calcHash(12345)));
		assertFalse(Arrays.equals(hc.calcHash(12345), hc.calcHash(54321)));
		assertTrue(Arrays.equals(hc.calcHash(a), hc.calcHash(a)));
		assertFalse(Arrays.equals(hc.calcHash(a), hc.calcHash(b)));
	}

}
