package top.geomatics.gazetteer.segment;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.junit.Test;

public class WordSegmenterTest {

	@Test
	public void testSegmentStringInt() {
		String text = "深圳市龙华区龙华街道东环一路天汇大厦B座906室";
		Result result = WordSegmenter.segment(text, 1);
		for (Term term : result.getTerms()) {
			System.out.println(term.getName());
		}
	}

	@Test
	public void testSegmentString() {
		String text = "深圳市龙华区观澜街道福城办事处桔坑路11号洋金工业园C栋四楼";
		List<String> result = WordSegmenter.segment(text);
		for (String word : result) {
			System.out.println(word);
		}
		text = "深圳市龙华区龙华街道东环一路天汇大厦B座906室";
		result = WordSegmenter.segment(text);
		for (String word : result) {
			System.out.println(word);
		}
	}

	@Test
	public void testGetStreet() {
		String text = "深圳市龙华区观澜街道福城办事处桔坑路11号洋金工业园C栋四楼";
		String resutlString = WordSegmenter.getStreet(text);
		assertTrue(resutlString.compareToIgnoreCase("观澜街道") == 0);
	}

	@Test
	public void testGetCommunity() {
		String text = "广东省深圳市龙华区龙华街道玉翠社区玉石新村242号";
		String resutlString = WordSegmenter.getCommunity(text);
		assertTrue(resutlString.compareToIgnoreCase("玉翠社区") == 0);
	}

	@Test
	public void testHouseNumberSegment() {
		String text = "广东省深圳市龙华区龙华街道玉翠社区玉石新村242号";
		List<String> resutl = WordSegmenter.houseNumberSegment(text);
		assertTrue(resutl.get(0).compareToIgnoreCase("玉石新村242号") == 0);
	}

}
