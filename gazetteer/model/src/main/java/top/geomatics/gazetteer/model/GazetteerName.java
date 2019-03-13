/**
 *
 */
package top.geomatics.gazetteer.model;

/**
 * <!--一个地址可能有多个名称-->
 * 
 * @author whudyj
 */
public class GazetteerName implements IGazetteerName {
	// 地址名称全称: 省级行政区+市级行政区+县级行政区+镇级行政区+社区+道路+门楼牌+小区/自然村+楼栋+ 单元+房间
	private String name;
	// 地址行政名称:省级行政区+市级行政区+县级行政区+镇级行政区+社区+小区/自然村+楼栋+单元+房间
	private String adminName;
	// 地址地理名称：省级行政区+市级行政区+县级行政区+道路/街巷+小区/自然村+楼栋+房间
	private String geoName;
	// 地址俗称，地址的通俗称呼或非正式名称
	private String convName;
	// 地址简称，地址的简单称呼
	private String abbrName;
	// 地址别称，多个别称之间用,号分隔
	private String alias;
	// 地址曾用名，多个曾用名之间用,号分隔
	private String usedNames;

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#getAdminName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.model.IGazetteerExt#setAdminName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#getAdminName()
	 */
	public String getAdminName() {
		return adminName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.model.IGazetteerExt#setAdminName(java.lang.String)
	 */
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#getGeoName()
	 */
	public String getGeoName() {
		return geoName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#setGeoName(java.lang.String)
	 */
	public void setGeoName(String geoName) {
		this.geoName = geoName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#getConvName()
	 */
	public String getConvName() {
		return convName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.model.IGazetteerExt#setConvName(java.lang.String)
	 */
	public void setConvName(String convName) {
		this.convName = convName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#getAbbrName()
	 */
	public String getAbbrName() {
		return abbrName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.model.IGazetteerExt#setAbbrName(java.lang.String)
	 */
	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#getAlias()
	 */
	public String getAlias() {
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#setAlias(java.lang.String)
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.model.IGazetteerExt#getUsedNames()
	 */
	public String getUsedNames() {
		return usedNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.model.IGazetteerExt#setUsedNames(java.lang.String)
	 */
	public void setUsedNames(String usedNames) {
		this.usedNames = usedNames;
	}
}
