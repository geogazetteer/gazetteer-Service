/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.BuildingRow;
import top.geomatics.gazetteer.utilities.database.excel2gpkg.GeopackageReader;

/**
 * @author whudyj
 */
@Api(value="/building",tags="建筑物地址查询")
@RestController
@RequestMapping("/building")
public class BuildingController {
	String addressFileName = "D:\\data\\LH_gazetteer.gpkg";
	GeopackageReader reader2 = new GeopackageReader(addressFileName);
	// reader2.close();
	/**
	 * example: http://localhost:8083/building/query?x=503361.375&y=2506786.75
	 * @param tablename
	 * @param x
	 * @param y
	 * @return
	 */
	@ApiOperation(value = "根据坐标查询建筑物", notes = "根据坐标查询建筑物信息\r\n 示例：/building/query?x=503361.375&y=2506786.75 ")
	@GetMapping("/query")
	public String query(
			@ApiParam(value = "查询的数据库表，如龙华区楼栋")
			@RequestParam(value = "tablename", required = false, defaultValue = "龙华区楼栋") String tablename,
			@ApiParam(value = "指定x坐标，如x=503361.375")
			@RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=2506786.75")
			@RequestParam(value = "y", required = true) Double y) {
		reader2.preQuery("龙华区楼栋");
		List<BuildingRow> rows = reader2.query2(x, y);
		// 使用阿里巴巴的fastjson
		return JSON.toJSONString(rows);
	}

}
