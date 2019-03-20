package top.geomatics.gazetteer.service.address;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiOperation;
import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.EnterpriseRow;

//编辑服务
@RestController
@RequestMapping("/editor")
public class EditorController {
	private static EnterpriseDatabaseHelper helper = new EnterpriseDatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static EnterpriseAddressMapper mapper = session.getMapper(EnterpriseAddressMapper.class);

	private static Map<String, Object> getRequestMap(String fields, String tablename, EnterpriseRow row, String orderby,
			int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", tablename);
		if (null != row) {
			// 11个字段
			String code = row.getCode();
			String name = row.getName();
			String street = row.getStreet();
			String owner = row.getOwner();
			String address = row.getAddress();
			// geometry字段;
			Integer iStatus = row.getStatus();
			String modifier = row.getModifier();
			Date date = row.getUpdate_date();
			String update_address = row.getUpdate_address();
			String update_address_id = row.getUpdate_address_id();

			if (code != null && !code.isEmpty())
				map.put("code", code);
			if (name != null && !name.isEmpty())
				map.put("name", name);
			if (street != null && !street.isEmpty())
				map.put("street", street);
			if (owner != null && !owner.isEmpty())
				map.put("owner", owner);
			if (address != null && !address.isEmpty())
				map.put("address", address);

			if (iStatus != null) {
				String status = iStatus.toString();
				if (!status.isEmpty()) {
					map.put("status", status);
				}
			}
			if (modifier != null && !modifier.isEmpty())
				map.put("modifier", modifier);
			if (date != null) {
				String update_date = date.toString();
				if (!update_date.isEmpty()) {
					map.put("update_date", update_date);
				}
			}
			if (update_address != null && !update_address.isEmpty())
				map.put("update_address", update_address);
			if (update_address_id != null && !update_address_id.isEmpty())
				map.put("update_address_id", update_address_id);
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
	@ApiOperation(value = "列出所有需要编辑的地址", notes = "列出所有需要编辑的地址")
	@GetMapping("/all")
	public String editAll(@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit, EnterpriseRow row) {
		Map<String, Object> map = getRequestMap(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = mapper.findEquals(map);
		// 使用阿里巴巴的fastjson
		return JSON.toJSONString(rows);
	}

	/**
	 * url:/query?fields={field}&tablename={tablename} \
	 * &address={address}&code={code}... \ &orderby={orderby}&limit={limit}
	 * examples:
	 * /query/?fields=fid,address&tablename=enterprise1&address=深圳市龙华区龙华街道东环一路天汇大厦B座906室
	 * 
	 * @return
	 */
	@ApiOperation(value = "根据条件查询", notes = "根据条件查询")
	@GetMapping("/query")
	public String editWithConditions(
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") String status,
			@RequestParam(value = "modifier", required = false, defaultValue = "") String modifier,
			@RequestParam(value = "update_date", required = false, defaultValue = "") String update_date,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow row = new EnterpriseRow();
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		if (null != status && !status.isEmpty()) {
			int iStatus = Integer.parseInt(status);
			row.setStatus(iStatus);
		}
		row.setModifier(modifier);

		Map<String, Object> map = getRequestMap(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = mapper.findEquals(map);
		return JSON.toJSONString(rows);
	}

	/**
	 * url:/fuzzyquery?fields={field}&tablename={tablename} \
	 * &address={address}&code={code}... \ &orderby={orderby}&limit={limit}
	 * examples:
	 * /fuzzyquery?fields=id,address&tablename=enterprise1&address=%天汇大厦B座906室%
	 * 
	 * @return
	 */
	@ApiOperation(value = "根据条件模糊查询", notes = "根据条件模糊查询")
	@GetMapping("/fuzzyquery")
	public String fuzzyEditWithConditions(
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") String status,
			@RequestParam(value = "modifier", required = false, defaultValue = "") String modifier,
			@RequestParam(value = "update_date", required = false, defaultValue = "") String update_date,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow row = new EnterpriseRow();
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		if (null != status && !status.isEmpty()) {
			int iStatus = Integer.parseInt(status);
			row.setStatus(iStatus);
		}
		row.setModifier(modifier);

		Map<String, Object> map = getRequestMap(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = mapper.findLike(map);
		return JSON.toJSONString(rows);
	}

	/**
	 * examples:http://localhost:8080/editor/code/91440300MA5DK2PU7P
	 * 
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "根据统一社会信用代码查询", notes = "获取对应统一社会信用代码的数据")
	@GetMapping("/code/{code}")
	public String editByCode(@PathVariable(value = "code", required = true) String code,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setCode(code);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/editor/name/深圳市明慧汽配有限公司
	 * 
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "根据企业名称查询", notes = "获取对应企业名称的数据")
	@GetMapping("/name/{name}")
	public String editByName(@PathVariable(value = "name", required = true) String name,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setName(name);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/editor/street/龙华
	 * 
	 * @param street
	 * @return
	 */
	@ApiOperation(value = "根据街道查询", notes = "获取对应街道的数据")
	@GetMapping("/street/{street}")
	public String editByStreet(@PathVariable(value = "street", required = true) String street,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setStreet(street);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/editor/owner/方海英
	 * 
	 * @param owner
	 * @return
	 */
	@ApiOperation(value = "根据法定代表人查询", notes = "获取对应法定代表人的数据")
	@GetMapping("/owner/{owner}")
	public String editByOwner(@PathVariable(value = "owner", required = true) String owner,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setOwner(owner);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/editor/address/深圳市龙华区龙华街道东环一路天汇大厦B座906室
	 * 
	 * @param address
	 * @return
	 */
	@ApiOperation(value = "根据JYCS查询", notes = "获取对应JYCS的数据")
	@GetMapping("/address/{address}")
	public String editByAddress(@PathVariable(value = "address", required = true) String address,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setAddress(address);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/editor/status/0
	 * 
	 * @param status
	 * @return
	 */
	@ApiOperation(value = "根据状态查询", notes = "获取对应状态的数据")
	@GetMapping("/status/{status}")
	public String editByStatus(@PathVariable(value = "status", required = true) int status,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setStatus(status);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/editor/modifier/Admin
	 * 
	 * @param modifier
	 * @return
	 */
	@ApiOperation(value = "根据编辑者查询", notes = "获取对应编辑者的数据")
	@GetMapping("/modifier/{modifier}")
	public String editByModifier(@PathVariable(value = "modifier", required = true) String modifier,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow aRow = new EnterpriseRow();
		aRow.setModifier(modifier);
		return editAll(fields, tablename, orderby, limit, aRow);
	}

	/**
	 * examples:http://localhost:8080/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * 
	 * @param status
	 * @return
	 */
	@ApiOperation(value = "更新状态", notes = "更新对应状态的数据")
	@GetMapping("/status/{status}")
	public void updateStatus(@PathVariable(value = "status", required = true) int status,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "modifier", required = false, defaultValue = "") String modifier,
			@RequestParam(value = "update_date", required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		row.setStatus(status);
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);

		row.setModifier(modifier);

		Map<String, Object> map = getRequestMap(null, tablename, row, null, 0);
		mapper.updateStatus(map);
		return;
	}

	/**
	 * examples:http://localhost:8080/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * 
	 * @param modifier
	 * @return
	 */
	@ApiOperation(value = "更新编辑者", notes = "更新对应编辑者的数据")
	@GetMapping("/modifier/{modifier}")
	public void updateModifier(@PathVariable(value = "modifier", required = true) String modifier,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") int status,
			@RequestParam(value = "update_date", required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		row.setModifier(modifier);
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		row.setStatus(status);

		Map<String, Object> map = getRequestMap(null, tablename, row, null, 0);
		mapper.updateStatus(map);
		return;
	}

	/**
	 * examples:http://localhost:8080/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * 
	 * @param update_date
	 * @return
	 */
	@ApiOperation(value = "更新编辑日期", notes = "更新对应编辑日期的数据")
	@GetMapping("/update_date/{update_date}")
	public void updateDate(@PathVariable(value = "update_date", required = true) String update_date,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") int status,
			@RequestParam(value = "modifier", required = false, defaultValue = "") String modifier) {
		EnterpriseRow row = new EnterpriseRow();
		row.setUpdate_date(new Date());// 暂时用系统日期
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		row.setStatus(status);
		row.setModifier(modifier);

		Map<String, Object> map = getRequestMap(null, tablename, row, null, 0);
		mapper.updateDate(map);
		return;
	}

	/**
	 * examples:http://localhost:8080/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * 
	 * @param update_address
	 * @return
	 */
	@ApiOperation(value = "更新地址", notes = "更新对应地址的数据")
	@GetMapping("/update_address/{update_address}")
	public void updateAddress(@PathVariable(value = "update_address", required = true) String update_address,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") int status,
			@RequestParam(value = "modifier", required = false, defaultValue = "") String modifier) {
		EnterpriseRow row = new EnterpriseRow();
		row.setUpdate_address(update_address);
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		row.setStatus(status);
		row.setModifier(modifier);

		Map<String, Object> map = getRequestMap(null, tablename, row, null, 0);
		mapper.updateAddress(map);
		return;
	}

	/**
	 * examples:http://localhost:8080/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * 
	 * @param update_address_id
	 * @return
	 */
	@ApiOperation(value = "更新地址编码", notes = "更新对应地址编码的数据")
	@GetMapping("/update_address_id/{update_address_id}")
	public void updateAddressId(@PathVariable(value = "update_address_id", required = true) String update_address_id,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") int status,
			@RequestParam(value = "modifier", required = false, defaultValue = "") String modifier) {
		EnterpriseRow row = new EnterpriseRow();
		row.setUpdate_address_id(update_address_id);
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		row.setStatus(status);
		row.setModifier(modifier);

		Map<String, Object> map = getRequestMap(null, tablename, row, null, 0);
		mapper.updateAddress(map);
		return;
	}

}
