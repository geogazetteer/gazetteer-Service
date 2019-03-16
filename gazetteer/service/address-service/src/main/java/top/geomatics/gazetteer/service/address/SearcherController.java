package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiOperation;
import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.service.utils.IndexUtil;
import top.geomatics.gazetteer.service.utils.SqlliteUtil;

/**
 * SearcherController
 * 
 * @author whudyj
 */
@RestController
@RequestMapping("/address")
public class SearcherController {

	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);

	@GetMapping("/all")
	public String selectAllAddress() {
		List<AddressRow> rows = mapper.selectAllAddress();
		// 使用阿里巴巴的fastjson
		return JSON.toJSONString(rows);
	}

	@GetMapping("/page")
	public String selectAllAddressWithLimit(@RequestParam(value="limit",required = true) int limit) {
		List<AddressRow> rows = mapper.selectAllAddressWithLimit(limit);
		return JSON.toJSONString(rows);
	}

	// 任意给定一组关键词进行搜索
	@GetMapping("/searcher/all")
	public String selectAllAddressWithKeywords(@RequestParam(value="keyword",required = true) String keywords) {
		List<AddressRow> rows = mapper.selectAllAddress();
		// 关键词匹配，?key=<关键词>;<关键词>……
		return JSON.toJSONString(rows);
	}

	// 任意给定一组关键词进行搜索
	@GetMapping("/searcher/page")
	public String selectAllAddressWithKeywordsAndLimit(@RequestParam(value="keyword",required = true)
			String keywords,@RequestParam(value="limit",required = true) int limit) {
		List<AddressRow> rows = mapper.selectAllAddressWithLimit(limit);
		// 关键词匹配，?key=<关键词>;<关键词>……
		return JSON.toJSONString(rows);
	}

	@ApiOperation(value = "根据地址查询", notes = "根据地址查询")
	@GetMapping("/selectByAddress")
	public String selectByAddress(@RequestParam String address) {
		List<AddressRow> rows = mapper.selectByAddress(address);
		// 使用阿里巴巴的fastjson
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "根据地理编码查询", notes = "根据地理编码查询")
	@GetMapping("/selectByCode")
	public String selectByCode(@RequestParam(value = "code") String code) {
		List<AddressRow> rows = mapper.selectByCode(code);
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "根据关键词查询", notes = "根据关键词查询")
	@GetMapping("/selectByKeyword")
	public String selectByKeyword(@RequestParam String keyword) {
		List<AddressRow> rows = mapper.selectByKeyword(keyword);
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "根据lucene索引查询", notes = "根据lucene索引查询")
	@GetMapping("/selectAddressBylucene")
	public String selectAddressBylucene(@RequestParam(value = "keyWord") String keyWord) {
		String json = null;
		try {
			List<String> list = new ArrayList<String>();
			IndexSearcher indexSearcher = IndexUtil.init();
			QueryParser queryParser = new QueryParser(Version.LUCENE_47, "address", new IKAnalyzer(true));
//          String q="select ADDRESS from dmdz where ADDRESS like '%"+keyWord+"%' limit 0,10";
			Query query = queryParser.parse(keyWord);
			Long start = System.currentTimeMillis();
			TopDocs topDocs = indexSearcher.search(query, 10);
			Long end = System.currentTimeMillis();
			System.out.println("lucene耗时" + (end - start));
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				list.add(doc.get("address"));
			}
			json = JSON.toJSONString(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return json;
		}

	}

	@ApiOperation(value = "获取街道", notes = "获取街道")
	@GetMapping("/streets")
	public String streets() {
		List<AddressRow> rows = mapper.selectstreets();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "获取社区", notes = "获取社区")
	@GetMapping("/communities")
	public String communities() {
		List<AddressRow> rows = mapper.selectcommunities();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "获取建筑物", notes = "获取建筑物")
	@GetMapping("/buildings")
	public String buildings() {
		List<AddressRow> rows = mapper.selectbuildings();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "获取房屋信息", notes = "获取房屋信息")
	@GetMapping("/houses")
	public String houses() {
		List<AddressRow> rows = mapper.selecthouses();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

}
