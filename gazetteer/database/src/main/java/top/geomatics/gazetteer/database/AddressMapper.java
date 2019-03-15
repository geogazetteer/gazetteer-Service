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
	public List<AddressRow> selectAddressById(String id) throws Exception;

		
}
