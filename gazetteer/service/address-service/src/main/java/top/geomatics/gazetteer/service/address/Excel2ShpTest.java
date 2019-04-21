package top.geomatics.gazetteer.service.address;

public class Excel2ShpTest {
	public static void main(String[] args) {
		String xlsfile="D:/data/upload/test2.xls";
		String shppath="D:/data/upload/test.shp";
		Excel2Shp.excel2Shp(xlsfile, shppath);
	}

}
