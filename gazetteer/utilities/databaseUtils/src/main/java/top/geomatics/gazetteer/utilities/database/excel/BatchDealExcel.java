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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
		HSSFWorkbook HSSFWorkbook = null;
		HSSFSheet HSSFSheet = null;
		try {
			in = new FileInputStream(file);
			HSSFWorkbook = new HSSFWorkbook(in);
			HSSFSheet = HSSFWorkbook.getSheetAt(0);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (HSSFSheet == null) {
			return null;
		}
		// 读取excel表格
		List<MatcherResultRow> addressRows = new ArrayList<>();
		for (int rowNum = 1; rowNum <= HSSFSheet.getLastRowNum(); rowNum++) {
			HSSFRow HSSFRow = HSSFSheet.getRow(rowNum);
			MatcherResultRow row = new MatcherResultRow();
			if (HSSFRow.getLastCellNum() < 3) {
				// 日志
			}
			// 列索引
			int index = 0;
			if (null != HSSFRow.getCell(index)) {
				row.setAddress(HSSFRow.getCell(index).toString());
			}
			index++;
			if (null != HSSFRow.getCell(index)) {
				row.setLongitude(Double.parseDouble(HSSFRow.getCell(index).toString()));
			}
			index++;
			if (null != HSSFRow.getCell(index)) {
				row.setLatitude(Double.parseDouble(HSSFRow.getCell(index).toString()));
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
			List<BuildingPositionRow> rows = BuildingAddress.findCoordsByAddress(addressString);
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
		// 反向处理，根据地址找坐标
		List<MatcherResultRow> leftRows = new ArrayList<>();
		for (MatcherResultRow row : addressRows) {
			if (row.getAddress() != null || !row.getAddress().isEmpty()) {
				continue;
			}
			String addressString = "";
			double longitude = row.getLongitude();
			double latitude = row.getLatitude();
			List<AddressRow> rows = BuildingAddress.findAddressByCoords(longitude, latitude);
			// 可能会找到多个
			if (rows.size() > 1) {
				addressString = rows.get(0).getAddress();
			}
			MatcherResultRow aRow = new MatcherResultRow();
			aRow.setAddress(addressString);
			aRow.setLongitude(longitude);
			aRow.setLatitude(latitude);
			leftRows.add(aRow);
		}

		try {
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		HSSFWorkbook = new HSSFWorkbook();
		HSSFSheet = HSSFWorkbook.createSheet("搜索完成的表格");
		HSSFRow HSSFRow = HSSFWorkbook.getSheet("搜索完成的表格").createRow(0);
		HSSFRow.createCell(0).setCellValue("address");
		HSSFRow.createCell(1).setCellValue("longitude");
		HSSFRow.createCell(2).setCellValue("latitude");

		List<MatcherResultRow> allRows = new ArrayList<>();
		allRows.addAll(rightRows);
		allRows.addAll(leftRows);
		for (int i = 0; i < allRows.size(); i++) {
			MatcherResultRow mRow = allRows.get(i);
			HSSFRow = HSSFWorkbook.getSheet("搜索完成的表格").createRow(i);
			HSSFRow.createCell(0).setCellValue(mRow.getAddress());
			HSSFRow.createCell(1).setCellValue(mRow.getLongitude());
			HSSFRow.createCell(2).setCellValue(mRow.getLatitude());
		}
		File file2 = new File(outpath);
		if (!file2.exists()) {
			try {
				file2.createNewFile();
				OutputStream out = new FileOutputStream(file2);
				HSSFWorkbook.write(out);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			HSSFWorkbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allRows;
	}
}
