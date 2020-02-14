package top.geomatics.gazetteer.utilities.database.boundary;

import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.boundary.Grid;
import top.geomatics.gazetteer.model.boundary.JD;
import top.geomatics.gazetteer.model.boundary.SQ;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundaryQuery {
    // 添加slf4j日志实例对象
    final static Logger logger = LoggerFactory.getLogger(BoundaryQuery.class);

    // 配置信息
    private static ResourcesManager manager = ResourcesManager.getInstance();
    private static final String BUILDING_GPKG_PATH = "boundary_db_file";
    private static String boundary_file = manager.getValue(BUILDING_GPKG_PATH);


    // gpkg数据库操作类
    private static GeoPackage geoPackage = null;
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
    private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    private static Map<String, FeatureEntry> entryMap =null;

    /**
     * <b>打开数据库，准备查询</b><br>
     */
    private static void open() {
        // 打开数据库文件
        try {
            // 日志
            geoPackage = new GeoPackage(new File(boundary_file));
        } catch (IOException e) {
            e.printStackTrace();
            // 日志
        }


    }

    /**
     * <b>关闭数据库</b><br>
     */
    private static void close() {
        if (geoPackage != null) {
            geoPackage.close();
            geoPackage = null;
        }
    }

    private static void openTable(String tableName) {
        if (null == geoPackage){
            BoundaryQuery.open();
        }
        // 打开数据库表
        if (null == entryMap) {
            entryMap = new HashMap<>();
        }
        if (!entryMap.containsKey(tableName) || null == entryMap.get(tableName)) {
            FeatureEntry entry = null;
            try {
                entry = geoPackage.feature(tableName);
            } catch (IOException e) {
                e.printStackTrace();
                // 日志
            }
            if (entry != null) {
                entryMap.put(tableName, entry);
            }
        }
    }

    /**
     * <b>根据坐标获得街道</b><br>
     *
     * @param x Double x坐标（经度）
     * @param y Double y坐标（纬度）
     * @return List 街道列表
     */
    public static List<JD> queryJD(Double x, Double y) {

        String tableName = "jd";
        openTable(tableName);

        FeatureEntry entry = entryMap.get(tableName);

        // 几何字段名称
        String geoCol = entry.getGeometryColumn();

        List<JD> results = new ArrayList<>();
        try {
            // 设置查询条件
            Point point = geometryFactory.createPoint(new Coordinate(x, y));
            Filter filter1 = ff.not(ff.isNull(ff.property(geoCol)));
            Filter filter2 = ff.contains(ff.property(geoCol), ff.literal(point));
            Filter filter = ff.and(filter1, filter2);
            // 查询
            SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
            while (feaReader.hasNext()) {
                SimpleFeature feature = feaReader.next();

                //返回数据
                JD jd = new JD();

                Object v1 = feature.getAttribute("JDCODE");
                if (null != v1) {
                    jd.setJDCODE(v1.toString());
                }
                Object v2 = feature.getAttribute("JDCODE_Y");
                if (null != v2) {
                    jd.setJDCODE_Y(v2.toString());
                }
                Object v3 = feature.getAttribute("JDNAME");
                if (null != v3) {
                    jd.setJDNAME(v3.toString());
                }
                Object v4 = feature.getAttribute("JDNAME_Y");
                if (null != v4) {
                    jd.setJDCODE_Y(v4.toString());
                }

                results.add(jd);

            }
            feaReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 日志

        }
        return results;
    }

    /**
     * <b>根据坐标获得社区</b><br>
     *
     * @param x Double x坐标（经度）
     * @param y Double y坐标（纬度）
     * @return List 社区列表
     */
    public static List<SQ> querySQ(Double x, Double y) {

        String tableName = "sq";
        openTable(tableName);

        FeatureEntry entry = entryMap.get(tableName);

        // 几何字段名称
        String geoCol = entry.getGeometryColumn();

        List<SQ> results = new ArrayList<>();
        try {
            // 设置查询条件
            Point point = geometryFactory.createPoint(new Coordinate(x, y));
            Filter filter1 = ff.not(ff.isNull(ff.property(geoCol)));
            Filter filter2 = ff.contains(ff.property(geoCol), ff.literal(point));
            Filter filter = ff.and(filter1, filter2);
            // 查询
            SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
            while (feaReader.hasNext()) {
                SimpleFeature feature = feaReader.next();

                //返回数据
                SQ jd = new SQ();

                Object v1 = feature.getAttribute("SQCODE");
                if (null != v1) {
                    jd.setSQCODE(v1.toString());
                }
                Object v2 = feature.getAttribute("SQCODE_Y");
                if (null != v2) {
                    jd.setSQCODE_Y(v2.toString());
                }
                Object v3 = feature.getAttribute("SQNAME");
                if (null != v3) {
                    jd.setSQNAME(v3.toString());
                }
                Object v4 = feature.getAttribute("SQNAME_Y");
                if (null != v4) {
                    jd.setSQNAME_Y(v4.toString());
                }

                results.add(jd);

            }
            feaReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 日志

        }
        return results;
    }

    /**
     * <b>根据坐标获得网格</b><br>
     *
     * @param x Double x坐标（经度）
     * @param y Double y坐标（纬度）
     * @return List 网格列表
     */
    public static List<Grid> queryGrid(Double x, Double y) {

        String tableName = "grid";
        openTable(tableName);

        FeatureEntry entry = entryMap.get(tableName);

        // 几何字段名称
        String geoCol = entry.getGeometryColumn();

        List<Grid> results = new ArrayList<>();
        try {
            // 设置查询条件
            Point point = geometryFactory.createPoint(new Coordinate(x, y));
            Filter filter1 = ff.not(ff.isNull(ff.property(geoCol)));
            Filter filter2 = ff.contains(ff.property(geoCol), ff.literal(point));
            Filter filter = ff.and(filter1, filter2);
            // 查询
            SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
            while (feaReader.hasNext()) {
                SimpleFeature feature = feaReader.next();

                //返回数据
                Grid jd = new Grid();

                Object v1 = feature.getAttribute("AREA1");
                if (null != v1) {
                    jd.setAREA1((Double) v1);
                }
                Object v2 = feature.getAttribute("BGNOTE");
                if (null != v2) {
                    jd.setBGNOTE(v2.toString());
                }
                Object v3 = feature.getAttribute("CHDATE");
                if (null != v3) {
                    jd.setCHDATE(v3.toString());
                }
                Object v4 = feature.getAttribute("GRIDCODE");
                if (null != v4) {
                    jd.setGRIDCODE(v4.toString());
                }
                Object v5 = feature.getAttribute("GRIDCODE_Y");
                if (null != v5) {
                    jd.setGRIDCODE_Y(v5.toString());
                }
                Object v6 = feature.getAttribute("GRIDMAIN");
                if (null != v6) {
                    jd.setGRIDMAIN(v6.toString());
                }
                Object v7 = feature.getAttribute("LASTCODE");
                if (null != v7) {
                    jd.setLASTCODE(v7.toString());
                }
                Object v8 = feature.getAttribute("LASTWCODE");
                if (null != v8) {
                    jd.setLASTWCODE(v8.toString());
                }
                Object v9 = feature.getAttribute("ORDATE");
                if (null != v9) {
                    jd.setORDATE(v9.toString());
                }
                Object v10 = feature.getAttribute("SQCODE");
                if (null != v10) {
                    jd.setSQCODE(v10.toString());
                }
                Object v11 = feature.getAttribute("WORKCODE");
                if (null != v11) {
                    jd.setWORKCODE(v11.toString());
                }
                Object v12 = feature.getAttribute("WORKCODNEW");
                if (null != v12) {
                    jd.setWORKCODNEW(v12.toString());
                }

                results.add(jd);

            }
            feaReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 日志

        }
        return results;
    }
}
