/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2sqlite;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.opencsv.CSVReader;

/**
 * @author whudyj 将CSV文件转换为Sqlite数据库
 */
public class CSV2SQLITE {

	/**
	 * @param args 第一个参数为CSV文件路径名，第二个参数为Sqlite文件路径名
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为2
		if (args.length != 2) {
			System.exit(0);
		}
		CSVReader csvReader = null;
		boolean isHeader = true; // CSV文件第一行
		long count = 0l; // CSV数据记录数
		int length = 0; // 数据字段数

		String[] nextLine = null;// CSV文件中的一行数据
		// 准备CSV文件
		try {
			csvReader = new CSVReader(new FileReader(args[0]));// 打开CSV文件
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// 准备SQLite数据库
		Connection connection = null;
		Statement statement = null;
		String sqlString = "";
		String strConn = "jdbc:sqlite:" + args[1];// 数据库地址
		String tableName = "dmdz";
		PreparedStatement pstmt = null;
		// create a database connection
		try {
			connection = DriverManager.getConnection(strConn);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
					// create table dmdz
					sqlString = "drop table if exists " + tableName;// 如果表已经存在，则先删除
					statement.executeUpdate(sqlString);

					sqlString = "create table " + tableName + " (";
					String insertString = "insert into " + tableName + " values(";

					for (int i = 0; i < nextLine.length; i++) {
						sqlString += nextLine[i];
						sqlString += " string";
						insertString += "?";
						if (i < nextLine.length - 1) {
							sqlString += ",";
							insertString += ",";
						}
					}
					sqlString += ")";
					insertString += ")";
					statement.executeUpdate(sqlString);
					pstmt = connection.prepareStatement(insertString);
					continue;
				}
				if (length != nextLine.length) {
					System.out.println("Warning");
				}
				count++;
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
				for (int i = 0; i < nextLine.length; i++) {
					pstmt.setString(i+1, nextLine[i]);
				}
				pstmt.addBatch();
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			pstmt.executeBatch();
			connection.commit();
			statement.close();
			pstmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(count);

	}

}
