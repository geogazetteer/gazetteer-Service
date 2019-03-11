package top.geomatics.gazetteer.model;

public interface IGLocation {

    int getCoordType();

    void setCoordType(int coordType);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

}