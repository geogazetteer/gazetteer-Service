package top.geomatics.gazetteer.utilities.database.shp2gpkg;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryType;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;

/**
 * @author whudyj
 *
 */
public class SHP2GPKG3 {
	private static DataStore dataStore = null;
	private static GeoPackage geopkg = null;

	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);
	private static Map<String, Object> map = new HashMap<String, Object>();
	private static String sql_tablename = "building_position";
	private static String bguid = "BGUID";
	private static String czwcode = "CZWCODE";

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

		session.close();
	}

	public static void prepare() {
		mapper.dropTable(sql_tablename);
		mapper.createBuildingPositionTable(sql_tablename);
		session.commit();
		map.put("sql_tablename", sql_tablename);
	}

	/**
	 * @param args 第一个参数为shapefile文件路径名，第二个参数为GeoPackage文件路径名
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(
					"Usage: java -jar XXX.jar D:\\data\\龙华区楼栋2000地理坐标系\\龙华区楼栋2000地理坐标系.shp d:\\data\\LH_building_4490.gpkg");
			System.exit(0);
		}
		readShapefile(args[0]);
		initiateGpkg(args[1]);
		prepare();

		String typeName;
		FeatureSource<SimpleFeatureType, SimpleFeature> source = null;
		FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;
		SimpleFeatureCollection featureCollection = null;
		SimpleFeatureType sfType = null;
		FeatureEntry entry = new FeatureEntry();
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
			entry.setSrid(4490);
			// entry.setSrid(4326);
			entry.setTableName(sfType.getTypeName());
			// 几何字段
			String geoNameString = sfType.getGeometryDescriptor().getName().toString();
			entry.setGeometryColumn(geoNameString);
			// 几何类型
			GeometryType gType = sfType.getGeometryDescriptor().getType();
			String geoType = gType.getName().toString();
			entry.setGeometryType(Geometries.getForName(geoType));
			// 创建一个表
			SimpleFeatureIterator iterator = featureCollection.features();
			while (iterator.hasNext()) {
				SimpleFeature aFeature = iterator.next();
				String bguid_value = aFeature.getAttribute(bguid).toString();
				String czwcode_value = aFeature.getAttribute(czwcode).toString();
				map.put("code", czwcode_value);
				map.put("building_id", bguid_value);
				Object og = aFeature.getDefaultGeometry();
				if (og instanceof MultiPolygon) {
					MultiPolygon polygon = (MultiPolygon) og;
					Point point = polygon.getCentroid();
					if (null != point) {
						map.put("longitude", point.getX());
						map.put("latitude", point.getY());
					}
				}
				mapper.insertBuildingPosition(map);
			}
			session.commit(true);
			////////////////////

			geopkg.add(entry, featureCollection);
			// 建立空间索引
			geopkg.createSpatialIndex(entry);

			iterator.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		close();

	}
}
