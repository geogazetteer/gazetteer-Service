package top.geomatics.gazetteer.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.lucene7.AnsjAnalyzer;
import org.ansj.lucene7.AnsjAnalyzer.TYPE;
import org.apache.ibatis.session.SqlSession;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.SimpleAddressRow;
import top.geomatics.gazetteer.segment.WordSegmenter;

/**
 * <b>建立地址索引</b>
 * 
 * @author whudyj
 *
 */
public class AddressIndexer {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(AddressIndexer.class);

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = Messages.getString("AddressIndexer.0"); //$NON-NLS-1$
	private static final String LUCENE_INDEX_PATH_PINYIN = Messages.getString("AddressIndexer.1"); //$NON-NLS-1$

	@Value("${index.path}")
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	public static String INDEX_PATH_PINYIN = manager.getValue(LUCENE_INDEX_PATH_PINYIN);
	private static Directory dir;
	private static Directory dir_pinyin;
	private static IndexWriter writer = null;
	private static IndexWriter writer_pinyin = null;

	private static final String ADDRESS_ID = Messages.getString("AddressIndexer.2"); //$NON-NLS-1$
	private static final String ADDRESS = Messages.getString("AddressIndexer.3"); //$NON-NLS-1$
	private static final String ADDRESSPINYIN = Messages.getString("AddressIndexer.4"); //$NON-NLS-1$
	private static final String SELECT_FIELDS = Messages.getString("AddressIndexer.5"); //$NON-NLS-1$

	private static final String TABLE_NAME = Messages.getString("AddressIndexer.6"); //$NON-NLS-1$
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * <b>准备建立索引</b><br>
	 * 
	 */
	static {
		Analyzer analyzer = new AnsjAnalyzer(TYPE.query_ansj);
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setRAMBufferSizeMB(16);
		try {
			dir = FSDirectory.open(Path.of(INDEX_PATH));
			writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("AddressIndexer.7"), dir.toString()); //$NON-NLS-1$
			logger.error(logMsgString);
		}
	}
	static {
		Analyzer analyzer = new AnsjAnalyzer(TYPE.query_ansj);
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setRAMBufferSizeMB(16);
		try {
			dir_pinyin = FSDirectory.open(Path.of(INDEX_PATH_PINYIN));
			writer_pinyin = new IndexWriter(dir_pinyin, iwc);
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("AddressIndexer.8"), dir_pinyin.toString()); //$NON-NLS-1$
			logger.error(logMsgString);
		}
	}

	/**
	 * <b>更新索引</b>
	 * 
	 */
	@Scheduled(cron = "0 0 8 1 * ? ")
	public static void updateIndex() {
		try {
			writer.deleteAll();
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("AddressIndexer.9"), dir.toString()); //$NON-NLS-1$
			logger.error(logMsgString);
		}

		// 从数据库中查询
		map.put(Messages.getString("AddressIndexer.10"), SELECT_FIELDS); //$NON-NLS-1$
		map.put(Messages.getString("AddressIndexer.11"), TABLE_NAME); //$NON-NLS-1$
		List<SimpleAddressRow> rows = mapper.findSimpleEquals(map);

		for (SimpleAddressRow row : rows) {
			Document doc = new Document();
			String address = row.getAddress();
			doc.add(new StringField(ADDRESS_ID, row.getId().toString(), Field.Store.YES));
			doc.add(new TextField(ADDRESS, address, Field.Store.YES));
			try {
				writer.addDocument(doc);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * <b>更新索引（含拼音）</b>
	 * 
	 */
	@Scheduled(cron = "0 0 8 1 * ? ")
	public static void updateIndexPinyin() {
		try {
			writer_pinyin.deleteAll();
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("AddressIndexer.12"), dir_pinyin.toString()); //$NON-NLS-1$
			logger.error(logMsgString);
		}

		// 从数据库中查询
		map.put(Messages.getString("AddressIndexer.13"), SELECT_FIELDS); //$NON-NLS-1$
		map.put(Messages.getString("AddressIndexer.14"), TABLE_NAME); //$NON-NLS-1$
		List<SimpleAddressRow> rows = mapper.findSimpleEquals(map);

		for (SimpleAddressRow row : rows) {
			Document doc = new Document();
			String address = row.getAddress();
			// 拼音处理
			List<String> pinyinAddress = addressPinyin(address);

			doc.add(new StringField(ADDRESS_ID, row.getId().toString(), Field.Store.YES));
			doc.add(new TextField(ADDRESS, address, Field.Store.YES));

			for (String str : pinyinAddress) {
				doc.add(new StringField(ADDRESSPINYIN, str, Field.Store.YES));
			}
			try {
				writer_pinyin.addDocument(doc);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		try {
			writer_pinyin.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * <b>汉字转为拼音</b><br>
	 * 
	 * @param chinese 汉字
	 * @return 转换后的拼音
	 */
	public static String toPinyin(String chinese) {
		String pinyinStr = Messages.getString("AddressIndexer.15"); //$NON-NLS-1$
		char[] newChar = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < newChar.length; i++) {
			if (newChar[i] > 128) {
				try {
					pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			} else {
				pinyinStr += newChar[i];
			}
		}
		return pinyinStr;
	}

	/**
	 * <b>地址分词，并转拼音</b><br>
	 * 
	 * @param address 地址
	 * @return 分词后的地址拼音
	 */
	private static List<String> addressPinyin(String address) {
		List<String> list = WordSegmenter.segment(address);
		List<String> pinyinAddress = new ArrayList<String>();
		for (String s : list) {
			String regularString = handleSpecialChar(s);
			pinyinAddress.add(toPinyin(regularString));
		}
		return pinyinAddress;
	}

	/**
	 * <b>去掉非汉字字符</b><br>
	 * 
	 * @param specialChar 含有特殊字符的汉字文本
	 * @return 规则化后的汉字
	 */
	public static String handleSpecialChar(String specialChar) {
		String regularChar = specialChar;
		regularChar = regularChar.replaceAll("[^\u4e00-\u9fa5]", Messages.getString("AddressIndexer.17")); //$NON-NLS-1$ //$NON-NLS-2$
		return regularChar;

	}

	/**
	 * <b>主方法，创建（更新）索引</b><br>
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		logger.info(Messages.getString("AddressIndexer.18")); //$NON-NLS-1$
		AddressIndexer.updateIndex();
		logger.info(Messages.getString("AddressIndexer.19")); //$NON-NLS-1$
		AddressIndexer.updateIndexPinyin();
		logger.info(Messages.getString("AddressIndexer.20")); //$NON-NLS-1$
		GeoNameIndexer.updateIndex();
		logger.info(Messages.getString("AddressIndexer.21")); //$NON-NLS-1$
		POIIndexer.updateIndex();

	}

}
