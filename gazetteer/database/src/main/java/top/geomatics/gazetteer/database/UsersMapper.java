/**
 * 
 */
package top.geomatics.gazetteer.database;

import top.geomatics.gazetteer.model.user.User;

/**
 * <b>用户数据库操作的mybatis映射接口</b><br>
 * 
 * @author whudyj
 *
 */
public interface UsersMapper {

	public User selectUserByName(String username);
	public void updateUserById(User user);
	public User selectUserById(long userid);
	public void insertUser(User user);

}
