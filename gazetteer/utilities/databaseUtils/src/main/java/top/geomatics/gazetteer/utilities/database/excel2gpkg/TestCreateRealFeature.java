package top.geomatics.gazetteer.utilities.database.excel2gpkg;

public class TestCreateRealFeature {

	public static void main(String[] args) {
		String enterpriseFileName = "D:\\data\\enterprise\\enterprise_gazetteer.gpkg";
		GeopackageReader reader3 = new GeopackageReader(enterpriseFileName);
		for (int i = 1; i <= 4; i++) {
			String oldName = "enterprise" + i;
			String newName = "real_features" + i;
			reader3.createRealFeature(oldName, newName);
		}
		reader3.close();
	}

}
