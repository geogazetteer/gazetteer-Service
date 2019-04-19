/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.gpkg;

import java.io.File;
import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

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
	private static FilterFactory2 ff = null;

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
		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
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
	 * <em>返回建筑物面的内部点坐标</em><br>
	 * 
	 * @param czwCode
	 * @return
	 */
	public GeoPoint query(String czwCode) {
		GeoPoint point = null;
		try {
			Filter filter = ff.equals(ff.property("CZWCODE"), ff.literal(czwCode));
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

}
