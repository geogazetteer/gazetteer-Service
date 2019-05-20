package top.geomatics.gazetteer.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * <b>地名搜索</b>
 * 
 * @author whudyj
 *
 */
public class GeoNameSearcher {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(GeoNameSearcher.class);

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = Messages.getString("GeoNameSearcher.0"); //$NON-NLS-1$
	private static final int MAX_HITS = 100000000;

	@Value("${index.path}")
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	private static IndexSearcher indexSearcher = null;
	private static final String GEONAME = Messages.getString("GeoNameSearcher.1"); //$NON-NLS-1$
	private static final String ADDRESS = Messages.getString("GeoNameSearcher.2"); //$NON-NLS-1$
	private static TopDocs topDocs = null;
	private static TotalHits totalHits = null;

	/**
	 * <b>初始化索引搜索对象</b><br>
	 * 
	 */
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
				String logMsgString = String.format(Messages.getString("GeoNameSearcher.3"), directory.toString()); //$NON-NLS-1$
				logger.error(logMsgString);
			}

		}
	}

	/**
	 * <b>构建搜索</b><br>
	 * 
	 * @param query 查询对象
	 */
	public static void buildSearch(Query query) {
		try {
			topDocs = indexSearcher.search(query, MAX_HITS);
			totalHits = topDocs.totalHits;
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("GeoNameSearcher.4"), query.toString()); //$NON-NLS-1$
			logger.error(logMsgString);
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
		QueryParser queryParser = null;
		switch (queryType) {
		case 1:
			query = new TermQuery(new Term(GEONAME, keyword));
			break;

		case 2:
			query = new WildcardQuery(new Term(GEONAME, keyword));
			break;
		case 0:
		default:
			queryParser = new QueryParser(GEONAME, new AnsjAnalyzer(TYPE.query_ansj));
			try {
				query = queryParser.parse(keyword);
			} catch (ParseException e) {
				e.printStackTrace();
				String logMsgString = String.format(Messages.getString("GeoNameSearcher.5"), keyword); //$NON-NLS-1$
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
		buildSearch(buildQuery(queryType, keyword));
		return totalHits.value;
	}

	/**
	 * <b>获得查询结果个数</b><br>
	 * 
	 * @param keyword 查询关键词 <i> 查询类型缺省为：0--QueryParse, 解析器查询 </i>
	 * 
	 * @return 查询结果个数
	 */
	public static long getCount(String keyword) {
		buildSearch(buildQuery(0, keyword));
		return totalHits.value;
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @param keywords String 搜索关键词
	 * @param maxHits  int 搜索词的最大个数，不起作用
	 * 
	 * @return List 返回一个企业法人地址数组
	 */
	public static List<EnterpriseRow> query(String keywords, int maxHits) {
		List<EnterpriseRow> list = new ArrayList<EnterpriseRow>();
		try {
			buildSearch(buildQuery(0, keywords));

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);

				EnterpriseRow row = new EnterpriseRow();
				row.setName(doc.get(GEONAME));
				row.setAddress(doc.get(ADDRESS));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("GeoNameSearcher.6"), keywords); //$NON-NLS-1$
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
		List<EnterpriseRow> list = new ArrayList<EnterpriseRow>();
		try {
			buildSearch(buildQuery(0, keywords));

			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				// System.out.println(keywords + "\t" + scoreDoc.score + "\t" + doc.get(GEONAME)
				// + "\t" + doc.get(ADDRESS));

				EnterpriseRow row = new EnterpriseRow();
				row.setName(doc.get(GEONAME));
				row.setAddress(doc.get(ADDRESS));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("GeoNameSearcher.7"), keywords); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		return list;
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
		buildSearch(buildQuery(0, keywords));
		int start = (pageIndex - 1) * pageSize;
		int end = (start + pageSize) < topDocs.scoreDocs.length ? (start + pageSize) : topDocs.scoreDocs.length;

		for (int i = start; i < end; i++) {
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			Document doc = null;
			try {
				doc = indexSearcher.doc(scoreDoc.doc);
			} catch (IOException e) {
				e.printStackTrace();
				String logMsgString = String.format(Messages.getString("GeoNameSearcher.8"), keywords); //$NON-NLS-1$
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
		for (EnterpriseRow row : rows) {
			List<SimpleAddressRow> srows = LuceneUtil.search(row.getAddress(), maxHits);
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
		List<EnterpriseRow> rows = GeoNameSearcher.query(keywords);
		List<SimpleAddressRow> simpleAddressRows = new ArrayList<SimpleAddressRow>();
		for (EnterpriseRow row : rows) {
			List<SimpleAddressRow> srows = LuceneUtil.search(row.getAddress(), 10);
			for (SimpleAddressRow aRow : srows) {
				simpleAddressRows.add(aRow);
				System.out.println(keywords + Messages.getString("GeoNameSearcher.9") + row.getAddress() + Messages.getString("GeoNameSearcher.10") + aRow.getAddress()); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			}
			// 只搜搜第一个（分数最高的）
			break;
		}
		return simpleAddressRows;
	}

}
