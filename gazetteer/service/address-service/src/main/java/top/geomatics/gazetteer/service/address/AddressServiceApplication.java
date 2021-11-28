package top.geomatics.gazetteer.service.address;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
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

	/**
	 * 使用@Bean注解注入第三方的解析框架（fastJson）
	 */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		// 1、首先需要先定义一个convert转换消息对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		// 2、添加fastJson的配置信息，比如：是否要格式化返回的json数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
		// 3、在convert中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);

		return new HttpMessageConverters(fastConverter);
	}

}
