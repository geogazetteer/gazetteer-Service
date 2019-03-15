/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.Locale;

import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaccardSimilarity;

/**
 * @author whudyj
 *
 */
public class AddressSimilarity {
	private static JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    private static FuzzyScore fuzzyScore = new FuzzyScore(Locale.CHINESE);

	/**
	 * 计算jaccard相似系数
	 */
	public double jaccard(String address1, String address2) {
		return jaccardSimilarity.apply(address1, address2);
	}

	/**
	 * 计算模糊匹配分数
	 */
	public Integer fuzzy(String address1, String address2) {
		return fuzzyScore.fuzzyScore(address1, address2);
	}

}
