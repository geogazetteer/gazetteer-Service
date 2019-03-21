package top.geomatics.gazetteer.utilities.database.dbprocessing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.IGazetteerConstant;
import top.geomatics.gazetteer.segment.WordSegmenter;

/**
 * 使用mybatis处理企业法人数据库
 * 
 * @author whudyj
 *
 */
public class EnterpriseDatabaseProcessor {
	// 标准地址数据库准备
	private static EnterpriseDatabaseHelper helper = new EnterpriseDatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static EnterpriseAddressMapper mapper = session.getMapper(EnterpriseAddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 建立表
	 * 
	 */
	private static void createTable() {
		String sql_tablename = "其他街道";
		// 建立街道表
		for (String street : IGazetteerConstant.STREET_LIST) {
			sql_tablename = street;
			map.put("sql_tablename", sql_tablename);
			mapper.dropTable(sql_tablename);
			mapper.createAddressTable(sql_tablename);
		}
		map.put("sql_tablename", sql_tablename);
		mapper.dropTable(sql_tablename);
		mapper.createAddressTable(sql_tablename);
		/// 建立街道表
		// 建立社区表
		for (String community : IGazetteerConstant.COMMUNITY_LIST) {
			sql_tablename = community;
			map.put("sql_tablename", sql_tablename);
			mapper.dropTable(sql_tablename);
			mapper.createAddressTable(sql_tablename);
		}
		session.commit(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createTable();
		query();

	}

	private static void query() {
		// 按查询所有记录
		String sql_tablename = "";
		map.clear();
		map.put("sql_fields", "code,name,street,owner,address");
		Map<String, Object> valueMap = new HashMap<String, Object>();
		for (int i = 0; i < 4; i++) {
			sql_tablename = "enterprise" + (i + 1);
			map.put("sql_tablename", sql_tablename);
			List<EnterpriseRow> rows = mapper.findEquals(map);
			for (EnterpriseRow row : rows) {
				valueMap.put("code", row.getCode());
				valueMap.put("name", row.getName());
				valueMap.put("street", row.getStreet());
				valueMap.put("owner", row.getOwner());
				valueMap.put("address", row.getAddress());
				// 先找到社区
				String community = WordSegmenter.getCommunity(row.getAddress());
				if (null != community && !community.isEmpty()) {
					valueMap.put("sql_tablename", community);
					mapper.insertAddress(valueMap);
				}
				// 找到街道
				String street = WordSegmenter.getStreet(row.getStreet());
				if (null != street && !street.isEmpty()) {
					valueMap.put("sql_tablename", street);
					mapper.insertAddress(valueMap);
				} else {
					valueMap.put("sql_tablename", "其他街道");
					mapper.insertAddress(valueMap);
				}
			}
		}

		session.commit(true);

	}
}
