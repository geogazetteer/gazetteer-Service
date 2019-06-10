package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;
import top.geomatics.gazetteer.utilities.database.BuildingQuery;

/**
 * <b>非标准地址修正服务</b><br>
 * 
 * @author whudyj
 *
 */
@Api(value = "/revision", tags = "非标准地址修正服务")
@RestController
@RequestMapping("/revision")
public class RevisionController {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(RevisionController.class);

	private static final String DEFAULT_USERNAME = "user_admin";
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
	 * <b>列出需要编辑的地址</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/all?fields=fid,origin_address%26tablename=dmdz_edit%26limit=10
	 * </i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,origin_address
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @param row       AddressEditorRow 指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "列出需要编辑的地址", notes = "列出需要编辑的地址。示例：/revision/all?fields=fid,origin_address&tablename=dmdz_edit&limit=10")
	@GetMapping("/all")
	public String editAll(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如:dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit,
			AddressEditorRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fields, tablename, row, orderby, limit);
		List<AddressEditorRow> rows = UserManager.getUserInfo(username).getMapper().findEquals(map);
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>返回记录总数</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/count?tablename=dmdz_edit </i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param row       AddressEditorRow 请求参数，指定查询条件
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "返回记录总数", notes = "返回记录总数")
	@GetMapping("/count")
	public String getCount(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			AddressEditorRow row) {
		// 选择status=0的记录
		Integer status = null;
		if (null != row) {
			status = row.getStatus();
		}
		if (status == null) {
			status = 0;
		}
		row.setStatus(status);

		Map<String, Object> map = ControllerUtils.getRequestMap_revision(null, tablename, row, null, 0);
		return "{ \"total\": " + UserManager.getUserInfo(username).getMapper().getCount(map) + "}";
	}

	/**
	 * <b>分页列出需要编辑的地址</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/page/1?fields=fid,origin_address%26tablename=dmdz_edit%26limit=10
	 * </i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param index     Integer 路径变量，当前页，从1开始
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,origin_address
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定每页查询的记录个数，如：limit=10
	 * @param row       AddressEditorRow 请求参数，指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "分页列出所有需要编辑的地址", notes = "分页列出所有需要编辑的地址，示例：/revision/page/1?fields=fid,origin_address&tablename=dmdz_edit&limit=10")
	@GetMapping("/page/{index}")
	public String editPage(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit,
			AddressEditorRow row) {
		// 选择status=0的记录
		Integer status = null;
		if (null != row) {
			status = row.getStatus();
		}
		if (status == null) {
			status = 0;
		}
		row.setStatus(status);

		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fields, tablename, row, orderby, limit);
		Integer page_start = (index - 1) * limit;
		map.put("page_start", page_start);
		List<AddressEditorRow> rows = UserManager.getUserInfo(username).getMapper().findPageEquals(map);
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>根据fid查询详细信息</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/fid/1?fields=*%26tablename=dmdz_edit </i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param fid       Integer 路径变量，数据库中记录的fid
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：*表示查询所有字段
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据fid查询详细信息", notes = "根据fid查询详细信息，示例：/revision/fid/1?fields=*&tablename=dmdz_edit")
	@GetMapping("/fid/{fid}")
	public String selectByFid(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "数据库中地址记录的fid") @PathVariable(value = IControllerConstant.ENTERPRISE_DB_ID, required = true) Integer fid,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, null, null, 0);
		map.put(IControllerConstant.ENTERPRISE_DB_ID, fid);
		List<AddressEditorRow> rows = UserManager.getUserInfo(username).getMapper().selectByFid(map);
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>根据fid查询并补充详细信息</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/guess/1?fields=*%26tablename=dmdz_edit </i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param fid       Integer 路径变量，数据库中记录的fid
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：*表示查询所有字段
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据fid查询并补充详细信息", notes = "根据fid查询并补充详细信息，示例：/revision/guess/1?fields=*&tablename=dmdz_edit")
	@GetMapping("/guess/{fid}")
	public String guessByFid(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "数据库中记录的fid") @PathVariable(value = IControllerConstant.ENTERPRISE_DB_ID, required = true) Integer fid,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename) {
		// 查询
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, null, null, 0);
		map.put(IControllerConstant.ENTERPRISE_DB_ID, fid);
		List<AddressEditorRow> rows = UserManager.getUserInfo(username).getMapper().selectByFid(map);
		// 计算
		List<AddressEditorRow> newRows = new ArrayList<AddressEditorRow>();
		for (AddressEditorRow row : rows) {
			AddressEditorRow newRow = new AddressEditorRow();
			// 如果没有原地址，则计算一个
			String originAddress = row.getOrigin_address();
			String street_ = row.getStreet_();
			String community_ = row.getCommunity_();
			String codeString = null;
			AddressRow arow = null;
			if (null == originAddress || originAddress.isEmpty()) {
				// 经纬度
				double longitude = row.getLongitude_();
				double latitude = row.getLatitude_();
				String tempString = longitude + "," + latitude;
				if (AddressProcessor.isCoordinatesExpression(tempString)) {
					List<String> buildingCodes = BuildingQuery.query(longitude, latitude);
					if (buildingCodes.size() > 0) {
						codeString = buildingCodes.get(0);
						AddressRow addrow = new AddressRow();
						addrow.setCode(codeString);
						Map<String, Object> para = ControllerUtils.getRequestMap("*", "dmdz", addrow, null, 1);
						List<AddressRow> addResRows = ControllerUtils.mapper.findEquals(map);
						if (addResRows.size() > 0) {
							arow = addResRows.get(0);
							originAddress = arow.getAddress();
						}
					}
				}
			}
			newRow.setOrigin_address(originAddress);
			if (null == street_ || street_.isEmpty()) {
				if (null != arow) {
					street_ = arow.getStreet();
				}
			}
			newRow.setStreet_(street_);

			if (null == community_ || community_.isEmpty()) {
				if (null != arow) {
					community_ = arow.getCommunity();
				}
			}
			newRow.setCommunity_(community_);

			newRows.add(newRow);
		}
		return ControllerUtils.getResponseBody_revision(newRows);
	}

	/**
	 * <b>根据一组fid查询详细信息</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/fids?in=1,2,3&field=*%26tablename=dmdz_edit</i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param fids      String 请求参数，指定查询的fid，多个fid以,分隔
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据一组fid查询详细信息", notes = "根据一组fid查询详细信息，示例：/revision/fids?in=1,2,3&field=*&tablename=dmdz_edit")
	@GetMapping("/fids")
	public String selectByFids(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
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
		List<AddressEditorRow> row = UserManager.getUserInfo(username).getMapper().selectByFids(map);
		return ControllerUtils.getResponseBody_revision(row);
	}

	/**
	 * <b>指定条件查询</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/query?fields=fid,origin_address%26tablename=dmdz_edit%26origin_address=深圳市龙华区龙华街道东环一路天汇大厦B座906室
	 * </i>
	 * 
	 * @param username         String 请求参数，用户名
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
	@ApiOperation(value = "根据条件精确查询", notes = "根据条件精确查询，返回所有查询的地址信息。示例：/revision/query?fields=fid,origin_address&tablename=dmdz_edit&origin_address=深圳市龙华区龙华街道东环一路天汇大厦B座906室")
	@GetMapping("/query")
	public String editWithConditions(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
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
		List<AddressEditorRow> rows = UserManager.getUserInfo(username).getMapper().findEquals(map);
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>指定条件模糊查询</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/revision/fuzzyquery?fields=fid,origin_address%26tablename=dmdz_edit%26origin_address=天汇大厦B座906室
	 * </i>
	 * 
	 * @param username         String 请求参数，用户名
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
	@ApiOperation(value = "根据条件模糊查询", notes = "根据条件模糊查询，返回所有查询的地址信息。示例：/revision/fuzzyquery?fields=fid,origin_address&tablename=dmdz_edit&origin_address=天汇大厦B座906室")
	@GetMapping("/fuzzyquery")
	public String fuzzyEditWithConditions(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
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
		List<AddressEditorRow> rows = UserManager.getUserInfo(username).getMapper().findLike(map);
		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>指定街道查询</b><br>
	 * <i>examples: <br>
	 * http://localhost:8083/revision/street_/民治街道</i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param street_   String 路径变量，指定街道
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道查询", notes = "根据街道查询，获取对应街道的数据，示例：/revision/street_/民治街道")
	@GetMapping("/street_/{street_}")
	public String editByStreet_(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "指定街道") @PathVariable(value = STREET_, required = true) String street_,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow aRow = new AddressEditorRow();
		aRow.setStreet_(street_);
		return editAll(username, fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>指定社区查询</b><br>
	 * <i>examples: <br>
	 * http://localhost:8083/revision/community_/龙塘社区</i>
	 * 
	 * @param username   String 请求参数，用户名
	 * @param community_ String 路径变量，指定社区
	 * @param fields     String 请求参数，需要选择的字段，如：*
	 * @param tablename  String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby    String 请求参数，指定查询结果排序方式
	 * @param limit      Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据社区查询", notes = "根据社区查询，获取对应社区的数据，示例：8083/revision/community_/龙塘社区")
	@GetMapping("/community_/{community_}")
	public String editByName(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@PathVariable(value = COMMUNITY_, required = true) String community_,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow aRow = new AddressEditorRow();
		aRow.setCommunity_(community_);
		return editAll(username, fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>指定原地址查询</b><br>
	 * <i>examples: <br>
	 * http://localhost:8083/revision/origin_address/深圳市龙华区民治街道龙塘社区龙塘新城东区4巷4号301</i>
	 * 
	 * @param username       String 请求参数，用户名
	 * @param origin_address String 路径变量，指定原地址
	 * @param fields         String 请求参数，需要选择的字段，如：*
	 * @param tablename      String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby        String 请求参数，指定查询结果排序方式
	 * @param limit          Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据原地址查询", notes = "根据原地址查询，获取对应原地址的数据，示例：/revision/origin_address/深圳市龙华区民治街道龙塘社区龙塘新城东区4巷4号301")
	@GetMapping("/origin_address/{origin_address}")
	public String editByStreet(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "指定街道，如龙华") @PathVariable(value = ORIGIN_ADDRESS, required = true) String origin_address,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow aRow = new AddressEditorRow();
		aRow.setOrigin_address(origin_address);
		return editAll(username, fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>指定相似地址查询</b><br>
	 * <i>examples: <br>
	 * http://localhost:8083/revision/similar_address/广东省深圳市龙华区民治街道龙塘社区龙塘新村53栋401</i>
	 * 
	 * @param username        String 请求参数，用户名
	 * @param similar_address String 路径变量，指定相似地址
	 * @param fields          String 请求参数，需要选择的字段，如：*
	 * @param tablename       String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby         String 请求参数，指定查询结果排序方式
	 * @param limit           Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据相似地址查询", notes = "根据相似地址查询，获取对应相似地址的数据，示例：/revision/similar_address/广东省深圳市龙华区民治街道龙塘社区龙塘新村53栋401")
	@GetMapping("/similar_address/{similar_address}")
	public String editByOwner(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "指定相似地址，如：方海英") @PathVariable(value = SIMILAR_ADDRESS, required = true) String similar_address,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow aRow = new AddressEditorRow();
		aRow.setSimilar_address(similar_address);
		return editAll(username, fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>指定标准地址查询</b><br>
	 * <i>examples: <br>
	 * http://localhost:8083/revision/address/广东省深圳市龙华区民治街道龙塘社区龙塘新村53栋401</i>
	 * 
	 * @param username         String 请求参数，用户名
	 * @param standard_address String 路径变量，指定标准地址
	 * @param fields           String 请求参数，需要选择的字段，如：*
	 * @param tablename        String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby          String 请求参数，指定查询结果排序方式
	 * @param limit            Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据标准地址查询", notes = "根据标准地址查询，获取对应标准地址的数据，示例：/revision/standard_address/广东省深圳市龙华区民治街道龙塘社区龙塘新村53栋401")
	@GetMapping("/standard_address/{standard_address}")
	public String editByAddress(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "指定标准地址，如：广东省深圳市龙华区民治街道龙塘社区龙塘新村53栋401") @PathVariable(value = STANDARD_ADDRESS, required = true) String standard_address,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow aRow = new AddressEditorRow();
		aRow.setStandard_address(standard_address);
		return editAll(username, fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>指定状态查询</b><br>
	 * <i>examples: <br>
	 * http://localhost:8083/revision/status/0</i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param status    String 路径变量，指定状态
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据状态查询", notes = "根据状态查询，获取对应状态的数据，示例：/revision/status/0")
	@GetMapping("/status/{status}")
	public String editByStatus(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "指定状态，如0表示未编辑状态，1表示已编辑状态，2表示无法编辑状态") @PathVariable(value = STATUS, required = true) int status,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow aRow = new AddressEditorRow();
		aRow.setStatus(status);
		return editAll(username, fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>指定编辑者查询</b><br>
	 * <i>examples: <br>
	 * http://localhost:8083/revision/modifier/张三 </i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param modifier  String 路径变量，指定编辑者
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据编辑者查询", notes = "根据编辑者查询，获取对应编辑者的数据，示例：/revision/modifier/张三")
	@GetMapping("/modifier/{modifier}")
	public String editByModifier(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "指定编辑者，如张三") @PathVariable(value = MODIFIER, required = true) String modifier,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		AddressEditorRow aRow = new AddressEditorRow();
		aRow.setModifier(modifier);
		return editAll(username, fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <b>更新需要编辑的地址</b><br>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param tablename String 请求参数，需要更新的数据库表名
	 * @param row       AddressEditorRow 更新条件
	 * @param new_row   AddressEditorRow 更新后的数据
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新所有需要编辑的地址", notes = "更新所有需要编辑的地址，示例：/revision/update?tablename=dmdz_edit")
	@PutMapping("/update")
	public String updateAll(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam @RequestBody AddressEditorRow row, @ApiParam @RequestBody AddressEditorRow new_row) {

		Map<String, Object> map = ControllerUtils.getRequestMap_revision(null, tablename, row, null, 0);
		// String new_status = new_row.getStatus().toString();
		String new_status = "1";
		// String new_modifier = new_row.getModifier();
		String new_modifier = UserManager.getUserInfo(username).getUserName();
		// String new_update_date = new_row.getUpdate_date().toString();
		String new_update_date = new Date().toString();
		String new_update_address = new_row.getUpdate_address();
		String new_update_building_code = new_row.getUpdate_building_code();

		String new_origin_address = new_row.getOrigin_address();
		String new_similar_address = new_row.getSimilar_address();
		String new_standrad_address = new_row.getStandard_address();

		if (null != new_origin_address)
			map.put(ORIGIN_ADDRESS_NEW, new_origin_address);
		if (null != new_similar_address)
			map.put(SIMILAR_ADDRESS_NEW, new_similar_address);
		if (null != new_standrad_address)
			map.put(STANDARD_ADDRESS_NEW, new_standrad_address);

		if (null != new_status)
			map.put(STATUS_NEW, new_status);
		if (null != new_modifier && !new_modifier.isEmpty())
			map.put(MODIFIER_NEW, new_modifier);
		if (null != new_update_date && !new_update_date.isEmpty())
			map.put(UPDATE_DATE_NEW, new_update_date);
		if (null != new_update_address && !new_update_address.isEmpty())
			map.put(UPDATE_ADDRESS_NEW, new_update_address);
		if (null != new_update_building_code && !new_update_building_code.isEmpty())
			map.put(UPDATE_BUILDING_CODE_NEW, new_update_building_code);

		Integer updatedRows = UserManager.getUserInfo(username).getMapper().updateAll(map);
		UserManager.getUserInfo(username).getSqlSession().commit(true);
		return ControllerUtils.getUpdateResponseBody(updatedRows);
	}

	/**
	 * <b>根据fid更新需要编辑的地址</b><br>
	 * <i> examples: <br>
	 * http://localhost:8083/revision/update/2?tablename=dmdz_edit </i>
	 * 
	 * @param username  String 请求参数，用户名
	 * @param fid       Integer 路径变量，需要更新的fid
	 * @param tablename String 请求参数，需要更新的数据库表名
	 * @param new_row   AddressEditorRow 更新后的数据
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "根据fid更新需要编辑的地址", notes = "根据fid更新需要编辑的地址，示例：/revision/update/2?tablename=dmdz_edit")
	@PutMapping("/update/{fid}")
	public String updateAllByFid(
			@ApiParam(value = "用户名") @RequestParam(value = "username", required = true, defaultValue = DEFAULT_USERNAME) String username,
			@ApiParam(value = "需要更新地址的fid") @PathVariable(value = IControllerConstant.ENTERPRISE_DB_ID, required = true) Integer fid,
			@ApiParam(value = "查询的数据库表，如dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam @RequestBody AddressEditorRow new_row) {

		AddressEditorRow row = new AddressEditorRow();
		row.setFid(fid);

		return updateAll(username, tablename, row, new_row);
	}

}
