package top.geomatics.gazetteer.service.address;

public class BatchDealExcelTest {
	public static void main(String[] args) {
	    String path = "D:/data/upload/test.xls";
		BatchDealExcel.batchDealExcel(path,"D:/data/upload/test2.xls");
	}

}
