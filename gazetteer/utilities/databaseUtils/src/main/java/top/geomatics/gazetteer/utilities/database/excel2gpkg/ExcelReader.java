package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressRecord;
import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressSchema;

/**
 * <em>用于读取excel表格数据</em>
 * 
 * @author whudyj
 */
public class ExcelReader implements Runnable {
	private String csvFName;// excel文件名
	private BlockingQueue<AddressRecord> blockingQueue;
	private OPCPackage xlsxPackage = null;
	private AddressSchema schema = null;

	private ReadOnlySharedStringsTable strings = null;
	private XSSFReader xssfReader = null;
	private StylesTable styles = null;
	private XSSFReader.SheetIterator iter = null;
	private int count = 0;

	/**
	 * @return AddressSchema <b>返回excel第一个sheet的表头</b><br>
	 */
	public AddressSchema getSchema() {
		return schema;
	}

	/**
	 * @param csvFName      excel文件名
	 * @param blockingQueue 读数据线程队列，AddressRecord表示excel中的一行
	 */
	public ExcelReader(String csvFName, BlockingQueue<AddressRecord> blockingQueue) {
		super();
		this.csvFName = csvFName;
		this.blockingQueue = blockingQueue;
	}

	/**
	 * 
	 * @throws Exception 异常
	 */
	public void openFile() throws Exception {
		File xlsxFile = new File(csvFName);
		if (!xlsxFile.exists()) {
			System.err.println("Not found or not a file: " + xlsxFile.getPath());
			return;
		}
		this.xlsxPackage = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);

		strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		xssfReader = new XSSFReader(this.xlsxPackage);
		styles = xssfReader.getStylesTable();
		iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
	}

	/**
	 * @throws IOException        异常
	 * @throws OpenXML4JException 异常
	 * @throws SAXException       异常
	 */
	private void process() throws IOException, OpenXML4JException, SAXException {

		int index = 0;// 只考虑一个sheet的情况
		while (iter.hasNext()) {
			try (InputStream stream = iter.next()) {
				String sheetName = iter.getSheetName();
				System.out.println();
				System.out.println(sheetName + " [index=" + index + "]:");
				ExcelSheetHandler sheet = new ExcelSheetHandler(this.blockingQueue);
				processSheet(styles, strings, sheet, stream);
				this.schema = sheet.getSchema();
				count += sheet.getCount();
			}
			++index;
		}
	}

	/**
	 * @param styles           样式
	 * @param strings          SharedStrings
	 * @param sheetHandler     SheetContentsHandler
	 * @param sheetInputStream InputStream
	 * @throws IOException  异常
	 * @throws SAXException 异常
	 */
	private void processSheet(Styles styles, SharedStrings strings, SheetContentsHandler sheetHandler,
			InputStream sheetInputStream) throws IOException, SAXException {
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		try {
			XMLReader sheetParser = SAXHelper.newXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("开始读取数据，请耐心等待......");
		long startTime = System.currentTimeMillis(); // 获取开始时间
		try {
			process();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("读取数据结束！记录数：" + count);
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("读取数据时间： " + (endTime - startTime) + "ms");
	}

}
