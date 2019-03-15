/**
 * 
 */
package top.geomatics.gazetteer.segment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;

/**
 * <!--创建地名地址分词词典-->
 * 
 * @author whudyj
 *
 */
public class DictionaryCreator {
	// 保存词典及词频，不重复
	private static Map<String, Long> wordMap = null;
	// 数据库连接
	private static DatabaseHelper helper = null;
	private static SqlSession session = null;
	private static AddressMapper mapper = null;
	// 输出词典文件
	private static String fileNString = "";
	private static File file = null;
	private static CSVWriter csvWriter = null;

	/**
	 * 打开、查询数据库
	 */
	private static void openDatabase() {
		wordMap = new HashMap<String, Long>();
		// 连接数据库
		helper = new DatabaseHelper();
		session = helper.getSession();
		mapper = session.getMapper(AddressMapper.class);
		// 查询
		query();
	}

	/**
	 * 输出词典
	 */
	public static void createDictionary() {
		// 输出词典文件
		fileNString = DictionaryCreator.class.getResource("/").getPath();
		fileNString += "userLibrary.dic";
		file = new File(fileNString);

		try {
			csvWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(file)).withSeparator('\t').withLineEnd("\r\t")
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加一些固定的词
		String nextLine[] = new String[3];
		nextLine[0] = "广东省";
		nextLine[1] = "pron";// 自定义的词性，表示省
		nextLine[2] = "10000";
		csvWriter.writeNext(nextLine, false);
		nextLine[0] = "深圳市";
		nextLine[1] = "city";// 自定义的词性，表示市
		nextLine[2] = "8000";
		nextLine[0] = "龙华区";
		nextLine[1] = "dict";// 自定义的词性，表示区
		nextLine[2] = "5000";
		csvWriter.writeNext(nextLine, false);

		// 缺省词性为
		String wordproperty = "n";
		for (String keyString : wordMap.keySet()) {
			//数字、字母、下划线不管
			if (keyString.matches("/[0-9()]")) {
				continue;
			}
			nextLine[0] = keyString;
			if (-1 != keyString.lastIndexOf("社区")) {
				wordproperty = "comm";
			} else if (-1 != keyString.lastIndexOf("街道")) {
				wordproperty = "street";
			} else if (-1 != keyString.lastIndexOf("路") || -1 != keyString.lastIndexOf("道")) {
				wordproperty = "road";
			} else if (-1 != keyString.lastIndexOf("村") || -1 != keyString.lastIndexOf("小区")) {
				wordproperty = "vill";
			} else if (-1 != keyString.lastIndexOf("楼") || -1 != keyString.lastIndexOf("大厦")) {
				wordproperty = "build";
			}
			nextLine[1] = wordproperty;
			nextLine[2] = wordMap.get(keyString).toString();
			csvWriter.writeNext(nextLine, false);
		}
		try {
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取出标准地址数据库中一些字段的值作为词典的词
	 */
	private static void query() {
		List<AddressRow> rows = null;
		try {
			rows = mapper.selectAddressForDictionary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AddressRow row : rows) {
			addWord(row.getStreet());
			addWord(row.getCommunity());
			addWord(row.getRoad());
			addWord(row.getRoad_num());
			addWord(row.getVillage());
		}

	}

	/**
	 * @param wordString
	 */
	public static void addWord(String wordString) {
		if (wordString.trim().isEmpty())
			return;
		Long count = 1L;
		if (wordMap.containsKey(wordString)) {
			count = wordMap.get(wordString) + 1L;
		}
		wordMap.put(wordString, count);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		openDatabase();
		createDictionary();
	}

}
