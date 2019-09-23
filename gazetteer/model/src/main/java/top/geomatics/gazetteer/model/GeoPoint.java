/**
 *
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 */
public class GeoPoint {
	private double x; // x坐标
	private double y; // y坐标
	
	public GeoPoint() {
		super();
	}

	public GeoPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
