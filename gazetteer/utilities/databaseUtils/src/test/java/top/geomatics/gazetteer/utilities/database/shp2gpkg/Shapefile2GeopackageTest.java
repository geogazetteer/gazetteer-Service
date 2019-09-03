package top.geomatics.gazetteer.utilities.database.shp2gpkg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Shapefile2GeopackageTest {
	private static final String shapefileName = "D:\\data\\数据整合_邓老师\\法人数据2\\法人数据（简化）1.shp";
	private static final String geopackageName = "H:\\temp\\data2\\企业法人数据.gpkg";
	private Map<String, String> settings = new HashMap<String, String>();
	
	@Test
	public void testExecute() {
		settings.put("NAME", "name_");
		settings.put("CZWCODE", "code_");
		settings.put("JYCS", "origin_address");
		settings.put("X", "longitude_");
		settings.put("Y", "latitude_");
		settings.put("JDNAME", "street_");
		settings.put("SQNAME", "community_");
		//settings.put("add_geometry", "true");
		Shapefile2Geopackage s2g = new Shapefile2Geopackage(shapefileName, geopackageName,settings);
		s2g.execute();
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
