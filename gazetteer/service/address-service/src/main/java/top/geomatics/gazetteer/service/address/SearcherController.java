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
 * @author whudyj
 */
@RestController
@RequestMapping("/address")
public class SearcherController {

	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session=helper.getSession();
	private static AddressMapper mapper=session.getMapper(AddressMapper.class);
//	private SqlliteUtil sqlliteUtil;

//	@ApiOperation(value = "鏌ヨ鍏ㄩ儴鏁版嵁锛堟祴璇曞墠10鏉★級", notes = "鏌ヨ鍏ㄩ儴鏁版嵁锛堟祴璇曞墠10鏉★級")
//	@GetMapping("/selectById")
//	public String selectById(@RequestParam String id) {
//		// 娴嬭瘯鍓�10鏉℃暟鎹�
//		List<AddressRow> rows = null;
//		try {
//			rows = mapper.selectAddressById("4403060080011800284");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// 浣跨敤闃块噷宸村反鐨刦astjson
//		String json = JSON.toJSONString(rows);
//		System.out.println(json);
//		return json;
//	}

	@ApiOperation(value = "鏍规嵁璇︾粏鍦板潃address鏌ヨ", notes = "鏍规嵁璇︾粏鍦板潃address鏌ヨ")
	@GetMapping("/selectByAddress")
	public String selectByAddress(@RequestParam String address) {
		List<AddressRow> rows = mapper.selectByAddress(address);
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "鏍规嵁鍦扮悊缂栫爜code鏌ヨ", notes = "鏍规嵁鍦扮悊缂栫爜code鏌ヨ")
	@GetMapping("/selectByCode")
	public  String selectByCode(@RequestParam(value="code") String code) {
		List<AddressRow> rows = mapper.selectByCode(code);
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "鏍规嵁鍏抽敭璇嶆煡璇�", notes = "鏍规嵁鍏抽敭璇嶆煡璇�")
	@GetMapping("/selectByKeyword")
	public String selectByKeyword(@RequestParam String keyword) {
		List<AddressRow> rows = mapper.selectByKeyword(keyword);
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	 
//	@ApiOperation(value = "妯＄硦鏌ヨ", notes = "妯＄硦鏌ヨ")
//	@GetMapping("/selectByAddressLike")
//	public String selectByAddressLike(@RequestParam(value = "keyWord") String keyWord) {
//		List<AddressRow> rows = mapper.fuzzyQuery(keyword);
//		String json = JSON.toJSONString(rows);
//		System.out.println(json);
//		return json;
//	}

	@ApiOperation(value = "鏍规嵁lucene绱㈠紩杩涜鏌ヨ", notes = "鏍规嵁lucene绱㈠紩杩涜鏌ヨ")
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
			System.out.println("lucene鎼滅储鍏辩粡鍘嗙殑鏃堕棿锛氭绉�  " + (end - start));
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

	@ApiOperation(value = "杩斿洖鎵�鏈夎閬�", notes = "杩斿洖鎵�鏈夎閬�")
	@GetMapping("/streets")
	public  String streets() {
		List<AddressRow> rows= mapper.selectstreets();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "杩斿洖鎵�鏈夌ぞ鍖�", notes = "杩斿洖鎵�鏈夌ぞ鍖�")
	@GetMapping("/communities")
	public  String communities() {
		List<AddressRow> rows= mapper.selectcommunities();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "杩斿洖鎵�鏈夊缓绛戠墿", notes = "杩斿洖鎵�鏈夊缓绛戠墿")
	@GetMapping("/buildings")
	public  String buildings() {
		List<AddressRow> rows = mapper.selectbuildings();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "杩斿洖鎵�鏈夋埧灞�", notes = "杩斿洖鎵�鏈夋埧灞�")
	@GetMapping("/houses")
	public  String houses() {
		List<AddressRow> rows = mapper.selecthouses();
		String json = JSON.toJSONString(rows);
		System.out.println(json);
		return json;
	}

}
