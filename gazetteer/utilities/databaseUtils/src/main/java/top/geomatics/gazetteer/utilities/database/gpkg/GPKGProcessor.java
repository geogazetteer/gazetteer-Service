/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.gpkg;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.Entry.DataType;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.database.AddressEditorMapper;
import top.geomatics.gazetteer.database.EditorDatabaseHelper;
import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.IGazetteerConstant;
import top.geomatics.gazetteer.utilities.address.AddressGuessor;
import top.geomatics.gazetteer.utilities.database.BuildingAddress;
import top.geomatics.gazetteer.utilities.database.shp2gpkg.Shapefile2Geopackage;

/**
 * @author whudyj
 *
 */
public class GPKGProcessor {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(Shapefile2Geopackage.class);

	private static final String GEOMETRY_COLUMN_NAME = "the_geom";

	private String geopackageName;
	private GeoPackage geopkg = null;

	private Properties prop = null;
	private EditorDatabaseHelper helper_revision;
	private SqlSession session_revision;
	private AddressEditorMapper mapper_revision;

	public GPKGProcessor(String geopackageName, boolean isSqlite) {
		super();
		this.geopackageName = geopackageName;

		if (isSqlite) {
			initiateSqlite();
		} else {
			initiateGpkg();
		}

	}

	public GPKGProcessor(String geopackageName) {
		super();
		this.geopackageName = geopackageName;

		initiateGpkg();
	}

	public GPKGProcessor(GeoPackage geopkg) {
		super();
		this.geopkg = geopkg;
		this.geopackageName = geopkg.getFile().getName();
	}

	private boolean initiateSqlite() {
		// 数据库配置
		String driver = "org.sqlite.JDBC";
		String url = "jdbc:sqlite:" + this.geopackageName;
		String username = "";
		String password = "";

		prop = new Properties();
		prop.put("driver", driver);
		prop.put("url", url);
		prop.put("username", username);
		prop.put("password", password);

		helper_revision = new EditorDatabaseHelper(prop);
		session_revision = helper_revision.getSession();
		mapper_revision = session_revision.getMapper(AddressEditorMapper.class);

		return true;
	}

	private boolean initiateGpkg() {
		try {
			File gf = new File(this.geopackageName);
			if (!gf.exists()) {
				// 日志
				String logMsgString = String.format("geopackage文件：%s 不存在！", this.geopackageName);
				logger.error(logMsgString);
				return false;
			}
			geopkg = new GeoPackage(gf);
			// geopkg.init();
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("初始化geopackage文件：%s 失败！", this.geopackageName);
			logger.error(logMsgString);
			return false;
		}
		return true;
	}

	public boolean createTable(String typeName, String typeSpe) {
		SimpleFeatureType sType = null;
		try {
			sType = DataUtilities.createType(typeName, typeSpe);
		} catch (SchemaException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("创建表：%s (%s) 失败！", typeName, typeSpe);
			logger.error(logMsgString);
			return false;
		}
		FeatureEntry entry = new FeatureEntry();
		entry.setDataType(DataType.Feature);
		entry.setTableName(typeName);
		entry.setSrid(4490);
		ReferencedEnvelope bounds = new ReferencedEnvelope(IGazetteerConstant.LH_BBOX.getMinx(),
				IGazetteerConstant.LH_BBOX.getMaxx(), IGazetteerConstant.LH_BBOX.getMiny(),
				IGazetteerConstant.LH_BBOX.getMaxy(), null);
		entry.setBounds(bounds);
		entry.setGeometryColumn(GEOMETRY_COLUMN_NAME);

		try {
			FeatureEntry e2 = geopkg.feature(entry.getTableName());
			if (null == e2) {
				geopkg.create(entry, sType);
			}

		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("在 %s 中创建表失败！", this.geopackageName);
			logger.error(logMsgString);
			return false;
		}
		return true;
	}

	public boolean createAddressTable() {
		String typeName = "newAddress";
		String typeSpe = GEOMETRY_COLUMN_NAME + ":Point,";
		Field[] fileds = AddressEditorRow.class.getDeclaredFields();
		for (int i = 0; i < fileds.length; i++) {
			// add attributes in order
			Field fld = fileds[i];
			if (fld.getName().compareToIgnoreCase("fid") == 0) {
				continue;
			}
			typeSpe = typeSpe + fld.getName() + ":" + fld.getType().getName();
			if (i < fileds.length - 1) {
				typeSpe += ",";
			}
		}
		return createTable(typeName, typeSpe);
	}

	public void close() {
		geopkg.close();
	}

	public void updateSqlite(boolean force) {
		// 查询所有记录
		String sql_fields = "*";
		String sql_tablename = "dmdz_edit";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", sql_fields);
		map.put("sql_tablename", sql_tablename);
		List<AddressEditorRow> oldRows = mapper_revision.findEquals(map);

		Map<String, Object> map_t = new HashMap<String, Object>();
		map_t.put("sql_tablename", sql_tablename);
		for (AddressEditorRow oldRow : oldRows) {
			// 更新一条记录
			List<AddressEditorRow> newRows = new ArrayList<AddressEditorRow>();
			AddressEditorRow newRow = new AddressEditorRow();
			if (!modifyRecordByAddress(oldRow, newRow,force))
				continue;
			newRows.add(newRow);
			map_t.put("list", newRows);
			Integer updatedRows = mapper_revision.updateBatch(map_t);
		}

		session_revision.commit();

	}

	public boolean modifyRecordByAddress(AddressEditorRow oldRow, AddressEditorRow newRow,boolean force) {
		if (null == oldRow || null == newRow) {
			return false;
		}
		Integer oldFid = oldRow.getFid();
		newRow.setFid(oldFid);

		String originAddress = oldRow.getOrigin_address();
		Double longitude = oldRow.getLongitude_();
		Double latitude = oldRow.getLatitude_();
		// 补充街道信息
		String oldStreet = oldRow.getStreet_();
		String newStreet = null;
		if (null == oldStreet || oldStreet.isEmpty()) {

			if (null != originAddress && !originAddress.isEmpty()) {
				// 从原地址中推测
				newStreet = AddressGuessor.guessStreet(originAddress);
			}
		}
		// 补充街道信息
		String oldCommunity = oldRow.getCommunity_();
		String newCommunity = null;

		if (null == oldCommunity || oldCommunity.isEmpty()) {
			if (null != originAddress && !originAddress.isEmpty()) {
				// 从原地址中推测
				newCommunity = AddressGuessor.guessCommunity(originAddress);
			}
		}
		// 找到了就返回
		if (null != newStreet && !newStreet.isEmpty() && null != newCommunity && !newCommunity.isEmpty()) {
			newRow.setStreet_(newStreet);
			newRow.setCommunity_(newCommunity);
			return true;
		}
		if (force) {
			// 根据坐标来推测
			List<AddressRow> aRows = null;
			if (null != longitude && null != latitude) {
				aRows = BuildingAddress.getInstance().findAddressByCoords(longitude, latitude);
			}
			if (null != aRows && aRows.size() > 0) {
				// 推荐用第一条记录
				AddressRow arow = aRows.get(0);
				if (null == newStreet || newStreet.isEmpty()) {
					newStreet = arow.getStreet();
				}
				if (null == newCommunity || newCommunity.isEmpty()) {
					newCommunity = arow.getCommunity();
				}
			}
			if (null == newCommunity && null == newStreet) {
				return false;
			}
			newRow.setStreet_(newStreet);
			newRow.setCommunity_(newCommunity);
			return true;
		}
		
		return false;
	}

}
