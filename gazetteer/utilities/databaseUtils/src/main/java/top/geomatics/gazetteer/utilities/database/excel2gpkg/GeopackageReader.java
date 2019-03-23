/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import top.geomatics.gazetteer.model.EnterpriseRow;

/**
 * @author whudyj
 *
 */
public class GeopackageReader {
	private String pdkgFilePath;// gpkg文件路径名

	private static GeoPackage geoPackage;
	private static FeatureEntry entry;
	private static String geoCol;
//	private static CoordinateReferenceSystem crsTarget = null;
//	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	public GeopackageReader(String pdkgFilePath) {
		super();
		this.pdkgFilePath = pdkgFilePath;

		try {
			geoPackage = new GeoPackage(new File(pdkgFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void preQuery(String tableName) {
		try {
			entry = geoPackage.feature(tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		geoCol = entry.getGeometryColumn();
//		try {
//			crsTarget = CRS.decode("EPSG:4547");
//		} catch (NoSuchAuthorityCodeException e) {
//			e.printStackTrace();
//		} catch (FactoryException e) {
//			e.printStackTrace();
//		}
	}

	public List<EnterpriseRow> query(double x, double y) {
		List<EnterpriseRow> rows = null;
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		try {
			// ReferencedEnvelope env = new ReferencedEnvelope(x - 0.02, y - 0.02, x + 0.02,
			// y + 0.02, crsTarget);
			Filter filter = ff.not(ff.isNull(ff.property(geoCol)));
			// Filter filter = ff.and(ff.not(ff.isNull(ff.property(geoCol))),
			// ff.bbox(ff.property(geoCol), env));
			filter = ff.and(ff.greaterOrEqual(ff.property("x"), ff.literal(x - 0.0002)),
					ff.lessOrEqual(ff.property("x"), ff.literal(x + 0.0002)));
			filter = ff.and(ff.greaterOrEqual(ff.property("y"), ff.literal(y - 0.0002)),
					ff.lessOrEqual(ff.property("y"), ff.literal(y + 0.0002)));
			SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
			rows = new ArrayList<EnterpriseRow>();
			while (feaReader.hasNext()) {
				SimpleFeature feature = feaReader.next();
				EnterpriseRow row = new EnterpriseRow();
				row.setCode(feature.getAttribute("code").toString());
				row.setName(feature.getAttribute("name").toString());
				row.setOwner(feature.getAttribute("owner").toString());
				row.setStreet(feature.getAttribute("street").toString());
				row.setAddress(feature.getAttribute("address").toString());
				String lsString = feature.getAttribute("x").toString();
				row.setLongitude(Double.parseDouble(lsString));
				row.setLatitude(Double.parseDouble(feature.getAttribute("y").toString()));
				rows.add(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}

	public void close() {
		geoPackage.close();
	}

	public String getPdkgFilePath() {
		return pdkgFilePath;
	}

	public void setPdkgFilePath(String pdkgFilePath) {
		this.pdkgFilePath = pdkgFilePath;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.exit(0);
		}
		GeopackageReader reader = new GeopackageReader(args[0]);
		preQuery("enterprise1");
		List<EnterpriseRow> rows = reader.query(505366.34375, 2505695.5);
		for (EnterpriseRow row : rows) {
			System.out.println(row.getAddress() + "\t" + row.getLongitude() + "\t" + row.getLatitude());
		}

	}

}
