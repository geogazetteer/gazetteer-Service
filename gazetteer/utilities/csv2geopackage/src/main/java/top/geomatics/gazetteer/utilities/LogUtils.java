/**
 * 
 */
package top.geomatics.gazetteer.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- 统一处理日志-->
 * 
 * @author whudyj
 *
 */
public class LogUtils {
	public static final Logger LOGGER = LoggerFactory.getLogger(LogUtils.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("hello world");
		LOGGER.info("hello Logger");
	}

}
