/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import top.geomatics.gazetteer.model.GeoPoint;

/**
 * @author whudyj
 *
 */
public class CoordTransformation {

	private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	private static final double pi = 3.1415926535897932384626; // π
	private static final double a = 6378245.0; // 长半轴
	private static final double ee = 0.00669342162296594323;// # 偏心率平方

	/**
	 * WGS84转GCJ02(火星坐标系)
	 * 
	 * @param point WGS84坐标系的点（经度，纬度）
	 * @return
	 */
	public static GeoPoint wgs84_to_gcj02(GeoPoint point) {

		double lng = point.getX();
		double lat = point.getY();
		GeoPoint newPoint = new GeoPoint();
		newPoint.setX(lng);
		newPoint.setY(lat);
		// 判断是否在国内
		if (out_of_china(point)) {
			return newPoint;
		}
		double dlat = transformlat(lng - 105.0, lat - 35.0);
		double dlng = transformlng(lng - 105.0, lat - 35.0);
		double radlat = lat / 180.0 * pi;
		double magic = Math.sin(radlat);
		magic = 1 - ee * magic * magic;
		double sqrtmagic = Math.sqrt(magic);
		dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
		dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
		double mglat = lat + dlat;
		double mglng = lng + dlng;
		newPoint.setX(mglng);
		newPoint.setY(mglat);
		return newPoint;
	}

	/**
	 * GCJ02(火星坐标系)转GPS84
	 * 
	 * @param point 火星坐标系的点（经度，纬度）
	 * @return
	 */
	public static GeoPoint gcj02_to_wgs84(GeoPoint point) {
		double lng = point.getX();
		double lat = point.getY();
		GeoPoint newPoint = new GeoPoint();
		newPoint.setX(lng);
		newPoint.setY(lat);
		// 判断是否在国内
		if (out_of_china(point)) {
			return newPoint;
		}
		double dlat = transformlat(lng - 105.0, lat - 35.0);
		double dlng = transformlng(lng - 105.0, lat - 35.0);
		double radlat = lat / 180.0 * pi;
		double magic = Math.sin(radlat);
		magic = 1 - ee * magic * magic;
		double sqrtmagic = Math.sqrt(magic);
		dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
		dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
		double mglat = lat + dlat;
		double mglng = lng + dlng;
		newPoint.setX(lng * 2 - mglng);
		newPoint.setY(lat * 2 - mglat);
		return newPoint;
	}

	/**
	 * <i>火星坐标系(GCJ-02)转百度坐标系(BD-09) </i> <i>谷歌、高德——>百度</i>
	 * 
	 * @param point 火星坐标系的点（经度，纬度）
	 * @return
	 */
	public static GeoPoint gcj02_to_bd09(GeoPoint point) {
		double lng = point.getX();
		double lat = point.getY();
		double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
		double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_pi);
		double bd_lng = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		GeoPoint newPoint = new GeoPoint();
		newPoint.setX(bd_lng);
		newPoint.setY(bd_lat);
		return newPoint;
	}

	/**
	 * <i>百度坐标系(BD-09)转火星坐标系(GCJ-02) </i> <i>百度——>谷歌、高德</i>
	 * 
	 * @param point 火星坐标系的点（经度，纬度）
	 * @return
	 */
	public static GeoPoint bd09_to_gcj02(GeoPoint point) {
		double bd_lon = point.getX();
		double bd_lat = point.getY();
		double x = bd_lon - 0.0065;
		double y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lng = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		GeoPoint newPoint = new GeoPoint();
		newPoint.setX(gg_lng);
		newPoint.setY(gg_lat);
		return newPoint;

	}

	public static GeoPoint bd09_to_wgs84(GeoPoint point) {
		GeoPoint p1 = bd09_to_gcj02(point);
		return gcj02_to_wgs84(p1);
	}

	public static GeoPoint wgs84_to_bd09(GeoPoint point) {
		GeoPoint p1 = wgs84_to_gcj02(point);
		return gcj02_to_bd09(p1);
	}

	public static boolean out_of_china(GeoPoint point) {
		double lng = point.getX();
		double lat = point.getY();
		boolean flag = false;
		if (lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55) {
			flag = true;
		}
		return flag;
	}

	private static double transformlat(double lng, double lat) {
		double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat
				+ 0.2 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformlng(double lng, double lat) {
		double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
		ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 * Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lng * pi) + 40.0 * Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lng / 12.0 * pi) + 300.0 * Math.sin(lng / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}

}
