/**
 * 
 */
package top.geomatics.gazetteer.utilities.wordsegment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

/**
 * <!--建立分词词库-->
 * 
 * @author whudyj
 *
 */
public class WordDictionary {
	// 准备SQLite数据库
	private static Connection connection = null;
	private static Statement statement = null;
	private static String strConn = "jdbc:sqlite:";// 数据库地址
	private static String strTableName = "dmdz";

	private static Map<String, Long> wordMap = new HashMap<String, Long>();

	// 只关注这些词性的词
	private static Set<String> expectedNature = new HashSet<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("n");
			add("v");
			add("vd");
			add("vn");
			add("vf");
			add("vx");
			add("vi");
			add("vl");
			add("vg");
			add("nt");
			add("nz");
			add("nw");
			add("nl");
			add("ng");
			add("userDefine");
			add("wh");
		}
	};

	public static void createDictionary() {
		String fileNString = WordDictionary.class.getResource("/").getPath();
		fileNString += "userLibrary.dic";
		File file = new File(fileNString);
		CSVWriter csvWriter = null;
		try {
			csvWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(file)).withSeparator('\t').withLineEnd("\r\t")
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String nextLine[] = new String[3];
		nextLine[0] = "广东省";
		nextLine[1] = "n";
		nextLine[2] = "5000";
		csvWriter.writeNext(nextLine, false);
		nextLine[0] = "深圳市";
		nextLine[1] = "n";
		nextLine[2] = "5000";
		nextLine[0] = "龙华区";
		nextLine[1] = "n";
		nextLine[2] = "5000";
		csvWriter.writeNext(nextLine, false);
		for (String keyString : wordMap.keySet()) {
			nextLine[0] = keyString;
			nextLine[1] = "n";
			nextLine[2] = wordMap.get(keyString).toString();
			csvWriter.writeNext(nextLine, false);
		}
		try {
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addWord(String wordString) {
		if (wordString.trim().isEmpty())
			return;
		Long count = 1l;
		if (wordMap.containsKey(wordString)) {
			count = wordMap.get(wordString) + 1l;
		}
		wordMap.put(wordString, count);
	}

	public static void testSegment(String wordString) {
		Result result = DicAnalysis.parse(wordString); // 分词结果的一个封装，主要是一个List<Term>的terms
		System.out.println(wordString);

		List<Term> terms = result.getTerms(); // 拿到terms
		for (int i = 0; i < terms.size(); i++) {
			String word = terms.get(i).getName(); // 拿到词
			String natureStr = terms.get(i).getNatureStr(); // 拿到词性
			if (expectedNature.contains(natureStr)) {
				System.out.print(word + "  ");
			}
		}
		System.out.println();
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

	// 查询
	private static void query() {
		String slqString = "select street,community,road,road_num," + "village,building,address from "
				+ strTableName;
		ResultSet rs = null;
		String str1, str2, str3, str4, str5, str6, str7, str8, str9, str10;
		try {
			rs = statement.executeQuery(slqString);
			while (rs.next()) {
				// str1 = rs.getString("province");
				// str2 = rs.getString("city");
				//str3 = rs.getString("district");
				str4 = rs.getString("street");
				str5 = rs.getString("community");
				str6 = rs.getString("road");
				str7 = rs.getString("road_num");
				str8 = rs.getString("village");
				str9 = rs.getString("building");
				str10 = rs.getString("address");
				// addWord(str1);
				// addWord(str2);
				//addWord(str3);
				addWord(str4);
				addWord(str5);
				addWord(str6);
				addWord(str7);
				addWord(str8);
				addWord(str9);
				if (str10.trim().isEmpty()) {
					continue;
				}
				testSegment(str10);
			}

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

	/**
	 * <!-- 一个参数为标准地址sqlite数据库名-->
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为1
		if (args.length != 1) {
			System.exit(0);
		}

		strConn += args[0];
		openDatabase();
		query();
	//	createDictionary();
		closeDatabase();
	}

}
