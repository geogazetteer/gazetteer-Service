/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

/**
 * @author whudyj
 *
 */
public class MYCSVReader {
	private String csvFName;// CSV文件名
	private CSVReader csvReader = null;

	public MYCSVReader(String csvFName) {
		super();
		this.csvFName = csvFName;
	}

	public void openFile() {
		// 准备CSV文件
		try {
			csvReader = new CSVReader(new FileReader(csvFName));// 打开CSV文件
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void closeFile() {
		// 关门文件
		try {
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String[]> getData() {
		String[] nextLine = null;// CSV文件中的一行数据
		List<String[]> list = new ArrayList<String[]>();
		try {
			while ((nextLine = csvReader.readNext()) != null) {
				list.add(nextLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
