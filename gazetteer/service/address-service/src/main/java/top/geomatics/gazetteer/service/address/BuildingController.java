/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.utilities.database.BuildingQuery;

/**
 * @author whudyj
 */
@Api(value = "/building", tags = "建筑物地址查询")
@RestController
@RequestMapping("/building")
public class BuildingController {
	BuildingQuery bquery = new BuildingQuery();

	/**
	 * example: http://localhost:8083/building/codes?x=114.043736&y=22.649473
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	@ApiOperation(value = "根据坐标查询建筑物编码", notes = "根据坐标查询建筑物编码\r\n 示例：/building/codes?x=114.043736&y=22.649473 ")
	@GetMapping("/codes")
	public String queryCodes(
			@ApiParam(value = "指定x坐标，如x=114.043736") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.649473") @RequestParam(value = "y", required = true) Double y) {
		return JSON.toJSONString(bquery.query(x, y));
	}

	/**
	 * example: http://localhost:8083/building/point?code=4403060070051200001
	 * 
	 * @param code
	 * @return
	 */
	@ApiOperation(value = "根据建筑物编码查询坐标", notes = "根据建筑物编码查询坐标\r\n 示例：/building/point?code=4403060070051200001")
	@GetMapping("/point")
	public String queryPoint(
			@ApiParam(value = "指定筑物编码，如4403060070051200001") @RequestParam(value = "code", required = true) String code) {
		return JSON.toJSONString(bquery.query(code));

	}
}
