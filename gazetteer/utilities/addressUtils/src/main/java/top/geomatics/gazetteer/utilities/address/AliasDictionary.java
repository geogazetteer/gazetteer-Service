/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * <em>别名</em><br>
 * 
 * @author whudyj
 *
 */
public class AliasDictionary {
	private static final String DICTIONARY_FILE = "alias_dictionary";
	private ChineseDictionary dictionary = new ChineseDictionary(DICTIONARY_FILE);

	private AliasDictionary() {
	}

	private static class AliasInstance {
		private static final AliasDictionary instance = new AliasDictionary();
	}

	public static AliasDictionary getInstance() {
		return AliasInstance.instance;
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
