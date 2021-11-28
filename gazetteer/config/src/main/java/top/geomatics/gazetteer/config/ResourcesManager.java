/**
 * 
 */
package top.geomatics.gazetteer.config;

import java.io.*;
import java.util.Properties;

/**
 * @author whudyj
 *
 */
public class ResourcesManager {
	//从资源文件gazetteer.properties获得配置文件路径
//	private static final String R_NAME = "gazetteer_windows.properties";
	private static final String R_NAME = "gazetteer_linux.properties";
	private static Properties pro = new Properties();
	private static InputStream in = ResourcesManager.class.getClassLoader().getResourceAsStream(R_NAME);

	private ResourcesManager() {
		try{
			pro.load(in);
		}catch (IOException e){
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
