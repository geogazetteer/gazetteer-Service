/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;
import top.geomatics.gazetteer.utilities.database.building.BuildingQueryExt;

/**
 * <b>建筑物查询服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/building", tags = "建筑物地址查询")
@RestController
@RequestMapping("/building")
public class BuildingController {

	private static BuildingQueryExt buildingQuery = new BuildingQueryExt();
	static {
		buildingQuery.open();
	}

	/**
	 * <b>根据坐标查询建筑物编码</b><br>
	 * 
	 * example: http://localhost:8083/building/codes?x=114.019777%26y=22.672456
	 * 
	 * @param x 指定x坐标，如x=114.019777
	 * @param y 指定y坐标，如y=22.672456
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询建筑物编码", notes = "根据坐标查询建筑物编码\r\n 示例：/building/codes?x=114.019777&y=22.672456")
	@GetMapping("/codes")
	public String queryCodes(
			@ApiParam(value = "指定x坐标，如x=114.019777") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.672456") @RequestParam(value = "y", required = true) Double y) {
		return JSON.toJSONString(buildingQuery.query(x, y));
	}

	/**
	 * <b>根据建筑物编码查询坐标</b><br>
	 * 
	 * example: http://localhost:8083/building/point?code=4403060070051200001
	 * 
	 * @param code 指定筑物编码，如4403060070051200001
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据建筑物编码查询坐标", notes = "根据建筑物编码查询坐标\r\n 示例：/building/point?code=4403060070051200001")
	@GetMapping("/point")
	public String queryPoint(
			@ApiParam(value = "指定筑物编码，如4403060070051200001") @RequestParam(value = "code", required = true) String code) {
		return JSON.toJSONString(buildingQuery.query(code));

	}

	/**
	 * <b>根据多个建筑物编码查询坐标</b><br>
	 * 
	 * example:
	 * http://localhost:8083/building/points?codes=4403060090030100104,44030600800448T0041,4403060100024900021,
	 * 4403060100037000131,4403060080030400113,4403060080040900084,44030600800109T0143,4403060080012100049,4403060100020500120
	 * 
	 * @param codes 指定筑物编码，如4403060070051200001
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据多个建筑物编码查询坐标", notes = "根据多个建筑物编码查询坐标\r\n 示例：/building/points")
	@GetMapping("/points")
	public String queryPoints(
			@ApiParam(value = "指定筑物编码，如4403060090030100104,44030600800448T0041,4403060100024900021,\r\n"
					+ "4403060100037000131,4403060080030400113,4403060080040900084,44030600800109T0143,4403060080012100049,4403060100020500120") @RequestParam(value = "code", required = true) String codes) {
		List<String> codes_t = new ArrayList<String>();
		String[] cs = codes.split(",");
		for (String s : cs) {
			codes_t.add(s);
		}
		return JSON.toJSONString(buildingQuery.query(codes_t));

	}

	/**
	 * <b>根据坐标查询标准地址</b><br>
	 * 
	 * example: http://localhost:8083/building/address?x=114.019777%26y=22.672456
	 * 
	 * @param x 指定x坐标，如x=114.019777
	 * @param y 指定y坐标，如y=22.672456
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询标准地址", notes = "根据坐标查询标准地址\r\n 示例：/building/address?x=114.019777&y=22.672456")
	@GetMapping("/address")
	public String queryAddress(
			@ApiParam(value = "指定x坐标，如x=114.019777") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.672456") @RequestParam(value = "y", required = true) Double y) {
		List<String> codes = buildingQuery.query(x, y);
		List<AddressRow> rowsTotal = new ArrayList<>();
		for (String code : codes) {
			// 根据建筑物编码搜索
			String fields = "*";
			String tablename = AddressProcessor.getCommunityFromBuildingCode(code);
			AddressRow aRow = new AddressRow();
			aRow.setCode(code);
			Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, aRow, null, 0);
			List<AddressRow> rows = ControllerUtils.mapper.findEquals(map);
			rowsTotal.addAll(rows);
		}
		return ControllerUtils.getResponseBody(rowsTotal);
	}
}
