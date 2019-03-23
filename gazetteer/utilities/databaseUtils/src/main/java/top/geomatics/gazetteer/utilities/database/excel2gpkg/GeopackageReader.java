/**
 * 
 */
package top.geomatics.gazetteer.utilities.database.excel2gpkg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import top.geomatics.gazetteer.model.BuildingRow;
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
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	public GeopackageReader(String pdkgFilePath) {
		super();
		this.pdkgFilePath = pdkgFilePath;

		try {
			geoPackage = new GeoPackage(new File(pdkgFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void preQuery(String tableName) {
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

	public void createRealFeature(String oldName, String newName) {
		try {
			entry = geoPackage.feature(oldName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		geoCol = entry.getGeometryColumn();
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		FeatureEntry newEntry = new FeatureEntry();
		newEntry.setTableName(newName);
		newEntry.setDataType(entry.getDataType());
		newEntry.setGeometryColumn(entry.getGeometryColumn());
		newEntry.setGeometryType(entry.getGeometryType());
		newEntry.setSrid(entry.getSrid());
		try {
			Filter filter = ff.not(ff.isNull(ff.property(geoCol)));
			SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
			SimpleFeatureType ftype = null;
			SimpleFeatureType newtype = null;
			List<SimpleFeature> features = new ArrayList<SimpleFeature>();
			while (feaReader.hasNext()) {
				SimpleFeature feature = feaReader.next();
				if (null == ftype) {
					ftype = feature.getFeatureType();
					SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
					builder.setName(newName);
					builder.setCRS(ftype.getCoordinateReferenceSystem());
					builder.setAttributes(ftype.getAttributeDescriptors());
					newtype = builder.buildFeatureType();
				}
				features.add(feature);
			}
			feaReader.close();
			SimpleFeatureCollection collection = new ListFeatureCollection(newtype, features);
			try {
				geoPackage.add(newEntry, collection);
				geoPackage.createSpatialIndex(newEntry);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				row.setLongitude(Double.parseDouble(feature.getAttribute("longitude").toString()));
				row.setLatitude(Double.parseDouble(feature.getAttribute("latitude").toString()));
				row.setX(Double.parseDouble(feature.getAttribute("x").toString()));
				row.setY(Double.parseDouble(feature.getAttribute("y").toString()));
				rows.add(row);
			}
			feaReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}

	public List<BuildingRow> query2(double x, double y) {
		List<BuildingRow> rows = null;
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		try {
			Point point = geometryFactory.createPoint(new Coordinate(x, y));
			Filter filter = ff.contains(ff.property(geoCol), ff.literal(point));
			SimpleFeatureReader feaReader = geoPackage.reader(entry, filter, null);
			rows = new ArrayList<BuildingRow>();
			while (feaReader.hasNext()) {
				SimpleFeature feature = feaReader.next();
				BuildingRow row = new BuildingRow();
				row.setqCode(Long.parseLong(feature.getAttribute("QCODE").toString()));
				row.setqName(feature.getAttribute("QNAME").toString());
				row.setJdCode(Long.parseLong(feature.getAttribute("JDCODE").toString()));
				row.setJdName(feature.getAttribute("JDNAME").toString());
				row.setSqCode(Long.parseLong(feature.getAttribute("SQCODE").toString()));
				row.setSqName(feature.getAttribute("SQNAME").toString());
				row.setBguid(feature.getAttribute("BGUID").toString());
				row.setCzwCode(feature.getAttribute("CZWCODE").toString());
				rows.add(row);
			}
			feaReader.close();
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
//		if (args.length != 1) {
//			System.exit(0);
//		}

	}

}
