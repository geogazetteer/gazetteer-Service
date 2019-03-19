package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

import com.opencsv.CSVReader;

import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressReader;
import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressRecord;
import top.geomatics.gazetteer.utilities.database.csv2sqlite.AddressWriter2;

/**
 * @author whudyj
 *
 */
public class Excel2GPKG {
	private static ExcelReader excelReader = null;
	private static GeopackageWriter gpkgWriter = null;

	/**
	 * @param args 第一个参数为excel文件路径名，第二个参数为GeoPackage文件路径名
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(
					"Usage: java -jar XXX.jar H:\\projects\\gazetteer\\data\\深圳龙华地名地址\\法人\\企业数据-统一社会信用代码\\企业数据-统一社会信用代码1.xlsx  D:\\data\\enterpriseenterprise_gazetteer.gpkg");
			System.exit(0);
		}
		String tableName = new File(args[0]).getName();
		tableName = tableName.substring(0, tableName.indexOf('.'));
		
		// 设置篮子中苹果的最大个数
		BlockingQueue<AddressRecord> blockingQueue = new ArrayBlockingQueue<AddressRecord>(1000);
		excelReader = new ExcelReader(args[0], blockingQueue);
		gpkgWriter = new GeopackageWriter(args[1], tableName,blockingQueue);

		try {
			excelReader.openFile();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		gpkgWriter.prepare();

		Thread th1 = new Thread(excelReader);
		th1.setName("ReaderThread");

		Thread th2 = new Thread(gpkgWriter);
		th2.setName("WriterThread");

		th1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		th2.start();
	}
}
