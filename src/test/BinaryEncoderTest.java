package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import serialisation.BinaryEncoder;
import serialisation.Deserializable;
import serialisation.Expose;
import serialisation.ObjectEncoder;

/**
 * Tests für Binary Encoder
 * 
 * @author Daniel Abrecht
 */
public class BinaryEncoderTest {
	
	private ObjectEncoder<byte[]> encoder;
	
	/**
	 * Initialisierung vor Testcase
	 */
	@Before
	public void setup(){
		encoder = new BinaryEncoder();
	}

	/**
	 * Cleanup nach Testcase
	 */
	@After
	public void cleanup(){
		encoder = null;
	}

	static class testObject {
		@Expose(position = 0)
		public int x;
		@Expose(position = 1)
		public String y;
	};
	
	/**
	 * Zu serialisierendes Objekt
	 * 
	 * @author Daniel Abrecht
	 */
	@Deserializable
	public static class testObject2 extends testObject {
		/**
		 * @author Daniel Abrecht
		 * @see Expose.TypeGetter
		 */
		public static class IntegerTypeGetter implements Expose.TypeGetter {
			@Override
			public Class<?> getType(Object o, Field f) {
				return Integer.class;
			}
		}
		@Expose(position = 0,getTypeGetterType=IntegerTypeGetter.class)
		public List<Integer> z;
	};
	
	/**
	 * Korrekte Serialisierung überprüfen
	 */
	@Test
	public void encodeTest() {
		testObject2 object = new testObject2();
		object.x = 3;
		object.y = "test";
		object.z = Arrays.asList(new Integer[]{7,6,5,-2});
		byte[] result = encoder.encode(object);
		assertArrayEquals(new byte[]{
			0, 0, 0, 3, 0, 0, 0, 4,
			0x74, 0x65, 0x73, 0x74, 0, 0, 0, 4,
			0, 0, 0, 7, 0, 0, 0, 6,
			0, 0, 0, 5, ~0, ~0, ~0, -2
		}, result);
	}

	/**
	 * Korrekte Deserialisierung überprüfen
	 */
	@Test
	public void decodeTest() {
		testObject2 result = encoder.decode(new byte[]{
			0, 0, 0, 3, 0, 0, 0, 4,
			0x74, 0x65, 0x73, 0x74, 0, 0, 0, 4,
			0, 0, 0, 7, 0, 0, 0, 6,
			0, 0, 0, 5, ~0, ~0, ~0, -2
		}, testObject2.class);
		assertEquals(result.x, 3);
		assertEquals(result.y, "test");
		assertArrayEquals(result.z.toArray(), new Integer[]{7,6,5,-2});
	}

}
