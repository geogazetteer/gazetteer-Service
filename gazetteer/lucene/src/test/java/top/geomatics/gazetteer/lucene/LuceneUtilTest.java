package top.geomatics.gazetteer.lucene;

import java.util.List;

import org.junit.Test;

import top.geomatics.gazetteer.model.SimpleAddressRow;

public class LuceneUtilTest {

	@Test
	public void testSearchString() {
		List<SimpleAddressRow> rows = LuceneUtil.search("广东省深圳市龙华区观湖街道润城社区求知路8号工商银行");
		System.out.println(rows.size());
		for (SimpleAddressRow row : rows) {
			System.out.println(row.getAddress());
		}
	}

	@Test
	public void testSearchStringInt() {
		List<SimpleAddressRow> rows = LuceneUtil.search("银行", 1000000);
//		for (SimpleAddressRow row : rows) {
//			System.out.println(row.getAddress());
//		}
	}

	@Test
	public void testSearchByPage() {
	}

	@Test
	public void testSearchByPinyin() {
	}

}
