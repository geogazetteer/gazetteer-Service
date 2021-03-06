/**
 * 
 */
package top.geomatics.gazetteer.model;

import java.util.Date;

/**
 * @author whudyj
 *
 */
public class AddressEditorRow {
	private Integer fid;// 第一个字段，数据库自增长，如果原数据中有该字段，忽略原数据中的字段。
	private String name_;// 通用名称
	private String code_;// 通用编码
	private String description_;// 通用描述

	private String street_;// 表示街道。
	private String community_;// 表示社区。
	private Double longitude_;// 表示经度。
	private Double latitude_;// 表示纬度。

	private String origin_address;// 原地址。
	private String similar_address;// 与原地址相似的标准地址。
	private String standard_address;// 与原地址对应的标准地址。如果没有对应的标准地址，则similar_address可以作为标准地址的建议。

	private Integer status;// 状态，0表示未修改，1表示已经修改，2表示无法修改
	private String modifier;// 修改者
	private Date update_date;// 修改时间
	private String update_address;// 修改后的地址
	private String update_building_code;// 修改后的建筑物编码

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getName_() {
		return name_;
	}

	public void setName_(String name_) {
		this.name_ = name_;
	}

	public String getCode_() {
		return code_;
	}

	public void setCode_(String code_) {
		this.code_ = code_;
	}

	public String getDescription_() {
		return description_;
	}

	public void setDescription_(String description_) {
		this.description_ = description_;
	}

	public String getStreet_() {
		return street_;
	}

	public void setStreet_(String street_) {
		this.street_ = street_;
	}

	public String getCommunity_() {
		return community_;
	}

	public void setCommunity_(String community_) {
		this.community_ = community_;
	}

	public Double getLongitude_() {
		return longitude_;
	}

	public void setLongitude_(Double longitude_) {
		this.longitude_ = longitude_;
	}

	public Double getLatitude_() {
		return latitude_;
	}

	public void setLatitude_(Double latitude_) {
		this.latitude_ = latitude_;
	}

	public String getOrigin_address() {
		return origin_address;
	}

	public void setOrigin_address(String origin_address) {
		this.origin_address = origin_address;
	}

	public String getSimilar_address() {
		return similar_address;
	}

	public void setSimilar_address(String similar_address) {
		this.similar_address = similar_address;
	}

	public String getStandard_address() {
		return standard_address;
	}

	public void setStandard_address(String standard_address) {
		this.standard_address = standard_address;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getUpdate_address() {
		return update_address;
	}

	public void setUpdate_address(String update_address) {
		this.update_address = update_address;
	}

	public String getUpdate_building_code() {
		return update_building_code;
	}

	public void setUpdate_building_code(String update_building_code) {
		this.update_building_code = update_building_code;
	}

	public String encoding() {
		String s = null;
		if (this.code_ != null) {
			s += this.code_;
		}
		if (this.longitude_ != null) {
			s += ",";
			s += this.longitude_.toString();
		}
		if (this.latitude_ != null) {
			s += ",";
			s += this.latitude_.toString();
		}
		if (this.origin_address != null) {
			s += ",";
			s += this.origin_address;
		}
		return s;

	}

}
