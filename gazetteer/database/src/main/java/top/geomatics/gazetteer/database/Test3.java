/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.model.AddressRow;

/**
 * @author whudyj
 *
 */
public class Test3 {

	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", "*");
		map.put("sql_tablename", "民治社区");
		map.put("building_id", "4403060090010500091");
		map.put("sql_limit", "3");
		List<AddressRow> list = mapper.findEquals(map);
		for (AddressRow row : list) {
			System.out.println(row.getId());
		}
	}

}
