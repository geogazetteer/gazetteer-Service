package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.utilities.database.AddressGuessor;
import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressRecord;
import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressSchema;
import top.geomatics.gazetteer.utilities.database.gpkg.GPKGProcessor;

/**
 * @author whudyj
 *
 */
public class Excel2Geopackage {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(Excel2Geopackage.class);

	private static final String SRID = "srid";
	private static final String BUILD_GEOMETRY = "buildGeometry";

	private static final String CREATE_INDEX = "create_spatial_index";
	private boolean isCreateIndex = false;
	private static final String GUESS_FROM_GEOMETRY = "guess_from_geometry";
	private boolean isForce = false;

	private String excelfileName = "";
	private String geopackageName = "";
	private Map<String, String> settings = null;

	private OPCPackage xlsxPackage = null;
	private ReadOnlySharedStringsTable strings = null;
	private XSSFReader xssfReader = null;
	private StylesTable styles = null;
	private XSSFReader.SheetIterator iter = null;
	private AddressSchema schema = null;
	private List<AddressRecord> records = new ArrayList<AddressRecord>();

	private static GeoPackage geopkg = null;
	private String tablename = "";
	private static final String TABLE_NAME = "dmdz_edit";
	private static final Integer STATUS = 0;
	private static final String STAT_STRING = "status";
	private String geoNameString = "the_geom";
	private boolean isGeometry = false;
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	public Excel2Geopackage(String shapefileName, String geopackageName, Map<String, String> settings) {
		super();
		this.excelfileName = shapefileName;
		this.geopackageName = geopackageName;
		this.settings = settings;
	}

	public Excel2Geopackage(String shapefileName, String geopackageName) {
		super();
		this.excelfileName = shapefileName;
		this.geopackageName = geopackageName;
	}

	public Excel2Geopackage(String shapefileName) {
		super();
		this.excelfileName = shapefileName;
	}

	private boolean readExcelfile() {
		File xlsxFile = new File(this.excelfileName);
		String fn = xlsxFile.getName();
		this.tablename = fn.substring(0, fn.lastIndexOf('.'));
		if (!xlsxFile.exists()) {
			System.err.println("Not found or not a file: " + xlsxFile.getPath());
			// 日志
			String logMsgString = String.format("打开文件：%s 失败！", this.excelfileName);
			logger.error(logMsgString);
			return false;
		}
		try {
			this.xlsxPackage = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
			strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
			xssfReader = new XSSFReader(this.xlsxPackage);
			styles = xssfReader.getStylesTable();
			iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		} catch (Exception e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format("读取文件：%s 失败！", this.excelfileName);
			logger.error(logMsgString);
			return false;
		}

		int index = 0;// 只考虑一个sheet的情况
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			String sheetName = iter.getSheetName();
			System.out.println();
			System.out.println(sheetName + " [index=" + index + "]:");
			ExcelSheetHandler2 sheet = new ExcelSheetHandler2(this.records);
			try {
				processSheet(styles, strings, sheet, stream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.schema = sheet.getSchema();

			++index;
			break;// 只考虑一个sheet
		}

		return true;
	}

	/**
	 * @param styles           样式
	 * @param strings          SharedStrings
	 * @param sheetHandler     SheetContentsHandler
	 * @param sheetInputStream InputStream
	 * @throws IOException  异常
	 * @throws SAXException 异常
	 */
	private void processSheet(Styles styles, SharedStrings strings, SheetContentsHandler sheetHandler,
			InputStream sheetInputStream) throws IOException, SAXException {
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		try {
			XMLReader sheetParser = SAXHelper.newXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		} catch (ParserConfigurationException e) {
			// 日志
			String logMsgString = "SAX parser appears to be broken - " + e.getMessage();
			logger.error(logMsgString);
			throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
		}
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
		return true;
	}

	private void close() {
		geopkg.close();
	}

	/**
	 * @return SimpleFeatureType 返回创建的要素类型
	 */
	private SimpleFeatureType createFeatureType() {
		// 空间参考
		CoordinateReferenceSystem crsTarget = null;
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
		// builder.setName(this.tablename);
		builder.setName(TABLE_NAME);
		builder.setCRS(crsTarget); // <- Coordinate reference system

		// 原属性结构
		for (String fld : this.schema.getFields()) {
			builder.add(fld, String.class);
		}
		// 几何字段
		if (settings != null && settings.containsKey(BUILD_GEOMETRY)) {
			String bg = settings.get(BUILD_GEOMETRY);
			isGeometry = Boolean.parseBoolean(bg);
		}
		if (isGeometry) {
			builder.add(geoNameString, Geometry.class);
		}
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
		if (readExcelfile() == false) {
			return false;
		}
		if (initiateGpkg() == false || geopkg == null) {
			return false;
		}
		FeatureEntry entry = new FeatureEntry();
		SimpleFeatureType targetSFType = createFeatureType();
		entry.setDataType(Entry.DataType.Feature);
		entry.setSrid(4490);
		// entry.setTableName(this.tablename);
		entry.setTableName(TABLE_NAME);
		// 几何字段
		if (isGeometry) {
			entry.setGeometryColumn(geoNameString);
			entry.setGeometryType(Geometries.POINT);
		}
		Map<Integer, String> attrMap = new HashMap<Integer, String>();
		for (int i = 0; i < this.schema.getFields().length; i++) {
			String fn = this.schema.getFields()[i];
			if (settings != null && settings.containsKey(fn)) {
				attrMap.put(i, settings.get(fn));
			}
		}

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(targetSFType);
		List<SimpleFeature> newFeas = new ArrayList<SimpleFeature>();
		for (AddressRecord record : records) {
			for (String value : record.getValues()) {
				featureBuilder.add(value.trim());
			}
			// 增加新的属性
			Double x = null;
			Double y = null;
			//Map<String, Object> kvp = new HashMap<String, Object>();
			for (Integer index : attrMap.keySet()) {
				String targetField = attrMap.get(index);
				String fieldValue = record.getValues()[index].trim();
				Object value = fieldValue;
				if (targetField.compareToIgnoreCase("longitude_") == 0) {
					if (!fieldValue.isEmpty()) {
						x = Double.parseDouble(fieldValue);
					} else {
						x = null;
					}
					value = x;
				}
				if (targetField.compareToIgnoreCase("latitude_") == 0) {
					if (!fieldValue.isEmpty()) {
						y = Double.parseDouble(fieldValue);
					} else {
						y = null;
					}
					value = y;
				}

				featureBuilder.set(targetField, value);
				//kvp.put(targetField,value);
			}
			featureBuilder.set(STAT_STRING, STATUS);
			if (isGeometry && x != null && y != null) {
				Point point = geometryFactory.createPoint(new Coordinate(x, y));
				featureBuilder.set(geoNameString, point);
			}
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
			
			SimpleFeature targetFeature = featureBuilder.buildFeature(null);
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
		////////////////////

		// 增加一个表newAddress
		GPKGProcessor processor = new GPKGProcessor(geopkg);
		processor.createAddressTable();

		close();

		// sqlite数据库更新
		if (isForce) {
			GPKGProcessor processor2 = new GPKGProcessor(geopackageName, true);
			processor2.updateSqlite(isForce);
		}

		return true;

	}

	public List<String> getFields() {
		if (readExcelfile() == false) {
			return null;
		}
		List<String> fields = new ArrayList<>();
		for (String str : this.schema.getFields()) {
			fields.add(str);
		}

		return fields;

	}
}
