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
	 * http://localhost:8083/location/code/44030600960102T0117
	 * 
	 * @param code      String 路径变量，指定查询的地址编码
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,code,name,address
	 * @param tablename String 请求参数，指定查询的数据库表
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址编码查询", notes = "根据地址编码查询，获取对应地址编码的所有地址信息。示例：/location/code/44030600960102T0117")
	@GetMapping("/code/{code}")
	public String selectByCode(
			@ApiParam(value = "查询的地址编码，如44030600960102T0117") @PathVariable(value = IControllerConstant.ADDRESS_CODE, required = true) String code) {
		map.put("code", code);
		List<BuildingPositionRow> rows = mapper.findBuildingEquals(map);
		return ControllerUtils.getResponseBody5(rows);
	}
}
