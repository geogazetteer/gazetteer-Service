/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * @author whudyj
 *
 */
public interface AddressMapper {

	// 搜索所有地址
	public List<AddressRow> selectAllAddress();

	// 搜索所有地址，返回limit个结果
	public List<AddressRow> selectAllAddressWithLimit(int limit);

	// 根据详细地址查询
	public List<AddressRow> selectByAddress(String address);

	// 根据地理编码查询
	public List<AddressRow> selectByCode(String code);

	// 模糊查询
	public List<AddressRow> selectByAddressLike(String keyword);

	// lucene
	public List<AddressRow> selectAddressBylucene(String keyword);

	// 关键字
	public List<AddressRow> selectByKeyword(String keyword);

	public List<AddressRow> selectstreets();

	public List<AddressRow> selectcommunities();

	public List<AddressRow> selectbuildings();

	public List<AddressRow> selecthouses();

	public List<AddressRow> selectAddressForDictionary() throws Exception;

	public List<AddressRow> findEquals(Map<String, Object> map);

	public List<AddressRow> findLike(Map<String, Object> map);

	// 创建更新数据库表
	public void dropTable(String tableName);

	public void createAddressTable(String tableName);

	public void insertAddress(Map<String, Object> map);

	public void createDistrictTable(String tableName);

	public void insertDistrict(Map<String, Object> map);

	public void createStreetTable(String tableName);

	public void insertStreet(Map<String, Object> map);

	// 通过id查询所有
	public List<AddressRow> selectById(Integer id);
	public List<AddressRow> selectByIds(List<Integer> ids);

	public List<SimpleAddressRow> findSimpleEquals(Map<String, Object> map);

}
