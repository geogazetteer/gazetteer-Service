package top.geomatics.gazetteer.utilities.database.csv2sqlite;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.opencsv.CSVReader;

/**
 * @author whudyj
 */
public class AddressReader implements Runnable {
	private String csvFName;// CSV文件名
	private BlockingQueue<AddressRecord> blockingQueue;

	private CSVReader csvReader = null;
	private long count = 0l;

	public AddressReader(String csvFName, BlockingQueue<AddressRecord> blockingQueue) {
		super();
		this.csvFName = csvFName;
		this.blockingQueue = blockingQueue;
	}

	public AddressSchema openFile() {
		AddressSchema schema = null;
		// 准备CSV文件
		try {
			csvReader = new CSVReader(new FileReader(csvFName));// 打开CSV文件
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// 第一行
		String[] fisrtLine = null;
		try {
			fisrtLine = csvReader.readNext();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fisrtLine != null) {
			String[] fields = new String[fisrtLine.length];
			for (int i = 0; i < fields.length; i++) {
				String fString = fisrtLine[i];
				fString = fString.toLowerCase();
				fString = fString.trim();
				fString += " string";
				fields[i] = fString;
			}
			schema = new AddressSchema(fields.length, fields);
		}
		return schema;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String[] nextLine = null;// CSV文件中的一行数据

		try {
			System.out.println("开始读取数据，请耐心等待......");
			while ((nextLine = csvReader.readNext()) != null) {
				String valueString[] = new String[nextLine.length];
				for (int i = 0; i < nextLine.length; i++) {
					valueString[i] = nextLine[i];
				}
				AddressRecord record = new AddressRecord(valueString.length, valueString);
				try {
					this.blockingQueue.put(record);
					count++;
					// System.out.println(
					// "当前线程：" + Thread.currentThread().getName() + "篮子中苹果个数：" +
					// this.blockingQueue.size());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 关门文件
		try {
			csvReader.close();
			System.out.println("读取数据结束！记录数：" + count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
