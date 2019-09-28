/**
 * 
 */
package top.geomatics.gazetteer.utilities.database;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.utilities.database.excel2gpkg.Excel2Geopackage;
import top.geomatics.gazetteer.utilities.database.shp2gpkg.Shapefile2Geopackage;

/**
 * @author whudyj
 *
 */
public class DataImpot {

	public boolean dataImport(String importXML, String upload_file_path, String download_file_path) {
		String destFN = upload_file_path + File.separator + importXML;
		File xmlFile = new File(destFN);
		if (!xmlFile.exists()) {
			return false;
		}
		Field[] fileds = AddressEditorRow.class.getDeclaredFields();

		Document document = load(destFN);
		Element root = document.getRootElement();
		List<Element> nodes = root.elements("DataFile");
		Iterator<Element> it = nodes.iterator();
		while (it.hasNext()) {
			Element elm = it.next();
			// 文件名和设置
			String fnString = "";
			String geometryString = "";
			List<Element> fElements = elm.elements("FileName");
			if (fElements.size() > 0) {
				fnString = fElements.get(0).getText();
			} else {
				continue;
			}
			if (fnString.trim().isEmpty()) {
				continue;
			}
			Map<String, String> settings = new HashMap<String, String>();
			List<Element> sElements = elm.elements("Settings");
			if (sElements.size() > 0) {
				Element se = sElements.get(0);
				List<Element> se2 = se.elements("Fields");
				if (se2.size() > 0) {
					Element se3 = se2.get(0);
					List<Element> lse3 = se3.elements("Field");
					if (lse3.size() > 0) {
						Element se4 = lse3.get(0);
						for (int i = 0; i < fileds.length; i++) {
							Field fld = fileds[i];
							String fldName = fld.getName();
							List<Element> lse4 = se4.elements(fldName);
							if (lse4.size() > 0) {
								Element se5 = lse4.get(0);
								String orgin = se5.getText();
								if (!orgin.isEmpty()) {
									settings.put(orgin, fldName);
								}
							}

						}
						List<Element> lse4_g = se4.elements("build_geometry");

						if (lse4_g.size() > 0) {
							Element se5 = lse4_g.get(0);
							geometryString = se5.getText();
							if (!geometryString.isEmpty()) {
								// settings.put("buildGeometry", geometryString);
							}
						}

					}
				}

			}
			String fname = fnString.substring(0, fnString.lastIndexOf('.'));
			String ftype = fnString.substring(fnString.lastIndexOf('.') + 1, fnString.length());
			String sourceFN = upload_file_path + File.separator + fnString;
			String targetFN = download_file_path + File.separator + fname + ".gpkg";

			if (ftype.compareToIgnoreCase("shp") == 0) {
				Shapefile2Geopackage s2g = new Shapefile2Geopackage(sourceFN, targetFN, settings);
				s2g.execute();
			} else if (ftype.compareToIgnoreCase("xlsx") == 0) {
				Excel2Geopackage s2g = new Excel2Geopackage(sourceFN, targetFN, settings);
				s2g.execute();
			}

		}
		return true;
	}

	private Document load(String filename) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(filename)); // 读取XML文件,获得document对象
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

}
