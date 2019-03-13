package top.geomatics.gazetteer.service.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

//将数据导出到excel表中
public class ExcelUtil {
	public String output() throws FileNotFoundException, IOException {
		// 测试，将数据导出到excel表
		FileOutputStream out = new FileOutputStream("数据表1.xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
		Sheet sheet1 = new Sheet(1, 0, ExcelPropertyIndexModel.class);
		sheet1.setSheetName("sheet1");
		List<ExcelPropertyIndexModel> data = new ArrayList<ExcelPropertyIndexModel>();
		for (int i = 0; i < 100; i++) {
			ExcelPropertyIndexModel item = new ExcelPropertyIndexModel();
			item.setAddress_id("id" + i);
			item.setCode("code" + i);
			item.setBuilding_id("building_id" + i);
			item.setHouse_id("house_id" + i);
			item.setProvince("广东省");
			item.setCity("深圳市");
			item.setDistrict("龙华区");
			data.add(item);
		}
		writer.write(data, sheet1);
		writer.finish();
		System.out.println("数据导出成功");
		return "";
	}

	public String input() throws FileNotFoundException, IOException {

		return "";
	}

}
