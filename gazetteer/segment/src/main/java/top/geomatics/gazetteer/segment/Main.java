/**
 * 
 */
package top.geomatics.gazetteer.segment;

import java.util.List;

/**
 * @author whudyj
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordSegmenter seg = new WordSegmenter();
		List<WordEntry> list = seg.segment("深圳市龙华区龙华街道清湖社区硅谷动力清湖园A3栋厂房第3层B区");
		for (WordEntry entry : list) {
			System.out.println(entry.getName());
			System.out.println(entry.getNature());
		}
	}

}
