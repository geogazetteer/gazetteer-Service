/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.config.DatabaseConfig;
import top.geomatics.gazetteer.database.AddressEditorMapper;
import top.geomatics.gazetteer.database.EditorDatabaseHelper;

/**
 * @author whudyj
 *
 */
public class DatabaseInformation {
	private static final String EDIT_DB_PROPERTIES = "editor_db_properties_file";

	private DatabaseConfig rm;
	// 数据库配置文件路径
	private String db_properties;
	private String dbPath;

	private EditorDatabaseHelper helper_revision;
	private SqlSession session_revision;
	private AddressEditorMapper mapper_revision;

	public DatabaseInformation(String dbPath) {
		super();
		this.dbPath = dbPath;
		rm = new DatabaseConfig(this.dbPath);
		// 数据库配置文件路径
		db_properties = rm.getValue(EDIT_DB_PROPERTIES);

		helper_revision = new EditorDatabaseHelper(this.dbPath,db_properties);
		session_revision = helper_revision.getSession();
		mapper_revision = session_revision.getMapper(AddressEditorMapper.class);
	}

	public String getDbPath() {
		return this.dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public String getDbProperties() {
		return this.db_properties;
	}

	public SqlSession getSqlSession() {
		return this.session_revision;
	}

	public AddressEditorMapper getMapper() {
		return this.mapper_revision;
	}

}
