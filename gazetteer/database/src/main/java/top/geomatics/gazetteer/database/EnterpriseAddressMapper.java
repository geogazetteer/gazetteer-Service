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

	public void insertAddress(Map<String, Object> map);

	public void updateStatus(Map<String, Object> map);

	public void updateModifier(Map<String, Object> map);

	public void updateDate(Map<String, Object> map);

	public void updateAddress(Map<String, Object> map);

	public void updateAddressId(Map<String, Object> map);

	public void updateAll(Map<String, Object> map);

	// 查询数据库表
	public List<EnterpriseRow> findEquals(Map<String, Object> map);

	public List<EnterpriseRow> findLike(Map<String, Object> map);

}
