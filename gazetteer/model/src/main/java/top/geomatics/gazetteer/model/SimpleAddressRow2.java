/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 *
 */
public class SimpleAddressRow2 {
	private Integer id; // 数据库id
	private String address; // 完整的地址名称
	private String name;//地名/POI

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
