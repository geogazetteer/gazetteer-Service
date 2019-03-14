/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * @author whudyj
 *
 */
@RestController
@RequestMapping("/")
public class ServiceController {
	@ApiOperation(value = "列出所有服务", notes = "列出所有服务")
	@GetMapping("/services")
	public String services() {
		// 利用模板引擎直接返回html页面
		return "services";
	}
}
