package top.geomatics.gazetteer.utilities.database.dbprocessing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;

/**
 * 使用mybatis处理数据库
 * 
 * @author whudyj
 *
 */
public class DatabaseProcessor3 {
	// 标准地址数据库准备
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();

	private static Map<String, String> streetMap = new HashMap<String, String>();
	private static Map<String, String> communityMap = new HashMap<String, String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		queryDistrict();
		queryCommunity();
		queryOthers();

	}

	private static void queryDistrict() {
		// 按查询所有记录
		map.clear();
		map.put("sql_fields", "street");
		map.put("sql_tablename", "dmdz");
		List<AddressRow> rows = mapper.findEquals(map);
		// 创建表"龙华区"
		String sql_tablename = "龙华区";
		map.put("sql_tablename", sql_tablename);
		mapper.dropTable(sql_tablename);
		mapper.createDistrictTable(sql_tablename);

		for (AddressRow row : rows) {
			String street = row.getStreet().trim();
			if (street.isEmpty()) {
				continue;
			}
			if (!streetMap.containsKey(street)) {
				streetMap.put(street, street);
				map.put("street", street);
				mapper.insertDistrict(map);
			}
		}
		session.commit(true);

	}

	// 查询每个街道下面有多少社区，每个街道创建一个表（表名为街道名），一个字段为community
	private static void queryCommunity() {
		if (streetMap.size() < 1) {
			return;
		}
		for (String keyString : streetMap.keySet()) {
			map.clear();
			map.put("sql_fields", "community");
			map.put("sql_tablename", "dmdz");
			map.put("street", keyString);
			List<AddressRow> rows = mapper.findEquals(map);
			Map<String, String> communityMap2 = new HashMap<String, String>();

			String tName = keyString;
			tName = tName.replace('(', '_');
			tName = tName.replace(')', '_');
			mapper.dropTable(tName);
			mapper.createStreetTable(tName);
			map.clear();
			map.put("sql_tablename", tName);
			for (AddressRow row : rows) {
				String community = row.getCommunity().trim();
				if (community.isEmpty()) {
					continue;
				}
				communityMap.put(community, community);
				if (!communityMap2.containsKey(community)) {
					communityMap2.put(community, community);
					map.put("community", community);
					mapper.insertStreet(map);
				}
			}
			session.commit(true);
		}
		
	}

	// 查询每个社区下面有多少记录，每个社区创建一个表（表名为社区名），字段为address_id,code,building_id,village,building,address
	private static void queryOthers() {
		if (communityMap.size() < 1) {
			return;
		}
		for (String community : communityMap.keySet()) {
			map.clear();
			map.put("sql_fields", "*");
			map.put("sql_tablename", "dmdz");
			map.put("community", community);
			List<AddressRow> rows = mapper.findEquals(map);
			String sql_tablename = community;
			sql_tablename = sql_tablename.replace('(', '_');
			sql_tablename = sql_tablename.replace(')', '_');
			mapper.dropTable(sql_tablename);
			mapper.createAddressTable(sql_tablename);
			map.clear();
			map.put("sql_tablename", sql_tablename);
			for (AddressRow row : rows) {
//				address_id ,
//		          code ,
//		          building_id ,
//		          house_id ,
//		          province ,
//		          city ,
//		          district ,
//		          street ,
//		          community ,
//		          road ,
//		          road_num ,
//		          village ,
//		          building ,
//		          floor ,
//		          address ,
//		          update_address_date ,
//		          publish ,
//		          create_address_date 
				map.put("address_id", row.getAddress_id());
				map.put("code", row.getCode());
				map.put("building_id", row.getBuilding_id());
				map.put("house_id", row.getHouse_id());
				map.put("province", row.getProvince());
				map.put("city", row.getCity());
				map.put("district", row.getDistrict());
				map.put("street", row.getStreet());
				map.put("community", row.getCommunity());
				map.put("road", row.getRoad());
				map.put("road_num", row.getRoad_num());
				map.put("village", row.getVillage());
				map.put("floor", row.getFloor());
				map.put("address", row.getAddress());
				map.put("update_address_date", row.getUpdate_address_date());
				map.put("publish", row.getPublish());
				map.put("create_address_date", row.getCreate_address_date());

				mapper.insertAddress(map);
			}
			session.commit(true);
		}
		
	}
}
