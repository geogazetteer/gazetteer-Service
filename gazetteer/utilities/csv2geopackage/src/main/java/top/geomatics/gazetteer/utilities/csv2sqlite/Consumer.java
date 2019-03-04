/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2sqlite;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author whudyj
 *
 */
public class Consumer implements Runnable {
	private PreparedStatement pstmt = null;
	private ArrayList<AddressRecord> recordList;

	public Consumer(PreparedStatement pstmt, ArrayList<AddressRecord> recordList) {
		super();
		this.pstmt = pstmt;
		this.recordList = recordList;
	}

	public PreparedStatement getSqlStatement() {
		return this.pstmt;
	}

	public void setSqlStatement(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public ArrayList<AddressRecord> getRecord() {
		return this.recordList;
	}

	public void setRecord(ArrayList<AddressRecord> recordList) {
		this.recordList = recordList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		synchronized (this.recordList) {
			int size = this.recordList.size() ;
			while (this.recordList.size() == 0) {
				try {
					this.recordList.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//
			try {
				for (AddressRecord record : this.recordList) {
					for (int i = 0; i < record.getFieldLength(); i++) {
						pstmt.setString(i + 1, record.getValues()[i]);
					}
					pstmt.addBatch();
					size--;
					System.out.println(size);
				}
				//boolean isRemovedAll = this.recordList.removeAll(this.recordList);
				//assert (true == isRemovedAll);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
			this.recordList.notify();
		}
	}

}
