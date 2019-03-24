/**
 * 
 */
package top.geomatics.gazetteer.lucene;

import java.util.List;

/**
 * @author whudyj
 *
 */
public class TestSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list = LuceneUtil.search("上塘农贸建材市场");
		for (String str : list) {
			System.out.println(str);
		}
	}

}
