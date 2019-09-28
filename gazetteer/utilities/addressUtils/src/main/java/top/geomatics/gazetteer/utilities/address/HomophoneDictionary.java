/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * <em>同音字</em>
 * 
 * @author whudyj
 *
 */
public class HomophoneDictionary {
	private static final String DICTIONARY_FILE = "homophone_dictionary";
	private ChineseDictionary dictionary = new ChineseDictionary(DICTIONARY_FILE);

	private HomophoneDictionary() {
	}

	private static class HomophoneInstance {
		private static final HomophoneDictionary instance = new HomophoneDictionary();
	}

	public static HomophoneDictionary getInstance() {
		return HomophoneInstance.instance;
	}

	public String getSynonym(String word) {
		return dictionary.getSynonym(word);
	}

	public boolean containsKey(String word) {
		return dictionary.containsKey(word);
	}

	public boolean contains(String word) {
		return dictionary.contains(word);
	}

	public String replace(String word) {
		return dictionary.replace(word);
	}

}
