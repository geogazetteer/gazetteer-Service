package top.geomatics.gazetteer.segment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;


//对enterprise1的
public class EnterpriseSegment {
	
	private static Connection connection = null;
	private static Statement statement = null;
	private static String strConn = "jdbc:sqlite:E:\\longhua\\gezetteer\\data\\sqlitedb\\enterprise1.db";// 数据库地址
	private static String strTableName = "dmdz";
	
	
	private static CSVWriter csvWriter = null;
	private static String FilePath = "E:\\longhua\\wordSegFileRR.csv";//输出的文件地址
	private static File file = new File(FilePath);
	
	private static List<String []> wordSegments = new ArrayList<String []>();
	
	
	
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
	
	public static void outputResult(List<String[]> wordSegments) {
		
		
		try {
			csvWriter = (CSVWriter) new CSVWriterBuilder(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")).withSeparator('\t')
					.build();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<wordSegments.size();i++) {
		csvWriter.writeNext(wordSegments.get(i));
		}
		
	}
	
	public static String[] segment(String wordString) {
		
		WordSegmenter seg = new WordSegmenter();
		List<WordEntry> result = new ArrayList<WordEntry>();
		result = seg.segment(wordString);
		String[] words = new String[100];
		for (int i =0; i<result.size();i++) {
			WordEntry word = new WordEntry();
			word = result.get(i);
			String wordName = word.getName(); // 拿到词
			words[i] = wordName;
		}
		
		
		return words;
	}
	
	private static void query() {
		
		String segmentStr[] = null;
		String sqlString = "select jycs from " + strTableName ;
		ResultSet rs = null;
		String str;
		try {
			rs = statement.executeQuery(sqlString);
			while(rs.next()) {
				str = rs.getString("jycs");
				segmentStr = segment(str);
				wordSegments.add(segmentStr);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		openDatabase();
		query();
		outputResult(wordSegments);
		closeDatabase();
		

	}

}
