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
public class SynonymDictionary extends ChineseDictionary {
	private static final String DICTIONARY_FILE = "synonym_dictionary";

	private SynonymDictionary() {
		super(DICTIONARY_FILE);
	}

	private static class SynonymInstance {
		private static final SynonymDictionary instance = new SynonymDictionary();
	}

	public static SynonymDictionary getInstance() {
		return SynonymInstance.instance;
	}

}
