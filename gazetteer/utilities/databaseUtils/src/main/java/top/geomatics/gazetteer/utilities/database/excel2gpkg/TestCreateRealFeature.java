package top.geomatics.gazetteer.utilities.database.excel2gpkg;

public class TestCreateRealFeature {

	public static void main(String[] args) {
//		String enterpriseFileName = "D:\\data\\enterprise\\enterprise_gazetteer.gpkg";
//		GeopackageReader reader3 = new GeopackageReader(enterpriseFileName);
//		for (int i = 1; i <= 4; i++) {
//			String oldName = "enterprise" + i;
//			String newName = "real_features" + i;
//			reader3.createRealFeature(oldName, newName);
//		}
//		reader3.close();
		String enterpriseFileName = "D:\\data\\enterprise1.gpkg";
		GeopackageReader reader3 = new GeopackageReader(enterpriseFileName);
		String oldName = "企业数据-统一社会信用代码1";
		String newName = "real_features";
		reader3.createRealFeature(oldName, newName);
		reader3.close();
	}

}
