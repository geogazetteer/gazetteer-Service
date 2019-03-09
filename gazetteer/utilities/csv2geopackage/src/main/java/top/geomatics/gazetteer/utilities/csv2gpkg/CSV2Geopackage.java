/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2gpkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
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

import com.google.common.collect.Sets;
import com.opencsv.CSVReader;

/**
 * @author whudyj 将CSV文件转换为GeoPackage文件
 */
public class CSV2Geopackage {
	private static String csvFName = null;
	private static String gpkgFName = null;
	private static CSVReader csvReader = null;
	private static GeoPackage geopkg = null;
	// 原CGCS2000经纬度：EPSG:4490
	private static CoordinateReferenceSystem crsSource = null;
	// 深圳高斯坐标系：EPSG:4547
	private static CoordinateReferenceSystem crsTarget = null;
	private static String[] firstLine = null;
	private static String fNameString = null;

	/**
	 * @param csvFName CSV 文件名
	 */
	private static String[] readHeader() {
		String[] nextLine = null;
		try {
			csvReader = new CSVReader(new FileReader(csvFName));
			nextLine = csvReader.readNext();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return nextLine;
	}

	private static void initiateGpkg() {
		try {
			geopkg = new GeoPackage(new File(gpkgFName));
			geopkg.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static SimpleFeatureType createFeatureType() {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName(fNameString);
		builder.setCRS(crsTarget); // <- Coordinate reference system

		// add attributes in order
		// 统一社会信用代码,企业名称,所在街道,法定代表人,JYCS,经度,纬度
		builder.add(firstLine[0], String.class); // 统一社会信用代码
		builder.add(firstLine[1], String.class); // 企业名称
		builder.add(firstLine[2], String.class); // 所在街道
		builder.add(firstLine[3], String.class); // 法定代表人
		builder.add(firstLine[4], String.class); // JYCS
		builder.add("geometry", Point.class);

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

	public static void close() {
		try {
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		geopkg.close();
	}

	/**
	 * @param args 第一个参数为CSV文件路径名，第二个参数为GeoPackage文件路径名
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.exit(0);
		}
		csvFName = args[0];
		gpkgFName = args[1];
		fNameString = new File(csvFName).getName();
		fNameString = fNameString.substring(0, fNameString.indexOf('.'));
		// 读取CSV文件的第一行
		// 统一社会信用代码,企业名称,所在街道,法定代表人,JYCS,经度,纬度
		firstLine = readHeader();
		// 准备Geopackage文件
		initiateGpkg();
		try {
			crsSource = CRS.decode("EPSG:4490");
			crsTarget = CRS.decode("EPSG:4547");
		} catch (NoSuchAuthorityCodeException e1) {
			e1.printStackTrace();
		} catch (FactoryException e1) {
			e1.printStackTrace();
		}

		// 一个文件对一个表
		FeatureEntry entry = new FeatureEntry();
		entry.setDataType(Entry.DataType.Feature);
		entry.setSrid(4547);
		String tableName = fNameString;
		entry.setTableName(tableName);
		entry.setGeometryColumn("geometry");
		entry.setGeometryType(Geometries.POINT);
		// 属性结构
		final SimpleFeatureType TYPE = createFeatureType();
		List<SimpleFeature> features = new ArrayList<SimpleFeature>();
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		long count = 0l;
		try {
			String[] nextLine = null;
			while ((nextLine = csvReader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				for (String str : nextLine) {
					System.out.print(str + "\t");
				}
				System.out.println();
				count++;

				String str1 = nextLine[0];
				String str2 = nextLine[1];
				String str3 = nextLine[2];
				String str4 = nextLine[3];
				String str5 = nextLine[4];
				String str6 = nextLine[5];
				String str7 = nextLine[6];
				if (str6.trim().isEmpty() || str7.trim().isEmpty())
					continue;
				double latitude = Double.parseDouble(str6);
				double longitude = Double.parseDouble(str7);

				featureBuilder.add(str1.trim());
				featureBuilder.add(str2.trim());
				featureBuilder.add(str3.trim());
				featureBuilder.add(str4.trim());
				featureBuilder.add(str5.trim());
				Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
				Point point2 = (Point) lonlat2xy(point);
				Point point3 = geometryFactory
						.createPoint(new Coordinate(point2.getCoordinate().y, point2.getCoordinate().x));

				featureBuilder.add(point3);
				SimpleFeature feature = featureBuilder.buildFeature(null);
				features.add(feature);

			}
			geopkg.add(entry, new ListFeatureCollection(TYPE, features));
			System.out.println(count);

		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}

}
