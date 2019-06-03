package top.geomatics.gazetteer.service.address;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import top.geomatics.gazetteer.model.user.User;
import top.geomatics.gazetteer.service.user.IUserService;

/**
 * <b>系统用户管理服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/user", tags = "系统用户管理服务")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserService userService;

	@ApiOperation(value = "用户注册", notes = "用户注册")
	@GetMapping("/register")
	public String register(@RequestParam User user, @RequestParam Model model, BindingResult bindingResult) {
		if (userService.existUser(user)) {
			bindingResult.rejectValue("username", "userExist", "用户名已存在");

		}
		if (bindingResult.hasErrors()) {

			// 获取错误
			List<ObjectError> errors = bindingResult.getAllErrors();
			model.addAttribute("errors", errors);
			model.addAttribute("registuser", user);
			System.out.println(user.getUsername());
			return "register";
		}

		else {
			userService.register(user);
			model.addAttribute("logininfo", "注册成功，请登陆！");
			return "myaccount";
		}
	}

	@ApiOperation(value = "用户登录", notes = "用户登录")
	@GetMapping("/login")
	public String login(@RequestParam User user, @RequestParam HttpSession session, @RequestParam Model model) {
		User user2 = userService.userLogin(user);

		if (user2 != null) {
			session.setAttribute("LoginUser", user2);
			return "forward:index.action";
		} else {
			model.addAttribute("error", "loginerror");
			return "myaccount";
		}
	}

	@ApiOperation(value = "用户注销", notes = "用户注销")
	@GetMapping("/logout")
	public String logout(@RequestParam HttpSession session) {
		session.removeAttribute("LoginUser");
		return "redirect:index.action";
	}

	@ApiOperation(value = "用户更新", notes = "用户更新")
	@GetMapping("/update")
	public String update(@RequestParam User user, @RequestParam HttpSession session, @RequestParam long userid) {
		user.setUserid(userid);
		userService.updateUser(user);

		User user2 = userService.selectUserByid(userid);
		session.setAttribute("LoginUser", user2);
		return "myaccount";
	}
}
