/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.Locale;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * <em>计算两个地址文本的相似度</em>
 * 
 * @author whudyj
 *
 */
public class AddressSimilarity {
	private static CosineDistance cosDistance = new CosineDistance();
	private static JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
	private static FuzzyScore fuzzyScore = new FuzzyScore(Locale.CHINESE);
	private static JaroWinklerDistance jaroDistance = new JaroWinklerDistance();
	private static SimilarityIndicator simIndicator = new SimilarityIndicator();

	/**
	 * <b>对两个地址进行预处理</b>
	 * 
	 * 
	 * @param address1 String 第一个地址，如标准地址，示例：广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋
	 * @param address2 String 第二个地址，如待比较（或查询）的地址，示例：深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2
	 * @return String[] 返回处理后的两个地址
	 */
	private static String[] preProcess(String address1, String address2) {
		String[] after = new String[2];
		after[0] = address1;
		after[1] = address2;
		// 可以认为在社区之前的地址内容是一致的，不需要计算相似度
		for (String community : IGazetteerConstant.COMMUNITY_LIST) {
			if (address1.contains(community) && address2.contains(community)) {// 在同一个社区
				// 取社区后面的地址内容比较
				after[0] = address1.substring(address1.lastIndexOf(community) + community.length());
				after[1] = address2.substring(address2.lastIndexOf(community) + community.length());
			}
		}
		return after;
	}

	/**
	 * <em>计算两个地址文本的余弦距离</em><br>
	 * 
	 * <b>余弦距离=1-相似度</b><br>
	 * <a href="https://en.wikipedia.org/wiki/Cosine_similarity">余弦距离定义</a>
	 * 
	 * @param address1 String 第一个地址，如标准地址，示例：广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋
	 * @param address2 String 第二个地址，如待比较（或查询）的地址，示例：深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2
	 * @return Double 【0,1】返回两个地址文本的余弦距离，距离越大，相似度越小
	 */
	public static Double cosDistance(String address1, String address2) {
//		String[] after = preProcess(address1, address2);
//		address1 = after[0];
//		address2=after[1];
		return (1.0 - cosDistance.apply(address1, address2));
	}

	/**
	 * <em>计算两个地址的Jaccard相似度</em><br>
	 * 
	 * <b>Jaccard相似度定义为两个字符串的交集与并集的商</b><br>
	 * <a href="https://en.wikipedia.org/wiki/Jaccard_index">Jaccard相似度定义</a>
	 * 
	 * @param address1 String 第一个地址，如标准地址，示例：广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋
	 * @param address2 String 第二个地址，如待比较（或查询）的地址，示例：深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2
	 * @return Double 【0,1】返回两个地址文本的Jaccard相似度
	 */
	public static Double jaccard(String address1, String address2) {
//		String[] after = preProcess(address1, address2);
//		address1 = after[0];
//		address2=after[1];
		return jaccardSimilarity.apply(address1, address2);
	}

	/**
	 * <em>计算两个地址的模糊匹配分数</em><br>
	 * 
	 * <b>模糊匹配分数算法常用于文本搜索。单个字符匹配加一分，字符串匹配加二分</b><br>
	 * <a href=
	 * "http://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/similarity/FuzzyScore.html">模糊匹配分数计算说明</a>
	 * 
	 * @param address1 String 第一个地址，如标准地址，示例：广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋
	 * @param address2 String 第二个地址，如待比较（或查询）的地址，示例：深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2
	 * @return Integer 返回两个地址文本的模糊匹配分数，匹配的字符数越多
	 */
	public static Integer fuzzy(String address1, String address2) {
//		String[] after = preProcess(address1, address2);
//		address1 = after[0];
//		address2=after[1];
		return fuzzyScore.fuzzyScore(address1, address2);
	}

	/**
	 * <em>计算两个地址的JaroWinkler相似度</em><br>
	 * 
	 * <b>JaroWinkler相似度定义为两个字符串字符匹配的百分比</b> <br>
	 * <a href=
	 * "https://en.wikipedia.org/wiki/Jaro-Winkler_distance">JaroWinkler相似度定义</a>
	 * 
	 * @param address1 String 第一个地址，如标准地址，示例：广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋
	 * @param address2 String 第二个地址，如待比较（或查询）的地址，示例：深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2
	 * @return Double 【0,1】返回两个地址文本的JaroWinkler相似度
	 */
	public static Double jaroWinkler(String address1, String address2) {
//		String[] after = preProcess(address1, address2);
//		address1 = after[0];
//		address2=after[1];
		return jaroDistance.apply(address1, address2);
	}

	/**
	 * <em>计算两个地址的多种相似度</em><br>
	 * 
	 * <a href=
	 * "http://commons.apache.org/proper/commons-text/javadocs/api-release/index.html">文本字符串相似度定义</a>
	 * 
	 * @param address1 String 第一个地址，如标准地址，示例：广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋
	 * @param address2 String 第二个地址，如待比较（或查询）的地址，示例：深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2
	 * @return SimilarityIndicator 返回两个地址的多种相似度
	 */
	public static SimilarityIndicator indicator(String address1, String address2) {
//		String[] after = preProcess(address1, address2);
//		address1 = after[0];
//		address2=after[1];
		simIndicator.cosineDistance = (1.0 - cosDistance.apply(address1, address2));
		simIndicator.fuzzyScore = fuzzyScore.fuzzyScore(address1, address2);
		simIndicator.jaccardSimilarity = jaccardSimilarity.apply(address1, address2);
		simIndicator.jaroWinklerDistance = jaroDistance.apply(address1, address2);
		return simIndicator;
	}

}
