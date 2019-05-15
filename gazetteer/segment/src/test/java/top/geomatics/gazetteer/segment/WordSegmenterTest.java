package top.geomatics.gazetteer.segment;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.junit.Test;

public class WordSegmenterTest {

	@Test
	public void testSegmentStringInt() {
		String text = "广东省深圳市龙华区民治街道龙塘社区上塘农贸建材市场L25号铁皮房";
		Result result = WordSegmenter.segment(text, 2);
		for (Term term : result.getTerms()) {
			System.out.println(term.getName());
		}
	}

	@Test
	public void testGetStreet() {
	}

	@Test
	public void testGetCommunity() {
	}

	@Test
	public void testHouseNumberSegment() {
	}

	@Test
	public void testSegmentString() {
	}

}
