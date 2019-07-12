package top.geomatics.gazetteer.service.address;

import org.junit.Test;

public class ZipFileUtilsTest {

	@Test
	public void testZipFiles() {
		String inFile = "G:\\data\\target\\企业数据-统一社会信用代码4.gpkg";
		String[] extensions = null;
		boolean recursive = false;
		String outPathStr = "G:\\data\\target\\企业数据-统一社会信用代码4.zip";
		//ZipFileUtils.zipFiles(inFile, extensions, recursive, outPathStr);
	}

	@Test
	public void testUnzip() {
		String zipFile = "G:\\data\\target\\企业数据-统一社会信用代码4.zip";
		String destDir = "G:\\temp";
		ZipFileUtils.unzip(zipFile, destDir);
	}

}
