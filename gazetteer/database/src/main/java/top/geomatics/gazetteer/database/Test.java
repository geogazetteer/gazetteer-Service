package top.geomatics.gazetteer.database;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.fastjson.JSON;

import top.geomatics.gazetteer.model.AddressRow;

public class Test {
	
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session=helper.getSession();
	private static AddressMapper mapper=session.getMapper(AddressMapper.class);
	
	public static void main(String[] args) {
		List<AddressRow> list=mapper.selectByCode("44030600960102T0117");
		String json = JSON.toJSONString(list);
		System.out.println(json);
		
		
	}

}
