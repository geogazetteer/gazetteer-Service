package top.geomatics.gazetteer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
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
 * <em>lucene工具类</em>
 * 
 * @author whudyj
 *
 */
public class LuceneUtil {

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = "lucene_index_path";

	@Value("${index.path}")
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	private static Directory dir;
	private static IndexSearcher indexSearcher;
	private static final String ADDRESS_ID = "id";
	private static final String ADDRESS = "address";
	private static final String SELECT_FIELDS = "id,address";
	private static final String TABLE_NAME = "dmdz";

	static {
		try {
			indexSearcher = init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static QueryParser queryParser = new QueryParser(Version.LUCENE_47, ADDRESS, new IKAnalyzer(true));

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

	private static IndexSearcher init() throws IOException {
		IndexSearcher indexSearcher = null;
		if (indexSearcher == null) {

			Directory directory = FSDirectory.open(new File(INDEX_PATH));
			DirectoryReader directoryReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(directoryReader);
		}
		return indexSearcher;
	}

	/**
	 * <em> 根据关键词进行搜索，支持多个关键词模糊搜索</em>
	 * 
	 * @param keywords String 搜索关键词，多个关键词以空格分隔
	 * @param maxHits  int 搜索词的最大个数
	 * @return List 返回一个简单地址数组
	 */
	public static List<SimpleAddressRow> search(String keywords, int maxHits) {
		List<SimpleAddressRow> list = new ArrayList<SimpleAddressRow>();
		try {
//			String words[] = keywords.split(" ");
//			String queryString = "";
//			for (int i = 0; i < words.length; i++) {
//				queryString += words[i];
//				if (i < words.length - 1) {
//					queryString += "~ OR ";// 多个关键词模糊查询
//				}
//			}
			Query query = queryParser.parse(keywords);
			Long start = System.currentTimeMillis();
			TopDocs topDocs = indexSearcher.search(query, maxHits);

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				SimpleAddressRow row = new SimpleAddressRow();
				row.setId(Integer.parseInt(doc.get(ADDRESS_ID)));
				row.setAddress(doc.get(ADDRESS));
				list.add(row);
			}
			Long end = System.currentTimeMillis();
			System.out.println("lucene wasted time: " + (end - start) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
