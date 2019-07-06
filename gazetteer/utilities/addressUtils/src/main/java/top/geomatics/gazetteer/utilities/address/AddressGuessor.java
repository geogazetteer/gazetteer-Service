/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * @author whudyj
 *
 */
public class AddressGuessor {
	public static String guessStreet(String originAddress) {
		// 规则1:从历史地址中推测
		String street = "";
		if (null != originAddress && !originAddress.isEmpty()) {
			for (String str : IGazetteerConstant.STREET_LIST) {
				if (originAddress.contains(str)) {
					street = str;
					break;
				}
			}
		}

		return street;
	}

	public static String guessCommunity(String originAddress) {
		String community = "";
		// 规则1:从历史地址中推测
		if (null != originAddress && !originAddress.isEmpty()) {
			for (String str : IGazetteerConstant.COMMUNITY_LIST) {
				if (originAddress.contains(str)) {
					community = str;
					break;
				}
			}
		}

		return community;
	}

}
