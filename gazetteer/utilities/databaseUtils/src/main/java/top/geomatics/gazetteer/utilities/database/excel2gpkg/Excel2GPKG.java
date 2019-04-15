package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressRecord;

/**
 * <b>excel表格数据转换到GeoPackage数据</b>
 * 
 * @author whudyj
 *
 */
public class Excel2GPKG {
	private static ExcelReader excelReader = null;
	private static GeopackageWriter gpkgWriter = null;

	/**
	 * <em>这是主方法</em>
	 * 
	 * @param args 第一个参数为excel文件路径名，第二个参数为GeoPackage文件路径名<br>
	 * 
	 *             <i>说明：</i>
	 *             <p>
	 *             <i> 这不是一个通用的程序。</i><br>
	 *             excel文件只有一个sheet，且格式固定为:<br>
	 *             <b>统一社会信用代码,企业名称,所在街道,法定代表人,JYCS,经度,纬度</b><br>
	 *             <em>实际使用时应修改代码</em>
	 *             </p>
	 * 
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java -jar XXX.jar D:\\data\\enterprise\\excel\\enterprise1.xlsx"
					+ "  D:\\data\\enterprise1.gpkg");
			System.exit(0);
		}
		String tableName = new File(args[0]).getName();
		tableName = tableName.substring(0, tableName.indexOf('.'));

		// 设置篮子中苹果的最大个数
		BlockingQueue<AddressRecord> blockingQueue = new ArrayBlockingQueue<AddressRecord>(1000);
		excelReader = new ExcelReader(args[0], blockingQueue);
		gpkgWriter = new GeopackageWriter(args[1], tableName, blockingQueue);

		try {
			excelReader.openFile();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		gpkgWriter.prepare();

		Thread th1 = new Thread(excelReader);
		th1.setName("ReaderThread");

		Thread th2 = new Thread(gpkgWriter);
		th2.setName("WriterThread");

		th1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		th2.start();
	}
}
