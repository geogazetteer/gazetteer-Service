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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;

/**
 * <b>创建地名地址分词词典</b><br>
 * 
 * @author whudyj
 *
 */
public class DictionaryCreator {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(DictionaryCreator.class);
	// 分词词典文件路径
	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String SEGMENT_DICTIONARY_PATH = "segment_dictionary_path";

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
	// 数据库中记录数
	private static int count = 0;

	/**
	 * <b>打开、查询数据库，获得词根</b><br>
	 * 
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
	 * <b>输出词典到文件</b><br>
	 * 
	 */
	private static void createDictionary() {
		// 输出词典文件
		fileNString = manager.getValue(SEGMENT_DICTIONARY_PATH);
		fileNString = fileNString + File.separator + "userLibrary.dic";
		file = new File(fileNString);

		try {
			csvWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(file)).withSeparator('\t').withLineEnd("\r\n")
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format("打开词典文件： %s 失败！", fileNString);
			logger.error(logMsgString);
		}
		// 加一些固定的词
		String nextLine[] = new String[3];
		nextLine[0] = "广东省";
		nextLine[1] = "pron";// 自定义的词性，表示省
		nextLine[2] = String.format("%d", count);
		csvWriter.writeNext(nextLine, false);

		nextLine[0] = "深圳市";
		nextLine[1] = "city";// 自定义的词性，表示市
		nextLine[2] = String.format("%d", count);
		csvWriter.writeNext(nextLine, false);

		nextLine[0] = "龙华区";
		nextLine[1] = "dict";// 自定义的词性，表示区
		nextLine[2] = String.format("%d", count);
		csvWriter.writeNext(nextLine, false);

		// 缺省词性为
		String wordproperty = "n";
		for (String keyString : wordMap.keySet()) {
			// 数字、字母、下划线不管
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
			logger.error(e.getMessage());
		}
	}

	/**
	 * <b>取出标准地址数据库中一些字段的值作为词典的词根</b><br>
	 * 
	 */
	private static void query() {
		List<AddressRow> rows = null;
		try {
			// SELECT street,community,road,road_num,village,building,address FROM dmdz
			rows = mapper.selectAddressForDictionary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		count = rows.size();
		for (AddressRow row : rows) {
			addWord(row.getStreet());
			addWord(row.getCommunity());
			addWord(row.getRoad());
			addWord(row.getRoad_num());
			addWord(row.getVillage());
		}

	}

	/**
	 * <b>添加词根，并统计词频，保存在内存</b><br>
	 * 
	 * @param wordString 词根
	 */
	private static void addWord(String wordString) {
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
		String logMsgString = String.format("创建词典文件： %s 完成！", fileNString);
		logger.info(logMsgString);
	}

}
