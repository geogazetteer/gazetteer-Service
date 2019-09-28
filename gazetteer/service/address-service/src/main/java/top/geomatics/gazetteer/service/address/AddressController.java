package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.BuildingPositionRow;

/**
 * <b>地名地址定位服务类</b><br>
 * 
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

	private static final String tablename = Messages.getString("AddressController.0"); //$NON-NLS-1$
	private static final String fields = Messages.getString("AddressController.1"); //$NON-NLS-1$

	private static Map<String, Object> map = new HashMap<String, Object>();
	static {
		map.put(Messages.getString("AddressController.2"), tablename); //$NON-NLS-1$
		map.put(Messages.getString("AddressController.3"), fields); //$NON-NLS-1$
	}

	/**
	 * <b>根据地址编码查询坐标</b><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/location/code/4403060090031200105
	 * 
	 * @param code String 路径变量，指定查询的地址编码
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址编码查询坐标", notes = "根据地址编码查询，获取对应地址编码的所有地址信息。示例：/location/code/4403060090031200105")
	@GetMapping("/code/{code}")
	public String selectByCode(
			@ApiParam(value = "查询的地址编码，如4403060090031200105") @PathVariable(value = IControllerConstant.ADDRESS_CODE, required = true) String code) {

		code = ControllerUtils.coding(code);

		map.put(Messages.getString("AddressController.4"), code); //$NON-NLS-1$
		List<BuildingPositionRow> rows = mapper.findBuildingEquals(map);
		return ControllerUtils.getResponseBody5(rows);
	}

	/**
	 * <b>根据坐标查询地址编码</b><br>
	 * 
	 * examples:<br>
	 * <p>
	 * http://localhost:8083/location/point?x=114.017776720804%26y=22.6390350934369
	 * </p>
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
		map.put(Messages.getString("AddressController.5"), x); //$NON-NLS-1$
		map.put(Messages.getString("AddressController.6"), y); //$NON-NLS-1$
		List<BuildingPositionRow> rows = mapper.findBuildingByPoint(map);
		return ControllerUtils.getResponseBody5(rows);
	}

	/**
	 * <b>根据地址编码查询社区和街道</b><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/location/grid/point?x=114.017776720804%26y=22.6390350934369
	 * 
	 * @param x Double 请求参数，指定查询的x坐标
	 * @param y Double 请求参数，指定查询的y坐标
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询社区和街道", notes = "根据坐标查询，获取对应的社区和街道信息。示例：/location/grid/point?x=114.017776720804%26y=22.6390350934369")
	@GetMapping("/grid/point")
	public String getGridByPoint(
			@ApiParam(value = "指定查询的x坐标，如114.017776720804") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定查询的y坐标，如22.6390350934369") @RequestParam(value = "y", required = true) Double y) {

		/*
		map.put(Messages.getString("AddressController.5"), x); //$NON-NLS-1$
		map.put(Messages.getString("AddressController.6"), y); //$NON-NLS-1$
		List<BuildingPositionRow> rows = mapper.findBuildingByPoint(map);

		List<AddressRow> addressRows = new ArrayList<AddressRow>();
		for (BuildingPositionRow row : rows) {
			String code = row.getCode();
			AddressRow arow = ControllerUtils.getAddressRowByCode(code);
			addressRows.add(arow);
		}
		*/
		String keywords = x.toString() + "," + y.toString();
		List<String> codes = CoordinateQuery.getCodesByCoords(keywords);
		//一个坐标只属于一个社区
		AddressRow arow = null;
		if (codes.size() > 0) {
			arow = ControllerUtils.getAddressRowByCode(codes.get(0));
		}
		
	
		return JSON.toJSONString(arow);
	}

	/**
	 * <b>根据坐标查询社区和街道</b><br>
	 * 
	 * examples:<br>
	 * http://localhost:8083/location/grid/code?code=4403060090031200105
	 * 
	 * @param code String 请求参数，指定查询的地址编码
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "根据地址编码查询社区和街道", notes = "根据地址编码查询，获取对应地址编码的社区和街道信息。示例：/location/grid/code?code=4403060090031200105")
	@GetMapping("/grid/code")
	public String getGridByCode(
			@ApiParam(value = "查询的地址编码，如4403060090031200105") @RequestParam(value = IControllerConstant.ADDRESS_CODE, required = true) String code) {

		AddressRow row = ControllerUtils.getAddressRowByCode(code);

		return JSON.toJSONString(row);
	}
}
