/**
 * 
 */
package top.geomatics.gazetteer.service.user;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.database.UsersDatabaseHelper;
import top.geomatics.gazetteer.database.UsersMapper;
import top.geomatics.gazetteer.model.user.User;

/**
 * @author whudyj
 *
 */
public class UserServiceImpl implements IUserService {

	private UsersDatabaseHelper helper = new UsersDatabaseHelper();
	private SqlSession session = helper.getSession();
	private UsersMapper userMapper = session.getMapper(UsersMapper.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.service.user.IUserService#userLogin(top.geomatics.
	 * gazetteer.model.user.User)
	 */
	@Override
	public User userLogin(User user) {
		User user2 = userMapper.selectUserByName(user.getUsername());
		if (user2 != null && user2.getPassword_().equals(user.getPassword_())) {
			return user2;
		}

		return user2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.service.user.IUserService#updateUser(top.geomatics.
	 * gazetteer.model.user.User)
	 */
	@Override
	public void updateUser(User user) {
		userMapper.updateUserById(user);
		session.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see top.geomatics.gazetteer.service.user.IUserService#selectUserByid(long)
	 */
	@Override
	public User selectUserByid(long userid) {
		return userMapper.selectUserById(userid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.service.user.IUserService#existUser(top.geomatics.
	 * gazetteer.model.user.User)
	 */
	@Override
	public boolean existUser(User user) {
		User user2 = userMapper.selectUserByName(user.getUsername());
		if (user2 != null)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * top.geomatics.gazetteer.service.user.IUserService#register(top.geomatics.
	 * gazetteer.model.user.User)
	 */
	@Override
	public void register(User user) {
		userMapper.insertUser(user);
		session.commit();
	}

}
