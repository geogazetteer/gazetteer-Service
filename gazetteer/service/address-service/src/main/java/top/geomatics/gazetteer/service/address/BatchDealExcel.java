package top.geomatics.gazetteer.service.address;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import top.geomatics.gazetteer.model.BuildingPositionRow;

//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
public class BatchDealExcel {

	public static Boolean batchDealExcel(String excelPath) {
		Boolean flag=false;
		File file=new File(excelPath);
		if(!file.exists()||!file.isFile()) {
			return flag;
		}else {
			try {
			InputStream in=new FileInputStream(file);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
			XSSFSheet xssfSheet=xssfWorkbook.getSheetAt(0);
			if(xssfSheet!=null) {
				List<Map<String, String>> list=new ArrayList<>();
				for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
						Map<String, String> map=new HashMap<String, String>();
					    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					    if(xssfRow.getCell(0)==null) {
					    	String longitude=xssfRow.getCell(1).toString();
					    	String latitude=xssfRow.getCell(2).toString();
					    	map.put("longtitude",longitude);
					    	map.put("latitude", latitude);
					    	String code=ControllerUtils.mapper.findAddressCodeBycoordinate(Double.valueOf(longitude), Double.valueOf(latitude));
					    	String address=ControllerUtils.mapper.findAddressBycoordinate(code);
					    	map.put("address", address);
					    	list.add(map);
					    }else {
					    	String address=xssfRow.getCell(0).toString();
					    	map.put("address", address);
					    	String code=ControllerUtils.mapper.findLonLatCodeByaddress(address);
					    	BuildingPositionRow bp=ControllerUtils.mapper.findLonLatByCode(code);
					    	map.put("longitude", String .valueOf(bp.getLongitude()));
					    	map.put("latitude", String.valueOf(bp.getLatitude()));
					    	list.add(map);
					    }
				}
				in.close();
				OutputStream out=new FileOutputStream(file);
				xssfWorkbook=new XSSFWorkbook();
				xssfSheet=xssfWorkbook.createSheet("搜索完成的表格");
				XSSFRow xssfRow=xssfWorkbook.getSheet("搜索完成的表格").createRow(0);
				xssfRow.createCell(0).setCellValue("address");
				xssfRow.createCell(1).setCellValue("longitude");
				xssfRow.createCell(2).setCellValue("latitude");
				int i=1;
				for(Map<String, String>map:list) {
					xssfRow=xssfWorkbook.getSheet("搜索完成的表格").createRow(i);
					xssfRow.createCell(0).setCellValue(map.get("address"));
					xssfRow.createCell(1).setCellValue(map.get("longitude"));
					xssfRow.createCell(2).setCellValue(map.get("latitude"));
					i++;
				}
				xssfWorkbook.write(out);
				out.close();
				flag=true;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}
}}
