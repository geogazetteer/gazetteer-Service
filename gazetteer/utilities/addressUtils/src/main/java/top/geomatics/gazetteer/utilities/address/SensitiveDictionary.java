/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * @author whudyj
 *
 */
public class SensitiveDictionary extends ChineseDictionary {
	private static final String DICTIONARY_FILE = "sensitive_dictionary";

	private SensitiveDictionary() {
		super(DICTIONARY_FILE);
	}

	private static class SensitiveInstance {
		private static final SensitiveDictionary instance = new SensitiveDictionary();
	}

	public static SensitiveDictionary getInstance() {
		return SensitiveInstance.instance;
	}

}
