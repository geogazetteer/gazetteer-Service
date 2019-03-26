/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.model.EnterpriseRow;

/**
 * @author whudyj
 *
 */
public interface EnterpriseAddressMapper {
	// 创建更新数据库表
	public void dropTable(String tableName);

	public void createAddressTable(String tableName);

	public Integer insertAddress(Map<String, Object> map);

	public Integer updateStatus(Map<String, Object> map);

	public Integer updateModifier(Map<String, Object> map);

	public Integer updateDate(Map<String, Object> map);

	public Integer updateAddress(Map<String, Object> map);

	public Integer updateAddressId(Map<String, Object> map);

	public Integer updateAll(Map<String, Object> map);

	// 查询数据库表
	public List<EnterpriseRow> selectByFid(Map<String, Object> map);

	public List<EnterpriseRow> selectByFids(Map<String, Object> map);

	public List<EnterpriseRow> findEquals(Map<String, Object> map);

	public List<EnterpriseRow> findLike(Map<String, Object> map);
	
	public List<EnterpriseRow> findPageEquals(Map<String, Object> map);

}
