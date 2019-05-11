/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>企业法人库操作的帮助类</b><br>
 * 
 * @author whudyj
 *
 */
public class EnterpriseDatabaseHelper {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(EnterpriseDatabaseHelper.class);

	private static final String resource = "enterprise_db_config.xml";

	private static SqlSessionFactory sessionFactory;

	static {
		try {
			// 使用MyBatis提供的Resources类加载mybatis的配置文件
			InputStream inputStream = Resources.getResourceAsStream(resource);
			// 构建sqlSession的工厂
			sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
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
