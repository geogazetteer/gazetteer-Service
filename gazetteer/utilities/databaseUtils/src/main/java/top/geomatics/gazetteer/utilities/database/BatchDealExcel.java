package top.geomatics.gazetteer.utilities.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class BatchDealExcel {

	public static Boolean batchDealExcel(String excelPath,String outpath) {
		System.out.println("开始处理");
		Boolean flag=false;
		File file=new File(excelPath);
		if(!file.exists()||!file.isFile()) {
			return flag;	
		}else {
			try {
			InputStream in=new FileInputStream(file);
			HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(in);
			HSSFSheet HSSFSheet=HSSFWorkbook.getSheetAt(0);
			if(HSSFSheet!=null) {
				List<Map<String, String>> list=new ArrayList<>();
				for (int rowNum = 1; rowNum <= HSSFSheet.getLastRowNum(); rowNum++) {
						Map<String, String> map=new HashMap<String, String>();
					    HSSFRow HSSFRow = HSSFSheet.getRow(rowNum);
					    if(HSSFRow.getCell(0)==null) {
					    	String longitude=HSSFRow.getCell(1).toString();
					    	String latitude=HSSFRow.getCell(2).toString();
					    	map.put("longitude",longitude);
					    	map.put("latitude", latitude);
////					    	String code=ControllerUtils.mapper.findAddressCodeBycoordinate(longitude, latitude);
////					    	String address=ControllerUtils.mapper.findAddressBycoordinate(code);
//					    	map.put("address", address);
//					    	list.add(map);				  		
//					    	System.out.println(JSON.toJSONString(list));
					    }else {
					    	String address=HSSFRow.getCell(0).toString();
					    	map.put("address", address);
//					    	String code=ControllerUtils.mapper.findLonLatCodeByaddress(address);
//					    	BuildingPositionRow bp=ControllerUtils.mapper.findLonLatByCode(code);
//					    	map.put("longitude", String .valueOf(bp.getLongitude()));
//					    	map.put("latitude", String.valueOf(bp.getLatitude()));
//					    	list.add(map);
//					    	System.out.println(JSON.toJSONString(list));
					    }
				}
				in.close();
				HSSFWorkbook=new HSSFWorkbook();
				HSSFSheet=HSSFWorkbook.createSheet("搜索完成的表格");
				HSSFRow HSSFRow=HSSFWorkbook.getSheet("搜索完成的表格").createRow(0);
				HSSFRow.createCell(0).setCellValue("address");
				HSSFRow.createCell(1).setCellValue("longitude");
				HSSFRow.createCell(2).setCellValue("latitude");
				int i=1;
				for(Map<String, String>map:list) {
					HSSFRow=HSSFWorkbook.getSheet("搜索完成的表格").createRow(i);
					HSSFRow.createCell(0).setCellValue(map.get("address"));
					HSSFRow.createCell(1).setCellValue(map.get("longitude"));
					HSSFRow.createCell(2).setCellValue(map.get("latitude"));
					i++;
				}
				File file2=new File(outpath);
				if(!file2.exists())file2.createNewFile();
				OutputStream out=new FileOutputStream(file2);
				HSSFWorkbook.write(out);
				out.close();
				flag=true;
			}
		}catch (Exception e) {
		}
		return flag;
	}
}
}
