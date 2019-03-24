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
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.wltea.analyzer.lucene.IKAnalyzer;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;

/**
 * <em>lucene工具类</em>
 * 
 * @author whudyj
 *
 */
public class LuceneUtil {
	@Value("${index.path}")
	public static String INDEX_PATH;
	private static Directory dir;

	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * @return IndexWriter
	 * @throws Exception 异常
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
		dir = FSDirectory.open(new File("D:\\data\\lucene_index"));
		IndexWriter writer = getWriter();
		writer.deleteAll();

		map.put("sql_fields", "code,address");
		map.put("sql_tablename", "dmdz");

		List<AddressRow> rows = mapper.findEquals(map);

		for (AddressRow row : rows) {
			Document doc = new Document();
			doc.add(new StringField("code", row.getCode(), Field.Store.YES));
			doc.add(new TextField("address", row.getAddress(), Field.Store.YES));
			writer.addDocument(doc);
		}
		writer.close();
	}

	private static IndexSearcher init() throws IOException {
		IndexSearcher indexSearcher = null;
		if (indexSearcher == null) {
			Directory directory = FSDirectory.open(new File("D:\\data\\lucene_index"));
			DirectoryReader directoryReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(directoryReader);
		}
		return indexSearcher;
	}

	public static List<String> search(String keyWord) {
		List<String> list = new ArrayList<String>();
		try {
			IndexSearcher indexSearcher = init();
			QueryParser queryParser = new QueryParser(Version.LUCENE_47, "address", new IKAnalyzer(true));
//        String q="select ADDRESS from dmdz where ADDRESS like '%"+keyWord+"%' limit 0,10";
			org.apache.lucene.search.Query query = queryParser.parse(keyWord);
			Long start = System.currentTimeMillis();
			TopDocs topDocs = indexSearcher.search(query, 10);
			Long end = System.currentTimeMillis();
			System.out.println("wasted time: " + (end - start));
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				list.add(doc.get("address"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
