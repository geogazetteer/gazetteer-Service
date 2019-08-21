/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.HashMap;
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
import top.geomatics.gazetteer.model.BuildingPositionRow;
import top.geomatics.gazetteer.model.GeoPoint;

/**
 * <b>建筑物查询服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/building", tags = "建筑物地址查询")
@RestController
@RequestMapping("/building")
public class BuildingController {
	private static final String TABLENAME = "building_position";

//	private static BuildingQueryExt buildingQuery = new BuildingQueryExt();
//	static {
//		buildingQuery.open();
//	}

	/**
	 * <b>根据坐标查询建筑物编码</b><br>
	 * 
	 * example: http://localhost:8083/building/codes?x=114.017776%26y=22.639035
	 * 
	 * @param x 指定x坐标，如x=114.017776
	 * @param y 指定y坐标，如y=22.639035
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询建筑物编码", notes = "根据坐标查询建筑物编码\r\n 示例：/building/codes?x=114.017776&y=22.639035")
	@GetMapping("/codes")
	public String queryCodes(
			@ApiParam(value = "指定x坐标，如x=114.017776") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.639035") @RequestParam(value = "y", required = true) Double y) {
		String fields = "code";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", TABLENAME);

		map.put("longitude", x);
		map.put("latitude", y);

		List<BuildingPositionRow> rows = ControllerUtils.mapper.findBuildingEquals(map);

		List<String> czwcodes = new ArrayList<>();
		for (BuildingPositionRow row : rows) {
			czwcodes.add(row.getCode());
		}

		return JSON.toJSONString(czwcodes);
	}

	/**
	 * <b>根据建筑物编码查询坐标</b><br>
	 * 
	 * example: http://localhost:8083/building/point?code=4403060090031200105
	 * 
	 * @param code 指定筑物编码，如4403060090031200105
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据建筑物编码查询坐标", notes = "根据建筑物编码查询坐标\r\n 示例：/building/point?code=4403060090031200105")
	@GetMapping("/point")
	public String queryPoint(
			@ApiParam(value = "指定筑物编码，如4403060090031200105") @RequestParam(value = "code", required = true) String code) {

		String fields = "longitude,latitude";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", TABLENAME);

		// 440306 009003 1200105
		// 440306 007003 3600024 000002
		// 只取前面19位
		if (code.length() > 19) {
			code = code.substring(0, 19);
		}

		map.put("code", code);

		List<BuildingPositionRow> rows = ControllerUtils.mapper.findBuildingEquals(map);

		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		for (BuildingPositionRow row : rows) {
			GeoPoint point = new GeoPoint();
			point.setX(row.getLongitude());
			point.setY(row.getLatitude());
			geoPoints.add(point);
		}

		return JSON.toJSONString(geoPoints);

	}

	/**
	 * <b>根据多个建筑物编码查询坐标</b><br>
	 * 
	 * example:
	 * http://localhost:8083/building/points?codes=4403060090030100104,44030600800448T0041,4403060100024900021
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
		String fields = "longitude,latitude";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", TABLENAME);

		map.put("list", codes_t);

		List<BuildingPositionRow> rows = ControllerUtils.mapper.selectBuildingByCodes(map);

		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		for (BuildingPositionRow row : rows) {
			GeoPoint point = new GeoPoint();
			point.setX(row.getLongitude());
			point.setY(row.getLatitude());
			geoPoints.add(point);
		}

		return JSON.toJSONString(geoPoints);

	}

	/**
	 * <b>根据坐标查询标准地址</b><br>
	 * 
	 * example: http://localhost:8083/building/address?x=114.017776%26y=22.639035
	 * 
	 * @param x 指定x坐标，如x=114.017776
	 * @param y 指定y坐标，如y=22.639035
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询标准地址", notes = "根据坐标查询标准地址\r\n 示例：/building/address?x=114.017776&y=22.639035")
	@GetMapping("/address")
	public String queryAddress(
			@ApiParam(value = "指定x坐标，如x=114.017776") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.639035") @RequestParam(value = "y", required = true) Double y) {

		String fields = "code";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", TABLENAME);

		map.put("longitude", x);
		map.put("latitude", y);

		List<BuildingPositionRow> rows = ControllerUtils.mapper.findBuildingEquals(map);

		List<AddressRow> rowsTotal = new ArrayList<>();
		for (BuildingPositionRow row : rows) {
			String code = row.getCode();
			// 根据建筑物编码搜索
			String fields_t = "*";
			// String tablename = AddressProcessor.getCommunityFromBuildingCode(code);
			String tablename = IControllerConstant.ADDRESS_TABLE;
			AddressRow aRow = new AddressRow();
			aRow.setCode(code);
			Map<String, Object> map_t = ControllerUtils.getRequestMap(fields_t, tablename, aRow, null, 0);
			List<AddressRow> rows_t = ControllerUtils.mapper.findEquals(map_t);
			rowsTotal.addAll(rows_t);
		}
		return ControllerUtils.getResponseBody(rowsTotal);
	}
}
