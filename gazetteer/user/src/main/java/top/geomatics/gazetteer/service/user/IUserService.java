/**
 * 
 */
package top.geomatics.gazetteer.service.user;

import top.geomatics.gazetteer.model.user.User;

/**
 * @author whudyj
 *
 */
public interface IUserService {
	public User userLogin(User user);

	public void updateUser(User user);
	public User selectUserByid(long userid);

	public boolean existUser(User user);

	public void register(User user);

}
