/**
 * 
 */
package top.geomatics.gazetteer.model.main;

import top.geomatics.gazetteer.model.BasicGrid;
import top.geomatics.gazetteer.model.Building;
import top.geomatics.gazetteer.model.City;
import top.geomatics.gazetteer.model.Community;
import top.geomatics.gazetteer.model.District;
import top.geomatics.gazetteer.model.GazetteerName;
import top.geomatics.gazetteer.model.Province;
import top.geomatics.gazetteer.model.Town;

/**
 * @author whudyj
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GazetteerName name = new GazetteerName();

		Province gdProvince = new Province();
		name.setAdminName("广东省");
		gdProvince.setName(name);
		gdProvince.setCode("440000");

		City szCity = new City();
		name.setAdminName("深圳市");
		szCity.setName(name);
		szCity.setCode("440300");

		District lhDistrict = new District();
		name.setAdminName("龙华区");
		lhDistrict.setName(name);
		lhDistrict.setCode("440306");// 新的编码可能是440309
		// 44 03 06 008 004 14 00023
		Town dlTown = new Town();
		name.setAdminName("大浪街道");
		dlTown.setName(name);
		dlTown.setCode("440306008");

		Community tsCommunity = new Community();
		name.setAdminName("同胜社区");
		tsCommunity.setName(name);
		tsCommunity.setCode("440306008004");

		BasicGrid fBasicGrid = new BasicGrid();
		name.setAdminName("14");
		fBasicGrid.setName(name);
		fBasicGrid.setCode("44030600800414");

		Building abBuilding = new Building();
		name.setAdminName("23号");
		abBuilding.setName(name);
		abBuilding.setCode("4403060080041400023");

		// 6位的房屋编码没有

	}

}
