/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * @author whudyj
 *
 */
public class SensitiveDictionary {
	private static final String DICTIONARY_FILE = "sensitive_dictionary";
	private ChineseDictionary dictionary = new ChineseDictionary(DICTIONARY_FILE);

	private SensitiveDictionary() {
		super();
	}

	private static class SensitiveInstance {
		private static final SensitiveDictionary instance = new SensitiveDictionary();
	}

	public static SensitiveDictionary getInstance() {
		return SensitiveInstance.instance;
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
