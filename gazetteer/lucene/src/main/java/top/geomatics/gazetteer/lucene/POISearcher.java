package top.geomatics.gazetteer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * <em>POI搜索</em>
 * 
 * @author whudyj
 *
 */
public class POISearcher {

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = "poi_index_path";
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	private static IndexSearcher indexSearcher;
	private static final String GEONAME = "name";
	private static final String ADDRESS = "address";

	static {
		try {
			indexSearcher = init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static QueryParser queryParser = new QueryParser(Version.LUCENE_47, GEONAME, new IKAnalyzer(true));

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
	public static List<EnterpriseRow> query(String keywords, int maxHits) {
		List<EnterpriseRow> list = new ArrayList<EnterpriseRow>();
		try {
			Query query = queryParser.parse(keywords);
			TopDocs topDocs = indexSearcher.search(query, maxHits);

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				EnterpriseRow row = new EnterpriseRow();
				row.setName(doc.get(GEONAME));
				row.setAddress(doc.get(ADDRESS));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<SimpleAddressRow> search(String keywords, int maxHits) {
		List<EnterpriseRow> rows = POISearcher.query(keywords, maxHits);
		List<SimpleAddressRow> simpleAddressRows = new ArrayList<SimpleAddressRow>();
		boolean flag = false;
		for (EnterpriseRow row : rows) {
			if (flag) {
				break;
			}
			List<SimpleAddressRow> srows = LuceneUtil.search(row.getAddress(), maxHits);
			for (SimpleAddressRow aRow : srows) {
				if (simpleAddressRows.size() >= maxHits) {
					flag = true;
					break;
				}
				simpleAddressRows.add(aRow);
			}
		}
		return simpleAddressRows;
	}

	public static void main(String[] args) {
		String strQuery = "肥佬餐馆";
		List<EnterpriseRow> eRows = POISearcher.query(strQuery, 10);
		for (EnterpriseRow row : eRows) {
			System.out.println(row.getName());
			System.out.println(row.getAddress());
		}
		List<SimpleAddressRow> list = POISearcher.search(strQuery, 10);
		for (SimpleAddressRow sp : list) {
			System.out.println(sp.getAddress());
			System.out.println(sp.getId());
		}

	}
}
