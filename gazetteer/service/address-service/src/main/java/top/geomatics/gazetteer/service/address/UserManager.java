/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.util.HashMap;
import java.util.Map;

import top.geomatics.gazetteer.model.user.User;
import top.geomatics.gazetteer.service.user.UserInformation;

/**
 * @author whudyj
 *
 */
public class UserManager {
	private static Map<String, UserInformation> userInfos = null;
	private static UserManager instanceManager = new UserManager();

	private UserManager() {
		userInfos = new HashMap<String, UserInformation>();
		// 增加admin用户
		User admin = new User();
		admin.setUsername("user_admin");
		admin.setPassword_("user_admin");
		userInfos.put(admin.getUsername(), new UserInformation(admin));
	}

	public static UserManager getInstance() {
		return instanceManager;
	}

	public void addUser(User user) {
		userInfos.put(user.getUsername(), new UserInformation(user));
	}

	public UserInformation getUserInfo(String username) {
		return userInfos.get(username);
	}
}
