package top.geomatics.gazetteer.utilities.database.csv2sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author whudyj
 */
public class AddressWriter implements Runnable {
    private String sqlFName;
    private BlockingQueue<AddressRecord> blockingQueue;

    // 准备SQLite数据库
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement pstmt = null;
    private long count = 0l;

    public AddressWriter(String sqlFName, BlockingQueue<AddressRecord> blockingQueue) {
        super();
        this.sqlFName = sqlFName;
        this.blockingQueue = blockingQueue;
    }

    public void prepare(AddressSchema schema) {
        if (null == schema)
            return;
        String sqlString = "";
        String tableName = "dmdz";
        String strConn = "jdbc:sqlite:" + sqlFName;// 数据库地址
        // create a database connection
        try {
            connection = DriverManager.getConnection(strConn);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // create table dmdz
        try {
            sqlString = "drop table if exists " + tableName;// 如果表已经存在，则先删除
            statement.executeUpdate(sqlString);
            sqlString = "create table " + tableName + "(";
            String insertString = "insert into " + tableName + " values(";
            for (int i = 0; i < schema.getFieldLength(); i++) {
                sqlString += schema.getFields()[i];
                insertString += "?";
                if (i < schema.getFieldLength() - 1) {
                    sqlString += ",";
                    insertString += ",";
                }
            }
            sqlString += ")";
            insertString += ")";
            statement.executeUpdate(sqlString);
            pstmt = connection.prepareStatement(insertString);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        boolean isRunning = true;
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
                try {
                    for (int i = 0; i < record.getFieldLength(); i++) {
                        pstmt.setString(i + 1, record.getValues()[i]);
                    }
                    pstmt.addBatch();
                    count++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                isRunning = false;
            }
        }
        // 提交数据，关闭数据库连接
        try {
            System.out.println("正在提交数据，请耐心等待......");
            pstmt.executeBatch();
            connection.commit();
            statement.close();
            pstmt.close();
            connection.close();
            System.out.println("提交数据结束！记录数：" + count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
