package top.geomatics.gazetteer.service.utils;

import org.springframework.stereotype.Component;

import top.geomatics.gazetteer.model.AddressRow;

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
@Component
public class SqlliteUtil {
    private static Connection connection = null;
    private static PreparedStatement pstat = null;
    private static ResultSet rst = null;

    public SqlliteUtil() {
        try {
            Class.forName("org.sqlite.JDBC");
            // 本地测试，注意修改sqlite数据库文件的地址
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\peiyuyu\\Desktop\\11\\data\\dmdz_sqlite.db");
            System.out.println("[SQL LITE]: 获取到sqlite的jdbc连接： " + connection);
        } catch (Exception e) {
            System.err.println("[SQL LITE]: 获取sqlite的jdbc连接失败");
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    public  List<AddressRow> fuzzyQuery(String keyWord) {
        return selectAll("select ADDRESS from dmdz where ADDRESS like '%"+keyWord+"%' limit 0,10");
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * 查询所有数据
     *
     *
     * @return
     */

    public  ResultSet getResultSet() {
        String sql = " SELECT CODE,ADDRESS from dmdz";
        ResultSet rst1=null;
        try {
            pstat = connection.prepareStatement(sql);
           rst1 = pstat.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return rst1;
        }

    }

    public List<AddressRow> selectAll(String sql) {
        List results = new ArrayList<AddressRow>();
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
     * @param code
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
     * 清除单次查询的连接
     */
    private static void close() {
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
