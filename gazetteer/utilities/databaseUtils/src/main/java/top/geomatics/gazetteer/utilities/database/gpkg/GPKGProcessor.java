/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.gpkg;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.Entry.DataType;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.IGazetteerConstant;
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

	public GPKGProcessor(String geopackageName) {
		super();
		this.geopackageName = geopackageName;

		initiateGpkg();
	}

	public GPKGProcessor(GeoPackage geopkg) {
		super();
		this.geopkg = geopkg;
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
		String typeSpe = GEOMETRY_COLUMN_NAME +":Point,";
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

}
