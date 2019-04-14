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
		List<WordEntry> list = seg.segment("广东省深圳市龙华区观湖街道樟坑径社区上围村1204T0055栋整套");
		for (WordEntry entry : list) {
			System.out.println(entry.getName());
		}
	}

}
