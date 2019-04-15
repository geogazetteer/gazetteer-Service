package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//数据导入导出
@Api(value = "/data", tags = "地址数据上传下载")
@RestController
@RequestMapping("/data")
public class DataController {
	@ApiOperation(value = "下载地址数据", notes = "下载地址数据，功能暂未实现")
	@GetMapping("/download")
	public String output() {
		return "数据下载成功";
	}

	@ApiOperation(value = "上传地址数据", notes = "上传地址数据，功能暂未实现")
	@GetMapping("/upload")
	public String input() {

		return "数据上传成功";
	}

}
