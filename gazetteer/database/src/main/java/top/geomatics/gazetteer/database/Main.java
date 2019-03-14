/**
 * 
 */
package top.geomatics.gazetteer.database;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.checkerframework.common.value.qual.StaticallyExecutable;

import top.geomatics.gazetteer.model.AddressRow;

/**
 * @author whudyj
 *
 */
public class Main {
	
	private static DatabaseHelper helper = new DatabaseHelper();

	private static void selectAddressById(){
	     SqlSession session=helper.getSession();
	     AddressMapper mapper=session.getMapper(AddressMapper.class);
	     try {
	    	 List<AddressRow> rows=   mapper.selectAddressById("4403060080011800284");
		     for (AddressRow row :rows) {
		    	 System.out.println(row.getAddress_id() + "\t" +
			    		 row.getCode() + "\t" +
			    		 row.getBuilding_id()  + "\t" +
			    		 row.getHouse_id()  + "\t" +
			    		 row.getProvince()  + "\t" +
			    		 row.getCity()  + "\t" + 
			    		 row.getDistrict()  + "\t" + 
			    		 row.getStreet()  + "\t" + 
			    		 row.getCommunity()  + "\t" + 
			    		 row.getRoad()  + "\t" + 
			    		 row.getRoad_num()  + "\t" + 
			    		 row.getVillage()  + "\t" + 
			    		 row.getBuilding_id()  + "\t" + 
			    		 row.getFloor()  + "\t" + 
			    		 row.getAddress()  + "\t" + 
			    		 row.getUpdate_address_date()  + "\t" + 
			    		 row.getPublish()  + "\t" + 
			    		 row.getCreate_address_date()  + "\t");
			}
	        session.commit();
	     } catch (Exception e) {
	         e.printStackTrace();
	         session.rollback();
	     }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		selectAddressById();
	}

}
