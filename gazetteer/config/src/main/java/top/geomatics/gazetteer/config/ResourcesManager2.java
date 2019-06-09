/**
 * 
 */
package top.geomatics.gazetteer.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author whudyj
 *
 */
public class ResourcesManager2 {

	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String USER_WORKSPACE_ROOT = "user_workspace_root";
	private static String user_workspace_root = manager.getValue(USER_WORKSPACE_ROOT);

	private static final String R_NAME = "db.properties";
	private static Properties pro = new Properties();

	public ResourcesManager2(String userName) {
		// String pathString = "C:\\gazetteer\\config";//System.getProperty("user.home")
		// + File.separator + R_PATH;
		String pathString = user_workspace_root + File.separator + userName + File.separator + "config";
		pathString = pathString + File.separator + R_NAME;
		File resFile = new File(pathString);
		if (!resFile.exists()) {
			// 日志
			try {
				throw new FileNotFoundException("resource file:" + pathString + " does not exist!");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			pro.load(new BufferedReader(new InputStreamReader(new FileInputStream(pathString), "UTF-8")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getValue(String key) {
		return pro.getProperty(key);

	}
}
