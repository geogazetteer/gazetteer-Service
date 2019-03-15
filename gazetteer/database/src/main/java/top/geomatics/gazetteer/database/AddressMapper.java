/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.util.List;

import top.geomatics.gazetteer.model.AddressRow;

/**
 * @author whudyj
 *
 */
public interface AddressMapper {
	//public List<AddressRow> selectAddressById(String id);
	//根据详细地址查询
	public List<AddressRow> selectByAddress(String address);
	//根据地理编码查询
	public List<AddressRow> selectByCode(String code);
	//模糊查询
	public List<AddressRow> selectByAddressLike(String keyword);
	//lucene
	public List<AddressRow> selectAddressBylucene(String keyword);
	//关键字
	public List<AddressRow> selectByKeyword(String keyword);
	
	public List<AddressRow> selectstreets();
	public List<AddressRow> selectcommunities();
	public List<AddressRow> selectbuildings();
	public List<AddressRow> selecthouses();
	

		
}
