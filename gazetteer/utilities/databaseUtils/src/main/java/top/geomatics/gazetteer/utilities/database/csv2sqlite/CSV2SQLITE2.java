package top.geomatics.gazetteer.utilities.database.csv2sqlite;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 将CSV文件转换为Sqlite数据库，使用Mybatis
 * 
 * @author whudyj
 */
public class CSV2SQLITE2 {

	/**
	 * @param args 第一个参数为CSV文件路径名
	 */
	public static void main(String[] args) {
		// 判断命令行参数是否为1
		if (args.length != 1) {
			System.out.println("Usage:java -jar XXX.jar " + "H:\\projects\\gazetteer\\data\\csv\\dmdz.csv");
			System.exit(0);
		}

		// 设置篮子中苹果的最大个数
		BlockingQueue<AddressRecord> blockingQueue = new ArrayBlockingQueue<AddressRecord>(10000);
		AddressReader aReader = new AddressReader(args[0], blockingQueue);
		AddressWriter2 aWriter = new AddressWriter2(blockingQueue);
		AddressSchema schema = aReader.openFile();
		if (null == schema) {
			assert (false);
		}
		aWriter.prepare(schema);
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
