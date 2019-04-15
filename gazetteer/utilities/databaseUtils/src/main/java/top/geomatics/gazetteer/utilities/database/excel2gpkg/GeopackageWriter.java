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
 * <em>数据转换到GeoPackage，并将坐标（原CGCS2000经纬度：EPSG:4490）转换到深圳高斯坐标系：EPSG:4547</em>
 * 
 * @author whudyj
 */
public class GeopackageWriter implements Runnable {
	private String gpkgFName = null;
	private String tableName = "enterprise";
	// 阻塞队列
	private BlockingQueue<AddressRecord> blockingQueue;

	private GeoPackage geopkg = null;
	// 原CGCS2000经纬度：EPSG:4490
	private static CoordinateReferenceSystem crsSource = null;
	// 深圳高斯坐标系：EPSG:4547
	private static CoordinateReferenceSystem crsTarget = null;

	{
		try {
			crsSource = CRS.decode("EPSG:4490");
			crsTarget = CRS.decode("EPSG:4547");
		} catch (NoSuchAuthorityCodeException e1) {
			e1.printStackTrace();
		} catch (FactoryException e1) {
			e1.printStackTrace();
		}
	}

	private int count = 0;
	private SimpleFeatureBuilder featureBuilder = null;
	private FeatureEntry entry = null;
	private SimpleFeatureType sfType = null;
	private List<SimpleFeature> features = null;
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	private static final AtomicLong nextSerialNum = new AtomicLong();

	/**
	 * <em>构造函数</em>
	 * 
	 * @param gpkgFName     String gpkg文件路径名
	 * @param tableName     String 表名
	 * @param blockingQueue BlockingQueue 线程队列，AddressRecord表示一行
	 */
	public GeopackageWriter(String gpkgFName, String tableName, BlockingQueue<AddressRecord> blockingQueue) {
		super();
		this.gpkgFName = gpkgFName;
		this.tableName = tableName;
		this.blockingQueue = blockingQueue;
	}

	/**
	 * <em>初始化GeoPackage</em>
	 * 
	 */
	private void initiateGpkg() {
		try {
			geopkg = new GeoPackage(new File(gpkgFName));
			geopkg.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <em>创建表，为写入数据准备</em>
	 * 
	 */
	public void prepare() {

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
		//entry.setSrid(4547);
		entry.setSrid(4326);
		entry.setTableName(tableName);
		entry.setGeometryColumn("geometry");
		entry.setGeometryType(Geometries.POINT);

		// 属性结构
		sfType = createFeatureType();
		features = new ArrayList<SimpleFeature>();
		featureBuilder = new SimpleFeatureBuilder(sfType);

	}

	/**
	 * @return SimpleFeatureType 返回创建的要素类型
	 */
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
		builder.add("longitude", Double.class); // 经度
		builder.add("latitude", Double.class); // 纬度
		builder.add("x", Double.class); // x坐标
		builder.add("y", Double.class); // y坐标
		builder.add("geometry", Point.class);// 坐标，x,y
		// 增加的字段
		builder.add("status", Integer.class);
		builder.add("modifier", String.class);
		builder.add("update_date", Date.class);
		builder.add("update_address", String.class);
		builder.add("update_address_id", String.class);

		return builder.buildFeatureType();
	}

	/**
	 * <em>经纬度转换为高斯坐标 </em>
	 * 
	 * @param geom Geometry 几何对象
	 * @return Geometry坐标转换后的几何对象
	 */
	public static Geometry lonlat2xy(Geometry geom) {
		try {

			// 投影转换
			MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
			return JTS.transform(geom, transform);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <em>经纬度转换为高斯坐标 </em>
	 * 
	 * @param longlat double [] 经度 纬度 坐标
	 * @return double [] x y坐标
	 */
	public static double[] lonlat2xy(double[] longlat) {
		double[] result = null;
		if (null == longlat || 2 > longlat.length || 0 != longlat.length % 2) {
			return result;
		}
		result = new double[longlat.length];
		for (int i = 0; i < longlat.length / 2; i++) {
			Point point = geometryFactory.createPoint(new Coordinate(longlat[2 * i], longlat[2 * i + 1]));
			Point point2 = (Point) lonlat2xy(point);
			result[2 * i] = point2.getY();
			result[2 * i + 1] = point2.getX();
		}
		return result;
	}

	/**
	 * <em>关闭GeoPackage文件</em>
	 */
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
					double longitude = Double.parseDouble(str6);
					double latitude = Double.parseDouble(str7);
					featureBuilder.add(longitude);
					featureBuilder.add(latitude);

					Point point = geometryFactory.createPoint(new Coordinate(latitude,longitude));
					Point point2 = (Point) lonlat2xy(point);
					Point point3 = geometryFactory
							.createPoint(new Coordinate(point2.getCoordinate().y, point2.getCoordinate().x));

					featureBuilder.add(point3.getCoordinate().x);
					featureBuilder.add(point3.getCoordinate().y);
					//featureBuilder.add(point3);
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
