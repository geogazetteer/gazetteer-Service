package top.geomatics.gazetteer.utilities.database.excel;

public class ShpZipTest {
	public static void main(String[] args) {
		 long start=System.currentTimeMillis(); 
	        String id="a6c870fd-9e51-4640-a4fe-ca0b0c131557";
	        String sourcePath = "D:/data/upload/"+id;
	        System.out.println(sourcePath);
	        String zipName="shp_"+id;
	        System.out.println(zipName);
	        
	        ShpZip.createCardImgZip(sourcePath,zipName);
	        long end=System.currentTimeMillis();
	        System.out.println(end-start);
	}

}
