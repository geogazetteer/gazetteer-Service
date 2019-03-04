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
	String values[];

	// 地址数据结构为：
	/*
	 * "ADDRESS_ID", "CODE", "BUILDING_ID", "HOUSE_ID", "PROVINCE", "CITY",
	 * "DISTRICT", "STREET", "COMMUNITY", "ROAD", "ROAD_NUM", "VILLAGE", "BUILDING",
	 * "FLOOR", "ADDRESS", "UPDATE_ADDRESS_DATE", "PUBLISH", "CREATE_ADDRESS_DATE"
	 */

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
