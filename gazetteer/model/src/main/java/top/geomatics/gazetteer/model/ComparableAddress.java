/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 *
 */
public class ComparableAddress implements Comparable<Object> {
	private String address;
	private Double similarity;

	public ComparableAddress(String address, Double similarity) {
		super();
		this.address = address;
		this.similarity = similarity;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof ComparableAddress){
			ComparableAddress address = (ComparableAddress) o;
            return address.getSimilarity().compareTo(this.similarity);//换相似性升序排序
        }
        throw new ClassCastException("unable cast to ComparableAddress...");
	}

}
