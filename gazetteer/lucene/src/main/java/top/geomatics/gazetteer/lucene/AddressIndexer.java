package top.geomatics.gazetteer.lucene;

import java.io.File;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.wltea.analyzer.lucene.IKAnalyzer;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * <em>建立地址索引</em>
 * 
 * @author whudyj
 *
 */
public class AddressIndexer {

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = "lucene_index_path";

	@Value("${index.path}")
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	private static Directory dir;
	private static final String ADDRESS_ID = "id";
	private static final String ADDRESS = "address";
	private static final String SELECT_FIELDS = "id,address";
	private static final String TABLE_NAME = "dmdz";
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * @return IndexWriter
	 * @throws Exception 异常 注释
	 */
	private static IndexWriter getWriter() throws Exception {
		Analyzer analyzer = new IKAnalyzer(true);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		iwc.setRAMBufferSizeMB(16);
		IndexWriter writer = new IndexWriter(dir, iwc);
		return writer;
	}

	/**
	 * <em>更新索引</em>
	 * 
	 * @throws Exception 异常
	 */
	@Scheduled(cron = "0 0 8 1 * ? ")
	public static void updateIndex() throws Exception {
		dir = FSDirectory.open(new File(INDEX_PATH));
		IndexWriter writer = getWriter();
		writer.deleteAll();

		map.put("sql_fields", SELECT_FIELDS);
		map.put("sql_tablename", TABLE_NAME);

		List<SimpleAddressRow> rows = mapper.findSimpleEquals(map);

		for (SimpleAddressRow row : rows) {
			Document doc = new Document();
			doc.add(new StringField(ADDRESS_ID, row.getId().toString(), Field.Store.YES));
			doc.add(new TextField(ADDRESS, row.getAddress(), Field.Store.YES));
			writer.addDocument(doc);
		}
		writer.close();
	}

	public static void main(String[] args) {
		try {
			AddressIndexer.updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
