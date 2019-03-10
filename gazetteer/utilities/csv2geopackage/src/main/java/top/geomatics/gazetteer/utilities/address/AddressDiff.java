/**
 *
 */
package top.geomatics.gazetteer.utilities.address;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.opengis.filter.And;

import com.opencsv.CSVReader;

/**
 * @author whudyj 比较两个地址
 */
public class AddressDiff {
    private static ArrayList<String> addressList = new ArrayList<String>();// 存储所有待匹配的地址
    // 标准地址库查询
    private static String tableName = "dmdz";
    private static Connection connection = null;
    private static Statement statement = null;
    // 结果输出库
    private static String outtableName = "matcher";
    private static Connection outconnection = null;
    private static Statement outstatement = null;
    private static PreparedStatement pstmt = null; // 需要输出的数据
    // 匹配计算
    private static JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    private static FuzzyScore fuzzyScore = new FuzzyScore(Locale.CHINESE);
    private static String jaString = "";
    private static String fuzzyString = "";// 最大fuzzyscore对应的地址

    /**
     * @param csvName 地址CSV文件名
     */
    public static void getAddressSource(String csvName) {
        CSVReader csvReader = null;
        boolean isHeader = true; // CSV文件第一行
        String[] nextLine = null;// CSV文件中的一行数据
        int idx = 0;

        // 打开CSV文件
        try {
            csvReader = new CSVReader(new FileReader(csvName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while ((nextLine = csvReader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                // 文件第一行是结构行，结构为：
                if (isHeader) {
                    isHeader = false;
                    for (int i = 0; i < nextLine.length; i++) {
                        if (0 == "JYCS".compareToIgnoreCase(nextLine[i])) {
                            idx = i;
                        }
                    }
                    continue;
                }
                addressList.add(nextLine[idx]);
            }
            System.out.println("总数是：" + addressList.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param indb 标准地址Sqlite文件名
     */
    public static void init_input(String indb) {
        // 准备SQLite数据库
        String strConn = "jdbc:sqlite:" + indb;// 数据库地址
        try {
            connection = DriverManager.getConnection(strConn);
            statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param outdb 结果输出的Sqlite文件名
     */
    public static void init_output(String outdb) {
        // 输出
        String outsqlString = "";
        String outstrConn = "jdbc:sqlite:" + outdb;// 数据库地址

        // out database
        try {
            outconnection = DriverManager.getConnection(outstrConn);
            outconnection.setAutoCommit(false);
            outstatement = outconnection.createStatement();
            outstatement.setQueryTimeout(30); // set timeout to 30 sec.

            // create table matcher
            outsqlString = "drop table if exists " + outtableName;// 如果表已经存在，则先删除
            outstatement.executeUpdate(outsqlString);

            outsqlString = "create table " + outtableName
                    + " (OAddress string,JSim double, JAddress string, FScore integer, FAddress string)";
            String insertString = "insert into " + outtableName + " values(?,?,?,?,?)";

            outstatement.executeUpdate(outsqlString);
            pstmt = outconnection.prepareStatement(insertString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出数据
     */
    public static void output() {
        try {
            pstmt.executeBatch();
            outconnection.commit();
            outstatement.close();
            pstmt.close();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param qString 需要检索的地址
     * @return 返回检索的结果数组
     */
    public static ArrayList<String> query(String qString) {
        ArrayList<String> qList = new ArrayList<String>();
        // qString示例： 深圳市龙华区观澜街道大富社区平安路60号康淮工业园1号厂房1501
        String regExpString = "['街道''社区''路''大厦''楼''栋']";// 地址分词
        // String queryString = "select ADDRESS from " + tableName;
        /*
         * int idx1 = qString.indexOf("龙华区"); int idx2 = qString.indexOf("街道"); int idx3
         * = qString.indexOf("社区"); String street = ""; String community = ""; String
         * rest = ""; if (idx1 != -1 && idx2 != -1 && idx2 > idx1) {// 可以找到 龙华区、街道
         * street = qString.substring(idx1, idx2); street = street.replace("龙华区", "");
         * street += "街道"; rest = qString.split(street)[1]; } if (!street.isEmpty()) {
         * queryString = queryString + " where STREET LIKE" + "\'%" + street + "%\'"; }
         * if (idx2 != -1 && idx3 != -1 && idx3 > idx2) {// 可以找到 街道 社区 community =
         * qString.substring(idx2, idx3); community = community.replace("街道", "");
         * community += "社区"; rest = qString.split(community)[1]; } if
         * (!community.isEmpty()) { queryString = queryString + " AND COMMUNITY LIKE" +
         * "\'%" + community + "%\'"; } queryString = queryString + "AND ADDRESS LIKE "
         * + "'%" + rest + "%'"; queryString = queryString + " WHERE ADDRESS LIKE " +
         * "'%" + qString + "%'";
         */

        String segString[] = qString.split(regExpString);
        String queryString = "select ADDRESS from " + tableName + " where ADDRESS LIKE ";
        String tempString = "";
        String ssString = "";
        ArrayList<String> sb = new ArrayList<String>();
        for (int i = 0; i < segString.length - 1; i++) {//最后一个不找
            ssString = segString[i].trim();
            if (ssString.isEmpty()) {// 去掉空字符串
                continue;
            }
            sb.add(ssString);
        }
        for (int j = 0; j < sb.size(); j++) {
            tempString += "'%" + sb.get(j) + "%'";

            if (j < sb.size() - 1) {
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
                reString.replace("广东省" , "");
                qList.add(reString);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return qList;

    }

    /**
     * 计算jaccard相似系数
     */
    public static double jaccard(String jycString, ArrayList<String> rStrings) {
        double maxSimJa = 0.0;
        jaString = "";
        for (String reString : rStrings) {
            double jcdsimilary = jaccardSimilarity.apply(jycString, reString);
            if (jcdsimilary > maxSimJa) {
                maxSimJa = jcdsimilary;
                jaString = reString;
            }
        }
        return maxSimJa;
    }

    /**
     * 计算模糊匹配分数
     */
    public static Integer fuzzy(String jycString, ArrayList<String> rStrings) {
        Integer maxFScore = 0;// 最大fuzzyscore
        fuzzyString = "";
        for (String reString : rStrings) {
            Integer fscore = fuzzyScore.fuzzyScore(reString, jycString);
            if (fscore > maxFScore) {
                maxFScore = fscore;
                fuzzyString = reString;
            }
        }
        return maxFScore;
    }

    /**
     * @param args 三个参数，第一个参数为标准地址数据库文件名，第二个参数为要比较的地址CSV文件名 第三个参数为结果输出的Sqlite文件名
     */
    public static void main(String[] args) {
        // 判断命令行参数是否为3
        if (args.length != 3) {
            System.exit(0);
        }
        // 读取要比较的地址CSV文件
        getAddressSource(args[1]);
        // 准备数据库
        init_input(args[0]);
        init_output(args[2]);
        // 地址匹配
        int pros = 1;
        for (String qString : addressList) {
            ArrayList<String> queryList = query(qString);// 初步查询到的标准地址
            // 从查询到的标准地址中进一步找到精确的标准地址
            double js = jaccard(qString, queryList);
            Integer scores = fuzzy(qString, queryList);
            // output
            try {
                pstmt.setString(1, qString);
                pstmt.setDouble(2, js);
                pstmt.setString(3, jaString);
                pstmt.setInt(4, scores);
                pstmt.setString(5, fuzzyString);
                pstmt.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("当前进度：" + (pros++));
            System.out.println("历史地址：\t\t\t\t\t\t" + qString);
            System.out.println("最大jaccard相似系数：" + js + "\t对应的地址是：" + jaString);
            System.out.println("最大FuzzyScore：       " + scores + "\t对应的地址是：" + fuzzyString);
            System.out.println("--------------------------------------");
        }
        output();
    }

}
