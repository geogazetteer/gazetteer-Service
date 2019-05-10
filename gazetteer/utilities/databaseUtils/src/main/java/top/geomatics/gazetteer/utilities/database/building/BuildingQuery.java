/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.building;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.GeoPoint;

/**
 * <b>建筑物数据库空间查询类</b><br>
 * 
 * <em>使用说明：</em><br>
 * <i>建筑物数据库信息在配置文件中配置，配置信息如下：</i><br>
 * <i>building_db_file: 数据库文件名称</i><br>
 * <i>building_table_name: 表名</i><br>
 * <i>building_field_czwcode: 建筑物编码字段名称</i><br>
 * 
 * @author whudyj
 *
 */
public class BuildingQuery {
	// 添加slf4j日志实例对象
	final static Logger logger = LoggerFactory.getLogger(BuildingQuery.class);

	// 配置信息
	private ResourcesManager manager = ResourcesManager.getInstance();
	private static final String BUILDING_GPKG_PATH = Messages.getString("BuildingQuery.0"); //$NON-NLS-1$
	private static final String BUILDING_TABLE_NAME = Messages.getString("BuildingQuery.1"); //$NON-NLS-1$
	private static final String BUILDING_FIELD_CZWCODE = Messages.getString("BuildingQuery.2"); //$NON-NLS-1$
	private String building_file = manager.getValue(BUILDING_GPKG_PATH);
	private String table_name = manager.getValue(BUILDING_TABLE_NAME);
	private String field_czwcode = manager.getValue(BUILDING_FIELD_CZWCODE);
	// gpkg数据库操作类
	private GeoPackage geoPackage = null;
	private FeatureEntry entry;
	private String geoCol;
	private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	/**
	 * <b>打开数据库，准备查询</b><br>
	 * 
	 */
	public void open() {
		// 打开数据库文件
		try {
			// 日志
			String logMsgString = String.format(Messages.getString("BuildingQuery.7"), building_file); //$NON-NLS-1$
			logger.info(logMsgString);
			
			geoPackage = new GeoPackage(new File(building_file));
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format(Messages.getString("BuildingQuery.3"), building_file); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		// 打开数据库表
		try {
			entry = geoPackage.feature(table_name);
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format(Messages.getString(Messages.getString("BuildingQuery.4")), table_name); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		// 几何字段名称
		geoCol = entry.getGeometryColumn();
	}

	/**
	 * <b>关闭数据库</b><br>
	 * 
	 */
	public void close() {
		geoPackage.close();
	}

	/**
	 * <b>根据建筑物编码查询其位置</b><br>
	 * 
	 * @param czwCode String 建筑物编码字段名称
	 * @return GeoPoint 建筑物位置坐标
	 */
	public GeoPoint query(String czwCode) {
		GeoPoint point = null;
		// 设置查询条件
		Filter filter = ff.equals(ff.property(field_czwcode), ff.literal(czwCode));
		SimpleFeatureReader feaReader = null;
		try {
			// 查询
			feaReader = geoPackage.reader(entry, filter, null);
			// 因为建筑物编码唯一，查询结果应该只有一个。如果有多个查询结果，则返回最后一个查询结果的建筑物位置坐标
			while (feaReader.hasNext()) {
				SimpleFeature feature = feaReader.next();
				Object g = feature.getAttribute(geoCol);// 几何
				Geometries geomType = Geometries.get((Geometry) g);
				point = new GeoPoint();
				Point p = null;
				switch (geomType) {
				case MULTIPOLYGON:
					MultiPolygon mPolygon = (MultiPolygon) g;
					p = mPolygon.getCentroid();
					point.setX(p.getX());
					point.setY(p.getY());
					break;

				case POLYGON:
					Polygon polygon = (Polygon) g;
					p = polygon.getCentroid();
					point.setX(p.getX());
					point.setY(p.getY());
					break;

				default:
					break;
				}
			}
			feaReader.close();
			feaReader = null;
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format(Messages.getString("BuildingQuery.5"), czwCode); //$NON-NLS-1$
			logger.error(logMsgString);
		}

		return point;
	}

	/**
	 * <b>根据坐标获得建筑物编码</b><br>
	 * 
	 * @param x double x坐标（经度）
	 * @param y double y坐标（纬度）
	 * @return List 建筑物编码列表
	 */
	public List<String> query(double x, double y) {
		List<String> czwcodes = new ArrayList<>();
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
				Object g = feature.getAttribute(field_czwcode);// 几何
				if (g instanceof String) {
					String tempString = g.toString();
					// 判断是否为有效的建筑物编码
					if (tempString != null && !tempString.isEmpty() && tempString.length() == 19) {
						czwcodes.add(tempString);
					}
				}
			}
			feaReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			// 日志
			String logMsgString = String.format(Messages.getString("BuildingQuery.6"), x, y); //$NON-NLS-1$
			logger.error(logMsgString);
		}
		return czwcodes;
	}

}
