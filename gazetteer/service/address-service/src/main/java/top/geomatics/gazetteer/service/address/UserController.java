package top.geomatics.gazetteer.service.address;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import top.geomatics.gazetteer.config.ResourcesManager;
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
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(DataController.class);

	private UserServiceImpl userService = new UserServiceImpl();
	private static HttpSession session = null;

	public static HttpSession getSession() {
		return session;
	}

	@ApiOperation(value = "用户注册", notes = "用户注册")
	@PostMapping("/register")
	public ResponseEntity<String> register(@ApiParam(value = "注册用户") @RequestBody User user) {
		if (userService.existUser(user)) {
			return new ResponseEntity<>(String.format("注册失败，用户名 %s 已存在", user.getUsername()), HttpStatus.UNAUTHORIZED);
		} else {
			// 创建用户空间
			if (createUserWorkspace(user.getUsername())) {
				userService.register(user);
			} else {
				return new ResponseEntity<>(String.format("注册用户 %s 失败，用户空间已经存在！", user.getUsername()),
						HttpStatus.UNAUTHORIZED);
			}
		}
		return new ResponseEntity<>(String.format("注册用户 %s 成功，请登录", user.getUsername()), HttpStatus.OK);
	}

	@ApiOperation(value = "用户登录", notes = "用户登录")
	@PutMapping("/login")
	public ResponseEntity<String> login(@ApiParam(value = "登录用户") @RequestBody User user) {

		UserController.session = CurrentSession.session();

		User user2 = userService.userLogin(user);

		if (user2 != null) {
			CurrentSession.setUser(user2);

			UserManager.getInstance().addUser(user2);

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

	private boolean createUserWorkspace(String username) {
		ResourcesManager manager = ResourcesManager.getInstance();
		String USER_WORKSPACE_ROOT = "user_workspace_root";
		String user_workspace_root = manager.getValue(USER_WORKSPACE_ROOT);// 根目录
		String user_workspace_path = user_workspace_root + File.separator + username;
		File file = new File(user_workspace_path);
		if (file.exists()) {
			return false;// 已经存在
		}

		if (!file.mkdir()) {
			return false;// 创建目录失败
		}
		// 创建config目录
		String config_path = user_workspace_path + File.separator + "config";
		File configFile = new File(config_path);
		configFile.mkdir();

		// 创建source目录
		String source_path = user_workspace_path + File.separator + "source";
		File sourceFile = new File(source_path);
		sourceFile.mkdir();
		// 创建target目录
		String target_path = user_workspace_path + File.separator + "target";
		File targetFile = new File(target_path);
		targetFile.mkdir();

		// 增加两个属性文件
		String db_prop = config_path + File.separator + "db.properties";
		String edit_prop = config_path + File.separator + "editor_db.properties";

		Configurations db_configs = new Configurations();
		File db_propertiesFile = new File(db_prop);
		if (!db_propertiesFile.exists()) {
			try {
				if (!db_propertiesFile.createNewFile()) {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileBasedConfigurationBuilder.setDefaultEncoding(PropertiesConfiguration.class, "UTF-8");
		FileBasedConfigurationBuilder<PropertiesConfiguration> db_builder = db_configs
				.propertiesBuilder(db_propertiesFile);

		try {
			Configuration db_config = db_builder.getConfiguration();
			// editor_db_properties_file =
			// D:\\gazetteer\\data\\user_enterprise1\\config\\editor_db.properties
			db_config.setProperty("editor_db_properties_file", edit_prop);
			// upload_file_path = D:\\gazetteer\\data\\user_enterprise1\\source
			db_config.setProperty("upload_file_path", source_path);
			// download_file_path = D:\\gazetteer\\data\\user_enterprise1\\target
			db_config.setProperty("download_file_path", target_path);

			db_builder.save();

		} catch (ConfigurationException e1) {
			e1.printStackTrace();
			// 日志
			String logMsgString = String.format("创建配置文件 %s 失败！", db_prop);
			logger.error(logMsgString);
			return false;
		}

		Parameters edit_params = new Parameters();
		File edit_propertiesFile = new File(edit_prop);
		if (!edit_propertiesFile.exists()) {
			try {
				if (!edit_propertiesFile.createNewFile()) {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileBasedConfigurationBuilder<FileBasedConfiguration> edit_builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
				PropertiesConfiguration.class).configure(edit_params.fileBased().setFile(edit_propertiesFile));
		try {
			Configuration edit_config = edit_builder.getConfiguration();
			// driver=org.sqlite.JDBC
			// url=
			// username=
			// password=
			edit_config.setProperty("driver", "org.sqlite.JDBC");
			edit_config.setProperty("url", "");
			edit_config.setProperty("username", "");
			edit_config.setProperty("password", "");

			edit_builder.save();

		} catch (ConfigurationException e1) {
			e1.printStackTrace();
			// 日志
			String logMsgString = String.format("创建配置文件 %s 失败！", edit_prop);
			logger.error(logMsgString);
			return false;
		}

		return true;
	}
}
