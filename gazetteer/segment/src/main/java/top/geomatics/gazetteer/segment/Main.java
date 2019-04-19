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
		
		List<String> list = WordSegmenter.segment("深圳市龙华区龙华街道清湖社区硅谷动力清湖园A3栋厂房第3层B区");
		for (String entry : list) {
			System.out.println(entry);
			
		}
	}

}
