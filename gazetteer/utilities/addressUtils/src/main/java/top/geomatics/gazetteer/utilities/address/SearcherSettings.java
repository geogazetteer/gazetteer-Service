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
	private boolean isAddressAlias = false;// 别名，简称，曾用名
	private boolean ishomophone = false;// 同音字
	private boolean isInterchangeable = false;// 通假字

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

}
