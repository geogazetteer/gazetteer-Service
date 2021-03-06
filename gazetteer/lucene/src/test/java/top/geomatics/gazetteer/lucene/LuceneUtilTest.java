package top.geomatics.gazetteer.lucene;

import java.util.List;

import org.junit.Test;

import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

public class LuceneUtilTest {

	@Test
	public void testSearchString() {
		String queryString = "广东省深圳市龙华区观湖街道润城社区求知路8号工商银行";
		List<SimpleAddressRow> rows = LuceneUtil.search(queryString);
		System.out.println("----------------搜索结果如下------------------");
		System.out.println("搜索关键词： " + queryString);
		System.out.println("总数： " + rows.size());
		for (SimpleAddressRow row : rows) {
			//System.out.println(row.getAddress());
		}
		System.out.println("=========================================");
	}

	@Test
	public void testSearchStringInt() {
		String queryString = "工商银行";
		List<SimpleAddressRow> rows = LuceneUtil.search(queryString,10000);
		System.out.println("----------------搜索结果如下------------------");
		System.out.println("搜索关键词： " + queryString);
		System.out.println("总数： " + rows.size());
		for (SimpleAddressRow row : rows) {
			System.out.println(row.getAddress());
		}
		System.out.println("=========================================");
	}

	@Test
	public void testSearchPage() {
		String queryString = "银行";
		long count = LuceneUtil.getCount(queryString);
		int pageSize = 10;
		int pages = (int) (count / pageSize);
		if (count % pageSize != 0) {
			pages++;
		}
		System.out.println("----------------搜索结果如下------------------");
		System.out.println("搜索关键词： " + queryString);
		System.out.println("总数： " + count + "\t页数： " + pages);
		for (int i = 1; i < pages + 1; i++) {
			List<SimpleAddressRow> rows = LuceneUtil.searchByPage(queryString, i, pageSize);
			System.out.println("当前页是：" + i);
			for (SimpleAddressRow row : rows) {
				System.out.println(row.getId() + "\t" + row.getAddress());
			}
			System.out.println("======================");
		}
	}

}
