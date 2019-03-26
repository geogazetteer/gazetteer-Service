package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import top.geomatics.gazetteer.model.EnterpriseRow;

//编辑服务
@RestController
@RequestMapping("/editor")
public class EditorController {

	/**
	 * examples:http://localhost:8083/editor/all?fields=code,name,address&tablename=enterprise1&limit=10
	 */
	@ApiOperation(value = "列出所有需要编辑的地址", notes = "列出所有需要编辑的地址")
	@GetMapping("/all")
	public String editAll(@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit, EnterpriseRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findEquals(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * examples:http://localhost:8083/editor/page/1?fields=code,name,address&tablename=enterprise1&limit=10
	 */
	@ApiOperation(value = "列出所有需要编辑的地址", notes = "列出所有需要编辑的地址")
	@GetMapping("/page/{index}")
	public String editPage(@PathVariable(value = "index", required = true) Integer index,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit, EnterpriseRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		Integer page_start = (index - 1) * limit;
		map.put("page_start", page_start);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findPageEquals(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * examples:http://localhost:8083/editor/fid/1?fields=*&tablename=民治街道
	 * @param fid
	 * @param fields
	 * @param tablename
	 * @return
	 */
	@ApiOperation(value = "根据fid查询详细信息", notes = "根据fid查询详细信息")
	@GetMapping("/fid/{fid}")
	public String selectByFid(@PathVariable(value = "fid", required = true) Integer fid,
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename) {
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, null, null, 0);
		map.put("fid", fid);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.selectByFid(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * examples:http://localhost:8083/editor/fids?in=1,2,3&field=*&tablename=民治街道
	 * @param fields
	 * @param tablename
	 * @param fids
	 * @return
	 */
	@ApiOperation(value = "根据一组fid查询详细信息", notes = "根据一组fid查询详细信息")
	@GetMapping("/fids")
	public String selectByFids(@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
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
	 * examples:http://localhost:8083/editor/query?fields=code,name,address&tablename=enterprise1&address=深圳市龙华区龙华街道东环一路天汇大厦B座906室
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

		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findEquals(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * examples:http://localhost:8083/editor/fuzzyquery?fields=code,name&tablename=enterprise1&address=%25天汇大厦B座906室%25&limit=10
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

		Map<String, Object> map = ControllerUtils.getRequestMap2(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = ControllerUtils.mapper2.findLike(map);
		return ControllerUtils.getResponseBody2(rows);
	}

	/**
	 * examples:http://localhost:8083/editor/code/91440300MA5DK2PU7P
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
	 * examples:http://localhost:8083/editor/name/深圳市明慧汽配有限公司
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
	 * examples:http://localhost:8083/editor/street/龙华?limit=10
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
	 * examples:http://localhost:8083/editor/owner/方海英
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
	 * examples:http://localhost:8083/editor/address/深圳市龙华区龙华街道东环一路天汇大厦B座906室
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
	 * examples:http://localhost:8083/editor/status/0
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
	 * examples:http://localhost:8083/editor/modifier/Admin
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
	 * examples:http://localhost:8083/editor/update/status/0?code=91440300MA5DK2PU7P
	 * @param status
	 * @return
	 */
	@ApiOperation(value = "更新状态", notes = "更新对应状态的数据")
	@GetMapping("/update/status/{status}")
	public String updateStatus(@PathVariable(value = "status", required = true) Integer status,
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

		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, row, null, 0);
		Integer updatedRows = ControllerUtils.mapper2.updateStatus(map);
		ControllerUtils.session2.commit(true);
		return "Ok,updated rows:" + updatedRows;
	}

	/**
	 * examples:http://localhost:8083/editor/update/modifier/Admin?code=91440300MA5DK2PU7P
	 * @param modifier
	 * @return
	 */
	@ApiOperation(value = "更新编辑者", notes = "更新对应编辑者的数据")
	@GetMapping("/update/modifier/{modifier}")
	public String updateModifier(@PathVariable(value = "modifier", required = true) String modifier,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") Integer status,
			@RequestParam(value = "update_date", required = false, defaultValue = "") String update_date) {
		EnterpriseRow row = new EnterpriseRow();
		row.setModifier(modifier);
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		row.setStatus(status);

		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, row, null, 0);
		Integer updatedRows = ControllerUtils.mapper2.updateModifier(map);
		ControllerUtils.session2.commit(true);
		return "Ok,updated rows:" + updatedRows;
	}

	/**
	 * examples:http://localhost:8083/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * @param update_date
	 * @return
	 */
	@ApiOperation(value = "更新编辑日期", notes = "更新对应编辑日期的数据")
	@GetMapping("/update/update_date/{update_date}")
	public String updateDate(@PathVariable(value = "update_date", required = true) String update_date,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") Integer status,
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

		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, row, null, 0);
		Integer updatedRows = ControllerUtils.mapper2.updateDate(map);
		ControllerUtils.session2.commit(true);
		return "Ok,updated rows:" + updatedRows;
	}

	/**
	 * examples:http://localhost:8083/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * @param update_address
	 * @return
	 */
	@ApiOperation(value = "更新地址", notes = "更新对应地址的数据")
	@GetMapping("/update/update_address/{update_address}")
	public String updateAddress(@PathVariable(value = "update_address", required = true) String update_address,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") Integer status,
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

		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, row, null, 0);
		Integer updatedRows = ControllerUtils.mapper2.updateAddress(map);
		ControllerUtils.session2.commit(true);
		return "Ok,updated rows:" + updatedRows;
	}

	/**
	 * examples:http://localhost:8083/editor/update/status/0?code=91440300MA5DK2PU7P&
	 * @param update_address_id
	 * @return
	 */
	@ApiOperation(value = "更新地址编码", notes = "更新对应地址编码的数据")
	@GetMapping("/update/update_address_id/{update_address_id}")
	public String updateAddressId(@PathVariable(value = "update_address_id", required = true) String update_address_id,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") Integer status,
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

		Map<String, Object> map = ControllerUtils.getRequestMap2(null, tablename, row, null, 0);
		Integer updatedRows = ControllerUtils.mapper2.updateAddress(map);
		ControllerUtils.session2.commit(true);
		return "Ok,updated rows:" + updatedRows;
	}

}
