/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.Locale;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.JaroWinklerDistance;

/**
 * @author whudyj
 *
 */
public class AddressSimilarity {
	private static CosineDistance cosDistance = new CosineDistance();
	private static JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
	private static FuzzyScore fuzzyScore = new FuzzyScore(Locale.CHINESE);
	private static JaroWinklerDistance jaroDistance = new JaroWinklerDistance();
	private static SimilarityIndicator simIndicator = new SimilarityIndicator();

	public static Double cosDistance(String address1, String address2) {
		return cosDistance.apply(address1, address2);
	}

	/**
	 * 计算jaccard相似系数
	 */
	public static Double jaccard(String address1, String address2) {
		return jaccardSimilarity.apply(address1, address2);
	}

	/**
	 * 计算模糊匹配分数
	 */
	public static Integer fuzzy(String address1, String address2) {
		return fuzzyScore.fuzzyScore(address1, address2);
	}

	public static Double jaroWinkler(String address1, String address2) {
		return jaroDistance.apply(address1, address2);
	}

	public static SimilarityIndicator indicator(String address1, String address2) {
		simIndicator.cosineDistance = cosDistance.apply(address1, address2);
		simIndicator.fuzzyScore = fuzzyScore.fuzzyScore(address1, address2);
		simIndicator.jaccardSimilarity = jaccardSimilarity.apply(address1, address2);
		simIndicator.jaroWinklerDistance = jaroDistance.apply(address1, address2);
		return simIndicator;
	}

}
