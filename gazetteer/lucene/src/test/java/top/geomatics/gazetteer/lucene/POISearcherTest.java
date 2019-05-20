package top.geomatics.gazetteer.lucene;

import java.util.List;

import org.junit.Test;

import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

public class POISearcherTest {

	@Test
	public void testGetCount() {
		String queryString = "肥佬餐馆";
		System.out.println("count is : " + GeoNameSearcher.getCount(queryString));
	}

	@Test
	public void testQueryStringInt() {
	}

	@Test
	public void testQueryString() {
		String queryString = "肥佬餐馆";
		List<EnterpriseRow> rows = GeoNameSearcher.query(queryString);
		for (EnterpriseRow row : rows) {
			// System.out.println(row.getName() + "\t" + row.getAddress());
		}
	}

	@Test
	public void testQueryPage() {
		String queryString = "肥佬餐馆";
		long count = GeoNameSearcher.getCount(queryString);
		int pageSize = 10;
		int pages = (int) (count / pageSize);
		if (count % pageSize != 0) {
			pages++;
		}
		for (int i = 1; i < pages + 1; i++) {
			List<EnterpriseRow> rows = GeoNameSearcher.queryPage(queryString, i, pageSize);
			System.out.println("当前页是：" + i);
			for (EnterpriseRow row : rows) {
				System.out.println(row.getName() + "\t" + row.getAddress());
			}
			System.out.println("======================");
		}

	}

	@Test
	public void testSearchStringInt() {
	}

	@Test
	public void testSearchString() {
		String queryString = "肥佬餐馆";
		List<SimpleAddressRow> rows = GeoNameSearcher.search(queryString);
		for (SimpleAddressRow row : rows) {
			// System.out.println(row.getId() + "\t" + row.getAddress());
		}
	}

}
