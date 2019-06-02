/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.BuildingPositionRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

/**
 * <b>标准地址数据库操作的mybatis映射接口</b><br>
 * 
 * @author whudyj
 *
 */
public interface AddressMapper {

	/**
	 * <b>在dmdz表中根据id查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>表名固定为：dmdz</i><br>
	 * 
	 * @param id 数据库中记录的序号（标识号）
	 * @return 搜索结果集
	 */
	public List<AddressRow> selectById(Integer id);

	/**
	 * <b>在dmdz表中根据一组id查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>表名固定为：dmdz</i><br>
	 * 
	 * @param ids 数据库中的一组记录序号（标识号）
	 * @return 搜索结果集
	 */
	public List<AddressRow> selectByIds(List<Integer> ids);

	/**
	 * <b>选择dmdz表中有关词汇用于建立分词词典</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>表名固定为：dmdz</i><br>
	 * 
	 * @return 搜索结果集
	 * @throws Exception 异常
	 */
	public List<AddressRow> selectAddressForDictionary() throws Exception;
	
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
	
	/**
	 * <b>获得查询结果个数</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL语句： select count(*) from dmdz where address like 工商银行 </i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 记录个数
	 */
	public Integer getTotalLike(Map<String, Object> map);

	/**
	 * <b>任意精确查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<AddressRow> findEquals(Map<String, Object> map);

	/**
	 * <b>任意精确查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句，返回结果中只有id，address。主要用于lucene搜索</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<SimpleAddressRow> findSimpleEquals(Map<String, Object> map);

	/**
	 * <b>任意精确查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句，返回结果中只有与建筑物相关的信息。主要用于建筑物地址关联搜索</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<BuildingPositionRow> findBuildingEquals(Map<String, Object> map);

	/**
	 * <b>任意模糊查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<AddressRow> findLike(Map<String, Object> map);
	
	/**
	 * <b>任意模糊、分页查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<SimpleAddressRow> findSimpleLikePage(Map<String, Object> map);
	
	/**
	 * <b>任意模糊查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<SimpleAddressRow> findSimpleLike(Map<String, Object> map);

	/**
	 * <b>根据位置坐标查询</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中任意的select语句</i><br>
	 * 
	 * @param map select语句中的所有参数，必须包含有位置坐标（经纬度）信息。
	 * @return 搜索结果集
	 */
	public List<BuildingPositionRow> findBuildingByPoint(Map<String, Object> map);
	
	/**
	 * <b>根据街道或社区、关键词搜索</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中有条件select语句</i><br>
	 * 
	 * @param map select语句中的所有参数
	 * @return 搜索结果集
	 */
	public List<AddressRow> findAddressLike(Map<String, Object> map);

	/**
	 * <b>删除表</b><br>
	 * 
	 * @param tableName 表名
	 */
	public void dropTable(String tableName);

	/**
	 * <b>创建标准地址表</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>标准地址表有19个字段</i><br>
	 * 
	 * @param tableName 表名
	 */
	public void createAddressTable(String tableName);

	/**
	 * <b>标准地址表中添加记录</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的insert into语句</i><br>
	 * 
	 * @param map insert into语句中的参数
	 */
	public void insertAddress(Map<String, Object> map);

	/**
	 * <b>创建建筑物位置表</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>建筑物位置表有4个字段</i><br>
	 * 
	 * @param tableName 表名
	 */
	public void createBuildingPositionTable(String tableName);

	/**
	 * <b>建筑物位置表中添加记录</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的insert into语句</i><br>
	 * 
	 * @param map insert into语句中的参数
	 */
	public void insertBuildingPosition(Map<String, Object> map);

	/**
	 * <b>创建龙华区表</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>龙华区表有2个字段</i><br>
	 * 
	 * @param tableName 表名
	 */
	public void createDistrictTable(String tableName);

	/**
	 * <b>龙华区表中添加记录</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的insert into语句</i><br>
	 * 
	 * @param map insert into语句中的参数
	 */
	public void insertDistrict(Map<String, Object> map);

	/**
	 * <b>创建街道表</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>街道表有2个字段</i><br>
	 * 
	 * @param tableName 表名
	 */
	public void createStreetTable(String tableName);

	/**
	 * <b>街道表中添加记录</b><br>
	 * 
	 * <i>说明：</i><br>
	 * <i>相当于SQL中的insert into语句</i><br>
	 * 
	 * @param map insert into语句中的参数
	 */
	public void insertStreet(Map<String, Object> map);

}
