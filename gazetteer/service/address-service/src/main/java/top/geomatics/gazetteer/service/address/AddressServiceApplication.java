package top.geomatics.gazetteer.service.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <b>Spring boot程序入口</b><br>
 * 
 * @author whudyj
 */
@SpringBootApplication
@EnableSwagger2
public class AddressServiceApplication {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(AddressServiceApplication.class);

	public static void main(String[] args) {
		String logMsgString = Messages.getString("AddressServiceApplication.0"); //$NON-NLS-1$
		logger.info(logMsgString);

		SpringApplication.run(AddressServiceApplication.class, args);
	}

}
