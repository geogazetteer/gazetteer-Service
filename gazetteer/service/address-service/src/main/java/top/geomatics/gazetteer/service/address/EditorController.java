package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.geomatics.gazetteer.model.EnterpriseRow;

/**
 * <em>编辑服务</em><br>
 * <i>说明</i><br>
 * <i>目前只针对企业法人数据库中的地名地址进行处理</i>
 * 
 * @author whudyj
 *
 */
@Api(value="/editor",tags="法人库地址编辑服务")
@RestController
@RequestMapping("/editor")
public class EditorController {

	/**
	 * <em>列出需要编辑的地址</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/editor/all?fields=fid,code,name,address&tablename=油松社区&limit=10-->
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表，如：油松社区
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @param row       EnterpriseRow 指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "列出需要编辑的地址", notes = "列出需要编辑的地址")
	@GetMapping("/all")
	public String editAll(
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit,
			EnterpriseRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findEquals(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * <em>返回记录总数</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/editor/count?tablename=油松社区-->
	 * 
	 * @param tablename String 请求参数，指定查询的数据库表，如：油松社区
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "返回记录总数", notes = "返回记录总数")
	@GetMapping("/count")
	public String getCount(
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename) {
		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, null, null, 0);
		return "{ \"total\": "  + ControllerUtils.mapper2.getCount(map) + "}";
	}

	/**
	 * <em>分页列出需要编辑的地址</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/editor/page/1?fields=fid,code,name,address&tablename=油松社区&limit=10-->
	 * 
	 * @param index     Integer 路径变量，当前页，从1开始
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表，如：油松社区
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定每页查询的记录个数，如：limit=10
	 * @param row       EnterpriseRow 请求参数，指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "列出所有需要编辑的地址", notes = "列出所有需要编辑的地址")
	@GetMapping("/page/{index}")
	public String editPage(@PathVariable(value = "index", required = true) Integer index,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit,
			EnterpriseRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		Integer page_start = (index - 1) * limit;
		map.put("page_start", page_start);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findPageEquals(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * <em>根据fid查询详细信息</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/editor/fid/1?fields=*&tablename=油松社区-->
	 * 
	 * @param fid       Integer 路径变量，数据库中记录的fid
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：*表示查询所有字段
	 * @param tablename String 请求参数，指定查询的数据库表，如：油松社区
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据fid查询详细信息", notes = "根据fid查询详细信息")
	@GetMapping("/fid/{fid}")
	public String selectByFid(@PathVariable(value = IControllerConstant.ENTERPRISE_DB_ID, required = true) Integer fid,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, null, null, 0);
		map.put(IControllerConstant.ENTERPRISE_DB_ID, fid);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.selectByFid(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * <em>根据一组fid查询详细信息</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/editor/fids?in=1,2,3&field=*&tablename=民治街道-->
	 * 
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：民治街道
	 * @param fids      String 请求参数，指定查询的fid，多个fid以,分隔
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据一组fid查询详细信息", notes = "根据一组fid查询详细信息")
	@GetMapping("/fids")
	public String selectByFids(
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = "in", required = true) String fids) {
		List<Integer> idList = new ArrayList<Integer>();
		String listString[] = fids.split(",");
		for (String str : listString) {
			idList.add(Integer.parseInt(str));
		}
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, null, null, 0);
		map.put("fids", idList);
		List<EnterpriseRow> row = ControllerUtils.mapper2.selectByFids(map);
		return ControllerUtils.getResponseBody2(row);
	}

	/**
	 * <em>指定条件查询</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/editor/query?fields=fid,code,name,address&tablename=enterprise1&address=深圳市龙华区龙华街道东环一路天汇大厦B座906室-->
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename   String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param fid         Integer 请求参数，指定fid字段的值
	 * @param code        String 请求参数，指定code字段的值
	 * @param name        String 请求参数，指定name字段的值
	 * @param street      String 请求参数，指定street字段的值
	 * @param owner       String 请求参数，指定owner字段的值
	 * @param address     String 请求参数，指定address字段的值，如：深圳市龙华区龙华街道东环一路天汇大厦B座906室
	 * @param status      Integer 请求参数，指定status字段的值
	 * @param modifier    String 请求参数，指定modifier字段的值
	 * @param update_date String 请求参数，指定update_date字段的值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       Integer 请求参数，限定查询的记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据条件查询", notes = "根据条件查询")
	@GetMapping("/query")
	public String editWithConditions(
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@RequestParam(value = IControllerConstant.ENTERPRISE_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ENTERPRISE_NAME, required = false, defaultValue = "") String name,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ENTERPRISE_OWNER, required = false, defaultValue = "") String owner,
			@RequestParam(value = IControllerConstant.ENTERPRISE_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS, required = false, defaultValue = "") Integer status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER, required = false, defaultValue = "") String modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE, required = false, defaultValue = "") String update_date,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow row = new EnterpriseRow();
		if (null != fid)
			row.setFid(fid);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != name && !name.isEmpty())
			row.setName(name);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != owner && !owner.isEmpty())
			row.setOwner(owner);
		if (null != address && !address.isEmpty())
			row.setAddress(address);
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier(modifier);

		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findEquals(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * <em>指定条件模糊查询</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/editor/fuzzyquery?fields=fid,code,name,address&tablename=enterprise1&address=天汇大厦B座906室
	 * -->
	 * 
	 * @param fields      String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename   String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param fid         Integer 请求参数，指定fid字段的值
	 * @param code        String 请求参数，指定code字段的值
	 * @param name        String 请求参数，指定name字段的值
	 * @param street      String 请求参数，指定street字段的值
	 * @param owner       String 请求参数，指定owner字段的值
	 * @param address     String 请求参数，指定address字段的值，如：天汇大厦B座906室
	 * @param status      Integer 请求参数，指定status字段的值
	 * @param modifier    String 请求参数，指定modifier字段的值
	 * @param update_date String 请求参数，指定update_date字段的值
	 * @param orderby     String 请求参数，指定查询结果排序方式
	 * @param limit       Integer 请求参数，限定查询的记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据条件模糊查询", notes = "根据条件模糊查询")
	@GetMapping("/fuzzyquery")
	public String fuzzyEditWithConditions(
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@RequestParam(value = IControllerConstant.ENTERPRISE_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ENTERPRISE_NAME, required = false, defaultValue = "") String name,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ENTERPRISE_OWNER, required = false, defaultValue = "") String owner,
			@RequestParam(value = IControllerConstant.ENTERPRISE_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS, required = false, defaultValue = "") Integer status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER, required = false, defaultValue = "") String modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE, required = false, defaultValue = "") String update_date,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow row = new EnterpriseRow();
		if (null != fid)
			row.setFid(fid);
		if (null != code && !code.isEmpty())
			row.setCode("%" + code + "%");
		if (null != name && !name.isEmpty())
			row.setName("%" + name + "%");
		if (null != street && !street.isEmpty())
			row.setStreet("%" + street + "%");
		if (null != owner && !owner.isEmpty())
			row.setOwner("%" + owner + "%");
		if (null != address && !address.isEmpty())
			row.setAddress("%" + address + "%");
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier("%" + modifier + "%");

		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findLike(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * <em>指定统一社会信用代码查询</em><br>
	 * examples: <br>
	 * http://localhost:8083/editor/code/91440300MA5DK2PU7P
	 * 
	 * @param code      String 路径变量，指定统一社会信用代码
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据统一社会信用代码查询", notes = "获取对应统一社会信用代码的数据")
	@GetMapping("/code/{code}")
	public String editByCode(@PathVariable(value = IControllerConstant.ENTERPRISE_CODE, required = true) String code,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setCode(code);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>指定企业名称查询</em><br>
	 * examples: <br>
	 * http://localhost:8083/editor/name/深圳市明慧汽配有限公司
	 * 
	 * @param name      String 路径变量，指定企业名称
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据企业名称查询", notes = "获取对应企业名称的数据")
	@GetMapping("/name/{name}")
	public String editByName(@PathVariable(value = IControllerConstant.ENTERPRISE_NAME, required = true) String name,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setName(name);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>指定街道查询</em><br>
	 * examples: <br>
	 * http://localhost:8083/editor/street/龙华?limit=10
	 * 
	 * @param street    String 路径变量，指定街道
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据街道查询", notes = "获取对应街道的数据")
	@GetMapping("/street/{street}")
	public String editByStreet(
			@PathVariable(value = IControllerConstant.ENTERPRISE_STREET, required = true) String street,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setStreet(street);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>指定法定代表人查询</em><br>
	 * examples: <br>
	 * http://localhost:8083/editor/owner/方海英
	 * 
	 * @param owner     String 路径变量，指定法定代表人
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据法定代表人查询", notes = "获取对应法定代表人的数据")
	@GetMapping("/owner/{owner}")
	public String editByOwner(@PathVariable(value = IControllerConstant.ENTERPRISE_OWNER, required = true) String owner,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setOwner(owner);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>指定JYCS（地址）查询</em><br>
	 * examples: <br>
	 * http://localhost:8083/editor/address/深圳市龙华区龙华街道东环一路天汇大厦B座906室
	 * 
	 * @param address   String 路径变量，指定JYCS（地址）
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据JYCS查询", notes = "获取对应JYCS的数据")
	@GetMapping("/address/{address}")
	public String editByAddress(
			@PathVariable(value = IControllerConstant.ENTERPRISE_ADDRESS, required = true) String address,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setAddress(address);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>指定状态查询</em><br>
	 * examples: <br>
	 * http://localhost:8083/editor/status/0
	 * 
	 * @param status    String 路径变量，指定状态
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据状态查询", notes = "获取对应状态的数据")
	@GetMapping("/status/{status}")
	public String editByStatus(@PathVariable(value = IControllerConstant.ENTERPRISE_STATUS, required = true) int status,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setStatus(status);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>指定编辑者查询</em><br>
	 * examples: <br>
	 * http://localhost:8083/editor/modifier/Admin
	 * 
	 * @param modifier  String 路径变量，指定编辑者
	 * @param fields    String 请求参数，需要选择的字段，如：*
	 * @param tablename String 请求参数，指定查询的数据库表，如：enterprise1
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     Integer 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据编辑者查询", notes = "获取对应编辑者的数据")
	@GetMapping("/modifier/{modifier}")
	public String editByModifier(
			@PathVariable(value = IControllerConstant.ENTERPRISE_MODIFIER, required = true) String modifier,
			@RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setModifier(modifier);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * <em>更新需要编辑的地址</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/editor/update/all?tablename=福民社区&new_status=0&new_modifier=admin
	 * -->
	 * 
	 * @param tablename             String 请求参数，需要更新的数据库表名
	 * @param new_status            Integer 请求参数，更新后的状态
	 * @param new_modifier          String 请求参数，更新者
	 * @param new_update_date       String 请求参数，更新日期
	 * @param new_update_address    String 请求参数，更新确认的标准地址
	 * @param new_update_address_id String 请求参数，更新确认的标准地址编码
	 * @param row                   EnterpriseRow 更新条件
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新需要编辑的地址", notes = "更新需要编辑的地址")
	@GetMapping("/update/all")
	public String updateAll(
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS_NEW, required = false) Integer new_status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER_NEW, required = false, defaultValue = "") String new_modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE_NEW, required = false, defaultValue = "") String new_update_date,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_ADDRESS_NEW, required = false, defaultValue = "") String new_update_address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_ADDRES_ID_NEW, required = false, defaultValue = "") String new_update_address_id,
			EnterpriseRow row) {

		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, row, null, 0);
		if (null != new_status)
			map.put(IControllerConstant.ENTERPRISE_STATUS_NEW, new_status);
		if (null != new_modifier && !new_modifier.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_MODIFIER_NEW, new_modifier);
		if (null != new_update_date && !new_update_date.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_UPDATE_DATE_NEW, new_update_date);
		if (null != new_update_address && !new_update_address.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_UPDATE_ADDRESS_NEW, new_update_address);
		if (null != new_update_address_id && !new_update_address_id.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_UPDATE_ADDRES_ID_NEW, new_update_address_id);
		Integer updatedRows = ControllerUtils.mapper2.updateAll(map);
		ControllerUtils.session2.commit(true);
		return ControllerUtils.getUpdateResponseBody(updatedRows);
	}

	/**
	 * <em>根据fid更新需要编辑的地址</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/editor/update/all/2?tablename=福民社区&new_status=0&new_modifier=李四
	 * -->
	 * 
	 * @param fid                   Integer 路径变量，需要更新的fid
	 * @param tablename             String 请求参数，需要更新的数据库表名
	 * @param new_status            Integer 请求参数，更新后的状态
	 * @param new_modifier          String 请求参数，更新者
	 * @param new_update_date       String 请求参数，更新日期
	 * @param new_update_address    String 请求参数，更新确认的标准地址
	 * @param new_update_address_id String 请求参数，更新确认的标准地址编码
	 * @param row                   EnterpriseRow 更新条件
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新需要编辑的地址", notes = "更新需要编辑的地址")
	@GetMapping("/update/all/{fid}")
	public String updateAllByFid(
			@PathVariable(value = IControllerConstant.ENTERPRISE_DB_ID, required = true) Integer fid,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS_NEW, required = false) Integer new_status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER_NEW, required = false, defaultValue = "") String new_modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE_NEW, required = false, defaultValue = "") String new_update_date,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_ADDRESS_NEW, required = false, defaultValue = "") String new_update_address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_ADDRES_ID_NEW, required = false, defaultValue = "") String new_update_address_id,
			EnterpriseRow row) {

		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, row, null, 0);
		map.put(IControllerConstant.ENTERPRISE_DB_ID, fid);
		if (null != new_status)
			map.put(IControllerConstant.ENTERPRISE_STATUS_NEW, new_status);
		if (null != new_modifier && !new_modifier.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_MODIFIER_NEW, new_modifier);
		if (null != new_update_date && !new_update_date.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_UPDATE_DATE_NEW, new_update_date);
		if (null != new_update_address && !new_update_address.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_UPDATE_ADDRESS_NEW, new_update_address);
		if (null != new_update_address_id && !new_update_address_id.isEmpty())
			map.put(IControllerConstant.ENTERPRISE_UPDATE_ADDRES_ID_NEW, new_update_address_id);
		Integer updatedRows = ControllerUtils.mapper2.updateAll(map);
		ControllerUtils.session2.commit(true);
		return ControllerUtils.getUpdateResponseBody(updatedRows);
	}

	/**
	 * <em>更新状态字段值</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/editor/update/status/1?tablename=福民社区&code=91440300342919578H
	 * -->
	 * 
	 * @param new_status  Integer 路径变量，更新后的状态，如：0（表示未修改），1（表示已修改），2（表示无法修改）
	 * @param tablename   String 请求参数，指定查询的数据库表，如：福民社区
	 * @param fid         Integer 请求参数，指定fid字段的值
	 * @param code        String 请求参数，指定code字段的值
	 * @param name        String 请求参数，指定name字段的值
	 * @param street      String 请求参数，指定street字段的值
	 * @param owner       String 请求参数，指定owner字段的值
	 * @param address     String 请求参数，指定address字段的值
	 * @param status      Integer 请求参数，指定status字段的值
	 * @param modifier    String 请求参数，指定modifier字段的值
	 * @param update_date String 请求参数，指定update_date字段的值
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新状态", notes = "更新对应状态的数据")
	@GetMapping("/update/status/{new_status}")
	public String updateStatus(
			@PathVariable(value = IControllerConstant.ENTERPRISE_STATUS_NEW, required = true) Integer new_status,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@RequestParam(value = IControllerConstant.ENTERPRISE_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ENTERPRISE_NAME, required = false, defaultValue = "") String name,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ENTERPRISE_OWNER, required = false, defaultValue = "") String owner,
			@RequestParam(value = IControllerConstant.ENTERPRISE_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS, required = false) Integer status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER, required = false, defaultValue = "") String modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE, required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		if (null != fid)
			row.setFid(fid);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != name && !name.isEmpty())
			row.setName(name);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != owner && !owner.isEmpty())
			row.setOwner(owner);
		if (null != address && !address.isEmpty())
			row.setAddress(address);
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier(modifier);

		return updateAll(tablename, new_status, null, null, null, null, row);
	}

	/**
	 * <em>更新编辑者字段值</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/editor/update/modifier/张三?tablename=福民社区&code=91440300342919578H
	 * -->
	 * 
	 * @param new_modifier String 路径变量，更新后的编辑者
	 * @param tablename    String 请求参数，指定查询的数据库表，如：福民社区
	 * @param fid          Integer 请求参数，指定fid字段的值
	 * @param code         String 请求参数，指定code字段的值
	 * @param name         String 请求参数，指定name字段的值
	 * @param street       String 请求参数，指定street字段的值
	 * @param owner        String 请求参数，指定owner字段的值
	 * @param address      String 请求参数，指定address字段的值
	 * @param status       Integer 请求参数，指定status字段的值
	 * @param modifier     String 请求参数，指定modifier字段的值
	 * @param update_date  String 请求参数，指定update_date字段的值
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新编辑者", notes = "更新对应编辑者的数据")
	@GetMapping("/update/modifier/{new_modifier}")
	public String updateModifier(
			@PathVariable(value = IControllerConstant.ENTERPRISE_MODIFIER_NEW, required = true) String new_modifier,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@RequestParam(value = IControllerConstant.ENTERPRISE_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ENTERPRISE_NAME, required = false, defaultValue = "") String name,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ENTERPRISE_OWNER, required = false, defaultValue = "") String owner,
			@RequestParam(value = IControllerConstant.ENTERPRISE_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER, required = false, defaultValue = "") String modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS, required = false, defaultValue = "") Integer status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE, required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		if (null != fid)
			row.setFid(fid);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != name && !name.isEmpty())
			row.setName(name);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != owner && !owner.isEmpty())
			row.setOwner(owner);
		if (null != address && !address.isEmpty())
			row.setAddress(address);
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier(modifier);

		return updateAll(tablename, null, new_modifier, null, null, null, row);
	}

	/**
	 * <em>更新编辑日期字段值</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/editor/update/update_date/20190327?tablename=福民社区&code=91440300342919578H
	 * -->
	 * 
	 * @param new_update_date String 路径变量，更新后的编辑日期
	 * @param tablename       String 请求参数，指定查询的数据库表，如：福民社区
	 * @param fid             Integer 请求参数，指定fid字段的值
	 * @param code            String 请求参数，指定code字段的值
	 * @param name            String 请求参数，指定name字段的值
	 * @param street          String 请求参数，指定street字段的值
	 * @param owner           String 请求参数，指定owner字段的值
	 * @param address         String 请求参数，指定address字段的值
	 * @param status          Integer 请求参数，指定status字段的值
	 * @param modifier        String 请求参数，指定modifier字段的值
	 * @param update_date     String 请求参数，指定update_date字段的值
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新编辑日期", notes = "更新对应编辑日期的数据")
	@GetMapping("/update/update_date/{new_update_date}")
	public String updateDate(
			@PathVariable(value = IControllerConstant.ENTERPRISE_UPDATE_DATE_NEW, required = true) String new_update_date,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@RequestParam(value = IControllerConstant.ENTERPRISE_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ENTERPRISE_NAME, required = false, defaultValue = "") String name,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ENTERPRISE_OWNER, required = false, defaultValue = "") String owner,
			@RequestParam(value = IControllerConstant.ENTERPRISE_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS, required = false, defaultValue = "") Integer status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER, required = false, defaultValue = "") String modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE, required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		if (null != fid)
			row.setFid(fid);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != name && !name.isEmpty())
			row.setName(name);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != owner && !owner.isEmpty())
			row.setOwner(owner);
		if (null != address && !address.isEmpty())
			row.setAddress(address);
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier(modifier);

		return updateAll(tablename, null, null, new_update_date, null, null, row);
	}

	/**
	 * <em>更新标准地址字段值</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/editor/update/update_address/广东省深圳市龙华区福城街道福民社区冼屋居民小组厂房一116号?tablename=福民社区&code=91440300342919578H
	 * -->
	 * 
	 * @param new_update_address String 路径变量，更新后的标准地址
	 * @param tablename          String 请求参数，指定查询的数据库表，如：福民社区
	 * @param fid                Integer 请求参数，指定fid字段的值
	 * @param code               String 请求参数，指定code字段的值
	 * @param name               String 请求参数，指定name字段的值
	 * @param street             String 请求参数，指定street字段的值
	 * @param owner              String 请求参数，指定owner字段的值
	 * @param address            String 请求参数，指定address字段的值
	 * @param status             Integer 请求参数，指定status字段的值
	 * @param modifier           String 请求参数，指定modifier字段的值
	 * @param update_date        String 请求参数，指定update_date字段的值
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新地址", notes = "更新对应地址的数据")
	@GetMapping("/update/update_address/{new_update_address}")
	public String updateAddress(
			@PathVariable(value = IControllerConstant.ENTERPRISE_UPDATE_ADDRESS_NEW, required = true) String new_update_address,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@RequestParam(value = IControllerConstant.ENTERPRISE_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ENTERPRISE_NAME, required = false, defaultValue = "") String name,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ENTERPRISE_OWNER, required = false, defaultValue = "") String owner,
			@RequestParam(value = IControllerConstant.ENTERPRISE_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS, required = false, defaultValue = "") Integer status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER, required = false, defaultValue = "") String modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE, required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		if (null != fid)
			row.setFid(fid);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != name && !name.isEmpty())
			row.setName(name);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != owner && !owner.isEmpty())
			row.setOwner(owner);
		if (null != address && !address.isEmpty())
			row.setAddress(address);
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier(modifier);

		return updateAll(tablename, null, null, null, new_update_address, null, row);
	}

	/**
	 * <em>更新标准地址编码字段值</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/editor/update/update_address_id/63EEDE6BA0046A3AE0538CC0C0C07BB0?tablename=福民社区&code=91440300342919578H-->
	 * 
	 * @param new_update_address_id String 路径变量，更新后的标准地址编码
	 * @param tablename             String 请求参数，指定查询的数据库表，如：福民社区
	 * @param fid                   Integer 请求参数，指定fid字段的值
	 * @param code                  String 请求参数，指定code字段的值
	 * @param name                  String 请求参数，指定name字段的值
	 * @param street                String 请求参数，指定street字段的值
	 * @param owner                 String 请求参数，指定owner字段的值
	 * @param address               String 请求参数，指定address字段的值
	 * @param status                Integer 请求参数，指定status字段的值
	 * @param modifier              String 请求参数，指定modifier字段的值
	 * @param update_date           String 请求参数，指定update_date字段的值
	 * @return String 返回JSON格式的更新结果说明
	 */
	@ApiOperation(value = "更新地址编码", notes = "更新对应地址编码的数据")
	@GetMapping("/update/update_address_id/{new_update_address_id}")
	public String updateAddressId(
			@PathVariable(value = IControllerConstant.ENTERPRISE_UPDATE_ADDRES_ID_NEW, required = true) String new_update_address_id,
			@RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = IControllerConstant.ENTERPRISE_TABLE_NAME) String tablename,
			@RequestParam(value = IControllerConstant.ENTERPRISE_DB_ID, required = false) Integer fid,
			@RequestParam(value = IControllerConstant.ENTERPRISE_CODE, required = false, defaultValue = "") String code,
			@RequestParam(value = IControllerConstant.ENTERPRISE_NAME, required = false, defaultValue = "") String name,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STREET, required = false, defaultValue = "") String street,
			@RequestParam(value = IControllerConstant.ENTERPRISE_OWNER, required = false, defaultValue = "") String owner,
			@RequestParam(value = IControllerConstant.ENTERPRISE_ADDRESS, required = false, defaultValue = "") String address,
			@RequestParam(value = IControllerConstant.ENTERPRISE_STATUS, required = false, defaultValue = "") Integer status,
			@RequestParam(value = IControllerConstant.ENTERPRISE_MODIFIER, required = false, defaultValue = "") String modifier,
			@RequestParam(value = IControllerConstant.ENTERPRISE_UPDATE_DATE, required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		if (null != fid)
			row.setFid(fid);
		if (null != code && !code.isEmpty())
			row.setCode(code);
		if (null != name && !name.isEmpty())
			row.setName(name);
		if (null != street && !street.isEmpty())
			row.setStreet(street);
		if (null != owner && !owner.isEmpty())
			row.setOwner(owner);
		if (null != address && !address.isEmpty())
			row.setAddress(address);
		if (null != status)
			row.setStatus(status);
		if (null != modifier && !modifier.isEmpty())
			row.setModifier(modifier);

		return updateAll(tablename, null, null, null, null, new_update_address_id, row);
	}

}
