/**
 * 
 */
package top.geomatics.gazetteer.utilities.database;

import java.util.Date;
import java.util.List;
import java.util.Map;

import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.AddressRow;
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

	public static boolean updateValue(Map<String, Object> oldMap, AddressEditorRow newRow, boolean force) {
		if (null == oldMap || null == newRow) {
			return false;
		}
		AddressEditorRow oldRow = new AddressEditorRow();
		for (String fld : oldMap.keySet()) {
			Object value = oldMap.get(fld);
			if (null != value && 0 == fld.compareToIgnoreCase("name_")) {
				String name = (String) value;
				oldRow.setName_(name);
			} else if (null != value && 0 == fld.compareToIgnoreCase("code_")) {
				String code = (String) value;
				oldRow.setCode_(code);
			} else if (null != value && 0 == fld.compareToIgnoreCase("description_")) {
				String description = (String) value;
				oldRow.setDescription_(description);
			} else if (null != value && 0 == fld.compareToIgnoreCase("street_")) {
				String street = (String) value;
				oldRow.setStreet_(street);
			} else if (null != value && 0 == fld.compareToIgnoreCase("community_")) {
				String community = (String) value;
				oldRow.setCommunity_(community);
			} else if (null != value && 0 == fld.compareToIgnoreCase("longitude_")) {
				Double lon = (Double) value;
				oldRow.setLongitude_(lon);
			} else if (null != value && 0 == fld.compareToIgnoreCase("latitude_")) {
				Double lat = (Double) value;
				oldRow.setLatitude_(lat);
			} else if (null != value && 0 == fld.compareToIgnoreCase("origin_address")) {
				String OD = (String) value;
				oldRow.setOrigin_address(OD);
			} else if (null != value && 0 == fld.compareToIgnoreCase("similar_address")) {
				String SD = (String) value;
				oldRow.setSimilar_address(SD);
			} else if (null != value && 0 == fld.compareToIgnoreCase("standard_address")) {
				String STD = (String) value;
				oldRow.setStandard_address(STD);
			} else if (null != value && 0 == fld.compareToIgnoreCase("status")) {
				Integer status = (Integer) value;
				oldRow.setStatus(status);
			} else if (null != value && 0 == fld.compareToIgnoreCase("modifier")) {
				String modifier = (String) value;
				oldRow.setModifier(modifier);
			} else if (null != value && 0 == fld.compareToIgnoreCase("update_date")) {
				Date dt = (Date) value;
				oldRow.setUpdate_date(dt);
			} else if (null != value && 0 == fld.compareToIgnoreCase("update_address")) {
				String uD = (String) value;
				oldRow.setUpdate_address(uD);
			} else if (null != value && 0 == fld.compareToIgnoreCase("update_building_code")) {
				String bc = (String) value;
				oldRow.setUpdate_building_code(bc);
			}
		}
		return modifyRecordByAddress(oldRow, newRow, force);
	}

	public static boolean modifyRecordByAddress(AddressEditorRow oldRow, AddressEditorRow newRow, boolean force) {
		if (null == oldRow || null == newRow) {
			return false;
		}
		Integer oldFid = oldRow.getFid();
		newRow.setFid(oldFid);

		String old_name = oldRow.getName_();
		String old_Code = oldRow.getCode_();
		String old_Street = oldRow.getStreet_();
		String old_Community = oldRow.getCommunity_();
		String old_originAddress = oldRow.getOrigin_address();
		Double old_longitude = oldRow.getLongitude_();
		Double old_latitude = oldRow.getLatitude_();
		// 需要更新的内容
		boolean isNameEmp = true;
		boolean isCodeEmp = true;
		boolean isStreetEmp = true;
		boolean isCommunityEmp = true;
		boolean isOriginAddressEmp = true;
		boolean canUpdate = false;
		if (null != old_name && !old_name.trim().isEmpty()) {
			isNameEmp = false;
		}
		if (null != old_Code && !old_Code.trim().isEmpty()) {
			isCodeEmp = false;
		}
		if (null != old_Street && !old_Street.trim().isEmpty()) {
			isStreetEmp = false;
		}
		if (null != old_Community && !old_Community.trim().isEmpty()) {
			isCommunityEmp = false;
		}
		if (null != old_originAddress && !old_originAddress.trim().isEmpty()) {
			isOriginAddressEmp = false;
		}
		// 没有需要更新的内容
		if (false == isNameEmp && false == isCodeEmp && false == isStreetEmp && false == isCommunityEmp
				&& false == isOriginAddressEmp) {
			return canUpdate;
		}

		// 补充信息
		String new_Name = null;
		String new_Code = null;
		String new_Street = null;
		String new_Community = null;
		String new_OriginAddress = null;
		// 街道
		if (isStreetEmp) {
			if (!isOriginAddressEmp) {
				// 首先从原地址中推测
				new_Street = AddressGuessor.guessStreet(old_originAddress);
				if (null != new_Street && !new_Street.trim().isEmpty()) {
					isStreetEmp = false;
					newRow.setStreet_(new_Street);
					canUpdate = true;
				}
			}
		}
		// 更新了街道
		if (false == isNameEmp && false == isCodeEmp && false == isStreetEmp && false == isCommunityEmp
				&& false == isOriginAddressEmp) {
			return canUpdate;
		}
		// 社区
		if (isCommunityEmp) {
			if (!isOriginAddressEmp) {
				// 首先从原地址中推测
				new_Community = AddressGuessor.guessCommunity(old_originAddress);
				if (null != new_Community && !new_Community.trim().isEmpty()) {
					isCommunityEmp = false;
					newRow.setCommunity_(new_Community);
					canUpdate = true;
				}
			}
		}
		// 更新了社区
		if (false == isNameEmp && false == isCodeEmp && false == isStreetEmp && false == isCommunityEmp
				&& false == isOriginAddressEmp) {
			return canUpdate;
		}
		AddressRow arow = null;
		// 根据Code来推测
		if (false == isCodeEmp) {
			List<AddressRow> aRows = BuildingAddress.getInstance().findAddressByCode(old_Code);
			if (null != aRows && aRows.size() > 0) {
				// 推荐用第一条记录
				arow = aRows.get(0);
			}
		}
		// 如果没有找到
		if (null == arow) {
			// 最后，根据坐标来推测
			if (force) {
				List<AddressRow> aRows = null;
				if (null != old_longitude && null != old_latitude) {
					aRows = BuildingAddress.getInstance().findAddressByCoords(old_longitude, old_latitude);
				}
				if (null != aRows && aRows.size() > 0) {
					// 推荐用第一条记录
					arow = aRows.get(0);
				}
			}
		}
		// 如果找到了，需要更新
		if (null != arow) {
			new_Street = arow.getStreet();
			new_Community = arow.getCommunity();
			new_Code = arow.getCode();
			new_OriginAddress = arow.getAddress();
			if (isStreetEmp) {
				newRow.setStreet_(new_Street);
				isStreetEmp = false;
				canUpdate = true;
			}
			if (isCommunityEmp) {
				newRow.setCommunity_(new_Community);
				isCommunityEmp = false;
				canUpdate = true;
			}
			if (isCodeEmp) {
				newRow.setCode_(new_Code);
				isCodeEmp = false;
				canUpdate = true;
			}
			if (isOriginAddressEmp) {
				newRow.setOrigin_address(new_OriginAddress);
				isOriginAddressEmp = false;
				canUpdate = true;
			}
		}
		if (false == isNameEmp && false == isCodeEmp && false == isStreetEmp && false == isCommunityEmp
				&& false == isOriginAddressEmp) {
			return true;
		}
		// 更新name
		if (isNameEmp) {
			if (!isOriginAddressEmp && null != old_originAddress) {
				new_Name = old_originAddress;
			} else if (!isOriginAddressEmp && null != new_OriginAddress) {
				new_Name = new_OriginAddress;
			}
			if (null != new_Name && !new_Name.trim().isEmpty()) {
				newRow.setName_(new_Name);
				isNameEmp = false;
				canUpdate = true;
			}
		}
		if (false == isNameEmp && false == isCodeEmp && false == isStreetEmp && false == isCommunityEmp
				&& false == isOriginAddressEmp) {
			return true;
		}
		// 更新Address
		if (isOriginAddressEmp) {
			if (!isNameEmp && null != old_name) {
				new_OriginAddress = old_name;
			} else if (!isNameEmp && null != new_Name) {
				new_OriginAddress = new_Name;
			}
			if (null != new_OriginAddress && !new_OriginAddress.trim().isEmpty()) {
				newRow.setOrigin_address(new_OriginAddress);
				isOriginAddressEmp = false;
				canUpdate = true;
			}
		}
		if (false == isNameEmp && false == isCodeEmp && false == isStreetEmp && false == isCommunityEmp
				&& false == isOriginAddressEmp) {
			return canUpdate;
		}

		return canUpdate;
	}

}
