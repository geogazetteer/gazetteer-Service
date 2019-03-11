/**
 *
 */
package top.geomatics.gazetteer.model;

/**
 * @author whudyj
 */
public abstract class GazetteerExt extends Gazetteer implements IGazetteerExt {
    private String adminName;        //地址行政名称
    private String geoName;            //地址地理名称
    private String convName;        //地址俗称
    private String abbrName;        //地址简称
    private String alias;            //地址别称，多个别称之间用,号分隔
    private String usedNames;        //地址曾用名，多个曾用名之间用,号分隔

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#getAdminName()
     */
    public String getAdminName() {
        return adminName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#setAdminName(java.lang.String)
     */
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#getGeoName()
     */
    public String getGeoName() {
        return geoName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#setGeoName(java.lang.String)
     */
    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#getConvName()
     */
    public String getConvName() {
        return convName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#setConvName(java.lang.String)
     */
    public void setConvName(String convName) {
        this.convName = convName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#getAbbrName()
     */
    public String getAbbrName() {
        return abbrName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#setAbbrName(java.lang.String)
     */
    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#getAlias()
     */
    public String getAlias() {
        return alias;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#setAlias(java.lang.String)
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#getUsedNames()
     */
    public String getUsedNames() {
        return usedNames;
    }

    /* (non-Javadoc)
     * @see top.geomatics.gazetteer.model.IGazetteerExt#setUsedNames(java.lang.String)
     */
    public void setUsedNames(String usedNames) {
        this.usedNames = usedNames;
    }
}
