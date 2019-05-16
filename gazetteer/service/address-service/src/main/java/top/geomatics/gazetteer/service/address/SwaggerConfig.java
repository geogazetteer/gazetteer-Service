package top.geomatics.gazetteer.service.address;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <b>Swagger2配置类</b><br>
 * 
 * @author whudyj
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket config() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage(Messages.getString("SwaggerConfig.0"))).build(); //$NON-NLS-1$
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(Messages.getString("SwaggerConfig.1")).termsOfServiceUrl(Messages.getString("SwaggerConfig.2")) //$NON-NLS-1$ //$NON-NLS-2$
				.version(Messages.getString("SwaggerConfig.3")).build(); //$NON-NLS-1$
	}

}
