package top.geomatics.gazetteer.utilities.database.excel2sqlite;

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
public class ExcelAddressWriter implements Runnable {
	private BlockingQueue<GazetteerRow> blockingQueue;

	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();
	String tableName = "dmdz";

	public ExcelAddressWriter(BlockingQueue<GazetteerRow> blockingQueue) {
		super();
		this.blockingQueue = blockingQueue;
	}

	public void prepare() {
		mapper.dropTable(tableName);
		mapper.createAddressTable(tableName);
		map.put("tableName", tableName);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		prepare();
		boolean isRunning = true;
		while (isRunning) {
			GazetteerRow row = null;
			try {
				row = this.blockingQueue.poll(2, TimeUnit.SECONDS);
				// System.out.println("当前线程：" + Thread.currentThread().getName() + "篮子中苹果个数：" +
				// this.blockingQueue.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (null != row) {
				map.put("building_id", row.getBuilding_id());
				map.put("building", row.getBuilding());
				map.put("address", row.getAddress());
				mapper.insertAddress(map);
			} else {
				isRunning = false;
			}
		}
	}

}
