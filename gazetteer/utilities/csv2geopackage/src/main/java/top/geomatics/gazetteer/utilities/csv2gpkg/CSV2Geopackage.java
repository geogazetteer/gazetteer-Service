/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2gpkg;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

/**
 * @author whudyj 将CSV文件转换为GeoPackage文件
 */
public class CSV2Geopackage {

	/**
	 * @param args 第一个参数为CSV文件路径名，第二个参数为GeoPackage文件路径名
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.exit(0);
		}
		CSVReader csvReader = null;
		boolean isHeader = true;
		long count = 0l;
		int length = 0;
		try {
			csvReader = new CSVReader(new FileReader(args[0]));
			String[] nextLine = null;
			while ((nextLine = csvReader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				/*
				 * for (String str : nextLine) { System.out.print(str + "\t"); }
				 * System.out.println();
				 */
				if (isHeader) {
					isHeader = false;
					length = nextLine.length;
					continue;
				}
				if (length != nextLine.length) {
					System.out.println("Warning");
				}
				count++;

			}
			System.out.println(count);
			csvReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
