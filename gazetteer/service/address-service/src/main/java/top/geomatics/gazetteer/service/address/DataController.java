package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

//数据导入导出
@RestController
@RequestMapping("/data")
public class DataController {
	@ApiOperation(value = "将数据导出到excel表", notes = "将数据导出到excel表")
	@RequestMapping("/output")
	public String output() {
		return "数据导出成功";
	}

	@ApiOperation(value = "导出数据", notes = "导出数据")
	@RequestMapping("/input")
	public String input() {

		return "数据导入成功";
	}

}
