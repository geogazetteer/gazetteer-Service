package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.lucene.AddressIndexer;
import top.geomatics.gazetteer.lucene.AddressSearcherPinyin;
import top.geomatics.gazetteer.lucene.GeoNameSearcher;
import top.geomatics.gazetteer.lucene.LuceneUtil;
import top.geomatics.gazetteer.lucene.POISearcher;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.GeoPoint;
import top.geomatics.gazetteer.model.SimpleAddressRow;
import top.geomatics.gazetteer.model.SimpleAddressRow2;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;
import top.geomatics.gazetteer.utilities.address.SearcherSettings;

/**
 * <b>搜索服务</b><br>
 * 
 * <i>说明</i><br>
 * <i>目前只针对标准地名地址数据库中的地名地址进行查询</i>
 * 
 * @author whudyj
 */
@Api(value = "/address", tags = "标准地名地址搜索")
@RestController
@RequestMapping("/address")
public class SearcherController {
	private SearcherSettings settings = new SearcherSettings();

	/**
	 * <b>搜索设置</b><br>
	 * 
	 * @param settings 搜索选项
	 * @return 设置结果
	 */
	@ApiOperation(value = "搜索设置", notes = "设置搜索选项。示例：/address/searcher/settings")
	@PutMapping("/searcher/settings")
	public SearcherSettings searchSettings(
			@ApiParam(value = "搜索设置") @RequestBody(required = true) SearcherSettings settings) {
		this.settings = settings;
		return this.settings;
	}

	/**
	 * <b>查询所有地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/all?fields=id,address%26tablename=民治社区%26limit=10
	 * </p>
	 * 
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,address
	 * @param tablename String 请求参数，指定查询的数据库表，如：油松社区
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @param row       AddressRow 指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询所有地址", notes = "查询所有地址。查询速度与地址数据量有关，请尽量缩小查询范围。示例：/address/all?fields=id,address&tablename=民治社区&limit=10")
	@GetMapping("/all")
	public String selectAll(
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如油松社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit,
			@ApiParam(value = "查询条件") AddressRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>查询一个区的所有街道</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/龙华区?limit=10
	 * </p>
	 * 
	 * @param district String 路径变量，固定为“龙华区”
	 * @param fields   String 请求参数，需要选择的字段，多个字段以,分隔，如：id,street
	 * @param orderby  String 请求参数，指定查询结果排序方式
	 * @param limit    int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个区的所有街道", notes = "按区查询所有街道，示例：/address/龙华区?limit=10")
	@GetMapping("/{district}")
	public String selectByDistrictNode(
			@ApiParam(value = "街道所在的区，固定为龙华区") @PathVariable(value = IControllerConstant.ADDRESS_DISTRICT, required = true) String district,
			@ApiParam(value = "查询字段，如 id,street") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, district, null, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>查询一个街道的所有社区</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/龙华区/民治街道?limit=10
	 * </p>
	 * 
	 * @param district String 路径变量，固定为“龙华区”
	 * @param street   String 路径变量，表示所在的街道，如：民治街道
	 * @param fields   String 请求参数，需要选择的字段，多个字段以,分隔，如：id,community
	 * @param orderby  String 请求参数，指定查询结果排序方式
	 * @param limit    int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个街道的所有社区", notes = "按街道查询所有社区，示例：/address/龙华区/民治街道?limit=10")
	@GetMapping("/{district}/{street}")
	public String selectByStreetNode(
			@ApiParam(value = "街道所在的区，固定为龙华区") @PathVariable(value = IControllerConstant.ADDRESS_DISTRICT, required = false) String district,
			@ApiParam(value = "街道，如：民治街道") @PathVariable(value = IControllerConstant.ADDRESS_STREET, required = true) String street,
			@ApiParam(value = "查询字段，如 id,community") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, street, null, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>查询一个社区的所有地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/龙华区/民治街道/民治社区?limit=5
	 * </p>
	 * 
	 * @param path_district  String 路径变量，固定为“龙华区”
	 * @param path_street    String 路径变量，表示所在的街道，如：民治街道
	 * @param path_community String 路径变量，表示所在的社区，如：民治社区
	 * @param fields         String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param orderby        String 请求参数，指定查询结果排序方式
	 * @param limit          int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个社区的所有地址", notes = "查询一个社区的所有地址，示例：/address/龙华区/民治街道/民治社区?limit=5")
	@GetMapping("/{district}/{street}/{community}")
	public String selectByCommunityNode(
			@ApiParam(value = "街道所在的区，固定为龙华区") @PathVariable(value = IControllerConstant.ADDRESS_DISTRICT, required = false) String path_district,
			@ApiParam(value = "街道，如：民治街道") @PathVariable(value = IControllerConstant.ADDRESS_STREET, required = false) String path_street,
			@ApiParam(value = "社区，如：民治社区") @PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String path_community,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") int limit) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_community, null, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>查询一个社区的所有小区</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/villages/民治社区
	 * </p>
	 * 
	 * @param path_community String 路径变量，表示所在的社区，如：民治社区
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个社区的所有小区", notes = "查询一个社区的所有小区，示例：/address/villages/民治社区")
	@GetMapping("/villages/{community}")
	public String selectVillageByCommunityNode(
			@ApiParam(value = "社区，如：民治社区") @PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String path_community) {
		String fields = "village";
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_community, null, null, 0);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		// 查找社区下所有的小区，不重复
		List<AddressRow> new_rows = new ArrayList<AddressRow>();
		Map<String, String> vMap = new HashMap<>();
		for (AddressRow row : rows) {
			String key = row.getVillage();
			if (key.trim().isEmpty()) {
				continue;
			}
			if (!vMap.containsKey(key)) {
				vMap.put(key, key);
				new_rows.add(row);
			}
		}
		return ControllerUtils.getResponseBody(new_rows);
	}

	/**
	 * <b>查询一个社区的所有道路</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/roads/民治社区
	 * </p>
	 * 
	 * @param path_district  String 路径变量，固定为“龙华区”
	 * @param path_street    String 路径变量，表示所在的街道，如：民治街道
	 * @param path_community String 路径变量，表示所在的社区，如：民治社区
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个社区的所有道路", notes = "查询一个社区的所有道路，示例：/address/roads/民治社区")
	@GetMapping("/roads/{community}")
	public String selectRoadByCommunityNode(
			@ApiParam(value = "社区，如：民治社区") @PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String path_community) {
		String fields = "road";
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_community, null, null, 0);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		// 查找社区下所有的道路，不重复
		List<AddressRow> new_rows = new ArrayList<AddressRow>();
		Map<String, String> vMap = new HashMap<>();
		for (AddressRow row : rows) {
			String key = row.getRoad();
			if (null == key || key.trim().isEmpty()) {
				continue;
			}
			if (!vMap.containsKey(key)) {
				vMap.put(key, key);
				new_rows.add(row);
			}
		}
		return ControllerUtils.getResponseBody(new_rows);
	}

	/**
	 * <b>查询一个社区的所有道路号码</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/road_nums/民治社区/民治大道
	 * </p>
	 * 
	 * @param path_community String 路径变量，表示所在的社区，如：民治社区
	 * @param path_road      String 路径变量，表示所在的道路，如：民治大道
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个社区道路下的所有道路号码", notes = "查询一个社区道路下的所有道路号码，示例：/address/road_nums/民治社区/民治大道")
	@GetMapping("/road_nums/{community}/{road}")
	public String selectRoadNumByRoadNode(
			@ApiParam(value = "社区，如：民治社区") @PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String path_community,
			@ApiParam(value = "道路，如：民治大道") @PathVariable(value = IControllerConstant.ADDRESS_ROAD, required = true) String path_road) {
		String fields = "road_num";
		AddressRow crow = new AddressRow();
		crow.setRoad(path_road);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_community, crow, null, 0);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		// 查找社区下所有的道路号码，不重复
		List<AddressRow> new_rows = new ArrayList<AddressRow>();
		Map<String, String> vMap = new HashMap<>();
		for (AddressRow row : rows) {
			String key = row.getRoad_num();
			if (null == key || key.trim().isEmpty()) {
				continue;
			}
			if (!vMap.containsKey(key)) {
				vMap.put(key, key);
				new_rows.add(row);
			}
		}
		return ControllerUtils.getResponseBody(new_rows);
	}

	/**
	 * <b>查询一个小区的所有建筑物编码</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/codes/民治社区/梅花山庄
	 * </p>
	 * 
	 * @param path_community String 路径变量，表示所在的社区，如：民治社区
	 * @param path_village   String 路径变量，表示所在的小区，如：梅花山庄
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个小区的所有建筑物编码", notes = "查询一个小区的所有建筑物编码，示例：/address/codes/民治社区/梅花山庄")
	@GetMapping("/codes/{community}/{village}")
	public String selectCodeByVillageNode(
			@ApiParam(value = "社区，如：民治社区") @PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String path_community,
			@ApiParam(value = "小区，如：梅花山庄") @PathVariable(value = IControllerConstant.ADDRESS_VILLAGE, required = true) String path_village) {
		String fields = "code";
		AddressRow aaRow = new AddressRow();
		aaRow.setVillage(path_village);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_community, aaRow, null, 0);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		// 查找小区下所有的建筑物编码，不重复
		List<AddressRow> new_rows = new ArrayList<AddressRow>();
		Map<String, String> vMap = new HashMap<>();
		for (AddressRow row : rows) {
			String key = row.getCode();
			if (null == key || key.trim().isEmpty()) {
				continue;
			}
			if (!vMap.containsKey(key)) {
				vMap.put(key, key);
				new_rows.add(row);
			}
		}
		return ControllerUtils.getResponseBody(new_rows);
	}

	/**
	 * <b>根据条件查询地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/searcher/?fields=id,address%26tablename=民治社区%26address=广东省深圳市龙华区民治街道民治社区沙吓村六巷7栋
	 * </p>
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param id          Integer 请求参数，指定查询的id字段值
	 * @param province    String 请求参数，指定查询的province字段值
	 * @param city        String 请求参数，指定查询的city字段值
	 * @param district    String 请求参数，指定查询的district字段值
	 * @param street      String 请求参数，指定查询的street字段值
	 * @param community   String 请求参数，指定查询的community字段值
	 * @param address     String 请求参数，指定查询的address字段值
	 * @param address_id  String 请求参数，指定查询的address_id字段值
	 * @param building    String 请求参数，指定查询的building字段值
	 * @param building_id String 请求参数，指定查询的building_id字段值
	 * @param code        String 请求参数，指定查询的code字段值
	 * @param road        String 请求参数，指定查询的road字段值
	 * @param road_num    String 请求参数，指定查询的road_num字段值
	 * @param village     String 请求参数，指定查询的village字段值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据条件精确查询地址", notes = "根据条件精确查询地址，获取满足条件的所有地址信息。示例：/address/searcher/?fields=id,address&tablename=民治社区&address=广东省深圳市龙华区民治街道民治社区沙吓村六巷7栋")
	@GetMapping("/searcher")
	public String selectWithConditions(
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如民治社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "条件：指定查询的id") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@ApiParam(value = "条件：指定查询的省") @RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@ApiParam(value = "条件：指定查询的市") @RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@ApiParam(value = "条件：指定查询的区") @RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@ApiParam(value = "条件：指定查询的街道") @RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@ApiParam(value = "条件：指定查询的社区") @RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@ApiParam(value = "条件：指定查询的地名地址，如：广东省深圳市龙华区民治街道民治社区沙吓村六巷7栋") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS, required = false, defaultValue = "") String address,
			@ApiParam(value = "条件：指定查询的地名地址id") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@ApiParam(value = "条件：指定查询的建筑物") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@ApiParam(value = "条件：指定查询的建筑物id") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@ApiParam(value = "条件：指定查询的地名地址编码") @RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,
			@ApiParam(value = "条件：指定查询的道路") @RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,
			@ApiParam(value = "条件：指定查询的道路编码") @RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,
			@ApiParam(value = "条件：指定查询的小区或村") @RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		if (null != id)
			row.setId(id);
		if (null != province && !province.isEmpty())
			row.setProvince(province);
		if (null != city && !city.isEmpty())
			row.setCity(city);
		if (null != district && !district.isEmpty())
			row.setDistrict(district);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != community && !community.isEmpty())
			row.setCommunity(community);
		if (null != address && !address.isEmpty())
			row.setAddress(address);
		if (null != address_id && !address_id.isEmpty())
			row.setAddress_id(address_id);
		if (null != building && !building.isEmpty())
			row.setBuilding(building);
		if (null != building_id && !building_id.isEmpty())
			row.setBuilding_id(building_id);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != road && !road.isEmpty())
			row.setRoad(road);
		if (null != road_num && !road_num.isEmpty())
			row.setRoad_num(road_num);
		if (null != village && !village.isEmpty())
			row.setVillage(village);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据条件模糊查询地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/fuzzysearcher/?fields=id,address%26tablename=民治社区%26address=沙吓村六巷7栋
	 * </p>
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param id          Integer 请求参数，指定查询的id字段值
	 * @param province    String 请求参数，指定查询的province字段值
	 * @param city        String 请求参数，指定查询的city字段值
	 * @param district    String 请求参数，指定查询的district字段值
	 * @param street      String 请求参数，指定查询的street字段值
	 * @param community   String 请求参数，指定查询的community字段值
	 * @param address     String 请求参数，指定查询的address字段值
	 * @param address_id  String 请求参数，指定查询的address_id字段值
	 * @param building    String 请求参数，指定查询的building字段值
	 * @param building_id String 请求参数，指定查询的building_id字段值
	 * @param code        String 请求参数，指定查询的code字段值
	 * @param road        String 请求参数，指定查询的road字段值
	 * @param road_num    String 请求参数，指定查询的road_num字段值
	 * @param village     String 请求参数，指定查询的village字段值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据条件模糊查询地址", notes = "根据条件模糊查询地址，获取满足条件的所有地址信息。示例：/address/fuzzysearcher/?fields=id,address&tablename=民治社区&address=沙吓村六巷7栋")
	@GetMapping("/fuzzysearcher")
	public String fuzzySelectWithConditions(
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如民治社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "条件：指定查询的id") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@ApiParam(value = "条件：指定查询的省") @RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@ApiParam(value = "条件：指定查询的市") @RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@ApiParam(value = "条件：指定查询的区") @RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@ApiParam(value = "条件：指定模糊查询的街道") @RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@ApiParam(value = "条件：指定模糊查询的社区") @RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@ApiParam(value = "条件：指定模糊查询的地址，如沙吓村六巷7栋") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS, required = false, defaultValue = "") String address,
			@ApiParam(value = "条件：指定模糊查询的地址id") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@ApiParam(value = "条件：指定模糊查询的建筑物") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@ApiParam(value = "条件：指定模糊查询的建筑物id") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@ApiParam(value = "条件：指定模糊查询的地址编码") @RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,
			@ApiParam(value = "条件：指定模糊查询的道路") @RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,
			@ApiParam(value = "条件：指定模糊查询的道路编码") @RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,
			@ApiParam(value = "条件：指定模糊查询的小区或村") @RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		if (null != id)
			row.setId(id);
		if (null != province && !province.isEmpty())
			row.setProvince("%" + province + "%");
		if (null != city && !city.isEmpty())
			row.setCity("%" + city + "%");
		if (null != district && !district.isEmpty())
			row.setDistrict("%" + district + "%");
		if (null != street && !street.isEmpty())
			row.setStreet("%" + street + "%");
		if (null != community && !community.isEmpty())
			row.setCommunity("%" + community + "%");
		if (null != address && !address.isEmpty())
			row.setAddress("%" + address + "%");
		if (null != address_id && !address_id.isEmpty())
			row.setAddress_id("%" + address_id + "%");
		if (null != building && !building.isEmpty())
			row.setBuilding("%" + building + "%");
		if (null != building_id && !building_id.isEmpty())
			row.setBuilding_id("%" + building_id + "%");
		if (null != code && !code.isEmpty())
			row.setCode("%" + code + "%");
		if (null != road && !road.isEmpty())
			row.setRoad("%" + road + "%");
		if (null != road_num && !road_num.isEmpty())
			row.setRoad_num("%" + road_num + "%");
		if (null != village && !village.isEmpty())
			row.setVillage("%" + village + "%");
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findLike(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据地址ID查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/address_id/63EEDE6B9E9D6A3AE0538CC0C0C07BB0
	 * </p>
	 * 
	 * @param address_id String 路径变量，指定查询的地址ID
	 * @param fields     String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename  String 请求参数，指定查询的数据库表
	 * @param orderby    String 请求参数，指定查询结果排序方式
	 * @param limit      int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址ID查询", notes = "根据地址ID查询，获取对应地址ID的所有地址信息。示例：/address/address_id/63EEDE6B9E9D6A3AE0538CC0C0C07BB0")
	@GetMapping("/address_id/{address_id}")
	public String selectByAddressId(
			@ApiParam(value = "查询的地址ID，如63EEDE6B9E9D6A3AE0538CC0C0C07BB0") @PathVariable(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = true) String address_id,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如油松社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setAddress_id(address_id);

		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据地址编码查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/code/44030600960102T0117?limit=5
	 * </p>
	 * 
	 * @param code      String 路径变量，指定查询的地址编码
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址编码查询", notes = "根据地址编码查询，获取对应地址编码的所有地址信息。示例：/address/code/44030600960102T0117?limit=5")
	@GetMapping("/code/{code}")
	public String selectByCode(
			@ApiParam(value = "查询的地址编码，如44030600960102T0117") @PathVariable(value = IControllerConstant.ADDRESS_CODE, required = true) String code,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如油松社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setCode(code);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据街道名称查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/street/民治街道?limit=5
	 * </p>
	 * 
	 * @param street    String 路径变量，指定查询的街道名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道名称查询", notes = "根据街道名称查询，获取对应街道的所有地址信息。示例：/address/street/民治街道?limit=5")
	@GetMapping("/street/{street}")
	public String selectByStreet(
			@ApiParam(value = "查询的街道名称，如民治街道") @PathVariable(value = IControllerConstant.ADDRESS_STREET, required = true) String street,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如油松社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setStreet(street);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据社区名称查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/community/龙塘社区?limit=5
	 * </p>
	 * 
	 * @param community String 路径变量，指定查询的社区名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据社区名称查询", notes = "根据社区名称查询，获取对应社区的所有地址信息。示例：/address/community/龙塘社区?limit=5")
	@GetMapping("/community/{community}")
	public String selectByCommunity(
			@ApiParam(value = "查询的社区名称，如龙塘社区") @PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String community,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如龙塘社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setCommunity(community);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据建筑物ID查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/buildingID/44030600960102T0117?limit=5
	 * </p>
	 * 
	 * @param building_id String 路径变量，指定查询的建筑物ID
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据建筑物ID查询", notes = "根据建筑物ID查询，获取对应建筑物的所有地址信息。示例：/address/buildingID/44030600960102T0117?limit=5")
	@GetMapping("/buildingID/{building_id}")
	public String selectByBuildingId(
			@ApiParam(value = "查询的建筑物ID，如44030600960102T0117") @PathVariable(value = IControllerConstant.ADDRESS_BUILDING_ID, required = true) String building_id,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如龙塘社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setBuilding_id(building_id);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据建筑物名称查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/building/L25号铁皮房?limit=5
	 * </p>
	 * 
	 * @param building  String 路径变量，指定查询的建筑物名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据建筑物名称查询", notes = "根据建筑物名称查询，获取对应建筑物的所有地址信息。示例：/address/building/L25号铁皮房?limit=5")
	@GetMapping("/building/{building}")
	public String selectByBuilding(
			@ApiParam(value = "查询的建筑物名称，如L25号铁皮房") @PathVariable(value = IControllerConstant.ADDRESS_BUILDING, required = true) String building,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如龙塘社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setBuilding(building);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据村名称查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/village/上塘农贸建材市场?limit=5
	 * </p>
	 * 
	 * @param village   String 路径变量，指定查询的村名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据小区或村名称查询", notes = "根据小区或村名称查询，获取对应小区或村名的所有地址信息。示例：/address/village/上塘农贸建材市场?limit=5")
	@GetMapping("/village/{village}")
	public String selectByVillage(
			@ApiParam(value = "查询的小区或村名称，如上塘农贸建材市场") @PathVariable(value = IControllerConstant.ADDRESS_VILLAGE, required = true) String village,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如龙塘社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setVillage(village);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据道路名称查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/road/下围工业二路?limit=5
	 * </p>
	 * 
	 * @param road      String 路径变量，指定查询的道路名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：id,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据道路名称查询", notes = "根据道路名称查询，获取对应道路的所有地址信息。示例：/address/road/下围工业二路?limit=5")
	@GetMapping("/road/{road}")
	public String selectByRoad(
			@ApiParam(value = "查询的道路名称，如下围工业二路") @PathVariable(value = IControllerConstant.ADDRESS_ROAD, required = true) String road,
			@ApiParam(value = "查询字段，如 id,code,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如龙塘社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setRoad(road);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>根据数据库id进行查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/id/1
	 * </p>
	 * 
	 * @param id Integer 请求参数，指定查询的数据库id
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据id查询详细信息", notes = "根据id查询详细信息，示例：/address/id/1")
	@GetMapping("/id/{id}")
	public String selectById(
			@ApiParam(value = "指定查询的地址数据库id") @PathVariable(value = "id", required = true) Integer id) {
		Long start = System.currentTimeMillis();
		List<AddressRow> row = ControllerUtils.mapper.selectById(id);
		Long end = System.currentTimeMillis();
		System.out.println("selectById wasted time: " + (end - start));
		return ControllerUtils.getResponseBody(row);
	}

	/**
	 * <b>根据一组数据库id进行查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/ids?in=1,2,3
	 * </p>
	 * 
	 * @param ids Integer 请求参数，指定查询的一组数据库id，以,分隔
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据一组id查询详细信息", notes = "根据一组id查询详细信息，示例：/address/ids?in=1,2,3")
	@GetMapping("/ids")
	public String selectByIds(
			@ApiParam(value = "指定查询的地址数据库id，以,分隔") @RequestParam(value = "in", required = true) String ids) {
		List<Integer> idList = new ArrayList<Integer>();
		String listString[] = ids.split(",");
		for (String str : listString) {
			idList.add(Integer.parseInt(str));
		}
		List<AddressRow> row = ControllerUtils.mapper.selectByIds(idList);
		return ControllerUtils.getResponseBody(row);
	}

	/**
	 * <b>返回记录总数</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/count?tablename=油松社区
	 * </p>
	 * 
	 * @param tablename String 请求参数，指定查询的数据库表，如：油松社区
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "返回记录总数", notes = "返回记录总数")
	@GetMapping("/count")
	public String getCount(
			@ApiParam(value = "查询的数据库表，如油松社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename) {
		Map<String, Object> map = ControllerUtils.getRequestMap(null, tablename, null, null, 0);
		return "{ \"total\": " + ControllerUtils.mapper.getCount(map) + "}";
	}

	/**
	 * <b>获取address字段模糊查询结果个数</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/total?keywords=工商银行
	 * </p>
	 * 
	 * @param keywords String 请求参数，指定查询的数据库表，如：工商银行
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "获取address字段模糊查询结果个数", notes = "获取address字段模糊查询结果个数")
	@GetMapping("/total")
	public String getTotalLike(
			@ApiParam(value = "查询关键词，如工商银行") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true) String keywords) {
		// 关键词转换处理
		keywords = AddressProcessor.transform(keywords, this.settings);
		// 设置查询条件
		String tablename = IControllerConstant.ADDRESS_TABLE;
		// 为了缩小搜索范围
//		String community = AddressGuessor.guessCommunity(keywords);
//		if (null != community && !community.trim().isEmpty()) {
//			tablename = community;
//		}
		AddressRow row = new AddressRow();
		row.setAddress("%" + keywords + "%");
		Map<String, Object> map = ControllerUtils.getRequestMap(null, tablename, row, null, 0);
		// 返回结果
		return "{ \"total\": " + ControllerUtils.mapper.getTotalLike(map) + "}";
	}

	/**
	 * <b>根据地址关键词进行模糊查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/address?keywords=工商银行
	 * </p>
	 * 
	 * @param keywords String 请求参数，查询关键词
	 * @param limit    Integer 请求参数，最多查询记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址关键词进行模糊查询", notes = "根据地址关键词进行模糊查询，示例：/address/address?keywords=工商银行&limit=10")
	@GetMapping("/address")
	public String selectByAddressLike(
			@ApiParam(value = "查询关键词，如：工商银行") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit) {
		// 关键词转换处理
		keywords = AddressProcessor.transform(keywords, this.settings);
		// 设置查询条件
		String tablename = IControllerConstant.ADDRESS_TABLE;
		// 为了缩小搜索范围
//		String community = AddressGuessor.guessCommunity(keywords);
//		if (null != community && !community.trim().isEmpty()) {
//			tablename = community;
//		}

		String fieldString = IControllerConstant.ADDRESS_FIELDS;
		AddressRow row = new AddressRow();
		row.setAddress("%" + keywords + "%");
		Map<String, Object> map = ControllerUtils.getRequestMap(fieldString, tablename, row, null, limit);
		// 返回查询结果
		return ControllerUtils.getResponseBody4(ControllerUtils.mapper.findSimpleLike(map));
	}

	/**
	 * <b>根据地址关键词进行模糊、分页查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/address/page/1?keywords=工商银行
	 * </p>
	 * 
	 * @param index    Integer 路径变量，当前页，从1开始
	 * @param keywords String 请求参数，查询关键词
	 * @param limit    Integer 请求参数，页大小
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址关键词进行模糊、分页查询", notes = "根据地址关键词进行模糊、分页查询，示例：/address/address/page/1?keywords=工商银行&limit=10")
	@GetMapping("/address/page/{index}")
	public String selectByAddressLikePage(
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "查询关键词，如：工商银行") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@ApiParam(value = "每页查询的记录个数，缺省为10") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit) {
		// 关键词转换处理
		keywords = AddressProcessor.transform(keywords, this.settings);
		// 设置查询条件
		String tablename = IControllerConstant.ADDRESS_TABLE;
		// 为了缩小搜索范围
//		String community = AddressGuessor.guessCommunity(keywords);
//		if (null != community && !community.trim().isEmpty()) {
//			tablename = community;
//		}

		String fieldString = IControllerConstant.ADDRESS_FIELDS;
		AddressRow row = new AddressRow();
		row.setAddress("%" + keywords + "%");
		Map<String, Object> map = ControllerUtils.getRequestMap(fieldString, tablename, row, null, 0);
		Integer page_start = (index - 1) * limit;
		map.put("page_start", page_start);
		// 返回查询结果
		List<SimpleAddressRow> rows = ControllerUtils.mapper.findSimpleLikePage(map);
		List<SimpleAddressRow2> rows2 = new ArrayList<SimpleAddressRow2>();
		for (SimpleAddressRow r : rows) {
			SimpleAddressRow2 r2 = new SimpleAddressRow2();
			r2.setId(r.getId());
			r2.setAddress(r.getAddress());
			r2.setCode(r.getCode());
			r2.setName(keywords);
			// 查询坐标
			GeoPoint point = ControllerUtils.getPointByCode(r.getCode());
			if (null != point) {
				r2.setX(point.getX());
				r2.setY(point.getY());
			}
			rows2.add(r2);
		}
		return ControllerUtils.getResponseBody7(rows2);
	}

	/**
	 * <b>获取查询结果个数</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/sum?keywords=工商银行
	 * </p>
	 * 
	 * @param keywords String 请求参数，查询关键词，如：工商银行
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "获取查询结果个数", notes = "获取查询结果个数")
	@GetMapping("/sum")
	public String getSum(
			@ApiParam(value = "查询关键词，如工商银行") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true) String keywords) {
		// 关键词转换处理
		keywords = AddressProcessor.transform(keywords, this.settings);
		long sum = 0L;
		String resFormat = "{ \"total\": " + "%d" + "}";
		String result = String.format(resFormat, sum);
		// 如果是坐标
		if (this.settings.isCoordinates()) {
			sum = CoordinateQuery.getCoordQuerys(keywords);
			return String.format(resFormat, sum);
		}
		// 如果是建筑物编码
		else if (this.settings.isBuildingCode()) {
			sum = CoordinateQuery.getCodeQuerys(keywords);
			return String.format(resFormat, sum);
		}
		// 如果是数据库查询
		if (true == this.settings.isDatabaseSearch()) {
			// 如果是地名或POI查询
			if (this.settings.isGeoName() || this.settings.isPOI()) {
				result = QueryUtils.getCountNameLike(keywords);
			}
			// 如果是地址
			else {
				result = getTotalLike(keywords);
			}
		}
		// 其他为lucene搜索
		else {
			String queryKeywords = "\"" + keywords + "\"";
			// 如果是地名
			if (this.settings.isGeoName()) {
				sum = GeoNameSearcher.getCount(queryKeywords);
				result = String.format(resFormat, sum);
			}
			// 如果是POI
			else if (this.settings.isPOI()) {
				sum = POISearcher.getCount(queryKeywords);
				result = String.format(resFormat, sum);
			}
			// 如果是地址
			else {
				sum = LuceneUtil.getCount(queryKeywords);
				result = String.format(resFormat, sum);
			}
		}
		return result;
	}

	/**
	 * <b>根据关键词进分页查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/page/1?keywords=工商银行%26limit=10
	 * </p>
	 * 
	 * @param index    Integer 页面索引
	 * @param keywords String 请求参数，查询关键词
	 * @param limit    Integer 页面的大小
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据关键词进行分页查询", notes = "根据关键词进行分页查询，示例：/address/page/1?keywords=工商银行&limit=10")
	@GetMapping("/page/{index}")
	public String selectAddressPage(
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "查询关键词，如：工商银行") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = true, defaultValue = "10") Integer limit) {
		// 关键词转换处理
		keywords = AddressProcessor.transform(keywords, this.settings);
		List<SimpleAddressRow> rows = null;
		// 如果是坐标
		if (this.settings.isCoordinates()) {
			rows = CoordinateQuery.getCoordQuerysPage(keywords, index, limit);
			// 返回结果
			return ControllerUtils.getResponseBody4(rows);
		}
		// 如果是建筑物编码
		else if (this.settings.isBuildingCode()) {
			rows = CoordinateQuery.getCodeQuerysPage(keywords, index, limit);
			// 返回结果
			return ControllerUtils.getResponseBody4(rows);
		}
		// 如果是数据库查询
		if (true == this.settings.isDatabaseSearch()) {
			// 如果是地名或POI查询
			if (this.settings.isGeoName() || this.settings.isPOI()) {
				return QueryUtils.queryPage(keywords, index, limit);
			}
			// 如果是地址
			else {
				return selectByAddressLikePage(index, keywords, limit);
			}

		}
		// 其他为lucene搜索
		else {
			String queryKeywords = "\"" + keywords + "\"";
			// 如果是地名
			if (this.settings.isGeoName()) {
				rows = GeoNameSearcher.searchPage(queryKeywords, index, limit);
			}
			// 如果是POI
			else if (this.settings.isPOI()) {
				rows = POISearcher.searchPage(queryKeywords, index, limit);
			}
			// 如果是地址
			else {
				rows = LuceneUtil.searchByPage(queryKeywords, index, limit);
			}
		}
		// 返回结果
		return ControllerUtils.getResponseBody4(rows);
	}

	/**
	 * <b>根据关键词进行快速模糊查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/hint?keywords=龙华%26limit=10
	 * </p>
	 * 
	 * @param keywords String 请求参数，查询关键词，多个关键词以空格分隔
	 * @param limit    Integer 请求参数，最多查询记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据关键词进行模糊查询", notes = "根据关键词进行模糊查询，示例：/address/hint?keywords=龙华&limit=10")
	@GetMapping("/hint")
	public String selectAddressByKeywords(
			@ApiParam(value = "查询关键词，多个关键词以空格分隔，如：龙华") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "1000") Integer limit) {
		keywords = AddressProcessor.transform(keywords, this.settings);
		if (this.settings.isCoordinates()) {
			// 根据坐标搜索
			return ControllerUtils.getResponseBody4(CoordinateQuery.getCoordQueryResults(keywords));
		}
		if (this.settings.isBuildingCode()) {
			// 根据建筑物编码搜索
			return ControllerUtils.getResponseBody4(CoordinateQuery.getBuildingCodeQueryResults(keywords));
		}
		if (this.settings.isGeoName()) {
			// 如果是查询地名
			return ControllerUtils.getResponseBody4(GeoNameSearcher.search(keywords, limit));
		}
		if (this.settings.isPOI()) {
			// 如果是查询POI
			return ControllerUtils.getResponseBody4(POISearcher.search(keywords, limit));
		}
		// 如果是查询标准地址
		return ControllerUtils.getResponseBody4(LuceneUtil.search(keywords, limit));
	}

	/**
	 * <b>根据关键词进行模糊查询，返回记录总数</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/hint/count?keywords=中华工业园
	 * </P>
	 * 
	 * @param keywords String 请求参数，查询关键词
	 * @return String 返回JSON格式的记录总数
	 */
	@ApiOperation(value = "返回查询记录总数", notes = "返回查询记录总数")
	@GetMapping("/hint/count")
	public String getHits(
			@ApiParam(value = "查询关键词，如：中华工业园") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords) {
		// 关键词转换处理
		keywords = AddressProcessor.transform(keywords, this.settings);

		long count = 0L;
		// 如果是查询地名
		if (this.settings.isGeoName()) {
			count = GeoNameSearcher.getCount(keywords);
		}
		// 如果是POI查询
		else if (this.settings.isPOI()) {
			count = POISearcher.getCount(keywords);
		}
		// 如果是地址查询
		else if (this.settings.isAddress()) {
			count = LuceneUtil.getCount(keywords);
		}
		return "{ \"total\": " + count + "}";
	}

	/**
	 * <b>根据关键词进行模糊、分页查询</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/hint/1?keywords=中华工业园%26limit=10
	 * </p>
	 * 
	 * @param index    Integer 页面索引
	 * @param keywords String 请求参数，查询关键词
	 * @param limit    Integer 页面的大小
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据关键词进行模糊查询分页展示", notes = "根据关键词进行模糊查询分页展示，示例：/address/hint/page/1?keywords=中华工业园&limit=10")
	@GetMapping("/hint/page/{index}")
	public String selectAddressByKeywords(
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "查询关键词，如：中华工业园") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = true, defaultValue = "10") Integer limit) {
		// 关键词转换处理
		keywords = AddressProcessor.transform(keywords, this.settings);
		// 如果是查询地名
		if (this.settings.isGeoName()) {
			return ControllerUtils.getResponseBody4(GeoNameSearcher.searchPage(keywords, index, limit));
		}
		// 如果是POI查询
		else if (this.settings.isPOI()) {
			return ControllerUtils.getResponseBody4(POISearcher.searchPage(keywords, index, limit));
		}
		// 如果是地址查询
		else if (this.settings.isAddress()) {
			return JSON.toJSONString(LuceneUtil.searchByPage(keywords, index, limit));
		}
		return "";

	}

	/**
	 * <b>根据关键词进行模糊查询分页展示(加上了同音字搜索功能)</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/hint/page/pinyin/2?keywords=神针是%26limit=10
	 * </p>
	 * 
	 * @param index    Integer 页面索引
	 * @param keywords String 请求参数，查询关键词，多个关键词以空格分隔
	 * @param limit    Integer 页面的大小
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据关键词进行模糊查询分页展示", notes = "根据关键词进行模糊查询分页展示，示例：/address/hint/page/pinyin/2?keywords=神针是&limit=10")
	@GetMapping("/hint/page/pinyin/{index}")
	public String selectAddressByKeywordsAndpinyin(
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "查询关键词，多个关键词以空格分隔，如：龙华") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = true, defaultValue = "10") Integer limit) {
		String pinyin = AddressIndexer.toPinyin(keywords);

		return JSON.toJSONString(AddressSearcherPinyin.searchPage(pinyin, index, limit));
	}

	/**
	 * <b>根据街道或社区、地址关键词查询地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/address/like?fields=id,address%26tablename=民治社区%26street=观湖街道%26community=润城社区%26keywords=工商银行
	 * </p>
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param id          Integer 请求参数，指定查询的id字段值
	 * @param province    String 请求参数，指定查询的province字段值
	 * @param city        String 请求参数，指定查询的city字段值
	 * @param district    String 请求参数，指定查询的district字段值
	 * @param street      String 请求参数，指定查询的street字段值
	 * @param community   String 请求参数，指定查询的community字段值
	 * @param keywords    String 请求参数，指定查询的地址关键词
	 * @param address_id  String 请求参数，指定查询的address_id字段值
	 * @param building    String 请求参数，指定查询的building字段值
	 * @param building_id String 请求参数，指定查询的building_id字段值
	 * @param code        String 请求参数，指定查询的code字段值
	 * @param road        String 请求参数，指定查询的road字段值
	 * @param road_num    String 请求参数，指定查询的road_num字段值
	 * @param village     String 请求参数，指定查询的village字段值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道或社区、地址关键词查询地址", notes = "根据街道或社区、地址关键词查询地址，获取满足条件的所有地址信息。示例：/address/address/like?fields=id,address&tablename=dmdz&street=观湖街道&community=润城社区&keywords=工商银行")
	@GetMapping("/address/like")
	public String selectWithConditions2(
			@ApiParam(value = "查询字段，如 id,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如民治社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "条件：指定查询的id") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@ApiParam(value = "条件：指定查询的省") @RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@ApiParam(value = "条件：指定查询的市") @RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@ApiParam(value = "条件：指定查询的区") @RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@ApiParam(value = "条件：指定查询的街道") @RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@ApiParam(value = "条件：指定查询的社区") @RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@ApiParam(value = "条件：指定查询的地名地址关键词，如：工商银行") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true, defaultValue = "") String keywords,
			@ApiParam(value = "条件：指定查询的地名地址id") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@ApiParam(value = "条件：指定查询的建筑物") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@ApiParam(value = "条件：指定查询的建筑物id") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@ApiParam(value = "条件：指定查询的地名地址编码") @RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,
			@ApiParam(value = "条件：指定查询的道路") @RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,
			@ApiParam(value = "条件：指定查询的道路编码") @RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,
			@ApiParam(value = "条件：指定查询的小区或村") @RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		if (null != id)
			row.setId(id);
		if (null != province && !province.isEmpty())
			row.setProvince(province);
		if (null != city && !city.isEmpty())
			row.setCity(city);
		if (null != district && !district.isEmpty())
			row.setDistrict(district);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != community && !community.isEmpty())
			row.setCommunity(community);
		if (null != keywords && !keywords.isEmpty()) {
			if (!keywords.startsWith("%")) {
				keywords = "%" + keywords;
			}
			if (!keywords.endsWith("%")) {
				keywords = keywords + "%";
			}
			row.setAddress(keywords);
		}

		if (null != address_id && !address_id.isEmpty())
			row.setAddress_id(address_id);
		if (null != building && !building.isEmpty())
			row.setBuilding(building);
		if (null != building_id && !building_id.isEmpty())
			row.setBuilding_id(building_id);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != road && !road.isEmpty())
			row.setRoad(road);
		if (null != road_num && !road_num.isEmpty())
			row.setRoad_num(road_num);
		if (null != village && !village.isEmpty())
			row.setVillage(village);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findAddressLike(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据街道或社区、小区关键词查询地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/village_like?fields=id,address%26tablename=民治社区%26street=观湖街道%26community=润城社区%26keywords=锦鲤一村
	 * </p>
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param id          Integer 请求参数，指定查询的id字段值
	 * @param province    String 请求参数，指定查询的province字段值
	 * @param city        String 请求参数，指定查询的city字段值
	 * @param district    String 请求参数，指定查询的district字段值
	 * @param street      String 请求参数，指定查询的street字段值
	 * @param community   String 请求参数，指定查询的community字段值
	 * @param keywords    String 请求参数，指定查询的小区关键词
	 * @param address_id  String 请求参数，指定查询的address_id字段值
	 * @param building    String 请求参数，指定查询的building字段值
	 * @param building_id String 请求参数，指定查询的building_id字段值
	 * @param code        String 请求参数，指定查询的code字段值
	 * @param road        String 请求参数，指定查询的road字段值
	 * @param road_num    String 请求参数，指定查询的road_num字段值
	 * @param village     String 请求参数，指定查询的village字段值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道或社区、小区关键词查询地址", notes = "根据街道或社区、小区关键词查询地址，获取满足条件的所有地址信息。示例：/address/village_like?fields=id,address&tablename=dmdz&street=观湖街道&community=润城社区&keywords=锦鲤一村")
	@GetMapping("/village_like")
	public String selectByVillageLike(
			@ApiParam(value = "查询字段，如 id,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如润城社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "条件：指定查询的id") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@ApiParam(value = "条件：指定查询的省") @RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@ApiParam(value = "条件：指定查询的市") @RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@ApiParam(value = "条件：指定查询的区") @RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@ApiParam(value = "条件：指定查询的街道") @RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@ApiParam(value = "条件：指定查询的社区") @RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@ApiParam(value = "条件：指定查询的小区地址关键词，如：锦鲤一村") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true, defaultValue = "") String keywords,
			@ApiParam(value = "条件：指定查询的地名地址id") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@ApiParam(value = "条件：指定查询的建筑物") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@ApiParam(value = "条件：指定查询的建筑物id") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@ApiParam(value = "条件：指定查询的地名地址编码") @RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,
			@ApiParam(value = "条件：指定查询的道路") @RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,
			@ApiParam(value = "条件：指定查询的道路编码") @RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,

			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		if (null != id)
			row.setId(id);
		if (null != province && !province.isEmpty())
			row.setProvince(province);
		if (null != city && !city.isEmpty())
			row.setCity(city);
		if (null != district && !district.isEmpty())
			row.setDistrict(district);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != community && !community.isEmpty())
			row.setCommunity(community);
		if (null != keywords && !keywords.isEmpty()) {
			if (!keywords.startsWith("%")) {
				keywords = "%" + keywords;
			}
			if (!keywords.endsWith("%")) {
				keywords = keywords + "%";
			}
			row.setVillage(keywords);
		}

		if (null != address_id && !address_id.isEmpty())
			row.setAddress_id(address_id);
		if (null != building && !building.isEmpty())
			row.setBuilding(building);
		if (null != building_id && !building_id.isEmpty())
			row.setBuilding_id(building_id);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != road && !road.isEmpty())
			row.setRoad(road);
		if (null != road_num && !road_num.isEmpty())
			row.setRoad_num(road_num);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findVillageLike(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据街道或社区、建筑物代码关键词查询地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/code_like?fields=id,address%26tablename=民治社区%26street=观湖街道%26community=润城社区%26keywords=34100023
	 * </p>
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param id          Integer 请求参数，指定查询的id字段值
	 * @param province    String 请求参数，指定查询的province字段值
	 * @param city        String 请求参数，指定查询的city字段值
	 * @param district    String 请求参数，指定查询的district字段值
	 * @param street      String 请求参数，指定查询的street字段值
	 * @param community   String 请求参数，指定查询的community字段值
	 * @param keywords    String 请求参数，指定查询的建筑物代码关键词
	 * @param address_id  String 请求参数，指定查询的address_id字段值
	 * @param building    String 请求参数，指定查询的building字段值
	 * @param building_id String 请求参数，指定查询的building_id字段值
	 * @param code        String 请求参数，指定查询的code字段值
	 * @param road        String 请求参数，指定查询的road字段值
	 * @param road_num    String 请求参数，指定查询的road_num字段值
	 * @param village     String 请求参数，指定查询的village字段值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道或社区、建筑物代码关键词查询地址", notes = "根据街道或社区、建筑物代码关键词查询地址，获取满足条件的所有地址信息。示例：/address/code_like?fields=id,address&tablename=dmdz&street=观湖街道&community=润城社区&keywords=34100023")
	@GetMapping("/code_like")
	public String selectByCodeLike(
			@ApiParam(value = "查询字段，如 id,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如民治社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "条件：指定查询的id") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@ApiParam(value = "条件：指定查询的省") @RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@ApiParam(value = "条件：指定查询的市") @RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@ApiParam(value = "条件：指定查询的区") @RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@ApiParam(value = "条件：指定查询的街道") @RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@ApiParam(value = "条件：指定查询的社区") @RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@ApiParam(value = "条件：指定查询的建筑物代码地址关键词，如：34100023") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true, defaultValue = "") String keywords,
			@ApiParam(value = "条件：指定查询的地名地址id") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@ApiParam(value = "条件：指定查询的建筑物") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@ApiParam(value = "条件：指定查询的建筑物id") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,

			@ApiParam(value = "条件：指定查询的道路") @RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,
			@ApiParam(value = "条件：指定查询的道路编码") @RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,
			@ApiParam(value = "条件：指定查询的小区或村") @RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		if (null != id)
			row.setId(id);
		if (null != province && !province.isEmpty())
			row.setProvince(province);
		if (null != city && !city.isEmpty())
			row.setCity(city);
		if (null != district && !district.isEmpty())
			row.setDistrict(district);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != community && !community.isEmpty())
			row.setCommunity(community);
		if (null != keywords && !keywords.isEmpty()) {
			if (!keywords.startsWith("%")) {
				keywords = "%" + keywords;
			}
			if (!keywords.endsWith("%")) {
				keywords = keywords + "%";
			}
			row.setCode(keywords);
		}

		if (null != address_id && !address_id.isEmpty())
			row.setAddress_id(address_id);
		if (null != building && !building.isEmpty())
			row.setBuilding(building);
		if (null != building_id && !building_id.isEmpty())
			row.setBuilding_id(building_id);
		if (null != road && !road.isEmpty())
			row.setRoad(road);
		if (null != road_num && !road_num.isEmpty())
			row.setRoad_num(road_num);
		if (null != village && !village.isEmpty())
			row.setVillage(village);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findCodeLike(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据街道或社区、道路关键词查询地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/road_like?fields=id,address%26tablename=民治社区%26street=观湖街道%26community=润城社区%26keywords=观澜大道
	 * </p>
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param id          Integer 请求参数，指定查询的id字段值
	 * @param province    String 请求参数，指定查询的province字段值
	 * @param city        String 请求参数，指定查询的city字段值
	 * @param district    String 请求参数，指定查询的district字段值
	 * @param street      String 请求参数，指定查询的street字段值
	 * @param community   String 请求参数，指定查询的community字段值
	 * @param keywords    String 请求参数，指定查询的地址关键词
	 * @param address_id  String 请求参数，指定查询的address_id字段值
	 * @param building    String 请求参数，指定查询的building字段值
	 * @param building_id String 请求参数，指定查询的building_id字段值
	 * @param code        String 请求参数，指定查询的code字段值
	 * @param road        String 请求参数，指定查询的road字段值
	 * @param road_num    String 请求参数，指定查询的road_num字段值
	 * @param village     String 请求参数，指定查询的village字段值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道或社区、道路关键词查询地址", notes = "根据街道或社区、道路关键词查询地址，获取满足条件的所有地址信息。示例：/address/road_like?fields=id,address&tablename=dmdz&street=观湖街道&community=润城社区&keywords=观澜大道")
	@GetMapping("/road_like")
	public String selectByRoadLike(
			@ApiParam(value = "查询字段，如 id,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如民治社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "条件：指定查询的id") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@ApiParam(value = "条件：指定查询的省") @RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@ApiParam(value = "条件：指定查询的市") @RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@ApiParam(value = "条件：指定查询的区") @RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@ApiParam(value = "条件：指定查询的街道") @RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@ApiParam(value = "条件：指定查询的社区") @RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@ApiParam(value = "条件：指定查询的道路关键词，如：观澜大道") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true, defaultValue = "") String keywords,
			@ApiParam(value = "条件：指定查询的地名地址id") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@ApiParam(value = "条件：指定查询的建筑物") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@ApiParam(value = "条件：指定查询的建筑物id") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@ApiParam(value = "条件：指定查询的地名地址编码") @RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,

			@ApiParam(value = "条件：指定查询的道路编码") @RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,
			@ApiParam(value = "条件：指定查询的小区或村") @RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		if (null != id)
			row.setId(id);
		if (null != province && !province.isEmpty())
			row.setProvince(province);
		if (null != city && !city.isEmpty())
			row.setCity(city);
		if (null != district && !district.isEmpty())
			row.setDistrict(district);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != community && !community.isEmpty())
			row.setCommunity(community);
		if (null != keywords && !keywords.isEmpty()) {
			if (!keywords.startsWith("%")) {
				keywords = "%" + keywords;
			}
			if (!keywords.endsWith("%")) {
				keywords = keywords + "%";
			}
			row.setRoad(keywords);
		}

		if (null != address_id && !address_id.isEmpty())
			row.setAddress_id(address_id);
		if (null != building && !building.isEmpty())
			row.setBuilding(building);
		if (null != building_id && !building_id.isEmpty())
			row.setBuilding_id(building_id);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != road_num && !road_num.isEmpty())
			row.setRoad_num(road_num);
		if (null != village && !village.isEmpty())
			row.setVillage(village);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findRoadLike(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据街道或社区、道路编号关键词查询地址</b><br>
	 * 
	 * <p>
	 * examples:<br>
	 * http://localhost:8083/address/road_num_like?fields=id,address%26tablename=民治社区%26street=观湖街道%26community=润城社区%26keywords=257
	 * </p>
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：id,address
	 * @param tablename   String 请求参数，指定查询的数据库表
	 * @param id          Integer 请求参数，指定查询的id字段值
	 * @param province    String 请求参数，指定查询的province字段值
	 * @param city        String 请求参数，指定查询的city字段值
	 * @param district    String 请求参数，指定查询的district字段值
	 * @param street      String 请求参数，指定查询的street字段值
	 * @param community   String 请求参数，指定查询的community字段值
	 * @param keywords    String 请求参数，指定查询的地址关键词
	 * @param address_id  String 请求参数，指定查询的address_id字段值
	 * @param building    String 请求参数，指定查询的building字段值
	 * @param building_id String 请求参数，指定查询的building_id字段值
	 * @param code        String 请求参数，指定查询的code字段值
	 * @param road        String 请求参数，指定查询的road字段值
	 * @param road_num    String 请求参数，指定查询的road_num字段值
	 * @param village     String 请求参数，指定查询的village字段值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道或社区、道路编号关键词查询地址", notes = "根据街道或社区、道路编号关键词查询地址，获取满足条件的所有地址信息。示例：/address/road_num_like?fields=id,address&tablename=dmdz&street=观湖街道&community=润城社区&keywords=257")
	@GetMapping("/road_num_like")
	public String selectByRoadNumLike(
			@ApiParam(value = "查询字段，如 id,address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询的数据库表，如民治社区") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@ApiParam(value = "条件：指定查询的id") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@ApiParam(value = "条件：指定查询的省") @RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@ApiParam(value = "条件：指定查询的市") @RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@ApiParam(value = "条件：指定查询的区") @RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@ApiParam(value = "条件：指定查询的街道") @RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@ApiParam(value = "条件：指定查询的社区") @RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@ApiParam(value = "条件：指定查询的道路编号关键词，如：257") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true, defaultValue = "") String keywords,
			@ApiParam(value = "条件：指定查询的地名地址id") @RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@ApiParam(value = "条件：指定查询的建筑物") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@ApiParam(value = "条件：指定查询的建筑物id") @RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@ApiParam(value = "条件：指定查询的地名地址编码") @RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,
			@ApiParam(value = "条件：指定查询的道路") @RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,

			@ApiParam(value = "条件：指定查询的小区或村") @RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow row = new AddressRow();
		if (null != id)
			row.setId(id);
		if (null != province && !province.isEmpty())
			row.setProvince(province);
		if (null != city && !city.isEmpty())
			row.setCity(city);
		if (null != district && !district.isEmpty())
			row.setDistrict(district);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != community && !community.isEmpty())
			row.setCommunity(community);
		if (null != keywords && !keywords.isEmpty()) {
			if (!keywords.startsWith("%")) {
				keywords = "%" + keywords;
			}
			if (!keywords.endsWith("%")) {
				keywords = keywords + "%";
			}
			row.setRoad_num(keywords);
		}

		if (null != address_id && !address_id.isEmpty())
			row.setAddress_id(address_id);
		if (null != building && !building.isEmpty())
			row.setBuilding(building);
		if (null != building_id && !building_id.isEmpty())
			row.setBuilding_id(building_id);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != road && !road.isEmpty())
			row.setRoad(road);
		if (null != village && !village.isEmpty())
			row.setVillage(village);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findRoadNumLike(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据id查询坐标信息</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/address/point?id=1%26tablename=dmdz </i>
	 * 
	 * @param id        Integer 请求参数，数据库中记录的id
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据id查询坐标信息", notes = "根据id查询坐标信息，示例：/address/point?id=1&tablename=dmdz")
	@GetMapping("/point")
	public String getPointByFid(
			@ApiParam(value = "数据库中地址记录的fid") @RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = true) Integer id,
			@ApiParam(value = "查询的数据库表，如dmdz") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename) {
		List<AddressRow> rows = ControllerUtils.mapper.selectById(id);

		// 找到建筑物编码
		List<String> codes = new ArrayList<String>();
		for (AddressRow row : rows) {
			String code = row.getCode();
			code = ControllerUtils.coding(code);
			codes.add(code);
		}

		List<GeoPoint> geoPoints = ControllerUtils.getPointsByCodes(codes);

		return JSON.toJSONString(geoPoints);
	}

	/**
	 * <b>根据一组id查询坐标信息</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/address/points?id=1,2,3%26tablename=dmdz </i>
	 * 
	 * @param ids       String 请求参数，数据库中记录的ids
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据一组id查询坐标信息", notes = "根据一组id查询坐标信息，示例：/address/points?id=1,2,3&tablename=dmdz")
	@GetMapping("/points")
	public String getPointsByFids(
			@ApiParam(value = "数据库中地址记录的ids,如1,2,3") @RequestParam(value = "ids", required = true) String ids,
			@ApiParam(value = "查询的数据库表，如dmdz") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename) {
		List<Integer> idList = new ArrayList<Integer>();
		if (ids.endsWith(",")) {
			ids = ids.substring(0, ids.length() - 1);
		}
		String listString[] = ids.split(",");
		for (String str : listString) {
			idList.add(Integer.parseInt(str));
		}

		List<AddressRow> rows = ControllerUtils.mapper.selectByIds(idList);

		// 找到建筑物编码
		List<String> codes = new ArrayList<String>();
		for (AddressRow row : rows) {
			String code = row.getCode();
			code = ControllerUtils.coding(code);
			codes.add(code);
		}

		List<GeoPoint> geoPoints = ControllerUtils.getPointsByCodes(codes);

		return JSON.toJSONString(geoPoints);
	}

}
