/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.database.csv.MYCSVReader;

/**
 * <em>同义词</em>
 * 
 * @author whudyj
 *
 */
public class SynonymDictionary {
	// 同义词文件
	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String DICTIONARY_FILE = "synonym_dictionary";

	private static String dic_file_name = manager.getValue(DICTIONARY_FILE);

	public static Map<String, String> dictionary = new HashMap<>();

	// 读取同义词
	static {
		MYCSVReader reader = new MYCSVReader(dic_file_name);
		reader.openFile();
		List<String[]> dataList = reader.getData();

		for (int i = 0; i < dataList.size(); i++) {
			String[] row = dataList.get(i);
			for (int j = 0; j < row.length; j++) {
				for (int j2 = 0; j2 < row.length; j2++) {
					if (j2 != j) {
						String keyString = row[j].trim();
						String valueString = row[j2].trim();
						if (keyString.isEmpty() || valueString.isEmpty()) {
							continue;
						}
						dictionary.put(keyString, valueString);
						dictionary.put(valueString, keyString);
					}
				}
			}
		}
		reader.closeFile();
	}

	public static String getSynonym(String word) {
		return dictionary.get(word);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (String key : dictionary.keySet()) {
			String value = dictionary.get(key);
			System.out.println(key + ": " + value);
		}
		System.out.println(SynonymDictionary.getSynonym("中原"));
		System.out.println(SynonymDictionary.getSynonym("华夏"));

	}

}
