/**
 * 
 */
package top.geomatics.gazetteer.utilities.dbprocessing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whudyj
 *
 */
public class DatabaseProcessing {
	// 准备SQLite数据库
	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement pstmt = null;
	private static String strConn = "jdbc:sqlite:";// 数据库地址
	private static String strTableName = "dmdz";

	private static Map<String, String> streetMap = new HashMap<String, String>();

	/**
	 * @param args 一个参数为标准地址sqlite数据库名
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为1
		if (args.length != 1) {
			System.exit(0);
		}
		strConn += args[0];
		openDatabase();
		queryDistrict();
		queryCommunity();
		queryOthers();

		closeDatabase();
	}

	// open database
	private static void openDatabase() {
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
	}

	// 查询龙华区下面有多少街道，创建表district，一个字段为street
	private static void queryDistrict() {
		String slqString = "select street from " + strTableName;
		ResultSet rs = null;
		String tName = "district";
		String dropString = "drop table if exists " + tName;// 如果表已经存在，则先删除
		String createString = "create table " + tName + "(street string)";
		try {
			rs = statement.executeQuery(slqString);
			while (rs.next()) {
				String key = rs.getString(1);
				if (key.isEmpty()) {
					continue;
				}
				streetMap.put(key, key);
			}
			if (streetMap.size() < 1) {
				return;
			}
			// 创建表LH_district
			statement.executeUpdate(dropString);
			statement.executeUpdate(createString);
			String insertString = "insert into " + tName + " values(?)";
			pstmt = connection.prepareStatement(insertString);
			for (String keyString : streetMap.keySet()) {
				pstmt.setString(1, keyString);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			connection.commit();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 查询每个街道下面有多少社区，每个街道创建一个表（表名为街道名），一个字段为community
	private static void queryCommunity() {
		if (streetMap.size() < 1) {
			return;
		}
		for (String keyString : streetMap.keySet()) {
			String slqString = "select community from " + strTableName + " where street = \'" + keyString + "\'";
			Map<String, String> communityMap = new HashMap<String, String>();

			ResultSet rs = null;
			String tName = keyString;
			String dropString = "drop table if exists " + tName;// 如果表已经存在，则先删除
			String createString = "create table " + tName + " (community string)";
			try {
				rs = statement.executeQuery(slqString);
				while (rs.next()) {
					String key = rs.getString(1);
					if (key.isEmpty()) {
						continue;
					}
					communityMap.put(key, key);
				}
				if (communityMap.size() < 1) {
					return;
				}
				// 创建表
				statement.executeUpdate(dropString);
				statement.executeUpdate(createString);
				String insertString = "insert into " + tName + " values(?)";
				pstmt = connection.prepareStatement(insertString);
				for (String cString : communityMap.keySet()) {
					pstmt.setString(1, cString);
					pstmt.addBatch();
				}
				pstmt.executeBatch();
				connection.commit();
				pstmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 查询每个社区下面有多少记录，每个社区创建一个表（表名为社区名），字段为address_id,code,building_id,village,building,address
	private static void queryOthers() {
		String slqString = "select street from district";
		ResultSet rs0 = null;
		try {
			rs0 = statement.executeQuery(slqString);
			while (rs0.next()) {
				String street = rs0.getString(1);// 街道
				String slq2 = "select community from " + street;
				ResultSet rs1 = null;
				rs1 = statement.executeQuery(slq2);
				while (rs1.next()) {
					String community = rs1.getString(1);// 社区
					String slq3 = "select code, building_id, village, building, address from " + strTableName
							+ " where community = \'" + community + "\'";
					ResultSet rs2 = null;

					String tName = community;
					String dropString = "drop table if exists " + tName;// 如果表已经存在，则先删除
					String createString = "create table " + tName
							+ " (code string ,building_id string ,village string,building string,address string)";
					// 创建表
					statement.executeUpdate(dropString);
					statement.executeUpdate(createString);
					rs2 = statement.executeQuery(slq3);
					String insertString = "insert into " + tName + " values(?,?,?,?,?)";
					pstmt = connection.prepareStatement(insertString);
					while (rs2.next()) {
						pstmt.setString(1, rs2.getString(1));
						pstmt.setString(2, rs2.getString(2));
						pstmt.setString(3, rs2.getString(3));
						pstmt.setString(4, rs2.getString(4));
						pstmt.setString(5, rs2.getString(5));
						//pstmt.setString(6, rs2.getString(6));
						pstmt.addBatch();
					}
					pstmt.executeBatch();
					
					pstmt.close();
				}
			}
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
