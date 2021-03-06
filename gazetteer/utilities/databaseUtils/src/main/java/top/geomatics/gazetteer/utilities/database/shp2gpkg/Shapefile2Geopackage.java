package top.geomatics.gazetteer.utilities.database.shp2gpkg;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.geometry.coordinate.ParamForPoint;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.utilities.database.AddressGuessor;
import top.geomatics.gazetteer.utilities.database.gpkg.GPKGProcessor;

/**
 * @author whudyj
 *
 */
public class Shapefile2Geopackage {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(Shapefile2Geopackage.class);

	private static final String SRID = "srid";

	private String shapefileName = "";
	private String geopackageName = "";
	private Map<String, String> settings = null;

	private static final String TABLE_NAME = "dmdz_edit";
	private static final Integer STATUS = 0;
	private static final String STAT_STRING = "status";
	private static final String CREATE_INDEX = "create_spatial_index";
	private boolean isCreateIndex = false;
	private static final String GUESS_FROM_GEOMETRY = "guess_from_geometry";
	private boolean isForce = false;
	private static final String ADD_GEOMETRY = "add_geometry";
	private boolean isAddGeometry = false;

	private static DataStore dataStore = null;
	private static GeoPackage geopkg = null;

	public Shapefile2Geopackage(String shapefileName, String geopackageName, Map<String, String> settings) {
		super();
		this.shapefileName = shapefileName;
		this.geopackageName = geopackageName;
		this.settings = settings;
	}

	public Shapefile2Geopackage(String shapefileName, String geopackageName) {
		super();
		this.shapefileName = shapefileName;
		this.geopackageName = geopackageName;
	}

	public Shapefile2Geopackage(String shapefileName) {
		super();
		this.shapefileName = shapefileName;
	}

	private boolean readShapefile() {
		File file = new File(this.shapefileName);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("url", file.toURI().toURL());
			dataStore = DataStoreFinder.getDataStore(map);
			if (dataStore instanceof ShapefileDataStore) {
				((ShapefileDataStore) dataStore).setCharset(Charset.forName("GBK"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("打开shapefile文件：%s 失败！", this.shapefileName);
			logger.error(logMsgString);
			return false;
		}
		return true;
	}

	private boolean initiateGpkg() {
		try {
			File gf = new File(this.geopackageName);
			if (gf.exists()) {
				// 日志
				String logMsgString = String.format("geopackage文件：%s 已经存在！", this.geopackageName);
				logger.error(logMsgString);
				return false;
			}
			geopkg = new GeoPackage(gf);
			geopkg.init();
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("初始化geopackage文件：%s 失败！", this.geopackageName);
			logger.error(logMsgString);
			return false;
		}
		if (this.settings != null && this.settings.containsKey(CREATE_INDEX)) {
			isCreateIndex = Boolean.parseBoolean(this.settings.get(CREATE_INDEX));
		}
		if (this.settings != null && this.settings.containsKey(GUESS_FROM_GEOMETRY)) {
			isForce = Boolean.parseBoolean(this.settings.get(GUESS_FROM_GEOMETRY));
		}
		if (this.settings != null && this.settings.containsKey(ADD_GEOMETRY)) {
			isAddGeometry = Boolean.parseBoolean(this.settings.get(ADD_GEOMETRY));
		}
		return true;
	}

	private void close() {
		geopkg.close();
	}

	/**
	 * @return SimpleFeatureType 返回创建的要素类型
	 */
	private SimpleFeatureType createFeatureType(SimpleFeatureType sfType) {
		// 空间参考
		CoordinateReferenceSystem crsSource = sfType.getCoordinateReferenceSystem();
		CoordinateReferenceSystem crsTarget = crsSource;
		String srid = "EPSG:4490";// 4547,4326
		if (settings != null && settings.containsKey(SRID)) {
			srid = settings.get(SRID);
		}
		try {
			crsTarget = CRS.decode(srid);
		} catch (Exception e1) {
			e1.printStackTrace();
			// 日志
			String logMsgString = String.format("解析空间参考：%s 失败！", srid);
			logger.error(logMsgString);
		}

		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		// builder.setName(sfType.getTypeName());
		builder.setName(TABLE_NAME);
		builder.setCRS(crsTarget); // <- Coordinate reference system

		// 原属性结构
		List<AttributeDescriptor> descriptors = sfType.getAttributeDescriptors();
		builder.addAll(descriptors);
		// 新增加的属性结构
		Field[] fileds = AddressEditorRow.class.getDeclaredFields();
		for (Field fld : fileds) {
			// add attributes in order
			if (fld.getName().compareToIgnoreCase("fid") == 0) {
				continue;
			}
			builder.add(fld.getName(), fld.getType());
		}

		return builder.buildFeatureType();
	}

	public boolean execute() {
		if (readShapefile() == false || dataStore == null) {
			return false;
		}
		if (initiateGpkg() == false || geopkg == null) {
			return false;
		}

		String typeName;
		FeatureSource<SimpleFeatureType, SimpleFeature> source = null;
		FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;
		SimpleFeatureCollection featureCollection = null;
		SimpleFeatureType sfType = null;
		SimpleFeatureType targetSFType = null;
		FeatureEntry entry = new FeatureEntry();
		try {
			typeName = dataStore.getTypeNames()[0];
			source = dataStore.getFeatureSource(typeName);
			features = source.getFeatures();
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("读取shapefile文件：%s 失败！", this.shapefileName);
			logger.error(logMsgString);
			return false;
		}
		sfType = features.getSchema();
		targetSFType = createFeatureType(sfType);
		if (features instanceof SimpleFeatureCollection) {
			featureCollection = (SimpleFeatureCollection) features;
		}
		entry.setDataType(Entry.DataType.Feature);
		entry.setSrid(4490);
		// entry.setTableName(sfType.getTypeName());
		entry.setTableName(TABLE_NAME);
		// 几何字段
		String geoNameString = sfType.getGeometryDescriptor().getName().toString();
		entry.setGeometryColumn(geoNameString);
		// 几何类型
		GeometryType gType = sfType.getGeometryDescriptor().getType();
		String geoType = gType.getName().toString();
		entry.setGeometryType(Geometries.getForName(geoType));

		List<AttributeDescriptor> descriptors = sfType.getAttributeDescriptors();
		Map<String, String> attrMap = new HashMap<String, String>();
		for (AttributeDescriptor desc : descriptors) {
			String fn = desc.getLocalName();
			if (settings != null && settings.containsKey(fn)) {
				attrMap.put(fn, settings.get(fn));
			}
		}

		
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(targetSFType);
		List<SimpleFeature> newFeas = new ArrayList<SimpleFeature>();

		SimpleFeatureIterator iterator = featureCollection.features();
		while (iterator.hasNext()) {
			SimpleFeature aFeature = iterator.next();
			SimpleFeature targetFeature = null;
			featureBuilder.addAll(aFeature.getAttributes());
			// 几何
			if (isAddGeometry) {
				Geometry geom = (Geometry) aFeature.getDefaultGeometry();
				if (geom != null && geom.isValid()) {
					Point p = geom.getCentroid();
					if (null != p) {
						featureBuilder.set("longitude_", p.getX());
						featureBuilder.set("latitude_", p.getY());
					}
				}
			}
			

			// 增加新的属性
			//Map<String, Object> kvp = new HashMap<String, Object>();
			for (String originField : attrMap.keySet()) {
				String targetField = attrMap.get(originField);
				Object fieldValue = aFeature.getAttribute(originField);

				featureBuilder.set(targetField, fieldValue);
				
				//kvp.put(targetField,fieldValue);
			}

			featureBuilder.set(STAT_STRING, STATUS);
			/*
			//推测、更新部分数据
			AddressEditorRow newRow = new AddressEditorRow();
			boolean flag = AddressGuessor.updateValue(kvp, newRow, isForce);
			//name
			String lfd_name = "name_";
			String val_name = null;
			if (kvp.containsKey(lfd_name)) {
				Object value = kvp.get(lfd_name);
				if (null != value) {
					val_name = (String)value;
				}
			}
			if (null == val_name || val_name.trim().isEmpty()) {
				//需要更新
				String new_name = newRow.getName_();
				if (null != new_name && !new_name.trim().isEmpty()) {
					featureBuilder.set(lfd_name, new_name);
				}
			}
			//code
			String lfd_code = "code_";
			String val_code = null;
			if (kvp.containsKey(lfd_code)) {
				Object value = kvp.get(lfd_code);
				if (null != value) {
					val_code = (String)value;
				}
			}
			if (null == val_code || val_code.trim().isEmpty()) {
				//需要更新
				String new_code = newRow.getCode_();
				if (null != new_code && !new_code.trim().isEmpty()) {
					featureBuilder.set(lfd_code, new_code);
				}
			}
			//code
			String lfd_street = "street_";
			String val_street = null;
			if (kvp.containsKey(lfd_street)) {
				Object value = kvp.get(lfd_street);
				if (null != value) {
					val_street = (String)value;
				}
			}
			if (null == val_street || val_street.trim().isEmpty()) {
				//需要更新
				String new_street = newRow.getStreet_();
				if (null != new_street && !new_street.trim().isEmpty()) {
					featureBuilder.set(lfd_street, new_street);
				}
			}
			//community
			String lfd_community = "community_";
			String val_community = null;
			if (kvp.containsKey(lfd_community)) {
				Object value = kvp.get(lfd_community);
				if (null != value) {
					val_community = (String)value;
				}
			}
			if (null == val_community || val_community.trim().isEmpty()) {
				//需要更新
				String new_community = newRow.getCommunity_();
				if (null != new_community && !new_community.trim().isEmpty()) {
					featureBuilder.set(lfd_community, new_community);
				}
			}
			//origin_address
			String lfd_origin_address = "origin_address";
			String val_origin_address = null;
			if (kvp.containsKey(lfd_origin_address)) {
				Object value = kvp.get(lfd_origin_address);
				if (null != value) {
					val_origin_address = (String)value;
				}
			}
			if (null == val_origin_address || val_origin_address.trim().isEmpty()) {
				//需要更新
				String new_origin_address = newRow.getOrigin_address();
				if (null != new_origin_address && !new_origin_address.trim().isEmpty()) {
					featureBuilder.set(lfd_origin_address, new_origin_address);
				}
			}
*/
			targetFeature = featureBuilder.buildFeature(null);
			newFeas.add(targetFeature);
		}
		try {
			geopkg.add(entry, new ListFeatureCollection(targetSFType, newFeas));
			if (isCreateIndex) {
				geopkg.createSpatialIndex(entry);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		iterator.close();
		////////////////////

		// 增加一个表newAddress
		GPKGProcessor processor = new GPKGProcessor(geopkg);
		processor.createAddressTable();

		close();
		//sqlite数据库更新
		if (isForce) {
			GPKGProcessor processor2 = new GPKGProcessor(geopackageName, true);
			processor2.updateSqlite(isForce);
		}
		
		return true;

	}

	public List<String> getFields() {
		if (readShapefile() == false || dataStore == null) {
			return null;
		}
		List<String> fields = new ArrayList<String>();
		String typeName;
		FeatureSource<SimpleFeatureType, SimpleFeature> source = null;
		FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;
		SimpleFeatureType sfType = null;
		try {
			typeName = dataStore.getTypeNames()[0];
			source = dataStore.getFeatureSource(typeName);
			features = source.getFeatures();
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("读取shapefile文件：%s 失败！", this.shapefileName);
			logger.error(logMsgString);
			return null;
		}
		sfType = features.getSchema();
		List<AttributeDescriptor> descriptors = sfType.getAttributeDescriptors();
		for (AttributeDescriptor desc : descriptors) {
			fields.add(desc.getLocalName());
		}

		return fields;

	}
}
