/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * <em>同义词</em>
 * 
 * @author whudyj
 *
 */
public class SynonymDictionary {
	private static final String DICTIONARY_FILE = "synonym_dictionary";
	private ChineseDictionary dictionary = new ChineseDictionary(DICTIONARY_FILE);

	private SynonymDictionary() {
	}

	private static class SynonymInstance {
		private static final SynonymDictionary instance = new SynonymDictionary();
	}

	public static SynonymDictionary getInstance() {
		return SynonymInstance.instance;
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
