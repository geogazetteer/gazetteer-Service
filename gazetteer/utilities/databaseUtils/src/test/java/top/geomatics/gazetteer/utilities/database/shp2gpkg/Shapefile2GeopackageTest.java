package top.geomatics.gazetteer.utilities.database.shp2gpkg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Shapefile2GeopackageTest {
	private static final String shapefileName = "D:\\data\\数据整合_邓老师\\兴趣点\\兴趣点.shp";
	private static final String geopackageName = "H:\\temp\\data2\\兴趣点（部件）数据.gpkg";
	private Map<String, String> settings = new HashMap<String, String>();
	
	@Test
	public void testExecute() {
		settings.put("Name", "name_");
		//settings.put("CZWCODE", "code_");
		//settings.put("ADDR_CHN", "origin_address");
		//settings.put("X_COORD", "longitude_");
		//settings.put("Y_COORD", "latitude_");
		settings.put("add_geometry", "true");
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
