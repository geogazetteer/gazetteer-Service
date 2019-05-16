package top.geomatics.gazetteer.segment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.EnterpriseRow;

/**
 * <b>对企业法人数据库中的地址进行分词测试</b><br>
 * 
 * @author whudyj
 *
 */
public class EnterpriseSegment {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(EnterpriseSegment.class);

	// 输出结果文件路径
	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String ENTERPRISE_SEGMENT_RESULT_PATH = Messages.getString("EnterpriseSegment.0"); //$NON-NLS-1$

	// 数据库连接
	private static EnterpriseDatabaseHelper helper = null;
	private static SqlSession session = null;
	private static EnterpriseAddressMapper mapper = null;
	private static List<EnterpriseRow> rows = new ArrayList<EnterpriseRow>();

	private static CSVWriter csvWriter = null;

	/**
	 * <b>打开、查询数据库</b><br>
	 * 
	 */
	private static void openDatabase() {
		// 连接数据库
		helper = new EnterpriseDatabaseHelper();
		session = helper.getSession();
		mapper = session.getMapper(EnterpriseAddressMapper.class);
		// 查询
		query();
	}

	private static void closeDatabase() {
		session.close();
	}

	public static void outputResult() {

		// 输出结果文件
		String fileNString = manager.getValue(ENTERPRISE_SEGMENT_RESULT_PATH);
		fileNString = fileNString + File.separator + Messages.getString("EnterpriseSegment.1"); //$NON-NLS-1$
		File file = new File(fileNString);

		try {
			csvWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(file)).withSeparator('\t').withLineEnd(Messages.getString("EnterpriseSegment.2")) //$NON-NLS-1$
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			String logMsgString = String.format(Messages.getString("EnterpriseSegment.3"), fileNString); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		// 写数据文件
		String aLine[] = new String[2];
		// 表头
		aLine[0] = Messages.getString("EnterpriseSegment.4"); //$NON-NLS-1$
		aLine[1] = Messages.getString("EnterpriseSegment.5"); //$NON-NLS-1$
		csvWriter.writeNext(aLine);
		for (EnterpriseRow row : rows) {
			String address = row.getAddress();
			aLine[0] = address;
			List<String> segList = WordSegmenter.segment(address);
			String after = Messages.getString("EnterpriseSegment.6"); //$NON-NLS-1$
			for (int i = 0; i < segList.size(); i++) {
				after = after + segList.get(i);
				if (i < segList.size() - 1) {
					after = after + Messages.getString("EnterpriseSegment.7"); //$NON-NLS-1$
				}
			}
			aLine[1] = after;
			csvWriter.writeNext(aLine);
		}
		try {
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <b>查询法人数据库，结果保存在内存中</b><br>
	 * 
	 */
	private static void query() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Messages.getString("EnterpriseSegment.8"), Messages.getString("EnterpriseSegment.9")); //$NON-NLS-1$ //$NON-NLS-2$
		for (int i = 1; i < 5; i++) {
			String tableName = Messages.getString("EnterpriseSegment.10") + i; //$NON-NLS-1$
			map.put(Messages.getString("EnterpriseSegment.11"), tableName); //$NON-NLS-1$

			try {
				// SELECT address FROM enterprise + i
				List<EnterpriseRow> result = mapper.findEquals(map);
				rows.addAll(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		openDatabase();
		outputResult();
		closeDatabase();
	}

}
