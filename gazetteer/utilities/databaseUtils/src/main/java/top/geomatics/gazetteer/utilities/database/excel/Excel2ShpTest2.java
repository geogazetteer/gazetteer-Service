package top.geomatics.gazetteer.utilities.database.excel;

import java.io.File;

public class Excel2ShpTest2 {
	public static void main(String[] args) {
		String id="a6c870fd-9e51-4640-a4fe-ca0b0c131557";
		String path="D:/data/upload/"+id;
		File file=new File(path);
		file.mkdir();
		
		String xlsfile="D:/data/upload/"+id+".xlsx";
		System.out.println(xlsfile);
		String shppath=path+"/shp_"+id+".shp";
        System.out.println(shppath);
        
        Excel2Shp.excel2Shp(xlsfile, shppath);
	}

}
