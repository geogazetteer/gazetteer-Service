package top.geomatics.gazetteer.service.address;

import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;

//程序入口
@SpringBootApplication
@EnableSwagger2
public class AddressServiceApplication {

	public static DatabaseHelper helper = new DatabaseHelper();
	public static SqlSession session = helper.getSession();
	public static AddressMapper mapper = session.getMapper(AddressMapper.class);

	public static void main(String[] args) {
		SpringApplication.run(AddressServiceApplication.class, args);
	}

}
