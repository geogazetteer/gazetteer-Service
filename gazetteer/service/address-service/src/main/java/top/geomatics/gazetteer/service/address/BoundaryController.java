/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.GeoPoint;
import top.geomatics.gazetteer.model.boundary.Grid;
import top.geomatics.gazetteer.model.boundary.JD;
import top.geomatics.gazetteer.model.boundary.SQ;
import top.geomatics.gazetteer.utilities.database.boundary.BoundaryQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>街道和社区查询服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/boundary", tags = "街道和社区查询")
@RestController
@RequestMapping("/boundary")
public class BoundaryController {

	/**
	 * <b>根据坐标查询街道</b><br>
	 * 
	 * example: http://localhost:8083/boundary/jd?x=114.017776%26y=22.639035
	 * 
	 * @param x 指定x坐标，如x=114.017776
	 * @param y 指定y坐标，如y=22.639035
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询街道", notes = "根据坐标查询街道\r\n 示例：/boundary/jd?x=114.017776&y=22.639035")
	@GetMapping("/jd")
	public String queryJD(
			@ApiParam(value = "指定x坐标，如x=114.017776") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.639035") @RequestParam(value = "y", required = true) Double y) {

		List<JD> codes =BoundaryQuery.queryJD(x,y);

		return JSON.toJSONString(codes);
	}

	/**
	 * <b>根据坐标查询社区</b><br>
	 *
	 * example: http://localhost:8083/boundary/sq?x=114.017776%26y=22.639035
	 *
	 * @param x 指定x坐标，如x=114.017776
	 * @param y 指定y坐标，如y=22.639035
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询社区", notes = "根据坐标查询社区\r\n 示例：/boundary/sq?x=114.017776&y=22.639035")
	@GetMapping("/sq")
	public String querySQ(
			@ApiParam(value = "指定x坐标，如x=114.017776") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.639035") @RequestParam(value = "y", required = true) Double y) {

		List<SQ> codes =BoundaryQuery.querySQ(x,y);

		return JSON.toJSONString(codes);
	}

	/**
	 * <b>根据坐标查询网格</b><br>
	 *
	 * example: http://localhost:8083/boundary/grid?x=114.017776%26y=22.639035
	 *
	 * @param x 指定x坐标，如x=114.017776
	 * @param y 指定y坐标，如y=22.639035
	 * @return JSON格式的查询结果
	 */
	@ApiOperation(value = "根据坐标查询网格", notes = "根据坐标查询网格\r\n 示例：/boundary/grid?x=114.017776&y=22.639035")
	@GetMapping("/grid")
	public String queryGrid(
			@ApiParam(value = "指定x坐标，如x=114.017776") @RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=22.639035") @RequestParam(value = "y", required = true) Double y) {

		List<Grid> codes =BoundaryQuery.queryGrid(x,y);

		return JSON.toJSONString(codes);
	}


}
