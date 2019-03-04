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
import java.util.ArrayList;

import com.opencsv.CSVReader;

/**
 * @author whudyj 将CSV文件转换为Sqlite数据库
 */
public class CSV2SQLITE {
	private static CSVReader csvReader = null;
	private static long count = 0l; // CSV数据记录数
	// 准备SQLite数据库
	private static Connection connection = null;
	private static Statement statement = null;
	private static String sqlString = "";
	private static String tableName = "dmdz";
	private static PreparedStatement pstmt = null;
	private static int count2 = 0;

	private static ArrayList<AddressRecord> recordList = new ArrayList<AddressRecord>();
	/**
	 * @param args 第一个参数为CSV文件路径名，第二个参数为Sqlite文件路径名
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为2
		if (args.length != 2) {
			System.exit(0);
		}
		openCSV(args[0]);
		openSqlite(args[1]);
	
		Producer apProducer = new Producer(csvReader, recordList);
		Consumer aConsumer = new Consumer(pstmt, recordList);
		Thread th1 = new Thread(apProducer);
		th1.setName("ReaderThread");
		th1.start();
		Thread th2 = new Thread(aConsumer);
		th2.setName("WriterThread");
		th2.start();
		if (!th1.isAlive() && !th2.isAlive()) {
			closeCSV();
			closeSqlite();
		}
	}

	public static void openCSV(String csvFName) {
		// 准备CSV文件
		try {
			csvReader = new CSVReader(new FileReader(csvFName));// 打开CSV文件
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static void closeCSV() {
		try {
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("读取的总记录数：\t" + count);
	}

	public static void openSqlite(String sqlFName) {
		String strConn = "jdbc:sqlite:" + sqlFName;// 数据库地址
		// create a database connection
		try {
			connection = DriverManager.getConnection(strConn);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// create table dmdz
		sqlString = "drop table if exists " + tableName;// 如果表已经存在，则先删除
		String insertString = "insert into " + tableName + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			statement.executeUpdate(sqlString);
			/*
			 * "ADDRESS_ID", "CODE", "BUILDING_ID", "HOUSE_ID", "PROVINCE", "CITY",
			 * "DISTRICT", "STREET", "COMMUNITY", "ROAD", "ROAD_NUM", "VILLAGE", "BUILDING",
			 * "FLOOR", "ADDRESS", "UPDATE_ADDRESS_DATE", "PUBLISH", "CREATE_ADDRESS_DATE"
			 */
			sqlString = "create table " + tableName + " (ADDRESS_ID string, CODE string,  BUILDING_ID string,"
					+ "HOUSE_ID string, PROVINCE string, CITY string,DISTRICT string, STREET string,"
					+ "COMMUNITY string, ROAD string, ROAD_NUM string, VILLAGE string,  BUILDING string,"
					+ "FLOOR string, ADDRESS string, UPDATE_ADDRESS_DATE string,PUBLISH string, CREATE_ADDRESS_DATE string)";
			statement.executeUpdate(sqlString);
			pstmt = connection.prepareStatement(insertString);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void closeSqlite() {
		try {
			pstmt.executeBatch();
			connection.commit();
			statement.close();
			pstmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("写入的总记录数：\t" + count2);
	}

}
