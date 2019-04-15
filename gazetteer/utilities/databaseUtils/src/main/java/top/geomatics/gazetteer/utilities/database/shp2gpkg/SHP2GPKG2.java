package top.geomatics.gazetteer.utilities.database.shp2gpkg;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import org.geotools.geometry.jts.Geometries;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryType;

import top.geomatics.gazetteer.utilities.database.CoordinatesTransformation;

/**
 * @author whudyj
 *
 */
public class SHP2GPKG2 {
	private static DataStore dataStore = null;
	private static GeoPackage geopkg = null;

	// 深圳高斯坐标系：EPSG:4547 --> WGS 84坐标 EPSG:4326
	private static CoordinatesTransformation ct = new CoordinatesTransformation();

	/**
	 * @param shpFName shapefile 文件名
	 */
	private static void readShapefile(String shpFName) {
		File file = new File(shpFName);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("url", file.toURI().toURL());
			dataStore = DataStoreFinder.getDataStore(map);
			if (dataStore instanceof ShapefileDataStore) {
				((ShapefileDataStore) dataStore).setCharset(Charset.forName("GBK"));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void initiateGpkg(String gpkgFName) {
		try {
			geopkg = new GeoPackage(new File(gpkgFName));
			geopkg.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		geopkg.close();
	}

	/**
	 * @param args 第一个参数为shapefile文件路径名，第二个参数为GeoPackage文件路径名
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(
					"Usage: java -jar XXX.jar H:\\projects\\gazetteer\\data\\深圳龙华地名地址\\建筑物\\龙华区楼栋2000\\龙华区楼栋.shp d:\\data\\building.gpkg");
			System.exit(0);
		}
		readShapefile(args[0]);
		initiateGpkg(args[1]);

		String typeName;
		FeatureSource<SimpleFeatureType, SimpleFeature> source = null;
		FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;
		SimpleFeatureCollection featureCollection = null;
		SimpleFeatureType sfType = null;
		FeatureEntry entry = new FeatureEntry();
		ct.setCRS(4547, 4490);
		try {
			typeName = dataStore.getTypeNames()[0];
			source = dataStore.getFeatureSource(typeName);
			features = source.getFeatures();
			sfType = features.getSchema();
			if (features instanceof SimpleFeatureCollection) {
				featureCollection = (SimpleFeatureCollection) features;
			}
			entry.setDataType(Entry.DataType.Feature);
			// entry.setSrid(4547);
			entry.setSrid(4326);
			entry.setTableName(sfType.getTypeName());
			// 几何字段
			String geoNameString = sfType.getGeometryDescriptor().getName().toString();
			entry.setGeometryColumn(geoNameString);
			// 几何类型
			GeometryType gType = sfType.getGeometryDescriptor().getType();
			String geoType = gType.getName().toString();
			entry.setGeometryType(Geometries.getForName(geoType));
			List<SimpleFeature> targetFeatures = new ArrayList<SimpleFeature>();
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(sfType);
			SimpleFeatureIterator iterator = featureCollection.features();
			try {
				while (iterator.hasNext()) {
					SimpleFeature feature = iterator.next();

					featureBuilder.addAll(feature.getAttributes());
					// 坐标转换为WGS84坐标
					Object geObject = feature.getAttribute(geoNameString);
					if (geObject instanceof Geometry) {
						Geometry targetGeometry = ct.transform((Geometry) geObject);
						featureBuilder.set(geoNameString, targetGeometry);
					}
					SimpleFeature targetFeature = featureBuilder.buildFeature(null);
					targetFeatures.add(targetFeature);
				}
				geopkg.add(entry, new ListFeatureCollection(sfType, targetFeatures));
				// 建立空间索引
				geopkg.createSpatialIndex(entry);

			} finally {
				iterator.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		close();

	}
}
