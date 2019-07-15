package top.geomatics.gazetteer.utilities.database.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.BuildingPositionRow;
import top.geomatics.gazetteer.model.MatcherResultRow;
import top.geomatics.gazetteer.utilities.database.BuildingAddress;

public class BatchDealExcel {

	public static List<MatcherResultRow> batchDealExcel(String excelPath, String outpath) {
		File file = new File(excelPath);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		InputStream in = null;
		XSSFWorkbook xssfWorkbook = null;
		XSSFSheet xssfSheet = null;
		try {
			in = new FileInputStream(file);
			xssfWorkbook = new XSSFWorkbook(in);
			xssfSheet = xssfWorkbook.getSheetAt(0);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (xssfSheet == null) {
			return null;
		}
		// 读取excel表格
		List<MatcherResultRow> addressRows = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			MatcherResultRow row = new MatcherResultRow();
			if (xssfRow.getLastCellNum() < 3) {
				// 日志
			}
			// 列索引
			int index = 0;
			if (null != xssfRow.getCell(index)) {
				row.setAddress(xssfRow.getCell(index).toString());
			}
			index++;
			if (null != xssfRow.getCell(index)) {
				row.setLongitude(Double.parseDouble(xssfRow.getCell(index).toString()));
			}
			index++;
			if (null != xssfRow.getCell(index)) {
				row.setLatitude(Double.parseDouble(xssfRow.getCell(index).toString()));
			}
			addressRows.add(row);
		}
		// 匹配处理
		// 正向处理，根据地址找坐标
		List<MatcherResultRow> rightRows = new ArrayList<>();
		for (MatcherResultRow row : addressRows) {
			if (row.getAddress() == null || row.getAddress().isEmpty()) {
				continue;
			}
			String addressString = row.getAddress();
			double longitude;
			double latitude;
			List<BuildingPositionRow> rows = BuildingAddress.getInstance().findCoordsByAddress(addressString);
			// 可能会匹配到0个或多个
			if (rows.size() < 1) {
				MatcherResultRow aRow = new MatcherResultRow();
				aRow.setAddress(addressString);
				rightRows.add(aRow);
			} else {
				for (BuildingPositionRow buildingRow : rows) {
					longitude = buildingRow.getLongitude();
					latitude = buildingRow.getLatitude();

					MatcherResultRow aRow = new MatcherResultRow();
					aRow.setAddress(addressString);
					aRow.setLongitude(longitude);
					aRow.setLatitude(latitude);
					rightRows.add(aRow);
				}
			}

		}
		// 反向处理，根据地址找坐标
		List<MatcherResultRow> leftRows = new ArrayList<>();
		for (MatcherResultRow row : addressRows) {
			if (row.getAddress() != null && !row.getAddress().isEmpty()) {
				continue;
			}
			String addressString = "！未匹配到地址！";
			double longitude = row.getLongitude();
			double latitude = row.getLatitude();
			List<AddressRow> rows = BuildingAddress.getInstance().findAddressByCoords(longitude, latitude);
			// 可能会匹配到0个或多个
			if (rows.size() < 1) {
				MatcherResultRow aRow = new MatcherResultRow();
				aRow.setAddress(addressString);
				aRow.setLongitude(longitude);
				aRow.setLatitude(latitude);
				leftRows.add(aRow);
			} else {
				for (AddressRow addressRow : rows) {
					addressString = addressRow.getAddress();

					MatcherResultRow aRow = new MatcherResultRow();
					aRow.setAddress(addressString);
					aRow.setLongitude(longitude);
					aRow.setLatitude(latitude);
					leftRows.add(aRow);
				}
			}
		}

		try {
			in.close();
			xssfWorkbook.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		xssfWorkbook = new XSSFWorkbook();
		xssfSheet = xssfWorkbook.createSheet("搜索完成的表格");
		XSSFRow xssfRow = xssfWorkbook.getSheet("搜索完成的表格").createRow(0);
		xssfRow.createCell(0).setCellValue("address");
		xssfRow.createCell(1).setCellValue("longitude");
		xssfRow.createCell(2).setCellValue("latitude");

		List<MatcherResultRow> allRows = new ArrayList<>();
		allRows.addAll(rightRows);
		allRows.addAll(leftRows);
		for (int i = 0; i < allRows.size(); i++) {
			MatcherResultRow mRow = allRows.get(i);
			xssfRow = xssfWorkbook.getSheet("搜索完成的表格").createRow(i);
			String address = mRow.getAddress();
			Double lon = mRow.getLongitude();
			Double lat = mRow.getLatitude();
			if (null != address)
				xssfRow.createCell(0).setCellValue(address);
			if (null != lon)
				xssfRow.createCell(1).setCellValue(lon);
			if (null != lat)
				xssfRow.createCell(2).setCellValue(lat);
		}
		File file2 = new File(outpath);
		if (!file2.exists()) {
			try {
				file2.createNewFile();
				OutputStream out = new FileOutputStream(file2);
				xssfWorkbook.write(out);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			xssfWorkbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allRows;
	}
}
