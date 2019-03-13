package top.geomatics.gazetteer.model;

public interface IGazetteer {

    String getCode();

    void setCode(String code);

    GazetteerName getName();

    void setName(GazetteerName name);

}