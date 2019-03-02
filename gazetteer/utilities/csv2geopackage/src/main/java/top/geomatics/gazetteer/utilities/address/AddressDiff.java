/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;

/**
 * @author whudyj 比较两个地址
 */
public class AddressDiff {

	/**
	 * @param args 三个参数，第一个参数为标准地址数据库文件名，第二个参数为要比较的地址CSV文件名 第三个参数为结果输出的CSV文件名
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为3
		if (args.length != 3) {
			System.exit(0);
		}
		CSVReader csvReader = null;
		boolean isHeader = true; // CSV文件第一行
		long count = 0l; // CSV数据记录数
		int length = 0; // 数据字段数
		String[] nextLine = null;// CSV文件中的一行数据
		Map<String, String> addressMap = new HashMap<String, String>();// 要比较的数据先缓存到内存
		int idx = 0;

		// 准备SQLite数据库
		Connection connection = null;
		Statement statement = null;
		String sqlString = "";
		String strConn = "jdbc:sqlite:" + args[0];// 数据库地址
		String tableName = "dmdz";
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
			// 打开CSV文件
			csvReader = new CSVReader(new FileReader(args[1]));
			while ((nextLine = csvReader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				// 先输出内容到console
				/*
				 * for (String str : nextLine) { System.out.print(str + "\t"); }
				 * System.out.println();
				 */

				// 文件第一行是结构行，结构为：
				if (isHeader) {
					isHeader = false;
					length = nextLine.length;
					for (int i = 0; i < length; i++) {
						if (0 == "JYCS".compareToIgnoreCase(nextLine[i])) {
							idx = i;
						}
					}
					continue;
				}
				if (length != nextLine.length) {
					System.out.println("Warning");
				}
				// 待比较的地址
				String jycString = nextLine[idx];
				System.out.println(jycString);
				// 地址分词
				// 深圳市龙华区观澜街道大富社区平安路60号康淮工业园1号厂房1501
				String regExpString = "['市''区''街道''社区''路''巷']";
				String segString[] = jycString.split(regExpString);
				/*
				 * for (int i = 0; i < segString.length; i++) { System.out.print(segString[i] +
				 * "\t"); } System.out.println();
				 */

				String queryString = "select ADDRESS from " + tableName + " where ADDRESS LIKE ";
				String tempString = "";
				tempString = "";
				String ssString = "";
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < segString.length - 1; i++) {// 最后一个不找
					ssString = segString[i];
					ssString.trim();
					if (ssString.isEmpty()) {
						continue;
					}
					sb.append(ssString);
				}
				for (int j = 0; j < sb.length(); j++) {
					tempString += "'%" + ssString + "%'";
					if (j < sb.length() - 1) {
						tempString += " AND ADDRESS LIKE ";
					}
				}
				queryString += tempString;
				ResultSet rs;
				String reString = null;
				try {
					rs = statement.executeQuery(queryString);
					while (rs.next()) {
						// read the result set
						reString = rs.getString("ADDRESS");
						// 匹配最后一个
						String laString = segString[segString.length - 1];
						laString.trim();
						laString = laString.replaceAll("[a-zA-Z0-9]", "");
						boolean b = Pattern.matches(laString, reString);
						if (b) {
							System.out.println(reString);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				count++;
			}
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
