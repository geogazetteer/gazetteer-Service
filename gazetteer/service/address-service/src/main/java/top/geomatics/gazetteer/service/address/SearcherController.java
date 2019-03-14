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
 *
 */
@Controller
@RequestMapping("/")
public class SearcherController {

	// 创建数据库连接
	//SqlliteUtil sqlliteUtil = new SqlliteUtil();
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session=helper.getSession();
	private static AddressMapper mapper=session.getMapper(AddressMapper.class);

	/**
	 * @return
	 */
	@ApiOperation(value = "查询全部数据（测试前10条）", notes = "查询全部数据（测试前10条）")
	@GetMapping("/address")
	public @ResponseBody String address() {
		// 测试前10条数据
		//String sql = "select * from dmdz limit 10";
		//List list = sqlliteUtil.selectAll(sql);
		List<AddressRow> rows = null;
		try {
			rows = mapper.selectAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用阿里巴巴的fastjson
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

//	@ApiOperation(value = "根据详细地址address查询", notes = "根据详细地址address查询")
//	@GetMapping("/selectByAddress")
//	public @ResponseBody String selectByAddress(@RequestParam String address) {
//		// 广东省深圳市龙华区民治街道龙塘社区上塘农贸建材市场L25号铁皮房
//		List list = sqlliteUtil.selectByAddress("dmdz", address);
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		return json;
//	}
//
//	@ApiOperation(value = "根据地理编码code查询", notes = "根据地理编码code查询")
//	@GetMapping("/selectByCode")
//	public @ResponseBody String selectByCode(@RequestParam String code) {
//		// 44030600960102T0117
//		List list = sqlliteUtil.selectByCode("dmdz", code);
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		return json;
//	}

//	@ApiOperation(value = "根据关键词查询", notes = "根据关键词查询")
//	@GetMapping("/selectByKeyword")
//	public @ResponseBody String selectByKeyword(@RequestParam String keyword) {
//		List list = sqlliteUtil.selectByKeyword("dmdz", keyword);
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		return json;
//	}

	// （测试上芬社区龙屋新村三巷8号508）1270毫秒
	@ApiOperation(value = "模糊查询", notes = "模糊查询")
	@GetMapping("/selectByAddressLike")
	public String selectByAddressLike(@RequestParam(value = "keyWord") String keyWord) {
		SqlliteUtil sqlliteUtil = new SqlliteUtil();
		long start = System.currentTimeMillis();
		String s = JSON.toJSONString(sqlliteUtil.fuzzyQuery(keyWord));
		long end = System.currentTimeMillis();
		System.out.println("直接模糊搜索共经历的时间：毫秒  " + (end - start));
		return s;
	}

	@ApiOperation(value = "根据lucene索引进行查询", notes = "根据lucene索引进行查询")
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
			System.out.println("lucene搜索共经历的时间：毫秒  " + (end - start));
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

//	@ApiOperation(value = "返回所有街道", notes = "返回所有街道")
//	@GetMapping("/streets")
//	public @ResponseBody String streets() {
//		List list = sqlliteUtil.selectstreets();
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		return json;
//	}
//
//	@ApiOperation(value = "返回所有社区", notes = "返回所有社区")
//	@GetMapping("/communities")
//	public @ResponseBody String communities() {
//		List list = sqlliteUtil.selectcommunities();
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		return json;
//	}
//
//	@ApiOperation(value = "返回所有建筑物", notes = "返回所有建筑物")
//	@GetMapping("/buildings")
//	public @ResponseBody String buildings() {
//		List list = sqlliteUtil.selectbuildings();
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		return json;
//	}
//
//	@ApiOperation(value = "返回所有房屋", notes = "返回所有房屋")
//	@GetMapping("/houses")
//	public @ResponseBody String houses() {
//		List list = sqlliteUtil.selecthouses();
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		return json;
//	}

}
