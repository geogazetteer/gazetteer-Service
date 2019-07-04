package top.geomatics.gazetteer.utilities.database;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataImpotTest {
	private DataImpot di = new DataImpot();
	String importXML;
	String upload_file_path;
	String download_file_path ="G:\\data\\target";

	boolean flag = false;
	@Test
	public void testDataImport() {
//		importXML = "POI数据_import.xml";
//		upload_file_path = "G:\\data\\user_POI数据";
//		flag = di.dataImport(importXML, upload_file_path, download_file_path);
//		assertTrue(flag);
		
		importXML = "企业数据-统一社会信用代码1_import.xml";
		upload_file_path = "G:\\data\\user_enterprise";
		flag = di.dataImport(importXML, upload_file_path, download_file_path);
		assertTrue(flag);
	}

}
