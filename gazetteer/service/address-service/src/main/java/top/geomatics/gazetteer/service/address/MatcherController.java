package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

//匹配服务
@RestController
@RequestMapping("/match")
public class MatcherController {

	@ApiOperation(value = "地址匹配", notes = "地址匹配")
	@GetMapping("/matcher")
	public String matcher(@PathVariable String source, @PathVariable String target) {

		return "";
	}

}
