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

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * <b>lucene工具类</b>
 * 
 * @author whudyj
 *
 */
public class LuceneUtil {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(LuceneUtil.class);

	// 搜索
	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String LUCENE_INDEX_PATH = Messages.getString("LuceneUtil.0"); //$NON-NLS-1$
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);

	private static IndexSearcher indexSearcher = null;

	private static final String ADDRESS_ID = Messages.getString("LuceneUtil.1"); //$NON-NLS-1$
	private static final String ADDRESS = Messages.getString("LuceneUtil.2"); //$NON-NLS-1$
	private static final int MAX_HITS = 100000000;
	private static TopDocs topDocs = null;
	private static TotalHits totalHits = null;

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
				String logMsgString = String.format(Messages.getString("LuceneUtil.3"), directory.toString()); //$NON-NLS-1$
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
			String logMsgString = String.format(Messages.getString("LuceneUtil.4"), query.toString()); //$NON-NLS-1$
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
			query = new TermQuery(new Term(ADDRESS, keyword));
			break;

		case 2:
			query = new WildcardQuery(new Term(ADDRESS, keyword));
			break;
		case 0:
		default:
			queryParser = new QueryParser(ADDRESS, new AnsjAnalyzer(TYPE.query_ansj));
			try {
				query = queryParser.parse(keyword);
			} catch (ParseException e) {
				e.printStackTrace();
				String logMsgString = String.format(Messages.getString("LuceneUtil.5"), keyword); //$NON-NLS-1$
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
		return getCount(keyword, 0);
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @param keywords String 搜索关键词
	 * @return List 返回一个简单地址数组
	 */
	public static List<SimpleAddressRow> search(String keywords) {
		List<SimpleAddressRow> list = new ArrayList<SimpleAddressRow>();
		buildSearch(buildQuery(0, keywords));
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = null;
			try {
				doc = indexSearcher.doc(scoreDoc.doc);
			} catch (IOException e) {
				e.printStackTrace();
				String logMsgString = String.format(Messages.getString("LuceneUtil.6"), keywords); //$NON-NLS-1$
				logger.error(logMsgString);
			}
			SimpleAddressRow row = new SimpleAddressRow();
			row.setId(Integer.parseInt(doc.get(ADDRESS_ID)));
			row.setAddress(doc.get(ADDRESS));
			list.add(row);
		}

		return list;
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @param keywords String 搜索关键词
	 * @param maxHits  int 搜索词的最大个数
	 * @return List 返回一个简单地址数组
	 */
	public static List<SimpleAddressRow> search(String keywords, int maxHits) {
		return search(keywords);
	}

	/**
	 * <b> 根据关键词进行分页搜索</b>
	 * 
	 * @param keywords  String 搜索关键词
	 * @param pageIndex 当前页索引，从1开始
	 * @param pageSize  页大小
	 * 
	 * @return List 返回一个地址数组
	 */
	public static List<SimpleAddressRow> searchByPage(String keywords, int pageIndex, int pageSize) {
		List<SimpleAddressRow> list = new ArrayList<SimpleAddressRow>();
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
				String logMsgString = String.format(Messages.getString("LuceneUtil.7"), keywords); //$NON-NLS-1$
				logger.error(logMsgString);
			}
			// System.out.println(keywords + "\t" + scoreDoc.score + "\t" + doc.get(GEONAME)
			// + "\t" + doc.get(ADDRESS));

			SimpleAddressRow row = new SimpleAddressRow();
			row.setId(Integer.parseInt(doc.get(ADDRESS_ID)));
			row.setAddress(doc.get(ADDRESS));
			list.add(row);
		}

		return list;
	}

}
