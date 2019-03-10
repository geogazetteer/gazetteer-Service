/**
 *
 */
package top.geomatics.gazetteer.utilities.csv2sqlite;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.opencsv.CSVReader;

/**
 * @author whudyj
 */
public class AddressReader implements Runnable {
    public static volatile boolean isOver = false;// 线程结束标志

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
                // nextLine[] is an array of values from the line
                // 先输出内容到console
//				for (String str : nextLine) {
//					System.out.print(str + "\t");
//				}
//				System.out.println();

                // "63EEDE6BA4206A3AE0538CC0C0C07BB0",
                // "44030600960102T0117",
                // "44030600960102T0117",
                // "",
                // "广东省",
                // "深圳市",
                // "龙华区",
                // "民治街道",
                // "龙塘社区",
                // "",
                // "",
                // "上塘农贸建材市场",
                // "L25号铁皮房",
                // "",
                // "广东省深圳市龙华区民治街道龙塘社区上塘农贸建材市场L25号铁皮房",
                // "2016/12/12 23:46:17",
                // "0",
                // "2018/7/3 20:30:11"
                /*
                 * "ADDRESS_ID", "CODE", "BUILDING_ID", "HOUSE_ID", "PROVINCE", "CITY",
                 * "DISTRICT", "STREET", "COMMUNITY", "ROAD", "ROAD_NUM", "VILLAGE", "BUILDING",
                 * "FLOOR", "ADDRESS", "UPDATE_ADDRESS_DATE", "PUBLISH", "CREATE_ADDRESS_DATE"
                 */
                String valueString[] = new String[nextLine.length];
                for (int i = 0; i < nextLine.length; i++) {
                    valueString[i] = nextLine[i];
                }
                AddressRecord record = new AddressRecord(valueString.length, valueString);
                try {
                    this.blockingQueue.put(record);
                    count++;
                    //System.out.println(
                    //		"当前线程：" + Thread.currentThread().getName() + "篮子中苹果个数：" + this.blockingQueue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isOver = true; // 读取数据结束
        // 关门文件
        try {
            csvReader.close();
            System.out.println("读取数据结束！记录数：" + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
