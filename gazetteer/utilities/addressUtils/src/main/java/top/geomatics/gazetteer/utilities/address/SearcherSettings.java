/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * @author whudyj
 *
 */
public class SearcherSettings {
	private boolean isComplexChar = false;// 繁体字转换
	private boolean isFullChar = false;// 全角字转换
	private boolean isChineseNumber = false;// 中文数字转换
	private boolean isAlias = false;// 别名转换
	private boolean isHomophone = false;// 同音字转换
	private boolean isSynonym = false;// 同义字转换
	private boolean isInterchangeable = false;// 通假字转换
	
	private boolean isAddress = true;// 地址
	private boolean isGeoName = false;// 地名
	private boolean isPOI = false;// POI
	private boolean isCoordinates = false;// 坐标
	private boolean isBuildingCode = false;// 建筑物编码
	
	private boolean isDatabaseSearch = true;// 普通搜索
	private boolean isLuceneSearch = false;// 普通搜索

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

	public boolean isAlias() {
		return isAlias;
	}

	public void setAlias(boolean isAlias) {
		this.isAlias = isAlias;
	}

	
	public boolean isInterchangeable() {
		return isInterchangeable;
	}

	public void setInterchangeable(boolean isInterchangeable) {
		this.isInterchangeable = isInterchangeable;
	}


	public boolean isGeoName() {
		return isGeoName;
	}

	public void setGeoName(boolean isGeoName) {
		this.isGeoName = isGeoName;
	}


	public boolean isPOI() {
		return isPOI;
	}

	public void setPOI(boolean isPOI) {
		this.isPOI = isPOI;
	}

	
	public boolean isAddress() {
		return isAddress;
	}

	public void setAddress(boolean isAddress) {
		this.isAddress = isAddress;
	}

	public boolean isDatabaseSearch() {
		return isDatabaseSearch;
	}

	public void setDatabaseSearch(boolean isDatabaseSearch) {
		this.isDatabaseSearch = isDatabaseSearch;
	}

	public boolean isLuceneSearch() {
		return isLuceneSearch;
	}

	public void setLuceneSearch(boolean isLuceneSearch) {
		this.isLuceneSearch = isLuceneSearch;
	}

	public boolean isCoordinates() {
		return isCoordinates;
	}

	public void setCoordinates(boolean isCoordinates) {
		this.isCoordinates = isCoordinates;
	}

	public boolean isHomophone() {
		return isHomophone;
	}

	public void setHomophone(boolean isHomophone) {
		this.isHomophone = isHomophone;
	}

	public boolean isSynonym() {
		return isSynonym;
	}

	public void setSynonym(boolean isSynonym) {
		this.isSynonym = isSynonym;
	}

	public boolean isBuildingCode() {
		return isBuildingCode;
	}

	public void setBuildingCode(boolean isBuildingCode) {
		this.isBuildingCode = isBuildingCode;
	}

}
