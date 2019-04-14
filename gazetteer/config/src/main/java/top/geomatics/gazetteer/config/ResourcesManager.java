/**
 * 
 */
package top.geomatics.gazetteer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author whudyj
 *
 */
public class ResourcesManager {
	private static Properties pro = new Properties();

	private ResourcesManager() {
		String pf = "D:\\gazetteer\\config\\gazetteer.properties";
		try {
			pro.load(new FileInputStream(new File(pf)));
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
