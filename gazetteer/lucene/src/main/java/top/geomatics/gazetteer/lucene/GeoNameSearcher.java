package top.geomatics.gazetteer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.wltea.analyzer.lucene.IKAnalyzer;

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
	private static final String LUCENE_INDEX_PATH = "geoname_index_path";
	private static final int MAX_HITS = 100000000;

	@Value("${index.path}")
	public static String INDEX_PATH = manager.getValue(LUCENE_INDEX_PATH);
	private static IndexSearcher indexSearcher = null;
	private static final String GEONAME = "name";
	private static final String ADDRESS = "address";
	private static final Version VERSION =Version.LUCENE_47;
	private static TopDocs topDocs = null;
	private static int totalHits = 0;

	static {
		init();
	}

	/**
	 * <b>初始化索引搜索对象</b><br>
	 * 
	 * @return 索引搜索对象
	 */
	public static void init() {
		if (indexSearcher == null) {

			Directory directory = null;
			DirectoryReader directoryReader = null;
			try {
				directory = FSDirectory.open(new File(INDEX_PATH));
				directoryReader = DirectoryReader.open(directory);
				indexSearcher = new IndexSearcher(directoryReader);
			} catch (IOException e) {
				e.printStackTrace();
				String logMsgString = String.format("打开索引目录：%s 失败！", directory.toString());
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
			String logMsgString = String.format("查询错误：%s ", query.toString());
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
		case 0:
			queryParser = new QueryParser(VERSION, GEONAME, new IKAnalyzer(true));
			try {
				query = queryParser.parse(keyword);
			} catch (ParseException e) {
				e.printStackTrace();
				String logMsgString = String.format("查询关键词：%s 异常", keyword);
				logger.error(logMsgString);
			}
			break;
		case 1:
			query = new TermQuery(new Term(GEONAME, keyword));
			break;

		case 2:
			query = new WildcardQuery(new Term(GEONAME, keyword));
			break;
		default:
			queryParser = new QueryParser(VERSION, GEONAME, new IKAnalyzer(true));
			try {
				query = queryParser.parse(keyword);
			} catch (ParseException e) {
				e.printStackTrace();
				String logMsgString = String.format("查询关键词：%s 异常", keyword);
				logger.error(logMsgString);
			}
			break;
		}
		return query;
	}

	/**
	 * <b>获得查询结果个数</b><br>
	 * 
	 *@param queryType 查询类型，0--QueryParse, 解析器查询 1--TermQuery 词根查询 2--
	 *                  WildcardQuery 通配符查询
	 * @param keyword   查询关键词
	 * 
	 * @return 查询结果个数
	 */
	public static int getCount(int queryType, String keyword) {
		buildSearch(buildQuery(queryType, keyword));
		return totalHits;
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @deprecated
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
			String logMsgString = String.format("查询关键词：%s 出现异常", keywords);
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
				EnterpriseRow row = new EnterpriseRow();
				row.setName(doc.get(GEONAME));
				row.setAddress(doc.get(ADDRESS));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format("查询关键词：%s 出现异常", keywords);
			logger.error(logMsgString);
		}
		return list;
	}

	/**
	 * <b> 根据关键词进行搜索</b>
	 * 
	 * @deprecated
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
			}
		}
		return simpleAddressRows;
	}

	public static void main(String[] args) {
		List<EnterpriseRow> rows = GeoNameSearcher.query("深圳市明亮伟业机械附件销售部");
		for (EnterpriseRow sp : rows) {
			System.out.println(sp.getAddress());
			System.out.println(sp.getName());
		}

	}
}
