package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Excel2GeopackageTest {
	private static final String shapefileName = "D:\\data\\数据整合_邓老师\\poi.xlsx";
	private static final String geopackageName = "H:\\\\temp\\\\data2\\\\兴趣点（百度）数据.gpkg";
	private Map<String, String> settings = new HashMap<String, String>();

	@Test
	public void testExecute() {
		settings.put("NAME_CHN", "name_");
		//settings.put("CZWCODE", "code_");
		settings.put("ADDR_CHN", "origin_address");
		settings.put("X_COORD", "longitude_");
		settings.put("Y_COORD", "latitude_");
		settings.put("BUILD_GEOMETRY", "true");
		Excel2Geopackage s2g = new Excel2Geopackage(shapefileName, geopackageName, settings);
		s2g.execute();
	}

	@Test
	public void testGetFields() {
		Excel2Geopackage s2g = new Excel2Geopackage(shapefileName);
		List<String> fields = s2g.getFields();
		if (null != fields) {
			for (String fld : fields) {
				System.out.println(fld);
			}
		}
	}

}
