/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * <em>相似度指标</em><br>
 * 
 * <b>相似度指标包括余弦距离、模糊分数、Jaccard相似度和Jaro Winkler 相似度等</b><br>
 * <a href=
 * "http://commons.apache.org/proper/commons-text/javadocs/api-release/index.html">相似度指标定义</a>
 * 
 * @author whudyj
 *
 */
public class SimilarityIndicator {
	// 余弦距离，距离越大，相似度越小
	public Double cosineDistance;
	// 模糊分数，分数越大，相似度越大，left:标准地址（term），right：非标准地址（query）
	public Integer fuzzyScore;
	// Jaccard相似度
	public Double jaccardSimilarity;
	// Jaro Winkler 相似度
	public Double jaroWinklerDistance;

}
