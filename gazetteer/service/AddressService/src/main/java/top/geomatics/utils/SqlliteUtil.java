package top.geomatics.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sqllite工具类
 */
public class SqlliteUtil {
	private Connection connection = null;
	private PreparedStatement pstat = null;
	private ResultSet rst = null;

	public SqlliteUtil() {
		try {
			Class.forName("org.sqlite.JDBC");
			// 本地测试，注意修改sqlite数据库文件的地址
			connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\chenfa\\Desktop\\dmdz_sqlite.db");
			System.out.println("[SQL LITE]: 获取到sqlite的jdbc连接： " + connection);
		} catch (Exception e) {
			System.err.println("[SQL LITE]: 获取sqlite的jdbc连接失败");
			System.err.println(e.getMessage());
		} finally {
			close();
		}
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * 查询所有数据
	 * 
	 * @param sql
	 * @return
	 */
	public List selectAll(String sql) {
		List results = new ArrayList();
		try {
			pstat = connection.prepareStatement(sql);
			rst = pstat.executeQuery();
			// 获取到数据
			ResultSetMetaData metaData = rst.getMetaData();
			int cols = metaData.getColumnCount();

			while (rst.next()) {
				// 封装一行数据
				Map map = new HashMap();
				for (int i = 0; i < cols; i++) {
					String key = metaData.getColumnName(i + 1);
					Object value = rst.getObject(i + 1);
					map.put(key, value);
				}
				results.add(map);
			}
			System.out.println("[SQL LIST]: " + results);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return results;
	}

	/**
	 * 按地址条件查询
	 * 
	 * @param tableName
	 * @param address
	 * @return
	 */
	public List selectByAddress(String tableName, String address) {
		List results = new ArrayList();
		try {
			String sql = "select * from " + tableName + " where address= ?";
			pstat = connection.prepareStatement(sql);
			pstat.setString(1, address);
			rst = pstat.executeQuery();
			// 获取到数据 ，getMetaData()返回 ResultSetMetaData 对象
			ResultSetMetaData metaData = rst.getMetaData();
			// 获取列的数目
			int cols = metaData.getColumnCount();
			while (rst.next()) {
				// 封装一行数据
				Map map = new HashMap();
				for (int i = 0; i < cols; i++) {
					String key = metaData.getColumnName(i + 1);
					Object value = rst.getObject(i + 1);
					map.put(key, value);
				}
				results.add(map);
			}
			System.out.println("[SQL LIST]: " + results);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return results;
	}

	/**
	 * 按地址条件查询
	 * 
	 * @param tableName
	 * @param BUILDING_ID
	 * 
	 */
	public List selectByCode(String tableName, String code) {
		List results = new ArrayList();
		try {
			String sql = "select * from " + tableName + " where code= ?";
			pstat = connection.prepareStatement(sql);
			pstat.setString(1, code);
			rst = pstat.executeQuery();
			// 获取到数据 ，getMetaData()返回 ResultSetMetaData 对象
			ResultSetMetaData metaData = rst.getMetaData();
			// 获取列的数目
			int cols = metaData.getColumnCount();
			while (rst.next()) {
				// 封装一行数据
				Map map = new HashMap();
				for (int i = 0; i < cols; i++) {
					String key = metaData.getColumnName(i + 1);
					Object value = rst.getObject(i + 1);
					map.put(key, value);
				}
				results.add(map);
			}
			System.out.println("[SQL LIST]: " + results);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return results;
	}

	/**
	 * 按地址条件查询
	 * 
	 * @param tableName
	 * @param BUILDING_ID
	 * 
	 */
	public List selectByKeyword(String tableName, String keyword) {
		List results = new ArrayList();
		try {
			String sql = " ";
			pstat = connection.prepareStatement(sql);
			pstat.setString(1, keyword);
			rst = pstat.executeQuery();
			// 获取到数据 ，getMetaData()返回 ResultSetMetaData 对象
			ResultSetMetaData metaData = rst.getMetaData();
			// 获取列的数目
			int cols = metaData.getColumnCount();
			while (rst.next()) {
				// 封装一行数据
				Map map = new HashMap();
				for (int i = 0; i < cols; i++) {
					String key = metaData.getColumnName(i + 1);
					Object value = rst.getObject(i + 1);
					map.put(key, value);
				}
				results.add(map);
			}
			System.out.println("[SQL LIST]: " + results);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return results;
	}

	/**
	 * 清除单次查询的连接
	 */
	private void close() {
		if (rst != null) {
			try {
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstat != null) {
			try {
				pstat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void relase() {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("[SQL LITE]: 关闭connection连接");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
