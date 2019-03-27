package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import top.geomatics.gazetteer.lucene.LuceneUtil;
import top.geomatics.gazetteer.model.AddressRow;

/**
 * <em>搜索服务</em><br>
 * <i>说明</i><br>
 * <i>目前只针对标准地名地址数据库中的地名地址进行查询</i>
 * 
 * @author whudyj
 */
@RestController
@RequestMapping("/address")
public class SearcherController {

	/**
	 * <em>查询所有地址</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/address/all?fields=id,address&tablename=民治社区&limit=10
	 * -->
	 * 
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表，如：油松社区
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @param row       AddressRow 指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@GetMapping("/all")
	public String selectAll(
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit,
			AddressRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, row, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <em>查询一个街道的所有地址</em><br>
	 * examples:<br>
	 * http://localhost:8083/address/龙华区/民治街道?limit=10
	 * 
	 * @param path_district String 路径变量，固定为“龙华区”
	 * @param path_street   String 路径变量，表示所在的街道，如：民治街道
	 * @param fields        String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param orderby       String 请求参数，指定查询结果排序方式
	 * @param limit         int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@GetMapping("/{district}/{street}")
	public String selectByStreetNode(
			@PathVariable(value = IControllerConstant.ADDRESS_DISTRICT, required = false) String path_district,
			@PathVariable(value = IControllerConstant.ADDRESS_STREET, required = true) String path_street,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_street, null, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <em>查询一个社区的所有地址</em><br>
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
	@GetMapping("/{district}/{street}/{community}")
	public String selectByCommunityNode(
			@PathVariable(value = IControllerConstant.ADDRESS_DISTRICT, required = false) String path_district,
			@PathVariable(value = IControllerConstant.ADDRESS_STREET, required = false) String path_street,
			@PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String path_community,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, path_community, null, orderby, limit);
		List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
		return ControllerUtils.getResponseBody(rows);
	}

	/**
	 * <em>根据条件查询地址</em><br>
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
	@GetMapping("/searcher")
	public String selectWithConditions(
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@RequestParam(value = IControllerConstant.ADDRESS_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,
			@RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,
			@RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
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
	 * <em>根据条件模糊查询地址</em><br>
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
	@GetMapping("/fuzzysearcher")
	public String fuzzySelectWithConditions(
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.ADDRESS_DB_ID, required = false) Integer id,
			@RequestParam(value = IControllerConstant.ADDRESS_PROVINCE, required = false, defaultValue = "") String province,
			@RequestParam(value = IControllerConstant.ADDRESS_CITY, required = false, defaultValue = "") String city,
			@RequestParam(value = IControllerConstant.ADDRESS_DISTRICT, required = false, defaultValue = "") String district,
			@RequestParam(value = IControllerConstant.ADDRESS_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ADDRESS_COMMUNITY, required = false, defaultValue = "") String community,
			@RequestParam(value = IControllerConstant.ADDRESS_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = false, defaultValue = "") String address_id,
			@RequestParam(value = IControllerConstant.ADDRESS_BUILDING, required = false, defaultValue = "") String building,
			@RequestParam(value = IControllerConstant.ADDRESS_BUILDING_ID, required = false, defaultValue = "") String building_id,
			@RequestParam(value = IControllerConstant.ADDRESS_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ADDRESS_ROAD, required = false, defaultValue = "") String road,
			@RequestParam(value = IControllerConstant.ADDRESS_ROAD_NUM, required = false, defaultValue = "") String road_num,
			@RequestParam(value = IControllerConstant.ADDRESS_VILLAGE, required = false, defaultValue = "") String village,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
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
	 * <em>根据地址ID查询</em><br>
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
	@ApiOperation(value = "根据地址ID查询", notes = "获取对应地址的信息")
	@GetMapping("/address_id/{address_id}")
	public String selectByAddressId(
			@PathVariable(value = IControllerConstant.ADDRESS_ADDRESS_ID, required = true) String address_id,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setAddress_id(address_id);

		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据地址编码查询</em><br>
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
	@ApiOperation(value = "根据地址编码查询", notes = "获取对应地址的信息")
	@GetMapping("/code/{code}")
	public String selectByCode(@PathVariable(value = IControllerConstant.ADDRESS_CODE, required = true) String code,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setCode(code);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据街道名称查询</em><br>
	 * examples:<br>
	 * http://localhost:8083/address/street/民治街道?limit=5
	 * 
	 * @param street    String 路径变量，指定查询的街道名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道名称查询", notes = "获取对应街道的所有地址信息")
	@GetMapping("/street/{street}")
	public String selectByStreet(
			@PathVariable(value = IControllerConstant.ADDRESS_STREET, required = true) String street,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setStreet(street);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据社区名称查询</em><br>
	 * examples:<br>
	 * http://localhost:8083/address/community/龙塘社区?limit=5
	 * 
	 * @param community String 路径变量，指定查询的社区名称
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据社区名称查询", notes = "获取对应社区的所有地址信息")
	@GetMapping("/community/{community}")
	public String selectByCommunity(
			@PathVariable(value = IControllerConstant.ADDRESS_COMMUNITY, required = true) String community,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setCommunity(community);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据建筑物ID查询</em><br>
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
	@ApiOperation(value = "根据建筑物ID查询", notes = "获取对应建筑物的所有地址信息")
	@GetMapping("/buildingID/{building_id}")
	public String selectByBuildingId(
			@PathVariable(value = IControllerConstant.ADDRESS_BUILDING_ID, required = true) String building_id,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setBuilding_id(building_id);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据建筑物名称查询</em><br>
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
	@ApiOperation(value = "根据建筑物名称查询", notes = "获取对应建筑物的所有地址信息")
	@GetMapping("/building/{building}")
	public String selectByBuilding(
			@PathVariable(value = IControllerConstant.ADDRESS_BUILDING, required = true) String building,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setBuilding(building);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据村名称查询</em><br>
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
	@ApiOperation(value = "根据村名称查询", notes = "获取对应村名的所有地址信息")
	@GetMapping("/village/{village}")
	public String selectByVillage(
			@PathVariable(value = IControllerConstant.ADDRESS_VILLAGE, required = true) String village,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setVillage(village);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据道路名称查询</em><br>
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
	@ApiOperation(value = "根据道路名称查询", notes = "获取对应道路的所有地址信息")
	@GetMapping("/road/{road}")
	public String selectByRoad(@PathVariable(value = IControllerConstant.ADDRESS_ROAD, required = true) String road,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = IControllerConstant.ADDRESS_ALL_FIELDS) String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ADDRESS_TABLE) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressRow aRow = new AddressRow();
		aRow.setRoad(road);
		return selectAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>根据关键词进行模糊查询</em><br>
	 * examples:<br>
	 * <!--
	 * http://localhost:8083/address/hint?keywords=龙华&limit=10
	 * -->
	 * 
	 * @param keywords String 请求参数，查询关键词，多个关键词以空格分隔
	 * @param limit    Integer 请求参数，最多查询记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据关键词进行模糊查询", notes = "根据关键词进行模糊查询")
	@GetMapping("/hint")
	public String selectAddressByKeywords(@RequestParam(value = IControllerConstant.QUERY_KEYWORDS) String keywords,
			@RequestParam(value = IControllerConstant.SQL_LIMIT) Integer limit) {
		return ControllerUtils.getResponseBody4(LuceneUtil.search(keywords, limit));
	}

	/**
	 * <em>根据数据库id进行查询</em><br>
	 * examples:<br>
	 * http://localhost:8083/address/id/1
	 * 
	 * @param id Integer 请求参数，指定查询的数据库id
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据id查询详细信息", notes = "根据id查询详细信息")
	@GetMapping("/id/{id}")
	public String selectById(@PathVariable(value = "id", required = true) Integer id) {
		Long start = System.currentTimeMillis();
		List<AddressRow> row = ControllerUtils.mapper.selectById(id);
		Long end = System.currentTimeMillis();
		System.out.println("selectById wasted time: " + (end - start));
		return ControllerUtils.getResponseBody(row);
	}

	/**
	 * <em>根据一组数据库id进行查询</em><br>
	 * examples:<br>
	 * http://localhost:8083/address/ids?in=1,2,3
	 * 
	 * @param ids Integer 请求参数，指定查询的一组数据库id，以,分隔
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据一组id查询详细信息", notes = "根据一组id查询详细信息")
	@GetMapping("/ids")
	public String selectByIds(@RequestParam(value = "in", required = true) String ids) {
		List<Integer> idList = new ArrayList<Integer>();
		String listString[] = ids.split(",");
		for (String str : listString) {
			idList.add(Integer.parseInt(str));
		}
		List<AddressRow> row = ControllerUtils.mapper.selectByIds(idList);
		return ControllerUtils.getResponseBody(row);
	}

}
