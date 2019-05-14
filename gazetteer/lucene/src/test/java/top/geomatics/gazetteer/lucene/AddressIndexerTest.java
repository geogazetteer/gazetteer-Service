package top.geomatics.gazetteer.lucene;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressIndexerTest {

	@Test
	public void testUpdateIndex() {
		// AddressIndexer.updateIndex();
	}

	@Test
	public void testToPinyin() {
		String hanziString = "汉字";
		assertTrue(AddressIndexer.toPinyin(hanziString).compareToIgnoreCase("hanzi") == 0);
	}

	@Test
	public void testHandleSpecialChar() {

		String hanziString = AddressIndexer.handleSpecialChar("汉字。（）12，——~-");
		assertTrue(hanziString.compareToIgnoreCase("汉字") == 0);
	}

}
