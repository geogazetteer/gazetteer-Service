/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * <em>相似性指标</em>
 * 
 * @author whudyj
 *
 */
public class SimilarityIndicator {
	//余弦距离，距离越大，相似性越小
	public Double cosineDistance; 
	//模糊分数，分数越大，相似性越大，left:标准地址（term），right：非标准地址（query）
	public Integer fuzzyScore;
	//Jaccard相似性
	public Double jaccardSimilarity; 
	//Jaro Winkler 相似性，
	public Double jaroWinklerDistance;

}
