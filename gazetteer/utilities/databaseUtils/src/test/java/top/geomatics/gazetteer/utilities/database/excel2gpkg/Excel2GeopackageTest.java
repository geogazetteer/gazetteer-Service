package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Excel2GeopackageTest {
	private static final String shapefileName = "H:\\temp\\企业数据-统一社会信用代码\\企业数据-统一社会信用代码1.xlsx";
	private static final String geopackageName = "H:\\temp\\企业数据-统一社会信用代码\\企业数据-统一社会信用代码1.gpkg";
	private Map<String, String> settings = new HashMap<String, String>();

	@Test
	public void testExecute() {
		settings.put("JYCS", "origin_address");
		settings.put("X", "longitude_");
		settings.put("Y", "latitude_");
		settings.put("buildGeometry", "true");
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
