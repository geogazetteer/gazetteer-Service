/**
 * 
 */
package top.geomatics.gazetteer.utilities.database;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * 坐标转换
 * 
 * @author whudyj
 *
 */
public class CoordinatesTransformation {

	private static CoordinateReferenceSystem crsSource = null;
	private static CoordinateReferenceSystem crsTarget = null;

	public void setCRS(int sourceSrcID, int targetSrcID) {
		String sourceString = "EPSG:" + sourceSrcID;
		String targetString = "EPSG:" + targetSrcID;
		try {
			crsSource = CRS.decode(sourceString);
			crsTarget = CRS.decode(targetString);
		} catch (NoSuchAuthorityCodeException e1) {
			e1.printStackTrace();
		} catch (FactoryException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * <em>经纬度转换为高斯坐标 </em>
	 * 
	 * @param geom Geometry 几何对象
	 * @return Geometry坐标转换后的几何对象
	 */
	public Geometry transform(Geometry geom) {
		try {
			// 投影转换
			MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
			return JTS.transform(geom, transform);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
