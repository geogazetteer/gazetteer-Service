package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.util.List;

import top.geomatics.gazetteer.model.EnterpriseRow;

public class TestQuery1 {

	public static void main(String[] args) {
		String enterpriseFileName = "D:\\data\\enterprise\\enterprise_gazetteer.gpkg";
		GeopackageReader reader = new GeopackageReader(enterpriseFileName);
		reader.preQuery("real_features2");
		List<EnterpriseRow> rows = reader.query(503361.375, 2506786.75);
		for (EnterpriseRow row : rows) {
			System.out.println("address: " + row.getAddress() );
			System.out.println("x : " + row.getX() + "\ty: " + row.getY() );
		}
		reader.close();
	}

}
