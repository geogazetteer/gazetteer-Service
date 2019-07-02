package top.geomatics.gazetteer.utilities.address;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressProcessorTest {

	@Test
	public void testFullToHalf() {
		String s = "广东省，深圳市，龙华区民治街道１２３号梅花山庄Ａ区７栋２楼";
		System.out.println(AddressProcessor.fullToHalf(s));
	}

	@Test
	public void testChineseToNumber() {
		String s = "广东省深圳市龙华区民治街道民治社区人民路一百二十九号";
		String s2 = ChineseNumber.convert(s);
		System.out.println(s2);
		assertTrue(s2.compareToIgnoreCase("广东省深圳市龙华区民治街道民治社区人民路129号")==0);
	}

	@Test
	public void testComToSimple() {
		String s = "广东省深圳市龍華區龙华街道民治社區梅花山莊";
		System.out.println(AddressProcessor.comToSimple(s));
	}

	@Test
	public void testTransform() {
	}

	@Test
	public void testIsCoordinatesExpression() {
	}

	@Test
	public void testIsBuildingCode() {
	}

	@Test
	public void testIsSensitiveWords() {
		String s = "广东省深圳市龙华区基地组织";
		System.out.println(AddressProcessor.isSensitiveWords(s));
	}

	@Test
	public void testExchangeWords() {
	}

	@Test
	public void testAlias() {
	}

	@Test
	public void testSynonym() {
	}

	@Test
	public void testHomophone() {
	}

	@Test
	public void testGetCommunityFromBuildingCode() {
	}

}
