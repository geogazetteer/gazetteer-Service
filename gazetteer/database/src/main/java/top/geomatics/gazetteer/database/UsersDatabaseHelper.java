/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.config.ResourcesManager;

/**
 * <b>用户数据库操作的帮助类</b><br>
 * 
 * @author whudyj
 *
 */
public class UsersDatabaseHelper {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(UsersDatabaseHelper.class);

	private static final String resource = "users_config.xml";

	private static SqlSessionFactory sessionFactory;

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String USERS_DB_PROPERTIES_FILE = "users_db_properties_file";
	private static String user_properties_file = manager.getValue(USERS_DB_PROPERTIES_FILE);

	static {
		try {
			// 使用MyBatis提供的Resources类加载mybatis的配置文件
			InputStream inputStream = Resources.getResourceAsStream(resource);
			Properties prop = new Properties();
			prop.load(new FileInputStream(new File(user_properties_file)));
			// 构建sqlSession的工厂
			sessionFactory = new SqlSessionFactoryBuilder().build(inputStream, prop);
		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("EnterpriseDatabaseHelper.1"), resource); //$NON-NLS-1$
			logger.error(logMsgString);
		}

	}

	// 创建能执行映射文件中sql的sqlSession
	public SqlSession getSession() {
		return sessionFactory.openSession();
	}
}
