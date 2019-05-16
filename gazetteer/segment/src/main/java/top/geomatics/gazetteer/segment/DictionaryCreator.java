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
	private static final String SEGMENT_DICTIONARY_PATH = Messages.getString("DictionaryCreator.0"); //$NON-NLS-1$

	// 保存词典及词频，不重复
	private static Map<String, String[]> wordMap = null;
	// 数据库连接
	private static DatabaseHelper helper = null;
	private static SqlSession session = null;
	private static AddressMapper mapper = null;
	// 输出词典文件
	private static String fileNString = Messages.getString("DictionaryCreator.1"); //$NON-NLS-1$
	private static File file = null;
	private static CSVWriter csvWriter = null;
	// 数据库中记录数
	private static int count = 0;

	/**
	 * <b>打开、查询数据库，获得词根</b><br>
	 * 
	 */
	private static void openDatabase() {
		wordMap = new HashMap<String, String[]>();
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
		fileNString = fileNString + File.separator + Messages.getString("DictionaryCreator.2"); //$NON-NLS-1$
		file = new File(fileNString);

		try {
			csvWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(file)).withSeparator('\t').withLineEnd(Messages.getString("DictionaryCreator.3")) //$NON-NLS-1$
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("DictionaryCreator.4"), fileNString); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		// 加一些固定的词
		String nextLine[] = new String[3];
		nextLine[0] = Messages.getString("DictionaryCreator.5"); //$NON-NLS-1$
		nextLine[1] = Messages.getString("DictionaryCreator.6");// 自定义的词性，表示省 //$NON-NLS-1$
		nextLine[2] = String.format(Messages.getString("DictionaryCreator.7"), count); //$NON-NLS-1$
		csvWriter.writeNext(nextLine, false);

		nextLine[0] = Messages.getString("DictionaryCreator.8"); //$NON-NLS-1$
		nextLine[1] = Messages.getString("DictionaryCreator.9");// 自定义的词性，表示市 //$NON-NLS-1$
		nextLine[2] = String.format(Messages.getString("DictionaryCreator.10"), count); //$NON-NLS-1$
		csvWriter.writeNext(nextLine, false);

		nextLine[0] = Messages.getString("DictionaryCreator.11"); //$NON-NLS-1$
		nextLine[1] = Messages.getString("DictionaryCreator.12");// 自定义的词性，表示区 //$NON-NLS-1$
		nextLine[2] = String.format(Messages.getString("DictionaryCreator.13"), count); //$NON-NLS-1$
		csvWriter.writeNext(nextLine, false);

		for (String keyString : wordMap.keySet()) {
			// 非汉字不管
//			if (keyString.matches("[^\\u4e00-\\u9fa5]")) {
//				continue;
//			}
			String[] word = wordMap.get(keyString);
			nextLine[0] = keyString;
			nextLine[1] = word[0];
			nextLine[2] = word[1];
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
			addWord(row.getStreet(), Messages.getString("DictionaryCreator.14")); //$NON-NLS-1$
			addWord(row.getCommunity(), Messages.getString("DictionaryCreator.15")); //$NON-NLS-1$
			addWord(row.getRoad(), Messages.getString("DictionaryCreator.16")); //$NON-NLS-1$
			addWord(row.getRoad_num(), Messages.getString("DictionaryCreator.17")); //$NON-NLS-1$
			addWord(row.getVillage(), Messages.getString("DictionaryCreator.18")); //$NON-NLS-1$
			addWord(row.getBuilding(), Messages.getString("DictionaryCreator.19")); //$NON-NLS-1$
		}

	}

	/**
	 * <b>添加词根，并统计词频，保存在内存</b><br>
	 * 
	 * @param wordString 词根
	 */
	private static void addWord(String wordString, String wordNature) {
		if (wordString.trim().isEmpty())
			return;
		String wordProperty[] = wordMap.get(wordString);
		String wordNew[] = null;
		Long count = 1L;
		if (!wordMap.containsKey(wordString) || null == wordProperty) {
			wordNew = new String[2];
			wordNew[0] = wordNature;
			wordNew[1] = String.format(Messages.getString("DictionaryCreator.20"), count); //$NON-NLS-1$
		} else {
			wordNew = wordProperty;
			wordNew[1] = String.format(Messages.getString("DictionaryCreator.21"), Integer.parseInt(wordNew[1]) + 1); //$NON-NLS-1$
		}
		wordMap.put(wordString, wordNew);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		openDatabase();
		createDictionary();
		String logMsgString = String.format(Messages.getString("DictionaryCreator.22"), fileNString); //$NON-NLS-1$
		logger.info(logMsgString);
	}

}
