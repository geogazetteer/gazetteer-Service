/**
 *
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 */
public abstract class GLocation implements IGLocation {
    private int coordType;        //坐标类型
    private double x;            //x坐标
    private double y;            //y坐标

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGLocation#getCoordType()
     */
    public int getCoordType() {
        return coordType;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGLocation#setCoordType(int)
     */
    public void setCoordType(int coordType) {
        this.coordType = coordType;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGLocation#getX()
     */
    public double getX() {
        return x;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGLocation#setX(double)
     */
    public void setX(double x) {
        this.x = x;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGLocation#getY()
     */
    public double getY() {
        return y;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGLocation#setY(double)
     */
    public void setY(double y) {
        this.y = y;
    }

    

}
