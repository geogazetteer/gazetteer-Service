package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

//数据导入导出
@RestController
@RequestMapping("/data")
public class DataController {
	@ApiOperation(value = "导出数据", notes = "导出数据")
	@GetMapping("/output")
	public String output() {
		return "数据导出成功";
	}

	@ApiOperation(value = "导入数据", notes = "导入数据")
	@GetMapping("/input")
	public String input() {

		return "数据导入成功";
	}

}
