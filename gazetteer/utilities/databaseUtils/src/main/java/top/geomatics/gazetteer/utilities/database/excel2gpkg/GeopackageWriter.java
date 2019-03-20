package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTS;
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
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressRecord;

/**
 * @author whudyj
 */
public class GeopackageWriter implements Runnable {
	private String gpkgFName = null;
	private String tableName = "enterprise";
	// 阻塞队列
	private BlockingQueue<AddressRecord> blockingQueue;

	private GeoPackage geopkg = null;
	// 原CGCS2000经纬度：EPSG:4490
	private CoordinateReferenceSystem crsSource = null;
	// 深圳高斯坐标系：EPSG:4547
	private CoordinateReferenceSystem crsTarget = null;

	private int count = 0;
	private SimpleFeatureBuilder featureBuilder = null;
	private FeatureEntry entry = null;
	private SimpleFeatureType sfType = null;
	private List<SimpleFeature> features = null;
	private GeometryFactory geometryFactory = null;

	private static final AtomicLong nextSerialNum = new AtomicLong();

	public GeopackageWriter(String gpkgFName, String tableName, BlockingQueue<AddressRecord> blockingQueue) {
		super();
		this.gpkgFName = gpkgFName;
		this.tableName = tableName;
		this.blockingQueue = blockingQueue;
	}

	private void initiateGpkg() {
		try {
			geopkg = new GeoPackage(new File(gpkgFName));
			geopkg.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void prepare() {
		try {
			crsSource = CRS.decode("EPSG:4490");
			crsTarget = CRS.decode("EPSG:4547");
		} catch (NoSuchAuthorityCodeException e1) {
			e1.printStackTrace();
		} catch (FactoryException e1) {
			e1.printStackTrace();
		}
		// 准备Geopackage文件
		initiateGpkg();

		try {
			entry = geopkg.feature(tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != entry) {
			tableName = entry.getTableName() + nextSerialNum;
		}
		entry = new FeatureEntry();
		entry.setDataType(Entry.DataType.Feature);
		entry.setSrid(4547);
		entry.setTableName(tableName);
		entry.setGeometryColumn("geometry");
		entry.setGeometryType(Geometries.POINT);

		// 属性结构
		sfType = createFeatureType();
		features = new ArrayList<SimpleFeature>();
		geometryFactory = JTSFactoryFinder.getGeometryFactory();

		featureBuilder = new SimpleFeatureBuilder(sfType);

	}

	private SimpleFeatureType createFeatureType() {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName(tableName);
		builder.setCRS(crsTarget); // <- Coordinate reference system

		// add attributes in order
		// 统一社会信用代码,企业名称,所在街道,法定代表人,JYCS,经度,纬度
		builder.add("code", String.class); // 统一社会信用代码
		builder.add("name", String.class); // 企业名称
		builder.add("street", String.class); // 所在街道
		builder.add("owner", String.class); // 法定代表人
		builder.add("address", String.class); // JYCS
		builder.add("geometry", Point.class);// 坐标，x,y
		// 增加的字段
		builder.add("status", Integer.class);
		builder.add("modifier", String.class);
		builder.add("update_date", Date.class);
		builder.add("update_address", String.class);
		builder.add("update_address_id", String.class);

		// build the type
		final SimpleFeatureType LOCATION = builder.buildFeatureType();

		return LOCATION;
	}

	/**
	 * 经纬度转换为高斯坐标
	 * 
	 * @param geom
	 * @return
	 */
	public Geometry lonlat2xy(Geometry geom) {
		try {

			// 投影转换
			MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
			return JTS.transform(geom, transform);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void close() {
		geopkg.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		boolean isRunning = true;
		System.out.println("准备写入数据，请耐心等待......");
		long startTime = System.currentTimeMillis(); // 获取开始时间
		prepare();

		while (isRunning) {
			AddressRecord record = null;
			try {
				record = this.blockingQueue.poll(5, TimeUnit.SECONDS);
				// System.out.println("当前线程：" + Thread.currentThread().getName() + "篮子中苹果个数：" +
				// this.blockingQueue.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (null != record && record.getValues().length > 6) {
				String str1 = record.getValues()[0];// code
				String str2 = record.getValues()[1];// name
				String str3 = record.getValues()[2];// street
				String str4 = record.getValues()[3];// owner
				String str5 = record.getValues()[4];// address
				String str6 = record.getValues()[5];// longitude
				String str7 = record.getValues()[6];// latitude

				featureBuilder.add(str1.trim());
				featureBuilder.add(str2.trim());
				featureBuilder.add(str3.trim());
				featureBuilder.add(str4.trim());
				featureBuilder.add(str5.trim());
				if (!str6.trim().isEmpty() && !str7.trim().isEmpty()) {
					double latitude = Double.parseDouble(str6);
					double longitude = Double.parseDouble(str7);

					Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
					Point point2 = (Point) lonlat2xy(point);
					Point point3 = geometryFactory
							.createPoint(new Coordinate(point2.getCoordinate().y, point2.getCoordinate().x));

					featureBuilder.add(point3);
				}

				SimpleFeature feature = featureBuilder.buildFeature(null);
				features.add(feature);

				count++;
			} else {
				isRunning = false;
				try {
					geopkg.add(entry, new ListFeatureCollection(sfType, features));
					geopkg.createSpatialIndex(entry);
				} catch (IOException e) {
					e.printStackTrace();
				}
				close();
				System.out.println("写入数据结束！记录数：" + count);
				long endTime = System.currentTimeMillis(); // 获取结束时间
				System.out.println("写入数据时间： " + (endTime - startTime) + "ms");
			}
		}
	}

}
