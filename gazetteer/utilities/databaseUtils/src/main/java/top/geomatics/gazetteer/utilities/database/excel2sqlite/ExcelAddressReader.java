package top.geomatics.gazetteer.utilities.database.excel2sqlite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

/**
 * @author whudyj
 */
public class ExcelAddressReader extends AnalysisEventListener<Object> implements Runnable {
	private String excelFName = "";
	private BlockingQueue<GazetteerRow> blockingQueue;

	public ExcelAddressReader(String excelFName, BlockingQueue<GazetteerRow> blockingQueue) {
		super();
		this.excelFName = excelFName;
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void invoke(Object object, AnalysisContext context) {
		try {
			blockingQueue.put((GazetteerRow)object);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// 打开excel文件
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(excelFName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			// 解析每行结果在listener中处理
			ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX,null, this);
			System.out.println("开始读取数据，请耐心等待......");
			excelReader.read(new Sheet(2, 1, GazetteerRow.class));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
