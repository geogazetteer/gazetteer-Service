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
public class AliasDictionary extends ChineseDictionary {
	private static final String DICTIONARY_FILE = "alias_dictionary";

	private AliasDictionary() {
		super(DICTIONARY_FILE);
	}

	private static class AliasInstance {
		private static final AliasDictionary instance = new AliasDictionary();
	}

	public static AliasDictionary getInstance() {
		return AliasInstance.instance;
	}

}
