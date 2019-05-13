/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.model.EnterpriseRow;

/**
 * <b>企业法人数据库操作的mybatis映射接口</b><br>
 * 
 * @author whudyj
 *
 */
public interface EnterpriseAddressMapper {

	/**
	 * <b>删除表</b><br>
	 * 
	 * @param tableName 表名
	 */
	public void dropTable(String tableName);

	/**
	 * <b>创建企业法人数据库表</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>标准地址表有17个字段</i><br>
	 * 
	 * @param tableName 表名
	 */
	public void createAddressTable(String tableName);

	/**
	 * <b>企业法人数据库表中添加记录</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的insert into语句</i><br>
	 * 
	 * @param map insert into语句中的参数
	 * @return 更新成功的记录个数
	 */
	public Integer insertAddress(Map<String, Object> map);

	/**
	 * <b>更新记录的Status字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的update语句</i><br>
	 * 
	 * @param map update语句中的参数
	 * @return 更新成功的记录个数
	 */
	public Integer updateStatus(Map<String, Object> map);

	/**
	 * <b>更新记录的Modifier字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的update语句</i><br>
	 * 
	 * @param map update语句中的参数
	 * @return 更新成功的记录个数
	 */
	public Integer updateModifier(Map<String, Object> map);

	/**
	 * <b>更新记录的Date字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的update语句</i><br>
	 * 
	 * @param map update语句中的参数
	 * @return 更新成功的记录个数
	 */
	public Integer updateDate(Map<String, Object> map);

	/**
	 * <b>更新记录的Address字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的update语句</i><br>
	 * 
	 * @param map update语句中的参数
	 * @return 更新成功的记录个数
	 */
	public Integer updateAddress(Map<String, Object> map);

	/**
	 * <b>更新记录的AddressId字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的update语句</i><br>
	 * 
	 * @param map update语句中的参数
	 * @return 更新成功的记录个数
	 */
	public Integer updateAddressId(Map<String, Object> map);

	/**
	 * <b>更新记录字段</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的update语句</i><br>
	 * 
	 * @param map update语句中的参数
	 * @return 更新成功的记录个数
	 */
	public Integer updateAll(Map<String, Object> map);

	/**
	 * <b>根据fid查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>必须在map参数中指定fid的值</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<EnterpriseRow> selectByFid(Map<String, Object> map);

	/**
	 * <b>根据一组fid查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>必须在map参数中指定fids的值</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<EnterpriseRow> selectByFids(Map<String, Object> map);

	/**
	 * <b>任意精确查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<EnterpriseRow> findEquals(Map<String, Object> map);

	/**
	 * <b>任意模糊查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<EnterpriseRow> findLike(Map<String, Object> map);

	/**
	 * <b>任意分页精确查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<EnterpriseRow> findPageEquals(Map<String, Object> map);

	/**
	 * <b>获得记录个数</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select count(*)语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 记录个数
	 */
	public Integer getCount(Map<String, Object> map);

}
