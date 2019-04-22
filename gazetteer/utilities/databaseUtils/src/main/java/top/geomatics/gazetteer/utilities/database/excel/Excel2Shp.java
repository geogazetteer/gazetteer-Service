package top.geomatics.gazetteer.utilities.database.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class Excel2Shp {
	private static HSSFSheet sheet;
	public static Boolean excel2Shp(String xlsfile,String shppath) {
		Boolean flag=false;
		File file2=new File(xlsfile);
		if(!file2.exists()||!file2.isFile()) {
			return flag;
		}else {
			 POIFSFileSystem fs;
			 HSSFWorkbook wb;
			 HSSFRow row;
			    try {
			      InputStream is = new FileInputStream(xlsfile);
			      fs = new POIFSFileSystem(is);
			      wb = new HSSFWorkbook(fs);
			      sheet = wb.getSheetAt(0);
			      //获取总列数
			      int colNum = sheet.getRow(0).getPhysicalNumberOfCells();
			      //获取最后一行标号
			      int rowNum = sheet.getLastRowNum();
			 
			      List list = getExcelHeader();
			      //创建shape文件对象
			      File file = new File(shppath);
//			      if(!file.exists())file.createNewFile();
			      Map<String, Serializable> params = new HashMap<String, Serializable>();
			      params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
			      ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			      //定义图形信息和属性信息
			      SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			      tb.setCRS(DefaultGeographicCRS.WGS84);
//			      tb.setName("shapefile");
//			      tb.add("the_geom", Point.class);
			      for (int i = 0; i < list.size(); i++) {
			        Map<String, Object> map = (Map<String, Object>) list.get(i);
			        tb.add(map.get("name").toString(), (Class) map.get("type"));
			      }
			      ds.createSchema(tb.buildFeatureType());
			      //设置编码
			      Charset charset = Charset.forName("GBK");
			      ds.setCharset(charset);
			      //设置Writer
			      FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
			      //写下一条
			      SimpleFeature feature = null;
			      for (int i = 1; i < rowNum; i++) {
			        row = sheet.getRow(i);
			        feature = writer.next();
			        Map mapLonLat = new HashMap();
			        for (int j = 0; j < colNum; j++) {
			          HSSFCell cell = row.getCell(j);
			          Map<String, Object> mapFields = (Map<String, Object>) list.get(j);
			          String fieldName = mapFields.get("name").toString();
			          feature.setAttribute(fieldName, String.class);
			          if (fieldName.toLowerCase().equals("longitude") || fieldName.toLowerCase().equals("latitude")) {
			            mapLonLat.put(fieldName, cell.getRichStringCellValue().getString());
			          }
			        }
//			        feature.setAttribute("the_geom", new GeometryFactory().createPoint(new Coordinate((double) mapLonLat.get("longitude"), (double) mapLonLat.get("latitude"))));
			      }
			      writer.write();
			      writer.close();
			      ds.dispose();
			 
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			  }
		return flag;
}
	private static List<Map<String, Object>> getExcelHeader() {
	    List<Map<String, Object>> list = new ArrayList();
	    HSSFRow header = sheet.getRow(0);
	    HSSFRow value = sheet.getRow(1);
	    //获取总列数
	    int colNum = header.getPhysicalNumberOfCells();
	    for (int i = 0; i < colNum; i++) {
	      HSSFCell cellField = header.getCell(i);
	      HSSFCell cellvalue = value.getCell(i);
	      String fieldName = cellField.getRichStringCellValue().getString();
	      
	      Map<String, Object> map = new HashMap<String, Object>();
	      map.put("name", fieldName);
	      map.put("type", String.class);
	      list.add(map);
	    }
	    return list;
	  }
	
	

}
