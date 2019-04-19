/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * @author whudyj
 *
 */
public class SearcherSettings {
	private boolean isComplexChar = false;// 繁体字
	private boolean isFullChar = false;// 全角字
	private boolean isChineseNumber = false;// 中文数字
	private boolean isAddressAlias = false;// 地址别名，简称，曾用名
	private boolean ishomophone = false;// 同音字
	private boolean isInterchangeable = false;// 通假字
	private boolean isCompleted = false;// 地址补全
	private boolean isGeoName = false;// 地名
	private boolean isGeoNameAlias = false;// 地名别名
	private boolean isPOI = false;// POI
	private boolean isPOIAlias = false;// POI别名
	private boolean isWithtin = false;// 地址范围识别
	private boolean isCoordinates = false;// 坐标识别

	public boolean isComplexChar() {
		return isComplexChar;
	}

	public void setComplexChar(boolean isComplexChar) {
		this.isComplexChar = isComplexChar;
	}

	public boolean isFullChar() {
		return isFullChar;
	}

	public void setFullChar(boolean isFullChar) {
		this.isFullChar = isFullChar;
	}

	public boolean isChineseNumber() {
		return isChineseNumber;
	}

	public void setChineseNumber(boolean isChineseNumber) {
		this.isChineseNumber = isChineseNumber;
	}

	public boolean isAddressAlias() {
		return isAddressAlias;
	}

	public void setAddressAlias(boolean isAddressAlias) {
		this.isAddressAlias = isAddressAlias;
	}

	public boolean isIshomophone() {
		return ishomophone;
	}

	public void setIshomophone(boolean ishomophone) {
		this.ishomophone = ishomophone;
	}

	public boolean isInterchangeable() {
		return isInterchangeable;
	}

	public void setInterchangeable(boolean isInterchangeable) {
		this.isInterchangeable = isInterchangeable;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public boolean isGeoName() {
		return isGeoName;
	}

	public void setGeoName(boolean isGeoName) {
		this.isGeoName = isGeoName;
	}

	public boolean isGeoNameAlias() {
		return isGeoNameAlias;
	}

	public void setGeoNameAlias(boolean isGeoNameAlias) {
		this.isGeoNameAlias = isGeoNameAlias;
	}

	public boolean isPOI() {
		return isPOI;
	}

	public void setPOI(boolean isPOI) {
		this.isPOI = isPOI;
	}

	public boolean isPOIAlias() {
		return isPOIAlias;
	}

	public void setPOIAlias(boolean isPOIAlias) {
		this.isPOIAlias = isPOIAlias;
	}

	public boolean isWithtin() {
		return isWithtin;
	}

	public void setWithtin(boolean isWithtin) {
		this.isWithtin = isWithtin;
	}

	public boolean isCoordinates() {
		return isCoordinates;
	}

	public void setCoordinates(boolean isCoordinates) {
		this.isCoordinates = isCoordinates;
	}

}
