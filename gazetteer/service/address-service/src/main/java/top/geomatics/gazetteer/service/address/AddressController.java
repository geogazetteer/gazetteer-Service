package top.geomatics.gazetteer.service.address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.BuildingPositionRow;

/**
 * <em>地名地址定位服务</em><br>
 * <i>说明</i><br>
 * <i>目前只针对标准地名地址数据库中的地名地址进行定位</i>
 * 
 * @author whudyj
 */
@Api(value = "/location", tags = "地名地址定位服务")
@RestController
@RequestMapping("/location")
public class AddressController {
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);

	private static final String tablename = "building_position";
	private static final String fields = "*";

	private static Map<String, Object> map = new HashMap<String, Object>();
	static {
		map.put("sql_tablename", tablename);
		map.put("sql_fields", fields);
	}

	/**
	 * <em>根据地址编码查询坐标</em><br>
	 * examples:<br>
	 * http://localhost:8083/location/code/4403060090031200105
	 * 
	 * @param code String 路径变量，指定查询的地址编码
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址编码查询", notes = "根据地址编码查询，获取对应地址编码的所有地址信息。示例：/location/code/4403060090031200105")
	@GetMapping("/code/{code}")
	public String selectByCode(
			@ApiParam(value = "查询的地址编码，如4403060090031200105") @PathVariable(value = IControllerConstant.ADDRESS_CODE, required = true) String code) {
		map.put("code", code);
		List<BuildingPositionRow> rows = mapper.findBuildingEquals(map);
		return ControllerUtils.getResponseBody5(rows);
	}

	/**
	 * <em>根据坐标查询地址编码</em><br>
	 * examples:<br>
	 * http://localhost:8083/location/point?x=114.017776720804&y=22.6390350934369
	 * 
	 * @param x Double 请求参数，指定查询的x坐标
	 * @param y Double 请求参数，指定查询的y坐标
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询地址编码", notes = "根据坐标查询地址编码，获取对应坐标的所有地址信息。示例：/location/point?x=114.017776720804&y=22.6390350934369")
	@GetMapping("/point")
	public String selectByPoint(
			@ApiParam(value = "指定查询的x坐标，如114.017776720804") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定查询的y坐标，如22.6390350934369") @RequestParam(value = "y", required = true) Double y) {
		map.put("longitude", x);
		map.put("latitude", y);
		List<BuildingPositionRow> rows = mapper.findBuildingByPoint(map);
		return ControllerUtils.getResponseBody5(rows);
	}
}
