package top.geomatics.gazetteer.utilities.database.shp2gpkg;

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
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whudyj
 */
public class Shapefile2Geopackage2 {
    // 添加slf4j日志实例对象
    private final static Logger logger = LoggerFactory.getLogger(Shapefile2Geopackage2.class);

    private static final String EPSG4490 =  "EPSG:4490";// 4547,4326;
	private static final Integer CGCS2000 = 4490;

    private String shapefileName = "";
    private String geopackageName = "";
    private Map<String, String> settings = null;

    private Map<String, DataStore> dataStores = null;
    private GeoPackage geopkg = null;

    public Shapefile2Geopackage2(String shapefileName, String geopackageName, Map<String, String> settings) {
        super();
        this.shapefileName = shapefileName;
        this.geopackageName = geopackageName;
        this.settings = settings;
    }

    public Shapefile2Geopackage2(String shapefileName, String geopackageName) {
        super();
        this.shapefileName = shapefileName;
        this.geopackageName = geopackageName;
    }

    public Shapefile2Geopackage2(String shapefileName) {
        super();
        this.shapefileName = shapefileName;
    }


    /**
     * 初始化geopackage
     *
     * @return
     */
    private boolean initiateGpkg() {
        //必须给文件名
        if (this.geopackageName == null || this.geopackageName.isEmpty()) {
            return false;
        }
        try {
            File gf = new File(this.geopackageName);
            //如果数据库文件已经存在，不能覆盖，初始化失败
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

        return true;
    }

    /**
     * 关闭数据库，释放资源
     */
    private void close() {
        geopkg.close();
    }

    /**
     * 读取shapefile数据
     *
     * @return 成功或失败
     */
    private boolean readShapefile() {
        //必须有数据文件或路径
        if (this.shapefileName == null || this.shapefileName.isEmpty()) {
            return false;
        }
        File file = new File(this.shapefileName);
        //文件或路径必须存在
        if (!file.exists()) {
            return false;
        }
        //获得所有.shp文件
        List<String> allFiles = new ArrayList<>();
        if (file.isDirectory()) {
            //搜索所有.shp文件
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".shp");
                }
            };
            File[] shpFiles = file.listFiles(filter);
            if (shpFiles == null || shpFiles.length < 1) {
                return false;
            }
            for (File f : shpFiles) {
				allFiles.add(f.getAbsolutePath());
            }

        } else {
            //判断是.shp文件
            allFiles.add(this.shapefileName);
        }

        //如果没有文件，返回
		if (allFiles.size() < 1){
			return false;
		}
		//读取所有文件数据，并缓存
		dataStores = new HashMap<>();
		for (String fn :allFiles){
			//截取文件名，作为key
			File sf = new File(fn);
			String tmpFN = sf.getName();
			String key = tmpFN.substring(0,tmpFN.lastIndexOf(".shp"));

			Map<String, Object> map = new HashMap<>();
			DataStore dataStore;
			try {
				map.put("url", sf.toURI().toURL());
				dataStore = DataStoreFinder.getDataStore(map);
				if (dataStore instanceof ShapefileDataStore) {
					((ShapefileDataStore) dataStore).setCharset(Charset.forName("GBK"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				// 日志
				String logMsgString = String.format("打开shapefile文件：%s 失败！", fn);
				logger.error(logMsgString);
				continue;
			}

			dataStores.put(key,dataStore);

		}

        return true;
    }

    /**
     * @return SimpleFeatureType 返回创建的要素类型
     */
    private SimpleFeatureType createFeatureType(String tableName,SimpleFeatureType sfType) {
        // 空间参考，使用EPSG:4490
//		CoordinateReferenceSystem crsSource = sfType.getCoordinateReferenceSystem();
//		CoordinateReferenceSystem crsTarget = crsSource;
        CoordinateReferenceSystem crsTarget = null;

        try {
            crsTarget = CRS.decode(EPSG4490);
        } catch (Exception e1) {
            e1.printStackTrace();
            // 日志
            String logMsgString = String.format("解析空间参考：%s 失败！", EPSG4490);
            logger.error(logMsgString);
        }

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        // builder.setName(sfType.getTypeName());
        builder.setName(tableName);
        builder.setCRS(crsTarget); // <- Coordinate reference system

        // 原属性结构
        List<AttributeDescriptor> descriptors = sfType.getAttributeDescriptors();
        builder.addAll(descriptors);

        return builder.buildFeatureType();
    }

    public boolean execute() {
        /**
         * 初始化数据库
         */
        if (initiateGpkg() == false || geopkg == null) {
            return false;
        }
        /**
         * 读取数据
         */
        if (readShapefile() == false || dataStores == null || dataStores.size() < 1) {
            return false;
        }

        //遍历每个数据文件
        for (String tableName :dataStores.keySet()){
        	DataStore dataStore = dataStores.get(tableName);

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
				String logMsgString = String.format("读取shapefile文件：%s 失败！", tableName);
				logger.error(logMsgString);
				continue;
			}
			sfType = features.getSchema();
			targetSFType = createFeatureType(tableName,sfType);
			if (features instanceof SimpleFeatureCollection) {
				featureCollection = (SimpleFeatureCollection) features;
			}
			entry.setDataType(Entry.DataType.Feature);
			entry.setSrid(CGCS2000);
			// entry.setTableName(sfType.getTypeName());
			entry.setTableName(tableName);
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


				targetFeature = featureBuilder.buildFeature(null);
				newFeas.add(targetFeature);
			}
			try {
				geopkg.add(entry, new ListFeatureCollection(targetSFType, newFeas));

				geopkg.createSpatialIndex(entry);

			} catch (IOException e) {
				e.printStackTrace();
			}
			iterator.close();
			////////////////////

		}




        close();


        return true;

    }

//    public List<String> getFields() {
//        if (readShapefile() == false || dataStore == null) {
//            return null;
//        }
//        List<String> fields = new ArrayList<String>();
//        String typeName;
//        FeatureSource<SimpleFeatureType, SimpleFeature> source = null;
//        FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;
//        SimpleFeatureType sfType = null;
//        try {
//            typeName = dataStore.getTypeNames()[0];
//            source = dataStore.getFeatureSource(typeName);
//            features = source.getFeatures();
//        } catch (IOException e) {
//            e.printStackTrace();
//            // 日志
//            String logMsgString = String.format("读取shapefile文件：%s 失败！", this.shapefileName);
//            logger.error(logMsgString);
//            return null;
//        }
//        sfType = features.getSchema();
//        List<AttributeDescriptor> descriptors = sfType.getAttributeDescriptors();
//        for (AttributeDescriptor desc : descriptors) {
//            fields.add(desc.getLocalName());
//        }
//
//        return fields;
//
//    }
}
