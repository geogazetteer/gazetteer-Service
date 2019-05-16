package top.geomatics.gazetteer.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.ansj.lucene7.AnsjAnalyzer;
import org.ansj.lucene7.AnsjAnalyzer.TYPE;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.wltea.analyzer.lucene.IKAnalyzer;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.utilities.csv.MYCSVReader;

/**
 * <em>建立POI的索引</em>
 * 
 * @author whudyj
 *
 */
public class POIIndexer {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(POIIndexer.class);

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = "poi_index_path";
	private static final String POI_FILE = "poi_file";

	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	private static String poi_file_name = manager.getValue(POI_FILE);
	private static Directory dir;
	private static final String GEONAME = "name";
	private static final String ADDRESS = "address";
	public static MYCSVReader csv = new MYCSVReader(poi_file_name);

	/**
	 * <b>准备建立索引</b><br>
	 * 
	 * @return IndexWriter 索引输出
	 */
	private static IndexWriter getWriter() {
		Analyzer analyzer = new AnsjAnalyzer(TYPE.query_ansj);
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setRAMBufferSizeMB(16);
		IndexWriter writer = null;
		try {
			dir = FSDirectory.open(Path.of(INDEX_PATH));
			writer = new IndexWriter(dir, iwc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}

	/**
	 * <em>更新索引</em>
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

		List<String[]> dataList = null;
		csv.openFile();
		dataList = csv.getData();

		for (String[] row : dataList) {
			if (row.length < 2) {
				continue;
			}
			String first = row[0];
			String temp[] = first.split("\\p{Blank}+");
			if (temp.length < 2) {
				continue;
			}
			String addressString = row[1].trim();
			Document doc = new Document();
			doc.add(new TextField(GEONAME, temp[1], Field.Store.YES));
			doc.add(new StringField(ADDRESS, addressString, Field.Store.YES));
			try {
				writer.addDocument(doc);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}

		csv.closeFile();

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
