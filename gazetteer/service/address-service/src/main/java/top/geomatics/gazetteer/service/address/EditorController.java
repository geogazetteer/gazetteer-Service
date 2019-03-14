package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

//编辑服务
@RestController
@RequestMapping("/editor")
public class EditorController {

	@ApiOperation(value = "所有需要编辑的地址", notes = "所有需要编辑的地址")
	@GetMapping(value = "/list")
	public String list() {

		return "";
	}

	@ApiOperation(value = "进行编辑", notes = "进行编辑")
	@GetMapping(value = "/edit")
	public String edit(@PathVariable String code) {

		return "";
	}

}
