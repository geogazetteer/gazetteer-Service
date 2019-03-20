/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 *
 */
public class EnterpriseRow extends AddressModifier{
	private String code;// 统一社会信用代码
	private String name;// 企业名称
	private String street;// 所在街道
	private String owner;// 法定代表人
	private String address;// JYCS
	private Double longitude;// 经度
	private Double latitude;// 经度

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
