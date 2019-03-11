package top.geomatics.test;

import top.geomatics.utils.CsvUtil;
import top.geomatics.utils.DBUtil;

public class Test {
	public static void main(String[] args) throws Exception {
//		SqlliteUtil sqlliteUtil = new SqlliteUtil();
//		List list = sqlliteUtil.selectByKeyword("dmdz", "上塘农贸建材市场");
//		// 使用阿里巴巴的fastjson
//		String json = JSON.toJSONString(list);
//		System.out.println(json);
//		ExcelUtil excelUtil = new ExcelUtil();
//		excelUtil.write();

//		String csvFilePath = "write.csv";
		CsvUtil csvUtil = new CsvUtil();
		DBUtil dbUtil = new DBUtil();
//		csvUtil.write();
//		Connection con = dbUtil.getConnection();
//		csvUtil.readCsv(csvFilePath, con);
	}

}
