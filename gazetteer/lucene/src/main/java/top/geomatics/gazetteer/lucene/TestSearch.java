/**
 * 
 */
package top.geomatics.gazetteer.lucene;

import java.util.List;

import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * @author whudyj
 *
 */
public class TestSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<SimpleAddressRow> rows = LuceneUtil.search("上塘农贸建材市场 库坑凹背村", 10000);
		long count = 0;
		for (SimpleAddressRow row : rows) {
			System.out.println(row.getId() + "\t" + row.getAddress());
			count++;
		}
		System.out.println("共找到记录\t" + count + "条");
	}

}
