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
public class HomophoneDictionary extends ChineseDictionary {
	private static final String DICTIONARY_FILE = "homophone_dictionary";

	private HomophoneDictionary() {
		super(DICTIONARY_FILE);
	}

	private static class HomophoneInstance {
		private static final HomophoneDictionary instance = new HomophoneDictionary();
	}

	public static HomophoneDictionary getInstance() {
		return HomophoneInstance.instance;
	}

}
