package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.apache.ibatis.javassist.expr.NewArray;
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
import org.springframework.web.bind.annotation.PathVariable;
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

	private static Map<String, Object> getRequestMap(String fields, String tablename, AddressRow row, String orderby,
			int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", tablename);
		if (null != row) {
			// 13个字段
			String province = row.getProvince();
			String city = row.getCity();
			String district = row.getDistrict();
			String street = row.getStreet();
			String community = row.getCommunity();
			String address = row.getAddress();
			String address_id = row.getAddress_id();
			String building = row.getBuilding();
			String building_id = row.getBuilding_id();
			String code = row.getCode();
			String road = row.getRoad();
			String road_num = row.getRoad_num();
			String village = row.getVillage();

			if (province != null && !province.isEmpty())
				map.put("province", province);
			if (city != null && !city.isEmpty())
				map.put("city", city);
			if (district != null && !district.isEmpty())
				map.put("district", district);
			if (street != null && !street.isEmpty())
				map.put("street", street);
			if (community != null && !community.isEmpty())
				map.put("community", community);
			if (address != null && !address.isEmpty())
				map.put("address", address);
			if (address_id != null && !address_id.isEmpty())
				map.put("address_id", address_id);
			if (building != null && !building.isEmpty())
				map.put("building", building);
			if (building_id != null && !building_id.isEmpty())
				map.put("building_id", building_id);
			if (code != null && !code.isEmpty())
				map.put("code", code);
			if (road != null && !road.isEmpty())
				map.put("road", road);
			if (road_num != null && !road_num.isEmpty())
				map.put("road_num", road_num);
			if (village != null && !village.isEmpty())
				map.put("village", village);
		}

		if (orderby != null && !orderby.isEmpty())
			map.put("sql_orderBy", orderby);
		if (limit > 0)
			map.put("sql_limit", limit);
		return map;
	}

	/**
	 * url:/all?fields={field}&tablename={tablename}&orderby={orderby}&limit={limit}
	 * examples: /all/?fields=id,address&tablename=民治社区
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public String selectAll(@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit, AddressRow row) {
		Map<String, Object> map = getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = mapper.findEquals(map);
		// 使用阿里巴巴的fastjson
		return JSON.toJSONString(rows);
	}

	/**
	 * url:/{district}/{street}?fields={field}&orderby={orderby}&limit={limit}
	 * examples: http://localhost:8080/address/龙华区/民治街道?limit=5
	 * 
	 * @return
	 */
	@GetMapping("/{district}/{street}")
	public String selectByStreetNode(@PathVariable(value = "district", required = false) String path_district,
			@PathVariable(value = "street", required = true) String path_street,
			// @PathVariable(value ="community",required = true) String path_community,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			// @RequestParam(value = "tablename", required = false, defaultValue = "dmdz")
			// String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		// Map<String, Object> map = getRequestMap(fields, tablename, null, orderby,
		// limit);
		Map<String, Object> map = getRequestMap(fields, path_street, null, orderby, limit);
		List<AddressRow> rows = mapper.findEquals(map);
		// 使用阿里巴巴的fastjson
		return JSON.toJSONString(rows);
	}

	/**
	 * url:/{district}/{street}/{community}?fields={field}&orderby={orderby}&limit={limit}
	 * examples: http://localhost:8080/address/龙华区/民治街道/民治社区?limit=5
	 * 
	 * @return
	 */
	@GetMapping("/{district}/{street}/{community}")
	public String selectByCommunityNode(@PathVariable(value = "district", required = false) String path_district,
			@PathVariable(value = "street", required = false) String path_street,
			@PathVariable(value = "community", required = true) String path_community,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			// @RequestParam(value = "tablename", required = false, defaultValue = "dmdz")
			// String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		// Map<String, Object> map = getRequestMap(fields, tablename, null, orderby,
		// limit);
		Map<String, Object> map = getRequestMap(fields, path_community, null, orderby, limit);
		List<AddressRow> rows = mapper.findEquals(map);
		// 使用阿里巴巴的fastjson
		return JSON.toJSONString(rows);
	}

	/**
	 * url:/searcher?fields={field}&tablename={tablename} \
	 * &address={address}&code={code}... \ &orderby={orderby}&limit={limit}
	 * examples:
	 * /searcher/?fields=id,address&tablename=民治社区&address=广东省深圳市龙华区民治街道民治社区沙吓村六巷7栋
	 * 
	 * @return
	 */
	@GetMapping("/searcher")
	public String selectWithConditions(
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "province", required = false, defaultValue = "") String province,
			@RequestParam(value = "city", required = false, defaultValue = "") String city,
			@RequestParam(value = "district", required = false, defaultValue = "") String district,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "community", required = false, defaultValue = "") String community,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "address_id", required = false, defaultValue = "") String address_id,
			@RequestParam(value = "building", required = false, defaultValue = "") String building,
			@RequestParam(value = "building_id", required = false, defaultValue = "") String building_id,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "road", required = false, defaultValue = "") String road,
			@RequestParam(value = "road_num", required = false, defaultValue = "") String road_num,
			@RequestParam(value = "village", required = false, defaultValue = "") String village,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		row.setProvince(province);
		row.setCity(city);
		row.setDistrict(district);
		row.setStreet(street);
		row.setCommunity(community);
		row.setAddress(address);
		row.setAddress_id(address_id);
		row.setBuilding(building);
		row.setBuilding_id(building_id);
		row.setCode(code);
		row.setRoad(road);
		row.setRoad_num(road_num);
		row.setVillage(village);
		Map<String, Object> map = getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = mapper.findEquals(map);
		return JSON.toJSONString(rows);
	}

	/**
	 * url:/searcher?fields={field}&tablename={tablename} \
	 * &address={address}&code={code}... \ &orderby={orderby}&limit={limit}
	 * examples: /searcher/?fields=id,address&tablename=民治社区&address=%沙吓村六巷7栋%
	 * 
	 * @return
	 */
	@GetMapping("/fuzzysearcher")
	public String fuzzySelectWithConditions(
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "province", required = false, defaultValue = "") String province,
			@RequestParam(value = "city", required = false, defaultValue = "") String city,
			@RequestParam(value = "district", required = false, defaultValue = "") String district,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "community", required = false, defaultValue = "") String community,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "address_id", required = false, defaultValue = "") String address_id,
			@RequestParam(value = "building", required = false, defaultValue = "") String building,
			@RequestParam(value = "building_id", required = false, defaultValue = "") String building_id,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "road", required = false, defaultValue = "") String road,
			@RequestParam(value = "road_num", required = false, defaultValue = "") String road_num,
			@RequestParam(value = "village", required = false, defaultValue = "") String village,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
			@RequestParam(value = "simtype", required = false, defaultValue = "0") int simtype,
			@RequestParam(value = "similarity", required = false, defaultValue = "0.0") double similarity) {
		AddressRow row = new AddressRow();
		row.setProvince(province);
		row.setCity(city);
		row.setDistrict(district);
		row.setStreet(street);
		row.setCommunity(community);
		row.setAddress(address);
		row.setAddress_id(address_id);
		row.setBuilding(building);
		row.setBuilding_id(building_id);
		row.setCode(code);
		row.setRoad(road);
		row.setRoad_num(road_num);
		row.setVillage(village);
		Map<String, Object> map = getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = mapper.findLike(map);
		// 计算相似性
		if (0 != simtype && similarity > 0.0) {

		}
		return JSON.toJSONString(rows);
	}

	/**
	 * examples:
	 * 
	 * @param address
	 * @return
	 */
	@ApiOperation(value = "根据地址ID查询", notes = "获取对应地址的信息")
	@GetMapping("/id/{address_id}")
	public String selectByAddressId(@PathVariable(value = "address_id", required = true) String address_id,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setAddress_id(address_id);

		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/address/code/44030600960102T0117?limit=5
	 * 
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "根据地址编码查询", notes = "获取对应地址的信息")
	@GetMapping("/code/{code}")
	public String selectByCode(@PathVariable(value = "code", required = true) String code,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setCode(code);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/address/street/民治街道?limit=5
	 * 
	 * @return
	 */
	@ApiOperation(value = "根据街道名称查询", notes = "获取对应街道的所有地址信息")
	@GetMapping("/street/{street}")
	public String selectByStreet(@PathVariable(value = "street", required = true) String street,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setStreet(street);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/address/community/龙塘社区?limit=5
	 * 
	 * @param community
	 * @return
	 */
	@ApiOperation(value = "根据社区名称查询", notes = "获取对应社区的所有地址信息")
	@GetMapping("/community/{community}")
	public String selectByCommunity(@PathVariable(value = "community", required = true) String community,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setCommunity(community);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/address/buildingID/44030600960102T0117?limit=5
	 * 
	 * @param building_id
	 * @return
	 */
	@ApiOperation(value = "根据建筑物ID查询", notes = "获取对应建筑物的所有地址信息")
	@GetMapping("/buildingID/{building_id}")
	public String selectByBuildingId(@PathVariable(value = "building_id", required = true) String building_id,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setBuilding_id(building_id);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/address/building/L25号铁皮房?limit=5
	 * 
	 * @param building
	 * @return
	 */
	@ApiOperation(value = "根据建筑物名称查询", notes = "获取对应建筑物的所有地址信息")
	@GetMapping("/building/{building}")
	public String selectByBuilding(@PathVariable(value = "building", required = true) String building,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setBuilding(building);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/address/village/上塘农贸建材市场?limit=5
	 * 
	 * @param village
	 * @return
	 */
	@ApiOperation(value = "根据村名称查询", notes = "获取对应村名的所有地址信息")
	@GetMapping("/village/{village}")
	public String selectByVillage(@PathVariable(value = "village", required = true) String village,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setVillage(village);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/address/road/下围工业二路?limit=5
	 * 
	 * @param road
	 * @return
	 */
	@ApiOperation(value = "根据道路名称查询", notes = "获取对应道路的所有地址信息")
	@GetMapping("/road/{road}")
	public String selectByRoad(@PathVariable(value = "road", required = true) String road,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "dmdz") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setRoad(road);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

//	@ApiOperation(value = "根据lucene索引查询", notes = "根据lucene索引查询")
//	@GetMapping("/selectAddressBylucene")
//	public String selectAddressBylucene(@RequestParam(value = "keyWord") String keyWord) {
//		LuceneUtil luceneUtil=new	LuceneUtil();
//		return luceneUtil.selectAddressBylucene(keyWord);
//		String json = null;
//		try {
//			List<String> list = new ArrayList<String>();
//			IndexSearcher indexSearcher = IndexUtil.init();
//			QueryParser queryParser = new QueryParser(Version.LUCENE_47, "address", new IKAnalyzer(true));
////          String q="select ADDRESS from dmdz where ADDRESS like '%"+keyWord+"%' limit 0,10";
//			Query query = queryParser.parse(keyWord);
//			Long start = System.currentTimeMillis();
//			TopDocs topDocs = indexSearcher.search(query, 10);
//			Long end = System.currentTimeMillis();
//			System.out.println("lucene耗时" + (end - start));
//			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//				Document doc = indexSearcher.doc(scoreDoc.doc);
//				list.add(doc.get("address"));
//			}
//			json = JSON.toJSONString(list);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			return json;
//		}
//
//	}

}
