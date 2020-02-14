package top.geomatics.gazetteer.utilities.database.shp2gpkg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Shapefile2Geopackage2Test {



    @Test
    public void execute() {
        Shapefile2Geopackage2 s2g = new Shapefile2Geopackage2("D:\\gazetteer\\data\\raw\\龙华行政区划",
                "D:\\gazetteer\\data\\geopackage\\龙华行政区划.gpkg");
        s2g.execute();
    }
}