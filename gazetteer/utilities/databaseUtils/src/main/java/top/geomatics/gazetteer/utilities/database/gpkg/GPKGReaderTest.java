/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.gpkg;

import top.geomatics.gazetteer.model.GeoPoint;

/**
 * @author whudyj
 *
 */
public class GPKGReaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GPKGReader reader = new GPKGReader("D:\\data\\LH_building_4490.gpkg");
		reader.preQuery("龙华区楼栋2000地理坐标系");
		GeoPoint point = reader.query("4403060090031200105");
		System.out.println(point.getX());
		System.out.println(point.getY());

		reader.close();
	}

}
