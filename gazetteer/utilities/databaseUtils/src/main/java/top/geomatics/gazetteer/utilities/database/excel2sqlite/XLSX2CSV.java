package top.geomatics.gazetteer.utilities.database.excel2sqlite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XLSX2CSV implements Runnable {
	private String fName;
	private BlockingQueue<GazetteerRow> blockingQueue;

	/**
	 * Uses the XSSF Event SAX helpers to do most of the work of parsing the Sheet
	 * XML, and outputs the contents as a (basic) CSV.
	 */
	private class SheetToCSV implements SheetContentsHandler {
		private boolean firstCellOfRow;
		private int currentRow = -1;
		private int currentCol = -1;
		private List<String> rowList = new ArrayList<String>();

		private void outputMissingRows(int number) {
			for (int i = 0; i < number; i++) {
				for (int j = 0; j < minColumns; j++) {
					output.append(',');
				}
				output.append('\n');
			}
		}

		@Override
		public void startRow(int rowNum) {
			// If there were gaps, output the missing rows
			outputMissingRows(rowNum - currentRow - 1);
			// Prepare for this row
			firstCellOfRow = true;
			currentRow = rowNum;
			currentCol = -1;
			rowList.clear();
		}

		@Override
		public void endRow(int rowNum) {
			// Ensure the minimum number of columns
			for (int i = currentCol; i < minColumns; i++) {
				output.append(',');
			}
			output.append('\n');

			if(-1 == rowNum) return;
			GazetteerRow row = new GazetteerRow();
			row.setAddress_id(rowList.get(0));
			row.setCode(rowList.get(1));
			row.setBuilding_id(rowList.get(2));
			row.setHouse_id(rowList.get(3));
			row.setProvince(rowList.get(4));
			row.setCity(rowList.get(5));
			row.setDistrict(rowList.get(6));
			row.setStreet(rowList.get(7));
			row.setCommunity(rowList.get(8));
			row.setRoad(rowList.get(9));
			row.setRoad_num(rowList.get(10));
			row.setVillage(rowList.get(11));
			row.setBuilding(rowList.get(12));
			row.setFloor(rowList.get(13));
			row.setAddress(rowList.get(14));
			row.setUpdate_address_date(rowList.get(15));
			row.setPublish(rowList.get(16));
			row.setCreate_address_date(rowList.get(17));
			
			try {
				blockingQueue.put(row);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void cell(String cellReference, String formattedValue, XSSFComment comment) {
			if (firstCellOfRow) {
				firstCellOfRow = false;
			} else {
				output.append(',');
			}

			// gracefully handle missing CellRef here in a similar way as XSSFCell does
			if (cellReference == null) {
				cellReference = new CellAddress(currentRow, currentCol).formatAsString();
			}

			// Did we miss any cells?
			int thisCol = (new CellReference(cellReference)).getCol();
			int missedCols = thisCol - currentCol - 1;
			for (int i = 0; i < missedCols; i++) {
				output.append(',');
			}
			currentCol = thisCol;

			rowList.add(formattedValue);

			// Number or string?
			try {
				// noinspection ResultOfMethodCallIgnored
				Double.parseDouble(formattedValue);
				output.append(formattedValue);
			} catch (NumberFormatException e) {
				output.append('"');
				output.append(formattedValue);
				output.append('"');
			}
		}
	}

	///////////////////////////////////////

	private final OPCPackage xlsxPackage;

	/**
	 * Number of columns to read starting with leftmost
	 */
	private final int minColumns;

	/**
	 * Destination for data
	 */
	private final PrintStream output;

	/**
	 * Creates a new XLSX -> CSV examples
	 *
	 * @param pkg        The XLSX package to process
	 * @param output     The PrintStream to output the CSV to
	 * @param minColumns The minimum number of columns to output, or -1 for no
	 *                   minimum
	 */
	public XLSX2CSV(String fName, BlockingQueue<GazetteerRow> blockingQueue,OPCPackage pkg, PrintStream output, int minColumns) {
		this.xlsxPackage = pkg;
		this.output = output;
		this.minColumns = minColumns;
		this.fName = fName;
		this.blockingQueue = blockingQueue;
	}

	/**
	 * Parses and shows the content of one sheet using the specified styles and
	 * shared-strings tables.
	 *
	 * @param styles           The table of styles that may be referenced by cells
	 *                         in the sheet
	 * @param strings          The table of strings that may be referenced by cells
	 *                         in the sheet
	 * @param sheetInputStream The stream to read the sheet-data from.
	 * 
	 * @exception java.io.IOException An IO exception from the parser, possibly from
	 *            a byte stream or character stream supplied by the application.
	 * @throws SAXException if parsing the XML data fails.
	 */
	public void processSheet(Styles styles, SharedStrings strings, SheetContentsHandler sheetHandler,
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

	/**
	 * Initiates the processing of the XLS workbook file to CSV.
	 *
	 * @throws IOException  If reading the data from the package fails.
	 * @throws SAXException if parsing the XML data fails.
	 */
	public void process() throws IOException, OpenXML4JException, SAXException {
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		int index = 0;
		while (iter.hasNext()) {
			try (InputStream stream = iter.next()) {
				String sheetName = iter.getSheetName();
				this.output.println();
				this.output.println(sheetName + " [index=" + index + "]:");
				processSheet(styles, strings, new SheetToCSV(), stream);
			}
			++index;
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Use:");
			System.err.println("  XLSX2CSV <xlsx file> [min columns]");
			return;
		}

		File xlsxFile = new File(args[0]);
		if (!xlsxFile.exists()) {
			System.err.println("Not found or not a file: " + xlsxFile.getPath());
			return;
		}

		int minColumns = -1;
		if (args.length >= 2)
			minColumns = Integer.parseInt(args[1]);

		// The package open is instantaneous, as it should be.
		try (OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ)) {
			XLSX2CSV xlsx2csv = new XLSX2CSV(null,null, p, System.out, minColumns);
			xlsx2csv.process();
		}
	}

	@Override
	public void run() {

		File xlsxFile = new File(fName);
		if (!xlsxFile.exists()) {
			System.err.println("Not found or not a file: " + xlsxFile.getPath());
			return;
		}

		int minColumns = -1;

		// The package open is instantaneous, as it should be.
		try (OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ)) {
			XLSX2CSV xlsx2csv = new XLSX2CSV(fName, blockingQueue,p, System.out, minColumns);
			try {
				xlsx2csv.process();
			} catch (OpenXML4JException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InvalidOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
