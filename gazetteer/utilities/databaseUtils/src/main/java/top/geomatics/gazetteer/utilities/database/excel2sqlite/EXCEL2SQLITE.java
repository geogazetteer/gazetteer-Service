package top.geomatics.gazetteer.utilities.database.excel2sqlite;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author whudyj 将excel文件转换为Sqlite数据库
 */
public class EXCEL2SQLITE {

	/**
	 * @param args 第一个参数为excel文件路径名，第二个参数为Sqlite文件路径名
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为2
		if (args.length != 1) {
			System.out.println("Usage:java -jar XXX.jar " + "H:\\projects\\gazetteer\\data\\深圳龙华地名地址\\地名地址\\dmdz.csv");
			System.exit(0);
		}

		// 设置篮子中苹果的最大个数
		BlockingQueue<GazetteerRow> blockingQueue = new ArrayBlockingQueue<GazetteerRow>(10000);
		//ExcelAddressReader aReader = new ExcelAddressReader(args[0], blockingQueue);
		XLSX2CSV aReader = new XLSX2CSV(args[0], blockingQueue,null,null,-1);
		ExcelAddressWriter aWriter = new ExcelAddressWriter(blockingQueue);

		aWriter.prepare();
		Thread th1 = new Thread(aReader);
		th1.setName("ReaderThread");

		Thread th2 = new Thread(aWriter);
		th2.setName("WriterThread");

		th1.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		th2.start();

	}

}
