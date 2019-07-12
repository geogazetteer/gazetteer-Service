/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * <em>通假字</em><br>
 * 
 * @author whudyj
 *
 */
public class ExchangeDictionary {
	private static final String DICTIONARY_FILE = "exchange_dictionary";
	private ChineseDictionary dictionary = new ChineseDictionary(DICTIONARY_FILE);

	private ExchangeDictionary() {
	}

	private static class ExchangeInstance {
		private static final ExchangeDictionary instance = new ExchangeDictionary();
	}

	public static ExchangeDictionary getInstance() {
		return ExchangeInstance.instance;
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
