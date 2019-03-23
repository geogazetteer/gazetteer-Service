/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 *
 */
public class BuildingRow {
	private Long qCode; // 区代码
	private String qName;// 区名称
	private Long jdCode; // 街道代码
	private String jdName;// 街道名称
	private Long sqCode;// 社区代码
	private String sqName;// 社区名称
	private String bguid;// 建筑唯一标识
	private String czwCode;// 唯一标识

	public Long getqCode() {
		return qCode;
	}

	public void setqCode(Long qCode) {
		this.qCode = qCode;
	}

	public String getqName() {
		return qName;
	}

	public void setqName(String qName) {
		this.qName = qName;
	}

	public Long getJdCode() {
		return jdCode;
	}

	public void setJdCode(Long jdCode) {
		this.jdCode = jdCode;
	}

	public String getJdName() {
		return jdName;
	}

	public void setJdName(String jdName) {
		this.jdName = jdName;
	}

	public Long getSqCode() {
		return sqCode;
	}

	public void setSqCode(Long sqCode) {
		this.sqCode = sqCode;
	}

	public String getSqName() {
		return sqName;
	}

	public void setSqName(String sqName) {
		this.sqName = sqName;
	}

	public String getBguid() {
		return bguid;
	}

	public void setBguid(String bguid) {
		this.bguid = bguid;
	}

	public String getCzwCode() {
		return czwCode;
	}

	public void setCzwCode(String czwCode) {
		this.czwCode = czwCode;
	}

}
