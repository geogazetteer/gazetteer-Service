/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.utilities.csv.MYCSVReader;

/**
 * <em>汉语字典，同义词、近义词、通假字、同音字</em><br>
 * 
 * @author whudyj
 *
 */
public class ChineseDictionary {
	private String config;
	// 同义词文件
	private ResourcesManager manager;
	private String dic_file_name;
	private Map<String, List<String>> dictionary;

	private Map<String, String> regexMap;

	public ChineseDictionary(String config) {
		super();
		this.config = config;
		manager = ResourcesManager.getInstance();
		dic_file_name = manager.getValue(this.config);
		
		dictionary = new HashMap<>();
		regexMap = new LinkedHashMap<String, String>();
		
		// 读取字典
		MYCSVReader reader = new MYCSVReader(dic_file_name);
		reader.openFile();
		List<String[]> dataList = reader.getData();

		for (int i = 0; i < dataList.size(); i++) {
			String[] row = dataList.get(i);
			if (row.length < 2) {
				continue;
			}

			for (int j = 0; j < row.length; j++) {
				String key = row[j];
				List<String> words = new ArrayList<>();
				for (int k = 0; k < row.length; k++) {
					if (k != j) {
						words.add(row[k]);
					}
				}
				dictionary.put(key, words);
			}
		}
		reader.closeFile();

		for (String key : dictionary.keySet()) {
			String regex = key.trim();
			List<String> words = dictionary.get(key);
			if (regex.isEmpty() || words.size() < 1) {
				continue;
			}
			String exper = words.get(0);
			regexMap.put(regex, exper);
		}
	}

	public String getSynonym(String word) {
		String synonym = "";
		List<String> words = dictionary.get(word);
		if (null != words && words.size() > 0) {
			synonym = words.get(0);
		}
		return synonym;
	}

	public boolean containsKey(String word) {
		boolean flag = false;
		flag = dictionary.containsKey(word);
		return flag;
	}

	public boolean contains(String word) {
		boolean flag = false;
		for (String key : dictionary.keySet()) {
			if (null != word && word.contains(key)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public String replace(String word) {
		String s = word;
		Pattern p;
		Matcher m;
		for (String regex : regexMap.keySet()) {
			p = Pattern.compile(regex);
			m = p.matcher(s);
			while (m.find()) {
				String exper = regexMap.get(regex);
				String text = m.group();
				String value = exper;
				s = s.replace(text, value);
			}
		}
		return s;
	}

}
