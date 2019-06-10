/**
 * 
 */
package top.geomatics.gazetteer.service.user;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.config.ResourcesManager2;
import top.geomatics.gazetteer.database.AddressEditorMapper;
import top.geomatics.gazetteer.database.EditorDatabaseHelper;
import top.geomatics.gazetteer.model.user.User;

/**
 * @author whudyj
 *
 */
public class UserInformation {
	private static final String UPLOAD_FILE_PATH = "upload_file_path";
	private static final String DOWNLOAD_FILE_PATH = "download_file_path";
	private static final String EDIT_DB_PROPERTIES = "editor_db_properties_file";

	private User user;

	private ResourcesManager2 rm;
	// 文件上传路径
	private String upload_file_path;
	// 文件下载路径
	private String download_file_path;
	// 数据库配置文件路径
	private String db_properties;

	private EditorDatabaseHelper helper_revision;
	private SqlSession session_revision;
	private AddressEditorMapper mapper_revision;

	public UserInformation(User user) {
		super();
		this.user = user;
		String userName = this.user.getUsername();

		rm = new ResourcesManager2(userName);
		// 文件上传路径
		upload_file_path = rm.getValue(UPLOAD_FILE_PATH);
		// 文件下载路径
		download_file_path = rm.getValue(DOWNLOAD_FILE_PATH);
		// 数据库配置文件路径
		db_properties = rm.getValue(EDIT_DB_PROPERTIES);

		helper_revision = new EditorDatabaseHelper(userName);
		session_revision = helper_revision.getSession();
		mapper_revision = session_revision.getMapper(AddressEditorMapper.class);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserName() {
		return getUser().getUsername();
	}

	public String getUploadPath() {
		return this.upload_file_path;
	}

	public String getDownloadPath() {
		return this.download_file_path;
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
