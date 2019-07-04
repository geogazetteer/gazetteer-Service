/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.config.ResourcesManager;

/**
 * @author whudyj
 *
 */
public class DatabaseManager {
	private static Map<String, DatabaseInformation> databaseInfos = null;
	private static DatabaseManager instanceManager = new DatabaseManager();

	private static ResourcesManager manager = null;
	private static final String DATABASE_WORKSPACE_ROOT = "database_workspace_root";
	private static String database_workspace_root = null;

	private DatabaseManager() {
		databaseInfos = new HashMap<String, DatabaseInformation>();
		manager = ResourcesManager.getInstance();
		database_workspace_root = manager.getValue(DATABASE_WORKSPACE_ROOT);
		// 得到数据库目录
		List<String> paths = getDirectories();
		for (String dbPaht : paths) {
			databaseInfos.put(dbPaht, new DatabaseInformation(dbPaht));
		}

	}

	public static DatabaseManager getInstance() {
		return instanceManager;
	}

	private List<String> getDirectories() {
		List<String> paths = new ArrayList<String>();
		File dir = new File(database_workspace_root);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					paths.add(f.getName());
				}
			}
		}
		return paths;
	}

	public DatabaseInformation getDbInfo(String dbPath) {
		DatabaseInformation dbInformation = null;
		if (null != databaseInfos) {
			dbInformation = databaseInfos.get(dbPath);
		}
		return dbInformation;
	}

	public DatabaseInformation[] list() {
		List<DatabaseInformation> dbInformations = new ArrayList<DatabaseInformation>();
		DatabaseInformation dbInformation = null;
		if (null != databaseInfos && databaseInfos.size() > 0) {
			for (String dbPath : databaseInfos.keySet()) {
				dbInformation = databaseInfos.get(dbPath);
				dbInformations.add(dbInformation);
			}

		}
		DatabaseInformation[] infos = null;
		if (dbInformations.size() >0) {
			infos = new DatabaseInformation[dbInformations.size()];
			for (int i =0; i < dbInformations.size(); i++) {
				infos[i] = dbInformations.get(i);
			}
		}
		
		return infos;
	}

}
