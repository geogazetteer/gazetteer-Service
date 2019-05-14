package top.geomatics.gazetteer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.wltea.analyzer.lucene.IKAnalyzer;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.EnterpriseRow;

/**
 * <b>建立地名索引</b>
 * 
 * @author whudyj
 *
 */
public class GeoNameIndexer {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(GeoNameIndexer.class);

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = "geoname_index_path";

	@Value("${index.path}")
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	private static Directory dir;
	private static final String GEONAME = "name";
	private static final String ADDRESS = "address";
	private static final String SELECT_FIELDS = "name,address";
	private static EnterpriseDatabaseHelper helper = new EnterpriseDatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static EnterpriseAddressMapper mapper = session.getMapper(EnterpriseAddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();

	/**
	 *  <b>准备建立索引</b><br>
	 *  
	 * @return IndexWriter 索引输出
	 */
	private static IndexWriter getWriter()  {
		Analyzer analyzer = new IKAnalyzer(true);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		iwc.setRAMBufferSizeMB(16);
		IndexWriter writer =null;
		try {
			dir = FSDirectory.open(new File(INDEX_PATH));
			writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}

	/**
	 * <b>更新索引</b>
	 * 
	 */
	@Scheduled(cron = "0 0 8 1 * ? ")
	public static void updateIndex() {
		
		IndexWriter writer = getWriter();
		try {
			writer.deleteAll();
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format("删除索引目录：%s 失败", dir.toString());
			logger.error(logMsgString);
		}

		map.put("sql_fields", SELECT_FIELDS);
		for (int i = 1; i < 5; i++) {
			String tableNameString = "enterprise" + i;
			map.put("sql_tablename", tableNameString);

			List<EnterpriseRow> rows = mapper.findEquals(map);

			for (EnterpriseRow row : rows) {
				Document doc = new Document();
				doc.add(new TextField(GEONAME, row.getName(), Field.Store.YES));
				doc.add(new StringField(ADDRESS, row.getAddress(), Field.Store.YES));
				try {
					writer.addDocument(doc);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
}
