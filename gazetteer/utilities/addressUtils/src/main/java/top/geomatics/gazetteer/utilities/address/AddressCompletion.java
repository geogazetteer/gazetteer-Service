/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.Iterator;
import java.util.List;

import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * <em>地名地址补全</em><br>
 * 
 * @author whudyj
 *
 */
public class AddressCompletion {
	/**
	 * <em>判断地址中是否包含规定的社区，并返回社区名称</em><br>
	 * 
	 * @param address String 待判断的地址
	 * @return String 如果地址中包含规定的社区，则返回社区名称，否则，返回空字符串
	 */
	public static String getCommunity(String address) {
		String communityIncluded = "";
		for (String community : IGazetteerConstant.COMMUNITY_LIST) {
			if (address.contains(community)) {
				communityIncluded = community;
				break;
			}
		}
		return communityIncluded;
	}

	/**
	 * <em>判断地址中是否包含规定的街道，并返回街道名称</em><br>
	 * 
	 * @param address String 待判断的地址
	 * @return String 如果地址中包含规定的街道，则返回街道名称，否则，返回空字符串
	 */
	public static String getStreet(String address) {
		if (null == address) {
			return "";
		}
		String streetIncluded = "";
		for (String street : IGazetteerConstant.STREET_LIST) {
			if (address.contains(street)) {
				streetIncluded = street;
				break;
			}
		}
		return streetIncluded;
	}

	/**
	 * <em>根据社区名称确定所在的街道</em><br>
	 * 
	 * @param community String 社区名称
	 * @return String 社区所在的的街道，如果没有找到，则返回空字符串
	 */
	public static String getStreetOfCommunity(String community) {
		if (null == community) {
			return "";
		}
		String street = "";
		boolean isFound = false;
		Iterator<String> iterator = IGazetteerConstant.STREET_COMMUNITY_LIST_MAP.keySet().iterator();
		while (iterator.hasNext() && false == isFound) {
			String key = iterator.next();
			List<String> communityLsit = IGazetteerConstant.STREET_COMMUNITY_LIST_MAP.get(key);
			for (String str : communityLsit) {
				if (str.equalsIgnoreCase(community)) {
					street = key;
					isFound = true;
					break;
				}
			}

		}
		return street;
	}

	/**
	 * <em>根据街道名称确定所包含的社区</em><br>
	 * 
	 * @param street String 街道名称
	 * @return List<String> 街道所包含的社区，如果没有找到，则返回null
	 */
	public static List<String> getCommunityListofStreet(String street) {
		if (null == street) {
			return null;
		}
		List<String> communityLsit = null;
		boolean isFound = false;
		Iterator<String> iterator = IGazetteerConstant.STREET_COMMUNITY_LIST_MAP.keySet().iterator();
		while (iterator.hasNext() && false == isFound) {
			String key = iterator.next();
			if (key.equalsIgnoreCase(street)) {
				communityLsit = IGazetteerConstant.STREET_COMMUNITY_LIST_MAP.get(key);
				isFound = true;
				break;
			}

		}
		return communityLsit;
	}

	public static String complete(String address, String prefix, String community) {
		String communityIncluded = getCommunity(address);
		String streetIncluded = getStreet(address);
		String leftString = "";
		String rightString = "";
		// 从右向左补全社区信息
		if (!communityIncluded.isEmpty() && !streetIncluded.isEmpty()) {// 如果地址中含有社区信息和街道信息
			String[] strArray = address.split(streetIncluded);
			leftString = strArray[0];
			rightString = strArray[1];
			if (leftString.isEmpty() && null != prefix) {
				return prefix + rightString;
			} else {
				return address;
			}
		} else if (!communityIncluded.isEmpty() && streetIncluded.isEmpty()) {// 如果地址中含有社区信息，不包含街道信息
			String streetTemp = getStreetOfCommunity(communityIncluded);
			String[] strArray = address.split(communityIncluded);
			leftString = strArray[0];
			rightString = strArray[1];
			if (leftString.isEmpty() && null != prefix) {
				return prefix + streetTemp + rightString;
			} else {
				return leftString + streetTemp + rightString;
			}
		} else if (communityIncluded.isEmpty() && !streetIncluded.isEmpty()) {// 如果地址中不包含社区信息，但包含街道信息
			String[] strArray = address.split(streetIncluded);
			leftString = strArray[0];
			rightString = strArray[1];
			if (null != community) {
				rightString = community + rightString;
			}
			if (leftString.isEmpty() && null != prefix) {
				return prefix + rightString;
			} else {
				return leftString + rightString;
			}
		} else {// 如果地址中不包含社区信息，也不包含街道信息
			String streetTemp = "";
			if (null != community) {
				streetTemp = getStreetOfCommunity(community);
			}
			if (streetTemp.isEmpty()) {
				// 无法补全
				return "";
			} else {
				if (null != prefix && address.contains(prefix)) {
					String[] strArray = address.split(prefix);
					leftString = strArray[0];
					rightString = strArray[1];
					return leftString + streetTemp + rightString;
				} else {
					// 无法补全
					return "";
				}
			}
		}
	}

	public static String complete(String address) {
		return complete(address, null, null);
	}

	public static String complete(String address, String prefix) {
		return complete(address, prefix, null);
	}

	public static String complete(String address, Double x, Double y) {
		return complete(address, null, null);
	}

}
