/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * @author whudyj
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String address1 = "启航大厦918室";
		String address2 = "启航大厦918室";

		SimilarityIndicator indicator = AddressSimilarity.indicator(address1, address2);

		System.out.println("余弦距离:" + indicator.cosineDistance);
		System.out.println("模糊分数:" + indicator.fuzzyScore);
		System.out.println("Jaccard相似性:" + indicator.jaccardSimilarity);
		System.out.println("Jaro Winkler 相似性:" + indicator.jaroWinklerDistance);
	}

}
