/**
 * 
 */
package top.geomatics.gazetteer.utilities.GpkgProcessing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author whudyj
 *
 */
public class GeopackageProcessor {
	// 准备SQLite数据库
	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement pstmt = null;
	private static String strConn = "jdbc:sqlite:";// 数据库地址
	private static String strTableName = "dmdz";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为1
		if (args.length != 1) {
			System.exit(0);
		}
		strConn += args[0];
		openDatabase();

		String slqString = "select 民治社区.building_id from 民治社区,龙华区楼栋 where 民治社区.building_id = 龙华区楼栋.BGUID";
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(slqString);
			while (rs.next()) {
				String key = rs.getString(1);
				if (key.isEmpty()) {
					continue;
				}
				System.out.println(key);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		closeDatabase();

	}

	// open database
	private static void openDatabase() {
		System.out.println("打开数据库，准备处理......");
		// create a database connection
		try {
			connection = DriverManager.getConnection(strConn);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// close database
	private static void closeDatabase() {
		// create a database connection
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("关闭数据库，处理结束！");
	}

}
