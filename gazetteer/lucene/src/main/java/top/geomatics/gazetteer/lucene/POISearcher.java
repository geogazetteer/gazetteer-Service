package top.geomatics.gazetteer.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.lucene7.AnsjAnalyzer;
import org.ansj.lucene7.AnsjAnalyzer.TYPE;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * <b>POI搜索</b>
 * 
 * @author whudyj
 *
 */
public class POISearcher {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(POISearcher.class);

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String POI_INDEX_PATH = Messages.getString("POISearcher.0"); //$NON-NLS-1$
	public static String INDEX_PATH = manager.getValue(POI_INDEX_PATH);
	private static IndexSearcher indexSearcher = null;
	private static final String GEONAME = Messages.getString("POISearcher.1"); //$NON-NLS-1$
	private static final String ADDRESS = Messages.getString("POISearcher.2"); //$NON-NLS-1$
	private static final int MAX_HITS = 10000;

	private static Map<String, TopDocs> docsMap = null;
	private static Map<String, List<SimpleAddressRow>> rows_map = null;
	private static QueryParser queryParser = new QueryParser(GEONAME, new AnsjAnalyzer(TYPE.query_ansj));

	static {
		if (indexSearcher == null) {

			Directory directory = null;
			DirectoryReader directoryReader = null;
			try {
				directory = FSDirectory.open(Path.of(INDEX_PATH));
				directoryReader = DirectoryReader.open(directory);
				indexSearcher = new IndexSearcher(directoryReader);
			} catch (IOException e) {
				e.printStackTrace();
				String logMsgString = String.format(Messages.getString("POISearcher.3"), directory.toString()); //$NON-NLS-1$
				logger.error(logMsgString);
			}

		}
	}

	/**
	 * <b>构建搜索</b><br>
	 * 
	 * @param query 查询对象
	 */
	public static void buildSearch(int queryType, String keywords) {
		Query query = null;
		TopDocs topDocs = null;
		if (null == docsMap) {
			docsMap = new HashMap<String, TopDocs>();
		}
		if (docsMap.containsKey(keywords)) {
			return;// 已经查过了
		}
		try {
			query = buildQuery(queryType, keywords);
			topDocs = indexSearcher.search(query, MAX_HITS);
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("POISearcher.4"), query.toString()); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		if (!docsMap.containsKey(keywords)) {
			docsMap.put(keywords, topDocs);
		}
	}

	/**
	 * <b>构建查询</b><br>
	 * 
	 * @param queryType 查询类型，0--QueryParse, 解析器查询 1--TermQuery 词根查询 2--
	 *                  WildcardQuery 通配符查询
	 * @param keyword   查询关键词
	 * 
	 * @return 查询对象
	 */
	public static Query buildQuery(int queryType, String keyword) {
		Query query = null;
		switch (queryType) {
		case 1:
			query = new TermQuery(new Term(GEONAME, keyword));
			break;

		case 2:
			query = new WildcardQuery(new Term(GEONAME, keyword));
			break;
		case 0:
		default:
			try {
				query = queryParser.parse(keyword);
			} catch (ParseException e) {
				e.printStackTrace();
				String logMsgString = String.format(Messages.getString("POISearcher.5"), keyword); //$NON-NLS-1$
				logger.error(logMsgString);
			}
			break;
		}
		return query;
	}

	/**
	 * <b>获得查询结果个数</b><br>
	 * 
	 * @param keyword   查询关键词
	 * @param queryType 查询类型，0--QueryParse, 解析器查询 1--TermQuery 词根查询 2--
	 *                  WildcardQuery 通配符查询
	 * 
	 * @return 查询结果个数
	 */
	public static long getCount(String keyword, int queryType) {
		if (null == docsMap || !docsMap.containsKey(keyword)) {
			buildSearch(queryType, keyword);
		}

		return docsMap.get(keyword).totalHits.value;
	}

	/**
	 * <b>获得查询结果个数</b><br>
	 * 
	 * @param keyword 查询关键词 <i> 查询类型缺省为：0--QueryParse, 解析器查询 </i>
	 * 
	 * @return 查询结果个数
	 */
	public static long getCount(String keyword) {
		return getCount(keyword, 0);
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @param keywords String 搜索关键词
	 * @param maxHits  int 搜索词的最大个数
	 * @return List 返回一个地址数组
	 */
	public static List<EnterpriseRow> query(String keywords, int maxHits) {
		List<EnterpriseRow> list = new ArrayList<EnterpriseRow>();
		if (null == docsMap || !docsMap.containsKey(keywords)) {
			buildSearch(0, keywords);
		}
		TopDocs topDocs = docsMap.get(keywords);
		try {
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);

				EnterpriseRow row = new EnterpriseRow();
				row.setName(doc.get(GEONAME));
				row.setAddress(doc.get(ADDRESS));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("POISearcher.6"), keywords); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		return list;
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @param keywords String 搜索关键词
	 * 
	 * @return List 返回一个企业法人地址数组
	 */
	public static List<EnterpriseRow> query(String keywords) {
		return query(keywords, MAX_HITS);
	}

	/**
	 * <b> 根据关键词进行分页搜索</b>
	 * 
	 * @param keywords  String 搜索关键词
	 * @param pageIndex 当前页索引，从1开始
	 * @param pageSize  页大小
	 * 
	 * @return List 返回一个企业法人地址数组
	 */
	public static List<EnterpriseRow> queryPage(String keywords, int pageIndex, int pageSize) {
		List<EnterpriseRow> list = new ArrayList<EnterpriseRow>();
		if (null == docsMap || !docsMap.containsKey(keywords)) {
			buildSearch(0, keywords);
		}
		TopDocs topDocs = docsMap.get(keywords);
		int start = (pageIndex - 1) * pageSize;
		int end = (start + pageSize) < topDocs.scoreDocs.length ? (start + pageSize) : topDocs.scoreDocs.length;

		for (int i = start; i < end; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			Document doc = null;
			try {
				doc = indexSearcher.doc(scoreDoc.doc);
			} catch (IOException e) {
				e.printStackTrace();
				String logMsgString = String.format(Messages.getString("POISearcher.8"), keywords); //$NON-NLS-1$
				logger.error(logMsgString);
			}
			// System.out.println(keywords + "\t" + scoreDoc.score + "\t" + doc.get(GEONAME)
			// + "\t" + doc.get(ADDRESS));

			EnterpriseRow row = new EnterpriseRow();
			row.setName(doc.get(GEONAME));
			row.setAddress(doc.get(ADDRESS));
			list.add(row);
		}

		return list;
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @param keywords String 搜索关键词
	 * @param maxHits  int 搜索词的最大个数，不起作用
	 * 
	 * @return List 返回一个简单地址数组
	 */
	public static List<SimpleAddressRow> search(String keywords, int maxHits) {
		List<EnterpriseRow> rows = GeoNameSearcher.query(keywords, maxHits);
		List<SimpleAddressRow> simpleAddressRows = new ArrayList<SimpleAddressRow>();
		int t = 0;
		for (EnterpriseRow row : rows) {
			// 二次查找，只找100次
			t++;
			if (t > 100) {
				break;
			}
			String queryKey = "\"" + row.getAddress() + "\"";

			List<SimpleAddressRow> srows = LuceneUtil.search(queryKey, maxHits);
			for (SimpleAddressRow aRow : srows) {
				simpleAddressRows.add(aRow);
			}
		}
		return simpleAddressRows;
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @param keywords String 搜索关键词
	 * 
	 * @return List 返回一个简单地址数组
	 */
	public static List<SimpleAddressRow> search(String keywords) {
		return search(keywords, MAX_HITS);
	}

	public static Integer getSum(String keywords) {
		if (rows_map == null) {
			rows_map = new HashMap<String, List<SimpleAddressRow>>();
		}
		if (!rows_map.containsKey(keywords)) {
			List<SimpleAddressRow> rows = search(keywords);
			rows_map.put(keywords, rows);
		}
		List<SimpleAddressRow> rows_t = rows_map.get(keywords);
		return rows_t.size();
	}

	public static List<SimpleAddressRow> searchPage(String keywords, Integer index, Integer limit) {
		if (null == rows_map || !rows_map.containsKey(keywords)) {
			getSum(keywords);
		}
		List<SimpleAddressRow> rows = rows_map.get(keywords);
		List<SimpleAddressRow> rows_t = new ArrayList<SimpleAddressRow>();
		int start = (index - 1) * limit;
		int end = (start + limit) < rows.size() ? (start + limit) : rows.size();
		for (int i = start; i < end; i++) {
			rows_t.add(rows.get(i));
		}
		return rows_t;
	}
}
