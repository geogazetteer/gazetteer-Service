/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * @author whudyj
 *
 */
public class DoubleCompare {
	private static final double tor = 1e-6;
	public static boolean isEqual(double x1,double x2) {
		return Math.abs(x1-x2) < tor;
	}
	public static boolean isEqual(double x1,Double y1,double x2,double y2) {
		return (Math.abs(x1-x2) < tor) && (Math.abs(y1-y2) < tor);
	}

}
