package top.geomatics.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.jumpmind.symmetric.csv.CsvReader;

//将csv文件导入数据库
public class CsvUtil {
	/**
	 * 导入CSV文件
	 * 
	 * @param con         数据库
	 * @param csvFilePath 文件路径
	 */
	public String readCsv(String csvFilePath, Connection con) {
		try {
			String sql = "INSERT INTO user(id,name,sex) VALUES(?,?,?)";
			PreparedStatement pStatement = con.prepareStatement(sql);
			ArrayList<String[]> csvList = new ArrayList<String[]>(); // 用来保存数据
			// 生成CsvReader对象，以，为分隔符，utf-8编码方式
			CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("utf-8")); // 一般用这编码读就可以了
			reader.readHeaders(); // 跳过表头 如果需要表头的话，不用这句
			// 逐条读取记录，直至读完
			while (reader.readRecord()) {
				csvList.add(reader.getValues());
			}
			reader.close();
			for (int row = 0; row < csvList.size(); row++) {
				String cell = csvList.get(row)[0]; // 取得第row行第0列的数据
				pStatement.setString(1, csvList.get(row)[0]);
				pStatement.setString(2, csvList.get(row)[1]);
				pStatement.setString(3, csvList.get(row)[2]);
				pStatement.execute();
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return "";

	}

	public String write() throws Exception {
		// 设置输出的文件路径
		File writeFile = new File("write.csv");
		try {
			// 通过BufferedReader类创建一个使用默认大小输出缓冲区的缓冲字符输出流
			BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
			// 将文档的下一行数据赋值给lineData，并判断是否为空，若不为空则输出
			for (int i = 1; i <= 10; i++) {
				writeText.newLine(); // 换行
				// 调用write的方法将字符串写到流中
				writeText.write("新用户" + i + ",男," + (18 + i));
			}
			writeText.flush();
			writeText.close();
		} catch (FileNotFoundException e) {
			System.out.println("没有找到指定文件");
		} catch (IOException e) {
			System.out.println("文件读写出错");
		}
		return "";
	}

}
