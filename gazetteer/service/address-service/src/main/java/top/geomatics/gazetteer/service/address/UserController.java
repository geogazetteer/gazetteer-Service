package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <b>系统用户管理服务类</b><br>
 * 
 * @author whudyj
 */
@ApiIgnore
@Api(value = "/user", tags = "系统用户管理服务")
@RestController
@RequestMapping("/user")
public class UserController {

	@ApiOperation(value = "用户注册", notes = "用户注册，功能暂未实现")
	@GetMapping("/register")
	public String register(@RequestParam String username, @RequestParam String password) {

		return "";
	}

	@ApiOperation(value = "用户登录", notes = "用户登录，功能暂未实现")
	@GetMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password) {

		return "";
	}

}
