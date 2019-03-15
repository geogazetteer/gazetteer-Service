package top.geomatics.gazetteer.utilities.database.csv2sqlite;

/**
 * @author whudyj 地址数据字段结构
 */
public class AddressSchema {
    private int fieldLength; // 字段长度
    String fields[]; // Sqlite 字段描述，如f1 string, f2 string,f3 string, f4 string

    public AddressSchema(int fieldLength, String[] fields) {
        super();
        this.fieldLength = fieldLength;
        this.fields = fields;
    }

    public int getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

}
