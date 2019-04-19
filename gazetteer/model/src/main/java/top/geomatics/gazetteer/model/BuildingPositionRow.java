/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 *
 */
public class BuildingPositionRow {
	private String code; // 建筑物编码，唯一，由权威机构确定，示例：44030600960102T0117 共19位
	private String building_id; // 建筑标识，唯一，由权威机构确定，示例：44030600960102T0117 共19位

	private Double longitude;// 经度
	private Double latitude;// 纬度

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(String building_id) {
		this.building_id = building_id;
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
