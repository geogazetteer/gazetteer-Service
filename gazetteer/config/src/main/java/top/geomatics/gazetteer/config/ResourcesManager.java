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
public class ResourcesManager {
	private static final String R_PATH = "gazetteer";
	private static final String R_NAME = "gazetteer.properties";
	private static Properties pro = new Properties();

	private ResourcesManager() {
		String pathString = System.getProperty("user.home") + File.separator + R_PATH;
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

	private static ResourcesManager single = new ResourcesManager();

	// 静态工厂方法
	public static ResourcesManager getInstance() {
		return single;
	}

	public String getValue(String key) {
		return pro.getProperty(key);

	}
}
