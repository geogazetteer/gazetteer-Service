/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2sqlite;

/**
 * @author whudyj
 *
 */
public class AddressRecord {
	private int fieldLength; // 字段长度
	String values[]; // 字段值数组

	public AddressRecord(int fieldLength, String[] values) {
		super();
		this.fieldLength = fieldLength;
		this.values = values;
	}

	public AddressRecord() {
		super();
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

}
