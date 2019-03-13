package top.geomatics.gazetteer.service.address;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.geomatics.gazetteer.service.utils.ExcelUtil;

@RestController
@RequestMapping("/data")
public class DataController {
	// 将数据导出到excel表
	@RequestMapping("/output")
	public String output() {
		ExcelUtil excelUtil = new ExcelUtil();
		try {
			excelUtil.output();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "数据导出成功";
	}

	// 导入数据
	@RequestMapping("/input")
	public String input() {

		return "数据导入成功";
	}

}
