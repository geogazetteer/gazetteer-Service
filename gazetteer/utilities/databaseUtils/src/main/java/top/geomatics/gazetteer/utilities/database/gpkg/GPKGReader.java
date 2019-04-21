/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.gpkg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.Geometry;

import top.geomatics.gazetteer.model.GeoPoint;

/**
 * @author whudyj
 *
 */
public class GPKGReader {
	private String pdkgFilePath;// gpkg文件路径名
	private static GeoPackage geoPackage;

	private static FeatureEntry entry;
	private static String geoCol;
	private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	private static final String CZWCODE_STRING = "CZWCODE";

	public GPKGReader(String pdkgFilePath) {
		super();
		this.pdkgFilePath = pdkgFilePath;

		try {
			geoPackage = new GeoPackage(new File(pdkgFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void preQuery(String tableName) {
		try {
			entry = geoPackage.feature(tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		geoCol = entry.getGeometryColumn();
	}

	public void close() {
		geoPackage.close();
	}

	public String getPdkgFilePath() {
		return pdkgFilePath;
	}

	public void setPdkgFilePath(String pdkgFilePath) {
		this.pdkgFilePath = pdkgFilePath;
	}

	/**
	 * <em>根据建筑物编码计算质心点坐标</em><br>
	 * 
	 * @param czwCode
	 * @return
	 */
	public GeoPoint query(String czwCode) {
		GeoPoint point = null;
		try {
			Filter filter = ff.equals(ff.property(CZWCODE_STRING), ff.literal(czwCode));
			SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
			while (feaReader.hasNext()) {
				SimpleFeature feature = feaReader.next();
				Object g = feature.getAttribute(geoCol);// 几何
				if (g instanceof MultiPolygon) {
					MultiPolygon gGeometry = (MultiPolygon) g;
					Point p = gGeometry.getCentroid();
					point = new GeoPoint();
					point.setX(p.getX());
					point.setY(p.getY());
				}
			}
			feaReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return point;
	}

	/**
	 * <em>根据坐标获得建筑物编码</em><br>
	 * 
	 * @param czwCode
	 * @return
	 */
	public List<String> query(double x, double y) {
		List<String> czwcodes = new ArrayList<>();
		try {
			Filter filter = ff.not(ff.isNull(ff.property(geoCol)));
			Point point = geometryFactory.createPoint(new Coordinate(x, y));
			ff.contains(ff.property(geoCol), ff.literal(point));
			SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
			while (feaReader.hasNext()) {
				SimpleFeature feature = feaReader.next();
				Object g = feature.getAttribute(CZWCODE_STRING);// 几何
				if (g instanceof String) {
					String tempString = g.toString();
					if (tempString != null && !tempString.isEmpty() && tempString.length() == 19) {
						czwcodes.add(tempString);
					}
				}
			}
			feaReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return czwcodes;
	}

}
