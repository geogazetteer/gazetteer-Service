package top.geomatics.gazetteer.service.address;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.user.User;
import top.geomatics.gazetteer.service.user.UserServiceImpl;

/**
 * <b>系统用户管理服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/user", tags = "系统用户管理服务")
@RestController
@RequestMapping("/user")
public class UserController {
	private UserServiceImpl userService = new UserServiceImpl();

	@ApiOperation(value = "用户注册", notes = "用户注册")
	@PostMapping("/register")
	public ResponseEntity<String> register(@ApiParam(value = "注册用户") @RequestBody User user) {
		if (userService.existUser(user)) {
			return new ResponseEntity<>(String.format("注册失败，用户名 %s 已存在", user.getUsername()), HttpStatus.UNAUTHORIZED);
		} else {
			userService.register(user);
		}
		return new ResponseEntity<>(String.format("注册用户 %s 成功，请登录", user.getUsername()), HttpStatus.OK);
	}

	@ApiOperation(value = "用户登录", notes = "用户登录")
	@PutMapping("/login")
	public ResponseEntity<String> login(@ApiParam(value = "登录用户") @RequestBody User user) {
		User user2 = userService.userLogin(user);

		if (user2 != null) {
			CurrentSession.setUser(user2);
			return new ResponseEntity<>("登录成功", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("登录失败", HttpStatus.UNAUTHORIZED);
		}
	}

	@ApiOperation(value = "用户注销", notes = "用户注销")
	@PutMapping("/logout")
	public void logout(@ApiParam(value = "用户注销") @RequestBody User user) {
		CurrentSession.removeUser(user);
	}

	@ApiOperation(value = "用户更新", notes = "用户更新")
	@PutMapping("/update")
	public ResponseEntity<String> update(@ApiParam(value = "用户更新") @RequestBody User user,
			@ApiParam(value = "用户标识") @RequestParam long userid) {
		user.setUserid(userid);
		userService.updateUser(user);

		User user2 = userService.selectUserByid(userid);

		if (user2 != null) {
			CurrentSession.setUser(user2);
			return new ResponseEntity<>("用户更新成功", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("用户更新失败", HttpStatus.UNAUTHORIZED);
		}
	}
}
