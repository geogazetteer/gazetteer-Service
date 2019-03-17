package top.geomatics.gazetteer.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.fastjson.JSON;

import top.geomatics.gazetteer.model.AddressRow;

public class Test {
	
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session=helper.getSession();
	private static AddressMapper mapper=session.getMapper(AddressMapper.class);
	
	public static void main(String[] args) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sql_fields", "*");
		map.put("sql_tablename", "民治社区");
//		map.put("building_id", "4403060090010500091");
//		map.put("building", "六巷7栋");
//		map.put("address", "广东省深圳市龙华区民治街道民治社区沙吓村六巷7栋");
		map.put("address", "%六巷7栋%601");
//		map.put("sql_limit", "3");
		map.put("sql_orderBy", "id DESC");
		
		List<AddressRow> list=mapper.findEquals(map);
		String json = JSON.toJSONString(list);
		System.out.println(json);
	}

}
