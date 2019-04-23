/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.database.csv.MYCSVReader;

/**
 * <em>同音字</em>
 * 
 * @author whudyj
 *
 */
public class HomophoneDictionary {
	// 同义词文件
	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String DICTIONARY_FILE = "homophone_dictionary";

	private static String dic_file_name = manager.getValue(DICTIONARY_FILE);

	public static Map<String, List<String>> dictionary = new HashMap<>();

	// 读取同音字
	static {
		MYCSVReader reader = new MYCSVReader(dic_file_name);
		reader.openFile();
		List<String[]> dataList = reader.getData();
		reader.closeFile();

		Map<String, List<String>> pinyinMap = new HashMap<>();
		for (int i = 0; i < dataList.size(); i++) {
			String[] row = dataList.get(i);
			if (row.length < 3) {
				continue;
			}
			String wordString = row[1].trim();
			String yinString = row[2].trim();
			yinString = yinString.substring(0, yinString.length() - 1);
			if (!pinyinMap.containsKey(yinString)) {
				List<String> words = new ArrayList<>();
				pinyinMap.put(yinString, words);
			}
			List<String> allWords = pinyinMap.get(yinString);
			allWords.add(wordString);
		}
		for (String yin : pinyinMap.keySet()) {
			List<String> words = pinyinMap.get(yin);
			for (int j = 0; j < words.size(); j++) {
				String keyString = words.get(j).trim();
				List<String> resultList = new ArrayList<>();
				for (int i = 0; i < words.size(); i++) {
					if (i == j) {
						continue;
					}
					String string = words.get(i);
					if (resultList.contains(string)) {
						continue;
					}
					resultList.add(string);
				}

				dictionary.put(keyString, resultList);
			}
		}
	}

	public static List<String> getHomophone(String word) {
		return dictionary.get(word);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (String key : dictionary.keySet()) {
			List<String> value = dictionary.get(key);
			System.out.println(key + ": " + value.toString());
		}
		System.out.println(HomophoneDictionary.getHomophone("啊"));
		System.out.println(HomophoneDictionary.getHomophone("阿").toString());

	}

}
