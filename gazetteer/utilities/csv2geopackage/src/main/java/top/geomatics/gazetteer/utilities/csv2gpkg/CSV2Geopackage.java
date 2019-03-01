/**
 * 
 */
package top.geomatics.gazetteer.utilities.csv2gpkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geopkg.Entry;
import org.geotools.geopkg.FeatureEntry;
import org.geotools.geopkg.GeoPackage;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.opencsv.CSVReader;

/**
 * @author whudyj 将CSV文件转换为GeoPackage文件
 */
public class CSV2Geopackage {

	/**
	 * @param args 第一个参数为CSV文件路径名，第二个参数为GeoPackage文件路径名
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.exit(0);
		}
		CSVReader csvReader = null;
		boolean isHeader = true;
		long count = 0l;
		int length = 0;

		//

		try {
			csvReader = new CSVReader(new FileReader(args[0]));
			String[] nextLine = null;
			GeoPackage geopkg = new GeoPackage(new File(args[1]));
			geopkg.init();
			FeatureEntry entry = new FeatureEntry();
			entry.setDataType(Entry.DataType.Feature);
			entry.setSrid(4326);
			DefaultFeatureCollection fcollection = null;
			SimpleFeatureType ftype = null;
			SimpleFeatureTypeBuilder sftBuilder = new SimpleFeatureTypeBuilder();
			sftBuilder.setCRS(DefaultGeographicCRS.WGS84);
			sftBuilder.setName("dmdz");
			while ((nextLine = csvReader.readNext()) != null) {
				// nextLine[] is an array of values from the line

				for (String str : nextLine) {
					System.out.print(str + "\t");
				}
				System.out.println();

				if (isHeader) {
					isHeader = false;
					length = nextLine.length;
					for (int i = 0; i < length; i++) {
						sftBuilder.add(nextLine[i], String.class);
					}
					ftype = sftBuilder.buildFeatureType();
					fcollection = new DefaultFeatureCollection("id", ftype);
					continue;
				}
				if (length != nextLine.length) {
					System.out.println("Warning");
				}
				fcollection.add(SimpleFeatureBuilder.build(ftype, nextLine, null));
				count++;

			}
			geopkg.add(entry, fcollection);
			System.out.println(count);
			csvReader.close();
			geopkg.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
