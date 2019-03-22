/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressRecord;
import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressSchema;

/**
 * <em>处理excel表中的一个sheet</em>
 * 
 * @author whudyj
 *
 */
public class ExcelSheetHandler implements SheetContentsHandler {
	private BlockingQueue<AddressRecord> blockingQueue;
	private boolean firstCellOfRow;
	private int currentRow = -1;
	private int currentCol = -1;
	private List<String> rowList = new ArrayList<String>();// 保存一行的数据
	private AddressSchema schema = null;
	private int length = 0;
	private int count = 0;

	/**
	 * <b>构造函数</b>
	 * 
	 * @param blockingQueue 线程队列，AddressRecord为sheet中的一行
	 */
	public ExcelSheetHandler(BlockingQueue<AddressRecord> blockingQueue) {
		super();
		this.blockingQueue = blockingQueue;
	}

	/**
	 * @return int 返回记录的个数
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return AddressSchema 返回sheet的表头数据
	 */
	public AddressSchema getSchema() {
		return schema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#
	 * startRow(int)
	 */
	@Override
	public void startRow(int rowNum) {
		// Prepare for this row
		firstCellOfRow = true;
		currentRow = rowNum;
		currentCol = -1;
		rowList.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#
	 * endRow(int)
	 */
	@Override
	public void endRow(int rowNum) {
		int len = rowList.size();
		if (len < 1) {
			return;
		}
		// 补齐空列
		for (int i = 0; i < length - len; i++) {
			rowList.add("");
		}

		if (0 == rowNum) // 第一行，表头
		{
			length = rowList.size();
			String fields[] = new String[len];
			for (int i = 0; i < len; i++) {
				fields[i] = rowList.get(i);
			}
			schema = new AddressSchema(len, fields);
			return;
		}
		// 将记录加入到阻塞队列中
		AddressRecord row = new AddressRecord();
		row.setFieldLength(length);
		String values[] = new String[length];
		for (int i = 0; i < length; i++) {
			values[i] = rowList.get(i);
		}
		row.setValues(values);
		try {
			blockingQueue.put(row);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		count++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#
	 * cell(java.lang.String, java.lang.String,
	 * org.apache.poi.xssf.usermodel.XSSFComment)
	 */
	@Override
	public void cell(String cellReference, String formattedValue, XSSFComment comment) {
		if (firstCellOfRow) {
			firstCellOfRow = false;
		}
		// gracefully handle missing CellRef here in a similar way as XSSFCell does
		if (cellReference == null) {
			cellReference = new CellAddress(currentRow, currentCol).formatAsString();
		}

		// Did we miss any cells?
		int thisCol = (new CellReference(cellReference)).getCol();
		int missedCols = thisCol - currentCol - 1;
		for (int i = 0; i < missedCols; i++) {
			rowList.add("");
		}
		currentCol = thisCol;

		rowList.add(formattedValue);
	}

}
