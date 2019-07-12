package top.geomatics.gazetteer.utilities.address;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChineseNumberTest {

	@Test
	public void testConvert() {
		String s = "珞喻路一百二十九号";
		String s2 = ChineseNumber.convert(s);
		assertTrue(s2.compareToIgnoreCase("珞喻路129号")==0);
	}

}
