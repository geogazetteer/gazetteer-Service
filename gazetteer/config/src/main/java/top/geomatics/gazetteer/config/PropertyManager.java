/**
 * 
 */
package top.geomatics.gazetteer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author whudyj
 *
 */
public class PropertyManager {
	// 统一资源文件配置
	private static final String R_PATH = "gazetteer";
	private static final String R_CONFIG = "config";
	private static final String R_NAME = "gazetteer.properties";
	private static String resourcesPath = "";

	// 统一配置信息
	// 1、数据库配置信息
//			# resource="sqlite.properties"
//			driver=org.sqlite.JDBC
//			url=jdbc:sqlite:d:\\data\\LH_gazetteer.gpkg
//			username=
//			password=
	private static final String DB_DRIVER_KEY = "driver";
	private static final String DB_URL_KEY = "url";
	private static final String DB_USERNAME_KEY = "username";
	private static final String DB_PASSWORD_KEY = "password";

	private static final String DB_DRIVER_VALUE = "org.sqlite.JDBC";
	private static final String DB_URL_VALUE_1 = "jdbc:sqlite:d:\\data\\LH_gazetteer.gpkg";
	private static final String DB_USERNAME_VALUE_1 = "";
	private static final String DB_PASSWORD_VALUE_1 = "";

	public static void writeProperty(Properties rProperties) {
		rProperties.setProperty(DB_DRIVER_KEY, DB_DRIVER_VALUE);
		rProperties.setProperty(DB_URL_KEY, DB_URL_VALUE_1);
		rProperties.setProperty(DB_USERNAME_KEY, DB_USERNAME_VALUE_1);
		rProperties.setProperty(DB_PASSWORD_KEY, DB_PASSWORD_VALUE_1);
	}

	public static boolean writeResources() {
		resourcesPath = getResourcesPath();
		if (resourcesPath.isEmpty()) {
			// 日志
			System.out.println(" resources file does not exist! ");
			return false;
		}
		File file = new File(resourcesPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			boolean rb = file.delete();
			if (rb) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// 日志
				System.out.println(resourcesPath + " resources file can not be deleted! ");
			}
		}
		Properties rProperties = new Properties();
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(file, false);
			// 写属性数据
			writeProperty(rProperties);
			rProperties.store(oFile, "create properties file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != oFile)
					oFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * @return String 返回当前用户路径下的资源文件名称
	 */
	public static String getResourcesPath() {
		String pathString = System.getProperty("user.home") + File.separator + R_PATH;
		pathString = pathString + File.separator + R_NAME;
		File resFile = new File(pathString);
		if (!resFile.exists()) {
			try {
				boolean b = resFile.createNewFile();
				if (!b) {
					// 日志
					System.out.println(pathString + " resources file does not exist! ");
					return "";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pathString;
	}

	/**
	 * @return String 返回config路径下的资源文件名称
	 */
	public static String getConfigPath() {
		URL jarUrl = PropertyManager.class.getProtectionDomain().getCodeSource().getLocation();
		if (null == jarUrl) {
			// 日志
			System.out.println("jar path does not exist !");
			return "";
		}

		File file = null;
		try {
			file = new File(jarUrl.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (!file.exists() || file.isDirectory()) {
			// 日志
			System.out.println("jar path is not correct !");
			return "";
		}
		// bin 目录
		File parentFile = new File(file.getAbsolutePath()).getParentFile();
		parentFile = parentFile.getParentFile();
		// config目录
		String pathString = parentFile.getPath() + File.separator + R_CONFIG;
		pathString = pathString + File.separator + R_NAME;
		File resFile = new File(pathString);
		if (!resFile.exists()) {
			try {
				boolean b = resFile.createNewFile();
				if (!b) {
					// 日志
					System.out.println(pathString + " resources file does not exist! ");
					return "";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pathString;
	}

	public static void printPathInfo() {
		// 系统环境变量，有些信息可以考虑放入到日志文件中
		System.out.println("----------------系统环境变量---------------");
		Map<String, String> map = System.getenv();
		for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			System.out.println(key + "=" + map.get(key));
		}
		// 系统属性
		System.out.println("----------------系统属性---------------");
		Properties prop = System.getProperties();
		// 利用循环遍历出key和value
		Iterator<Object> itr = prop.keySet().iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			System.out.println(str + " = " + prop.get(str));
		}
		// Java路径问题
		// 绝对路径，URL
		System.out.println("----------------Java路径问题---------------");
		String userdir = System.getProperty("user.dir");// 用户当前路径
		String userhome = System.getProperty("user.home");// 用户系统路径，如：C:\Users\whudyj
		String classpath = System.getProperty("java.class.path");// 当前类路径
		File file1 = new File(userdir);
		System.out.println("user.dir : " + file1.getPath());
		File parent = file1.getParentFile();// 处理路径的层次关系
		System.out.println("parent of user.dir :" + parent.getPath());
		URI uri = parent.toURI();
		System.out.println(uri.toString());
		try {
			System.out.println(uri.toURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			// 路径中有正文编码时，用URLDecoder/URLEncoder类来解决
			String url1 = URLDecoder.decode(classpath, "utf-8");
			System.out.println("classpath:" + url1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 相对路径问题
		System.out.println("----------------classpath路径---------------");
		// 1、使用类名称
		URL url2 = PropertyManager.class.getResource("");// \target\classes\...
		if (null != url2) {
			System.out.println(url2.toString());
		}

		url2 = PropertyManager.class.getResource("/");// \target\classes
		if (null != url2) {
			System.out.println(url2.toString());
		}
		// 2、使用当前线程，推荐使用
		url2 = Thread.currentThread().getContextClassLoader().getResource("");
		if (null != url2) {
			System.out.println(url2.toString());
		}
		// 3、
		url2 = PropertyManager.class.getClassLoader().getResource("");
		if (null != url2) {
			System.out.println(url2.toString());
		}
		// 4、
		url2 = ClassLoader.getSystemResource("");
		if (null != url2) {
			System.out.println(url2.toString());
		}
		// \target\classes或jar包的路径
		URL jarUrl = PropertyManager.class.getProtectionDomain().getCodeSource().getLocation();
		if (null != jarUrl) {
			System.out.println(jarUrl.toString());
		}
		// maven路径变量
//				${basedir} 项目根目录
//				${project.build.directory} 构建目录，缺省为target
//				${project.build.outputDirectory} 构建过程输出目录，缺省为target/classes
//				${project.build.finalName} 产出物名称，缺省为${project.artifactId}-${project.version}
//				${project.packaging} 打包类型，缺省为jar
//				${project.xxx} 当前pom文件的任意节点的内容
		System.out.println("----------------resources路径---------------");
		// 只有src/main/resources目录下的资源会默认打包，其他目录下的资源需要pom配置resources结点
		// 根路径是target/classes
		URL resUrl = PropertyManager.class.getResource("/test1.properties");
		if (null != resUrl) {
			System.out.println(resUrl.toString());
		}
		resUrl = PropertyManager.class.getResource("/test2.properties");
		if (null != resUrl) {
			System.out.println(resUrl.toString());
		}
		// mybatis路径变量
		// Spring和Spring boot路径变量
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		printPathInfo();
		getResourcesPath();
		getConfigPath();
		writeResources();
	}

}
