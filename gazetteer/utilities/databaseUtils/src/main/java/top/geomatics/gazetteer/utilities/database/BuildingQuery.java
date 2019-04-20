/**
 * 
 */
package top.geomatics.gazetteer.utilities.database;

import java.util.List;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.GeoPoint;
import top.geomatics.gazetteer.utilities.database.gpkg.GPKGReader;

/**
 * @author whudyj
 *
 */
public class BuildingQuery {
	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String BUILDING_GPKG_PATH = "building_db_file";
	private static final String BUILDING_TABLE_NAME = "building_table_name";
	public static String building_file = manager.getValue(BUILDING_GPKG_PATH);
	public static String table_name = manager.getValue(BUILDING_TABLE_NAME);

	public static GPKGReader gpkgReader = new GPKGReader(building_file);

	public static void close() {
		gpkgReader.close();
	}

	/**
	 * <em>根据建筑物编码计算质心点坐标</em><br>
	 * 
	 * @param czwCode
	 * @return
	 */
	public static GeoPoint query(String czwCode) {
		gpkgReader.preQuery(table_name);
		return gpkgReader.query(czwCode);
	}

	/**
	 * <em>根据坐标获得建筑物编码</em><br>
	 * 
	 * @param czwCode
	 * @return
	 */
	public static List<String> query(double x, double y) {
		gpkgReader.preQuery(table_name);
		return gpkgReader.query(x, y);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeoPoint point = query("4403060070051200001");
		System.out.println(point.getX() + "\t" + point.getY());
		List<String> codes = query(114.043736,22.649473);
		for (String str:codes) {
			System.out.println(str);
		}
	}

}
