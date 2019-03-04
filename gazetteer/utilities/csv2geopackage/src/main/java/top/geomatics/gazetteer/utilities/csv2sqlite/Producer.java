/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2sqlite;

import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;

/**
 * @author whudyj
 *
 */
public class Producer implements Runnable {
	private CSVReader csvReader;
	private ArrayList<AddressRecord> recordList;

	// 最大容量
	public static final int MAX_SIZE = 1000;

	public Producer(CSVReader csvReader, ArrayList<AddressRecord> recordList) {
		super();
		this.setCsvReader(csvReader);
		this.setRecord(recordList);
	}

	public CSVReader getCsvFName() {
		return this.csvReader;
	}

	public void setCsvReader(CSVReader csvReader) {
		this.csvReader = csvReader;
	}

	public ArrayList<AddressRecord> getRecord() {
		return this.recordList;
	}

	public void setRecord(ArrayList<AddressRecord> recordList) {
		this.recordList = recordList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String[] nextLine = null;// CSV文件中的一行数据
		boolean isHeader = true; // CSV文件第一行
		int length = 0; // 数据字段数

		synchronized (this.recordList) {
			
			try {
				while ((nextLine = csvReader.readNext()) != null) {
					// nextLine[] is an array of values from the line
					// 先输出内容到console
					for (String str : nextLine) {
						System.out.print(str + "\t");
					}
					System.out.println();

					// 文件第一行是结构行，结构为：
					/*
					 * "ADDRESS_ID", "CODE", "BUILDING_ID", "HOUSE_ID", "PROVINCE", "CITY",
					 * "DISTRICT", "STREET", "COMMUNITY", "ROAD", "ROAD_NUM", "VILLAGE", "BUILDING",
					 * "FLOOR", "ADDRESS", "UPDATE_ADDRESS_DATE", "PUBLISH", "CREATE_ADDRESS_DATE"
					 */
					if (isHeader) {
						isHeader = false;
						length = nextLine.length;
						continue;
					}
					if (length != nextLine.length) {
						System.out.println("Warning");
					}
					// "63EEDE6BA4206A3AE0538CC0C0C07BB0",
					// "44030600960102T0117",
					// "44030600960102T0117",
					// "",
					// "广东省",
					// "深圳市",
					// "龙华区",
					// "民治街道",
					// "龙塘社区",
					// "",
					// "",
					// "上塘农贸建材市场",
					// "L25号铁皮房",
					// "",
					// "广东省深圳市龙华区民治街道龙塘社区上塘农贸建材市场L25号铁皮房",
					// "2016/12/12 23:46:17",
					// "0",
					// "2018/7/3 20:30:11"
					/*
					 * "ADDRESS_ID", "CODE", "BUILDING_ID", "HOUSE_ID", "PROVINCE", "CITY",
					 * "DISTRICT", "STREET", "COMMUNITY", "ROAD", "ROAD_NUM", "VILLAGE", "BUILDING",
					 * "FLOOR", "ADDRESS", "UPDATE_ADDRESS_DATE", "PUBLISH", "CREATE_ADDRESS_DATE"
					 */
					String valueString[] = new String[length];
					for (int i = 0; i < nextLine.length; i++) {
						valueString[i] = nextLine[i];
					}
					AddressRecord record = new AddressRecord(length, valueString);
					this.recordList.add(record);
					
					while (this.recordList.size() == MAX_SIZE) {
						try {
							this.recordList.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			this.recordList.notify();
		}

	}

}
