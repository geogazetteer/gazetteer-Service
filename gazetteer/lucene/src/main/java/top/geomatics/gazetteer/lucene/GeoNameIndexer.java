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
import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.EnterpriseRow;

/**
 * <em>建立地名索引</em>
 * 
 * @author whudyj
 *
 */
public class GeoNameIndexer {

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
		for (int i = 1; i < 5; i++) {
			String tableNameString = "enterprise" + i;
			map.put("sql_tablename", tableNameString);

			List<EnterpriseRow> rows = mapper.findEquals(map);

			for (EnterpriseRow row : rows) {
				Document doc = new Document();
				doc.add(new TextField(GEONAME, row.getName(), Field.Store.YES));
				doc.add(new StringField(ADDRESS, row.getAddress(), Field.Store.YES));
				writer.addDocument(doc);
			}
		}

		writer.close();
	}

	public static void main(String[] args) {
		try {
			GeoNameIndexer.updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
