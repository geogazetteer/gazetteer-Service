package top.geomatics.gazetteer.utilities.database.excel;

public class Excel2ShpTest {
	public static void main(String[] args) {
		String xlsfile="D:/data/upload/test.xls";
		String shppath="D:/data/upload/test.shp";
		Excel2Shp.excel2Shp(xlsfile, shppath);
	}

}
