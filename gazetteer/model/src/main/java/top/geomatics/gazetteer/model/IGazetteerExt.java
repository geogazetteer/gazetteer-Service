package top.geomatics.gazetteer.model;

public interface IGazetteerExt {

    String getAdminName();

    void setAdminName(String adminName);

    String getGeoName();

    void setGeoName(String geoName);

    String getConvName();

    void setConvName(String convName);

    String getAbbrName();

    void setAbbrName(String abbrName);

    String getAlias();

    void setAlias(String alias);

    String getUsedNames();

    void setUsedNames(String usedNames);

}