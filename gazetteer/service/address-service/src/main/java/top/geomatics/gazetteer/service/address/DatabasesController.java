package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.AddressEditorRow;

/**
 * <b>数据库资源查询服务</b><br>
 * 
 * @author whudyj
 *
 */
@Api(value = "/databases", tags = "数据库资源查询服务")
@RestController
@RequestMapping("/databases")
public class DatabasesController {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(DatabasesController.class);

	private static final String TABLENAME = "dmdz_edit";// 表名
	// 字段名称
	private static final String STREET_ = "street_";
	private static final String COMMUNITY_ = "community_";
	private static final String ORIGIN_ADDRESS = "origin_address";
	private static final String SIMILAR_ADDRESS = "similar_address";
	private static final String STANDARD_ADDRESS = "standard_address";
	private static final String STATUS = "status";
	private static final String MODIFIER = "modifier";
	private static final String UPDATE_DATE = "update_date";
	private static final String UPDATE_ADDRESS = "update_address";
	private static final String UPDATE_BUILDING_CODE = "update_building_code";

	private static final String STATUS_NEW = "new_status";
	private static final String MODIFIER_NEW = "new_modifier";
	private static final String UPDATE_DATE_NEW = "new_update_date";
	private static final String UPDATE_ADDRESS_NEW = "new_update_address";
	private static final String UPDATE_BUILDING_CODE_NEW = "new_update_building_code";
	private static final String ORIGIN_ADDRESS_NEW = "new_origin_address";
	private static final String SIMILAR_ADDRESS_NEW = "new_similar_address";
	private static final String STANDARD_ADDRESS_NEW = "new_standard_address";

	/**
	 * <b>查询数据</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/all?fields=fid,origin_address%26tablename=dmdz_edit%26limit=10
	 * </i>
	 * 
	 * @param username  String 请求参数，数据库目录
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,origin_address
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @param row       AddressEditorRow 指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询数据", notes = "查询数据。示例：/databases/all?fields=fid,origin_address&tablename=dmdz_edit&limit=10")
	@GetMapping("/all")
	public String editAll(@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = true) String dbPath,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如:dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit,
			AddressEditorRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fields, tablename, row, orderby, limit);
		DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
		List<AddressEditorRow> rows = null;
		if (null != dbInformation) {
			rows = dbInformation.getMapper().findEquals(map);
		}
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>返回记录总数</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/count?tablename=dmdz_edit </i>
	 * 
	 * @param dbPath    String 请求参数，数据库目录
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param row       AddressEditorRow 请求参数，指定查询条件
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "返回记录总数", notes = "返回记录总数")
	@GetMapping("/count")
	public String getCount(@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = true) String dbPath,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			AddressEditorRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap_revision(null, tablename, row, null, 0);
		DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
		String resFormat = "{ \"total\": " + "%d" + "}";
		long count = 0L;
		if (null != dbInformation) {
			count = dbInformation.getMapper().getCount(map);
		}
		return String.format(resFormat, count);
	}

	/**
	 * <b>分页查询数据</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/page/1?fields=fid,origin_address%26tablename=dmdz_edit%26limit=10
	 * </i>
	 * 
	 * @param dbPath    String 请求参数，数据库目录
	 * @param index     Integer 路径变量，当前页，从1开始
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,origin_address
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定每页查询的记录个数，如：limit=10
	 * @param row       AddressEditorRow 请求参数，指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "分页查询数据", notes = "分页查询数据，示例：/databases/page/1?fields=fid,origin_address&tablename=dmdz_edit&limit=10")
	@GetMapping("/page/{index}")
	public String editPage(@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = true) String dbPath,
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit,
			AddressEditorRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fields, tablename, row, orderby, limit);
		Integer page_start = (index - 1) * limit;
		map.put("page_start", page_start);
		DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
		List<AddressEditorRow> rows = null;
		if (null != dbInformation) {
			rows = dbInformation.getMapper().findPageEquals(map);
		}
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>返回查询记录总数</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/sum?tablename=dmdz_edit </i>
	 * 
	 * @param keywords String 请求参数，查询关键词，如：深圳市明慧汽配有限公司
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "返回查询记录总数", notes = "返回查询记录总数")
	@GetMapping("name/sum")
	public String getCountNameLike(
			@ApiParam(value = "查询关键词，如深圳市明慧汽配有限公司") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true) String keywords) {

		return QueryUtils.getCountNameLike(keywords);
	}

	/**
	 * <b>分页查询数据</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/name/page/1?keywords=深圳市明慧汽配有限公司%26limit=10
	 * </i>
	 * 
	 * @param keywords String 请求参数，查询关键词，如：深圳市明慧汽配有限公司
	 * @param index    Integer 路径变量，当前页，从1开始
	 * @param limit    Integer 请求参数，限定每页查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "分页查询数据", notes = "分页查询数据，示例：/databases/page/1?keywords=深圳市明慧汽配有限公司&limit=10")
	@GetMapping("name/page/{index}")
	public String queryAllPage(
			@ApiParam(value = "查询关键词，如深圳市明慧汽配有限公司") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true) String keywords,
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit) {

		return QueryUtils.queryPage(keywords, index, limit);
	}

	/**
	 * <b>根据fid查询详细信息</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/fid/1?fields=*%26tablename=dmdz_edit </i>
	 * 
	 * @param dbPath    String 请求参数，数据库目录
	 * @param fid       Integer 路径变量，数据库中记录的fid
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：*表示查询所有字段
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据fid查询详细信息", notes = "根据fid查询详细信息，示例：/databases/fid/1?fields=*&tablename=dmdz_edit")
	@GetMapping("/fid/{fid}")
	public String selectByFid(@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = true) String dbPath,
			@ApiParam(value = "数据库中地址记录的fid") @PathVariable(value = IControllerConstant.ENTERPRISE_DB_ID, required = true) Integer fid,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, null, null, 0);
		map.put(IControllerConstant.ENTERPRISE_DB_ID, fid);
		DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
		List<AddressEditorRow> rows = null;
		if (null != dbInformation) {
			rows = dbInformation.getMapper().selectByFid(map);
		}
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>根据一组fid查询详细信息</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/fids?in=1,2,3&field=*%26tablename=dmdz_edit</i>
	 * 
	 * @param dbPath    String 请求参数，数据库目录
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param fids      String 请求参数，指定查询的fid，多个fid以,分隔
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据一组fid查询详细信息", notes = "根据一组fid查询详细信息，示例：/databases/fids?in=1,2,3&field=*&tablename=dmdz_edit")
	@GetMapping("/fids")
	public String selectByFids(
			@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = true) String dbPath,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "数据库中地址记录的fid，如 1,2,3") @RequestParam(value = "in", required = true) String fids) {
		List<Integer> idList = new ArrayList<Integer>();
		String listString[] = fids.split(",");
		for (String str : listString) {
			idList.add(Integer.parseInt(str));
		}
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, null, null, 0);
		map.put("fids", idList);
		DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
		List<AddressEditorRow> rows = null;
		if (null != dbInformation) {
			rows = dbInformation.getMapper().selectByFids(map);
		}
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>指定条件查询</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/query?fields=fid,origin_address%26tablename=dmdz_edit%26origin_address=深圳市龙华区龙华街道东环一路天汇大厦B座906室
	 * </i>
	 * 
	 * @param dbPath           String 请求参数，数据库目录
	 * @param fields           String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,origin_address
	 * @param tablename        String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param fid              Integer 请求参数，指定fid字段的值
	 * @param street_          String 请求参数，指定street_字段的值
	 * @param community_       String 请求参数，指定community_字段的值
	 * @param origin_address   String 请求参数，指定origin_address字段的值
	 * @param similar_address  String 请求参数，指定similar_address字段的值
	 * @param standard_address String 请求参数，指定standard_address字段的值
	 * @param status           Integer 请求参数，指定status字段的值
	 * @param modifier         String 请求参数，指定modifier字段的值
	 * @param update_date      String 请求参数，指定update_date字段的值
	 * @param orderby          String 请求参数，指定查询结果排序方式
	 * @param limit            Integer 请求参数，限定查询的记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据条件精确查询", notes = "根据条件精确查询，返回所有查询的地址信息。示例：/databases/query?fields=fid,origin_address&tablename=dmdz_edit&origin_address=深圳市龙华区龙华街道东环一路天汇大厦B座906室")
	@GetMapping("/query")
	public String editWithConditions(
			@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = true) String dbPath,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "条件：数据库中地址记录的fid") @RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@ApiParam(value = "条件：指定街道") @RequestParam(value = STREET_, required = false, defaultValue = "") String street_,
			@ApiParam(value = "条件：指定社区") @RequestParam(value = COMMUNITY_, required = false, defaultValue = "") String community_,
			@ApiParam(value = "条件：指定原地址") @RequestParam(value = ORIGIN_ADDRESS, required = false, defaultValue = "") String origin_address,
			@ApiParam(value = "条件：指定相似地址") @RequestParam(value = SIMILAR_ADDRESS, required = false, defaultValue = "") String similar_address,
			@ApiParam(value = "条件：指定标准地址") @RequestParam(value = STANDARD_ADDRESS, required = false, defaultValue = "") String standard_address,
			@ApiParam(value = "条件：指定编辑状态") @RequestParam(value = STATUS, required = false, defaultValue = "") Integer status,
			@ApiParam(value = "条件：指定编辑者") @RequestParam(value = MODIFIER, required = false, defaultValue = "") String modifier,
			// @ApiParam(value = "条件：指定编辑日期") @RequestParam(value = UPDATE_DATE, required =
			// false, defaultValue = "") String update_date,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow row = new AddressEditorRow();
		if (null != fid)
			row.setFid(fid);
		if (null != street_ && !street_.isEmpty())
			row.setStreet_(street_);
		if (null != community_ && !community_.isEmpty())
			row.setCommunity_(community_);
		if (null != origin_address && !origin_address.isEmpty())
			row.setOrigin_address(origin_address);
		if (null != similar_address && !similar_address.isEmpty())
			row.setSimilar_address(similar_address);
		if (null != standard_address && !standard_address.isEmpty())
			row.setStandard_address(standard_address);
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier(modifier);

		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fields, tablename, row, orderby, limit);
		DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
		List<AddressEditorRow> rows = null;
		if (null != dbInformation) {
			rows = dbInformation.getMapper().findEquals(map);
		}
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>指定条件模糊查询</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/databases/fuzzyquery?fields=fid,origin_address%26tablename=dmdz_edit%26origin_address=天汇大厦B座906室
	 * </i>
	 * 
	 * @param dbPath           String 请求参数，数据库目录
	 * @param fields           String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,origin_address
	 * @param tablename        String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param fid              Integer 请求参数，指定fid字段的值
	 * @param street_          String 请求参数，指定street_字段的值
	 * @param community_       String 请求参数，指定community_字段的值
	 * @param origin_address   String 请求参数，指定origin_address字段的值
	 * @param similar_address  String 请求参数，指定similar_address字段的值
	 * @param standard_address String 请求参数，指定standard_address字段的值，如：天汇大厦B座906室
	 * @param status           Integer 请求参数，指定status字段的值
	 * @param modifier         String 请求参数，指定modifier字段的值
	 * @param update_date      String 请求参数，指定update_date字段的值
	 * @param orderby          String 请求参数，指定查询结果排序方式
	 * @param limit            Integer 请求参数，限定查询的记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据条件模糊查询", notes = "根据条件模糊查询，返回所有查询的地址信息。示例：/databases/fuzzyquery?fields=fid,origin_address&tablename=dmdz_edit&origin_address=天汇大厦B座906室")
	@GetMapping("/fuzzyquery")
	public String fuzzyEditWithConditions(
			@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = true) String dbPath,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "条件：数据库中地址记录的fid") @RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@ApiParam(value = "条件：指定街道") @RequestParam(value = STREET_, required = false, defaultValue = "") String street_,
			@ApiParam(value = "条件：指定社区") @RequestParam(value = COMMUNITY_, required = false, defaultValue = "") String community_,
			@ApiParam(value = "条件：指定原地址") @RequestParam(value = ORIGIN_ADDRESS, required = false, defaultValue = "") String origin_address,
			@ApiParam(value = "条件：指定相似地址") @RequestParam(value = SIMILAR_ADDRESS, required = false, defaultValue = "") String similar_address,
			@ApiParam(value = "条件：指定标准地址") @RequestParam(value = STANDARD_ADDRESS, required = false, defaultValue = "") String standard_address,
			@ApiParam(value = "条件：指定编辑状态") @RequestParam(value = STATUS, required = false, defaultValue = "") Integer status,
			@ApiParam(value = "条件：指定编辑者") @RequestParam(value = MODIFIER, required = false, defaultValue = "") String modifier,
			// @ApiParam(value = "条件：指定编辑日期") @RequestParam(value = UPDATE_DATE, required =
			// false, defaultValue = "") String update_date,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow row = new AddressEditorRow();
		if (null != fid)
			row.setFid(fid);
		if (null != street_ && !street_.isEmpty())
			row.setStreet_("%" + street_ + "%");
		if (null != community_ && !community_.isEmpty())
			row.setCommunity_("%" + community_ + "%");
		if (null != origin_address && !origin_address.isEmpty())
			row.setOrigin_address("%" + origin_address + "%");
		if (null != similar_address && !similar_address.isEmpty())
			row.setSimilar_address("%" + similar_address + "%");
		if (null != standard_address && !standard_address.isEmpty())
			row.setStandard_address("%" + standard_address + "%");
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier("%" + modifier + "%");

		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fields, tablename, row, orderby, limit);
		DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
		List<AddressEditorRow> rows = null;
		if (null != dbInformation) {
			rows = dbInformation.getMapper().findLike(map);
		}
		return ControllerUtils.getResponseBody_revision(rows);
	}

}
