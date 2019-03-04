/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 *
 */
public abstract class Gazetteer extends GLocation implements IGazetteer {
	private String code; 			// 地址编码
	private String name; 			// 地址全称
	/* (non-Javadoc)
	 * @see top.geomatics.gazetteer.model.IGazetteer#getCode()
	 */
	public String getCode() {
		return code;
	}
	/* (non-Javadoc)
	 * @see top.geomatics.gazetteer.model.IGazetteer#setCode(java.lang.String)
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/* (non-Javadoc)
	 * @see top.geomatics.gazetteer.model.IGazetteer#getName()
	 */
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see top.geomatics.gazetteer.model.IGazetteer#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
}
