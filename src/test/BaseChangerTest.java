package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chat.resources.BaseChanger;

public class BaseChangerTest {

	String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	BaseChanger baseChanger;

	@Before
	public void setup() {
		baseChanger = new BaseChanger();
	}

	@After
	public void cleanup() {
		baseChanger = null;
	}

	@Test
	public void encodeTest() {
		String result = baseChanger.encode(base64, "Hello World!");
		System.out.println(result);
	}

}
