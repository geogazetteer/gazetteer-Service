package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
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
import springfox.documentation.annotations.ApiIgnore;
import top.geomatics.gazetteer.lucene.AddressIndexer;
import top.geomatics.gazetteer.lucene.GeoNameSearcher;
import top.geomatics.gazetteer.lucene.LuceneUtil;
import top.geomatics.gazetteer.lucene.POISearcher;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;
import top.geomatics.gazetteer.utilities.address.SearcherSettings;
import top.geomatics.gazetteer.utilities.database.BuildingQuery;

/**
 * <b>搜索服务</b><br>
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
	public SearcherSettings searchSettings(@RequestBody SearcherSettings settings) {
		this.settings = settings;
		return this.settings;
	}

	/**
	 * <b>查询所有地址</b><br>
	 * examples:<br>
	 * http://localhost:8083/address/all?fields=id,address%26tablename=民治社区%26limit=10
	 * 
	 * 
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,address
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
			@ApiIgnore AddressRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>查询一个街道的所有社区</b><br>
	 * examples:<br>
	 * http://localhost:8083/address/龙华区/民治街道?limit=10
	 * 
	 * @param path_district String 路径变量，固定为“龙华区”
	 * @param path_street   String 路径变量，表示所在的街道，如：民治街道
	 * @param fields        String 请求参数，需要选择的字段，多个字段以,分隔，如：id,community
	 * @param orderby       String 请求参数，指定查询结果排序方式
	 * @param limit         int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询一个街道的所有社区", notes = "按街道查询所有社区，示例：/address/龙华区/民治街道?limit=10")
	@GetMapping("/{district}/{street}")
	public String selectByStreetNode(
			@ApiParam(value = "街道所在的区，固定为龙华区") @PathVariable(value = IControllerConstant.ADDRESS_DISTRICT, required = false) String path_district,
			@ApiParam(value = "街道，如：民治街道") @PathVariable(value = IControllerConstant.ADDRESS_STREET, required = true) String path_street,
			@ApiParam(value = "查询字段，如 id,community") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_street, null, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>查询一个社区的所有地址</b><br>
	 * examples:<br>
	 * http://localhost:8083/address/龙华区/民治街道/民治社区?limit=5
	 * 
	 * @param path_district  String 路径变量，固定为“龙华区”
	 * @param path_street    String 路径变量，表示所在的街道，如：民治街道
	 * @param path_community String 路径变量，表示所在的社区，如：民治社区
	 * @param fields         String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_community, null, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <b>根据条件查询地址</b><br>
	 * examples:<br>
	 * <!--
	 * http://localhost:8083/address/searcher/?fields=id,address&tablename=民治社区&address=广东省深圳市龙华区民治街道民治社区沙吓村六巷7栋
	 * -->
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
	 * examples:<br>
	 * <!--
	 * http://localhost:8083/address/fuzzysearcher/?fields=id,address&tablename=民治社区&address=沙吓村六巷7栋
	 * -->
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
			row.setProvince(province);
		if (null != city && !city.isEmpty())
			row.setCity(city);
		if (null != district && !district.isEmpty())
			row.setDistrict(district);
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
	 * examples:<br>
	 * http://localhost:8083/address/address_id/63EEDE6B9E9D6A3AE0538CC0C0C07BB0
	 * 
	 * @param address_id String 路径变量，指定查询的地址ID
	 * @param fields     String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
	 * examples:<br>
	 * http://localhost:8083/address/code/44030600960102T0117?limit=5
	 * 
	 * @param code      String 路径变量，指定查询的地址编码
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
	 * examples:<br>
	 * http://localhost:8083/address/street/民治街道?limit=5
	 * 
	 * @param street    String 路径变量，指定查询的街道名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,address
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
	 * examples:<br>
	 * http://localhost:8083/address/community/龙塘社区?limit=5
	 * 
	 * @param community String 路径变量，指定查询的社区名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,address
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
	 * examples:<br>
	 * http://localhost:8083/address/buildingID/44030600960102T0117?limit=5
	 * 
	 * @param building_id String 路径变量，指定查询的建筑物ID
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
	 * examples:<br>
	 * http://localhost:8083/address/building/L25号铁皮房?limit=5
	 * 
	 * @param building  String 路径变量，指定查询的建筑物名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
	 * examples:<br>
	 * http://localhost:8083/address/village/上塘农贸建材市场?limit=5
	 * 
	 * @param village   String 路径变量，指定查询的村名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
	 * examples:<br>
	 * http://localhost:8083/address/road/下围工业二路?limit=5
	 * 
	 * @param road      String 路径变量，指定查询的道路名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
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
	 * <b>根据关键词进行模糊查询</b><br>
	 * examples:<br>
	 * <!-- http://localhost:8083/address/hint?keywords=龙华&limit=10 -->
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
		if (this.settings.isGeoName()) {
			return ControllerUtils.getResponseBody4(GeoNameSearcher.search(keywords, limit));
		}
		if (this.settings.isPOI()) {
			return ControllerUtils.getResponseBody4(POISearcher.search(keywords, limit));
		}
		if (this.settings.isCoordinates()) {
			// 根据输入的坐标搜索
			if (!AddressProcessor.isCoordinatesExpression(keywords)) {
				return "";
			}
			String coordString[] = keywords.split(",");
			double x = Double.parseDouble(coordString[0]);
			double y = Double.parseDouble(coordString[1]);
			List<String> codes = BuildingQuery.query(x, y);
			List<AddressRow> rowsTotal = new ArrayList<>();
			for (String code : codes) {
				// 根据建筑物编码搜索
				String fields = "id,address";
				String tablename = AddressProcessor.getCommunityFromBuildingCode(code);
				AddressRow aRow = new AddressRow();
				aRow.setCode(code);
				Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, aRow, null, 0);
				List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
				rowsTotal.addAll(rows);
			}
			return ControllerUtils.getResponseBody(rowsTotal);
		}
		if (this.settings.isBuildingCode()) {
			// 根据建筑物编码搜索
			String fields = "id,address";
			String tablename = AddressProcessor.getCommunityFromBuildingCode(keywords);
			AddressRow aRow = new AddressRow();
			aRow.setCode(keywords);
			Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, aRow, null, 0);
			List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
			return ControllerUtils.getResponseBody(rows);
		}

		return ControllerUtils.getResponseBody4(LuceneUtil.search(keywords, limit));
	}

	/**
	 * <b>根据关键词进行模糊查询分页展示</b><br>
	 * examples:<br>
	 * <!-- http://localhost:8083/address/hint/1?keywords=民治街道&pageNo=10&pageSize=10
	 * -->
	 * 
	 * @param index    Integer 页面索引
	 * @param keywords String 请求参数，查询关键词，多个关键词以空格分隔
	 * @param limit    Integer 页面的大小
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据关键词进行模糊查询分页展示", notes = "根据关键词进行模糊查询分页展示，示例：/address/hint/page/1?keywords=龙华民治&limit=10")
	@GetMapping("/hint/page/{index}")
	public String selectAddressByKeywords(
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "查询关键词，多个关键词以空格分隔，如：龙华") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = true, defaultValue = "10") Integer limit) {
		keywords = AddressProcessor.transform(keywords, this.settings);
		if (this.settings.isGeoName()) {
			return ControllerUtils.getResponseBody4(GeoNameSearcher.search(keywords, limit));// 暂时没有分页
		}
		if (this.settings.isPOI()) {
			return ControllerUtils.getResponseBody4(POISearcher.search(keywords, limit));// 暂时没有分页
		}
		return JSON.toJSONString(LuceneUtil.searchByPage(keywords, index, limit));
	}

	/**
	 * <b>根据关键词进行模糊查询分页展示(加上了同音字搜索功能)</b><br>
	 * examples:<br>
	 * <!-- http://localhost:8083/address/hint/page/pinyin/2?keywords=神针是&limit=10
	 * -->
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
		String pinyin = AddressIndexer.ToPinyin(keywords);

		return JSON.toJSONString(LuceneUtil.searchByPinyin(pinyin, index, limit));
	}

	/**
	 * <b>根据数据库id进行查询</b><br>
	 * examples:<br>
	 * http://localhost:8083/address/id/1
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
	 * examples:<br>
	 * http://localhost:8083/address/ids?in=1,2,3
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

}
