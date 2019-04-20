package top.geomatics.gazetteer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.wltea.analyzer.lucene.IKAnalyzer;

import top.geomatics.gazetteer.config.ResourcesManager;
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
	private static IndexSearcher indexSearcher;
	private static final String ADDRESS_ID = "id";
	private static final String ADDRESS = "address";
	private static Map<String, String> mapQuery;
	private static Integer total;
	static {
		try {
			mapQuery = new HashMap<String, String>();
			indexSearcher = init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static QueryParser queryParser = new QueryParser(Version.LUCENE_47, ADDRESS, new IKAnalyzer(true));

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

	/**
	 * 分页搜索
	 * 
	 * @param keywords
	 * @param pageNow
	 * @param pageSize
	 * @return
	 */
	public static Map<String, Object> searchByPage(String keywords, int pageNow, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			   判断是否已经从这条关键词走过如果走过就不需要再查总数量了
			if (mapQuery.get(keywords) == null) {
				mapQuery = new HashMap<String, String>();
				mapQuery.put(keywords, "true");
				Query query = queryParser.parse(keywords);
				TopDocs topDocs1 = indexSearcher.search(query, 100000000);
				total = topDocs1.totalHits;
			}
			List<SimpleAddressRow> list = new ArrayList<SimpleAddressRow>();
			int start = (pageNow - 1) * pageSize;
			// 查询数据， 结束页面自前的数据都会查询到，但是只取本页的数据
			Query query = queryParser.parse(keywords);
			map.put("total", total);
			TopDocs topDocs = indexSearcher.search(query, start);
			// 获取到上一页最后一条
			ScoreDoc preScore = topDocs.scoreDocs[start - 1];

			// 查询最后一条后的数据的一页数据
			topDocs = indexSearcher.searchAfter(preScore, query, pageSize);

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				SimpleAddressRow row = new SimpleAddressRow();
				row.setId(Integer.parseInt(doc.get(ADDRESS_ID)));
				row.setAddress(doc.get(ADDRESS));
				list.add(row);
			}
			map.put("datalist", list);
			Long end = System.currentTimeMillis();
			System.out.println("lucene wasted time: " + (end - start) + "ms");

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;

		/*
		 * ScoreDoc[] scores = topDocs.scoreDocs; System.out.println("查询到的条数\t" +
		 * topDocs.totalHits); //读取数据 for (int i = 0; i < scores.length; i++) { Document
		 * doc = reader.document(scores[i].doc); System.out.println(doc.get("id") + ":"
		 * + doc.get("username") + ":" + doc.get("email")); } } catch (Exception e) {
		 * e.printStackTrace(); } finally { coloseReader(reader); }
		 */
	}

	public static void main(String[] args) {
		Map<String, Object> map = searchByPage("民治街道", 10, 10);
		List<SimpleAddressRow> s = (List<SimpleAddressRow>) map.get("datalist");
		for (SimpleAddressRow sp : s) {
			System.out.println(sp.getAddress());
			System.out.println(sp.getId());
		}
	}
}
