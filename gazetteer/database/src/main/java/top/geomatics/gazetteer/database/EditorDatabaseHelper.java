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

import top.geomatics.gazetteer.config.ResourcesManager2;

/**
 * <b>企业法人库操作的帮助类</b><br>
 * 
 * @author whudyj
 *
 */
public class EditorDatabaseHelper {
	private String userName;

	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(EditorDatabaseHelper.class);

	private static final String resource = "editor_db_config.xml";

	private static SqlSessionFactory sessionFactory = null;

	private ResourcesManager2 manager = new ResourcesManager2(this.userName);
	private final String EDITOR_DB_PROPERTIES_FILE = "editor_db_properties_file";
	private String editor_properties_file = manager.getValue(EDITOR_DB_PROPERTIES_FILE);

	public EditorDatabaseHelper(String userName) {
		super();
		this.userName = userName;
	}

	// 创建能执行映射文件中sql的sqlSession
	public SqlSession getSession() {
		try {
			// 使用MyBatis提供的Resources类加载mybatis的配置文件
			InputStream inputStream = Resources.getResourceAsStream(resource);
			Properties prop = new Properties();
			prop.load(new FileInputStream(new File(editor_properties_file)));
			// 构建sqlSession的工厂
			sessionFactory = new SqlSessionFactoryBuilder().build(inputStream, prop);
		} catch (Exception e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("EnterpriseDatabaseHelper.1"), resource); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		return sessionFactory.openSession();
	}
}
