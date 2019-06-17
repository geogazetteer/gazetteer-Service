/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * @author whudyj
 *
 */
public class AddressGuessor {
	public static String guessStreet(AddressEditorRow originRow) {
		String street = originRow.getStreet_();
		street = street.trim();
		// 规则1:从历史地址中推测
		if (street.isEmpty()) {
			String originAddress = originRow.getOrigin_address().trim();
			if (null != originAddress && !originAddress.isEmpty()) {
				for (String str : IGazetteerConstant.STREET_LIST) {
					if (originAddress.contains(str)) {
						street = str;
						break;
					}
				}
			}
		}

		return street;
	}

	public static String guessCommunity(AddressEditorRow originRow) {
		String community = originRow.getCommunity_();
		community = community.trim();
		// 规则1:从历史地址中推测
		if (community.isEmpty()) {
			String originAddress = originRow.getOrigin_address().trim();
			if (null != originAddress && !originAddress.isEmpty()) {
				for (String str : IGazetteerConstant.COMMUNITY_LIST) {
					if (originAddress.contains(str)) {
						community = str;
						break;
					}
				}
			}
		}

		return community;
	}

}
