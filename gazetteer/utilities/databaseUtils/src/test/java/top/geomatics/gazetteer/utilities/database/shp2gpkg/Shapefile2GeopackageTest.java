package top.geomatics.gazetteer.utilities.database.shp2gpkg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Shapefile2GeopackageTest {
	private static final String shapefileName = "H:\\temp\\深圳龙华社康\\民营医院社康中心.shp";
	private static final String geopackageName = "H:\\temp\\深圳龙华社康\\民营医院社康中心.gpkg";
	private Map<String, String> settings = new HashMap<String, String>();
	
	@Test
	public void testExecute() {
//		settings.put("地址", "origin_address");
//		settings.put("X", "longitude_");
//		settings.put("Y", "latitude_");
//		Shapefile2Geopackage s2g = new Shapefile2Geopackage(shapefileName, geopackageName,settings);
//		s2g.execute();
	}
	@Test
	public void testGetFields() {
		Shapefile2Geopackage s2g = new Shapefile2Geopackage(shapefileName);
		List<String> fields = s2g.getFields();
		if (null != fields) {
			for (String fld :fields) {
				System.out.println(fld);
			}
		}
	}

}
