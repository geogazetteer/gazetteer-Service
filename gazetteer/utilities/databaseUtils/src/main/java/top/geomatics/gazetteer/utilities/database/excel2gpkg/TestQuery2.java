package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.util.List;

import top.geomatics.gazetteer.model.BuildingRow;

public class TestQuery2 {

	public static void main(String[] args) {
		String addressFileName = "D:\\data\\LH_gazetteer.gpkg";
		GeopackageReader reader2 = new GeopackageReader(addressFileName);
		reader2.preQuery("龙华区楼栋");
		List<BuildingRow> rows2 = reader2.query2(503361.375, 2506786.75);
		for (BuildingRow row : rows2) {
			System.out.println(row.getBguid());
			System.out.println(row.getCzwCode());
		}
		reader2.close();

	}

}
