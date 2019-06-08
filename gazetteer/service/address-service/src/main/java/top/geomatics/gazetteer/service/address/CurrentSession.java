/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import top.geomatics.gazetteer.model.user.User;

/**
 * @author whudyj
 *
 */
public class CurrentSession {
	public static final String CURRENT_USER = "user";

	public static HttpSession session() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

	public static User getCurrentUser() {
		HttpSession session = session();
		return (User) session.getAttribute(CURRENT_USER);
	}

	public static void setUser(User user) {
		HttpSession session = session();
		session.setAttribute(CURRENT_USER, user);
	}

	public static void removeUser(User user) {
		HttpSession session = session();
		session.removeAttribute((CURRENT_USER));
	}

	public static String getCurrentUserName() {
		User user = getCurrentUser();

		return user.getUsername();
	}

}
