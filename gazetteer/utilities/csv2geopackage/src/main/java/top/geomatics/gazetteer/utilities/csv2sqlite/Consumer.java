/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2sqlite;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * @author whudyj
 *
 */
public class Consumer implements Runnable {
	private PreparedStatement pstmt = null;
	private BlockingQueue<AddressRecord> blockingQueue;

	public Consumer(PreparedStatement pstmt, BlockingQueue<AddressRecord> blockingQueue) {
		super();
		this.pstmt = pstmt;
		this.blockingQueue = blockingQueue;
	}

	public PreparedStatement getSqlStatement() {
		return this.pstmt;
	}

	public void setSqlStatement(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public BlockingQueue<AddressRecord> getRecord() {
		return this.blockingQueue;
	}

	public void setRecord(BlockingQueue<AddressRecord> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		while (true) {
			AddressRecord record = null;
			try {
				record = this.blockingQueue.take();
				System.out.println("还有苹果:" + this.blockingQueue.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (null != record) {
				try {
					for (int i = 0; i < record.getFieldLength(); i++) {
						pstmt.setString(i + 1, record.getValues()[i]);
					}
					pstmt.addBatch();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
