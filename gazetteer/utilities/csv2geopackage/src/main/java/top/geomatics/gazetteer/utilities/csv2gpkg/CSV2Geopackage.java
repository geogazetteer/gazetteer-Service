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
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.google.common.collect.Sets;
import com.opencsv.CSVReader;

/**
 * @author whudyj 将CSV文件转换为GeoPackage文件
 */
public class CSV2Geopackage {
	private static CSVReader csvReader = null;
	private static GeoPackage geopkg = null;

	/**
	 * @param csvFName CSV 文件名
	 */
	private static String[] readHeader(String csvFName) {
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

	private static void initiateGpkg(String gpkgFName) {
		try {
			geopkg = new GeoPackage(new File(gpkgFName));
			geopkg.init();
//			geopkg.addCRS(4369);
//			geopkg.addCRS(0);
//			geopkg.addCRS(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static SimpleFeatureType createFeatureType() {

		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("Location");
		builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system

		// add attributes in order
		builder.add("geometry", Point.class);
		builder.length(15).add("Name", String.class); // <- 15 chars width for name field
		builder.add("number", Integer.class);

		// build the type
		final SimpleFeatureType LOCATION = builder.buildFeatureType();

		return LOCATION;
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
		// 读取CSV文件的第一行
		// LAT, LON, CITY, NUMBER, YEAR
		String[] nextLine = readHeader(args[0]);
		// 准备Geopackage文件
		initiateGpkg(args[1]);
		// 一个文件对一个表
		FeatureEntry entry = new FeatureEntry();
		entry.setDataType(Entry.DataType.Feature);
		entry.setSrid(4326);
		String tableName = "locations";
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
			while ((nextLine = csvReader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				for (String str : nextLine) {
					System.out.print(str + "\t");
				}
				System.out.println();
				count++;

				double latitude = Double.parseDouble(nextLine[0]);
				double longitude = Double.parseDouble(nextLine[1]);
				String name = nextLine[2].trim();
				int number = Integer.parseInt(nextLine[3].trim());

				/* Longitude (= x coord) first ! */
				Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

				featureBuilder.add(point);
				featureBuilder.add(name);
				featureBuilder.add(number);
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
