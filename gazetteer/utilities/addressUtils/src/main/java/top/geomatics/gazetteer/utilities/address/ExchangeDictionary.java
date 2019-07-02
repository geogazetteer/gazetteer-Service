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
public class ExchangeDictionary extends ChineseDictionary {
	private static final String DICTIONARY_FILE = "exchange_dictionary";

	private ExchangeDictionary() {
		super(DICTIONARY_FILE);
	}

	private static class ExchangeInstance {
		private static final ExchangeDictionary instance = new ExchangeDictionary();
	}

	public static ExchangeDictionary getInstance() {
		return ExchangeInstance.instance;
	}

}
