/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.opencsv.CSVWriter;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * @author whudyj
 *
 */
public class Main {

	// 连接地址数据库
	public static DatabaseHelper helper = new DatabaseHelper();
	public static SqlSession session = helper.getSession();
	public static AddressMapper mapper = session.getMapper(AddressMapper.class);

	public static EnterpriseDatabaseHelper helper2 = new EnterpriseDatabaseHelper();
	public static SqlSession session2 = helper2.getSession();
	public static EnterpriseAddressMapper mapper2 = session2.getMapper(EnterpriseAddressMapper.class);

	public static Map<String, Object> map1 = new HashMap<String, Object>();
	public static Map<String, Object> map2 = new HashMap<String, Object>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		map1.put("sql_fields", "id,address");
		map2.put("sql_fields", "fid,address");
		String filePath = "d:\\data\\sim2";
		for (String community : IGazetteerConstant.COMMUNITY_LIST) {
			map1.put("sql_tablename", community);
			map2.put("sql_tablename", community);

			List<EnterpriseRow> rows2 = mapper2.findEquals(map2);
			// output
			// char separator, char quotechar, char escapechar, String lineEnd
			CSVWriter writer = null;
			String fileName = filePath + File.separator + community + ".csv";
			try {
				writer = new CSVWriter(new FileWriter(fileName), ',', '\'', '\'', "\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (EnterpriseRow row2 : rows2) {
				String address2 = row2.getAddress();
				String queryString = "%" + address2.substring(address2.lastIndexOf(community) + community.length())
						+ "%";
				map1.put("address", queryString);
				List<AddressRow> rows = mapper.findLike(map1);
				for (AddressRow row : rows) {
					String address1 = row.getAddress();
					SimilarityIndicator indicator = AddressSimilarity.indicator(address1, address2);
//					String str = row2.getFid() + ", " + row.getId() + ", " + indicator.cosineDistance + ", "
//							+ indicator.jaccardSimilarity + ", " + indicator.jaroWinklerDistance + ", "
//							+ indicator.fuzzyScore;
//					System.out.println(str);
					// feed in your array (or convert your data to an array)
					String[] entries = new String[6];
					entries[0] = String.format("%6d", row2.getFid());
					entries[1] = String.format("%6d", row.getId());
					entries[2] = String.format("%6.3f", indicator.cosineDistance);
					entries[3] = String.format("%6.3f", indicator.jaccardSimilarity);
					entries[4] = String.format("%6.3f", indicator.jaroWinklerDistance);
					entries[5] = String.format("%6d", indicator.fuzzyScore);
					//entries[6] = address2;
					//entries[7] = address1;
					writer.writeNext(entries, false);
				}
			}
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
