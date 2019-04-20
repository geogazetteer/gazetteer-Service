package top.geomatics.gazetteer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.scheduling.annotation.Scheduled;
import org.wltea.analyzer.lucene.IKAnalyzer;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.utilities.database.csv.MYCSVReader;

/**
 * <em>建立POI的索引</em>
 * 
 * @author whudyj
 *
 */
public class POIIndexer {

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
			writer.addDocument(doc);
		}

		csv.closeFile();

		writer.close();
	}

	public static void main(String[] args) {
		try {
			POIIndexer.updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
