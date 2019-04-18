package top.geomatics.gazetteer.utilities.database.csv2sqlite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;

/**
 * @author whudyj
 */
public class AddressWriter2 implements Runnable {
	// 阻塞队列
	private BlockingQueue<AddressRecord> blockingQueue;
	// 标准地址数据库准备
	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();
	String sql_tablename = "dmdz";
	private static Long count = 0L;

	public AddressWriter2(BlockingQueue<AddressRecord> blockingQueue) {
		super();
		this.blockingQueue = blockingQueue;
	}

	public void prepare(AddressSchema schema) {
		mapper.dropTable(sql_tablename);
		mapper.createAddressTable(sql_tablename);
		session.commit();
		map.put("sql_tablename", sql_tablename);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		boolean isRunning = true;
		System.out.println("开始写入数据，请耐心等待......");
		long startTime = System.currentTimeMillis(); // 获取开始时间

		while (isRunning) {
			AddressRecord record = null;
			try {
				record = this.blockingQueue.poll(2, TimeUnit.SECONDS);
				// System.out.println("当前线程：" + Thread.currentThread().getName() + "篮子中苹果个数：" +
				// this.blockingQueue.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (null != record) {
//            	address_id ,
//                code ,
//                building_id ,
//                house_id ,
//                province ,
//                city ,
//                district ,
//                street ,
//                community ,
//                road ,
//                road_num ,
//                village ,
//                building ,
//                floor ,
//                address ,
//                update_address_date ,
//                publish ,
//                create_address_date
				map.put("address_id", record.getValues()[0]);
				map.put("code", record.getValues()[1]);
				map.put("building_id", record.getValues()[2]);
				map.put("house_id", record.getValues()[3]);
				map.put("province", record.getValues()[4]);
				map.put("city", record.getValues()[5]);
				map.put("district", record.getValues()[6]);
				map.put("street", record.getValues()[7]);
				map.put("community", record.getValues()[8]);
				map.put("road", record.getValues()[9]);
				map.put("road_num", record.getValues()[10]);
				map.put("village", record.getValues()[11]);
				map.put("building", record.getValues()[12]);
				map.put("floor", record.getValues()[13]);
				map.put("address", record.getValues()[14]);
				map.put("update_address_date", record.getValues()[15]);
				map.put("publish", record.getValues()[16]);
				map.put("create_address_date", record.getValues()[17]);
				mapper.insertAddress(map);
				count++;
			} else {
				isRunning = false;
				session.commit(true);
				System.out.println("写入数据结束！记录数：" + count);
				long endTime = System.currentTimeMillis(); // 获取结束时间
				System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
			}
		}
	}

}
