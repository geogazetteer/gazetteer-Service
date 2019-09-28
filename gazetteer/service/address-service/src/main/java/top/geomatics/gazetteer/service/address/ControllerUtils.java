/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.BuildingPositionRow;
import top.geomatics.gazetteer.model.ComparableAddress;
import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.GeoPoint;
import top.geomatics.gazetteer.model.IGazetteerConstant;
import top.geomatics.gazetteer.model.MatcherResultRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;
import top.geomatics.gazetteer.model.SimpleAddressRow2;

/**
 * @author whudyj
 *
 */
public class ControllerUtils {

	public static DatabaseHelper helper = new DatabaseHelper();
	public static SqlSession session = helper.getSession();
	public static AddressMapper mapper = session.getMapper(AddressMapper.class);

	public static EnterpriseDatabaseHelper helper2 = new EnterpriseDatabaseHelper();
	public static SqlSession session2 = helper2.getSession();
	public static EnterpriseAddressMapper mapper2 = session2.getMapper(EnterpriseAddressMapper.class);

	/**
	 * @param fields
	 * @param tablename
	 * @param row
	 * @param orderby
	 * @param limit
	 * @return
	 */
	public static Map<String, Object> getRequestMap(String fields, String tablename, AddressRow row, String orderby,
			int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", tablename);
		if (null != row) {
			// 13个字段
			String province = row.getProvince();
			String city = row.getCity();
			String district = row.getDistrict();
			String street = row.getStreet();
			String community = row.getCommunity();
			String address = row.getAddress();
			String address_id = row.getAddress_id();
			String building = row.getBuilding();
			String building_id = row.getBuilding_id();
			String code = row.getCode();
			String road = row.getRoad();
			String road_num = row.getRoad_num();
			String village = row.getVillage();

			if (province != null && !province.isEmpty())
				map.put("province", province);
			if (city != null && !city.isEmpty())
				map.put("city", city);
			if (district != null && !district.isEmpty())
				map.put("district", district);
			if (street != null && !street.isEmpty())
				map.put("street", street);
			if (community != null && !community.isEmpty())
				map.put("community", community);
			if (address != null && !address.isEmpty())
				map.put("address", address);
			if (address_id != null && !address_id.isEmpty())
				map.put("address_id", address_id);
			if (building != null && !building.isEmpty())
				map.put("building", building);
			if (building_id != null && !building_id.isEmpty())
				map.put("building_id", building_id);
			if (code != null && !code.isEmpty())
				map.put("code", code);
			if (road != null && !road.isEmpty())
				map.put("road", road);
			if (road_num != null && !road_num.isEmpty())
				map.put("road_num", road_num);
			if (village != null && !village.isEmpty())
				map.put("village", village);
		}

		if (orderby != null && !orderby.isEmpty())
			map.put("sql_orderBy", orderby);
		if (limit > 0)
			map.put("sql_limit", limit);
		return map;
	}

	/**
	 * @param fields
	 * @param tablename
	 * @param row
	 * @param orderby
	 * @param limit
	 * @return
	 */
	public static Map<String, Object> getRequestMap2(String fields, String tablename, EnterpriseRow row, String orderby,
			int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", tablename);
		if (null != row) {
			// 11个字段
			String code = row.getCode();
			String name = row.getName();
			String street = row.getStreet();
			String owner = row.getOwner();
			String address = row.getAddress();
			// geometry字段;
			Integer iStatus = row.getStatus();
			String modifier = row.getModifier();
			Date date = row.getUpdate_date();
			String update_address = row.getUpdate_address();
			String update_address_id = row.getUpdate_address_id();

			if (code != null && !code.isEmpty())
				map.put("code", code);
			if (name != null && !name.isEmpty())
				map.put("name", name);
			if (street != null && !street.isEmpty())
				map.put("street", street);
			if (owner != null && !owner.isEmpty())
				map.put("owner", owner);
			if (address != null && !address.isEmpty())
				map.put("address", address);

			if (iStatus != null) {
				String status = iStatus.toString();
				if (!status.isEmpty()) {
					map.put("status", status);
				}
			}
			if (modifier != null && !modifier.isEmpty())
				map.put("modifier", modifier);
			if (date != null) {
				String update_date = date.toString();
				if (!update_date.isEmpty()) {
					map.put("update_date", update_date);
				}
			}
			if (update_address != null && !update_address.isEmpty())
				map.put("update_address", update_address);
			if (update_address_id != null && !update_address_id.isEmpty())
				map.put("update_address_id", update_address_id);
		}

		if (orderby != null && !orderby.isEmpty())
			map.put("sql_orderBy", orderby);
		if (limit > 0)
			map.put("sql_limit", limit);
		return map;
	}

	/**
	 * @param fields
	 * @param tablename
	 * @param row
	 * @param orderby
	 * @param limit
	 * @return
	 */
	public static Map<String, Object> getRequestMap_revision(String fields, String tablename, AddressEditorRow row,
			String orderby, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", tablename);
		if (null != row) {
			// 字段
			Integer fid = row.getFid();
			String name_ = row.getName_();
			String code_ = row.getCode_();

			String street_ = row.getStreet_();
			String community_ = row.getCommunity_();
			String origin_address = row.getOrigin_address();
			String similar_address = row.getSimilar_address();
			String standard_address = row.getStandard_address();
			// geometry字段;
			Integer iStatus = row.getStatus();
			String modifier = row.getModifier();
			Date date = row.getUpdate_date();
			String update_address = row.getUpdate_address();
			String update_building_code = row.getUpdate_building_code();

			if (fid != null) {
				String fidString = fid.toString();
				if (!fidString.isEmpty()) {
					map.put("fid", fidString);
				}
			}
			if (name_ != null && !name_.isEmpty())
				map.put("name_", name_);
			if (code_ != null && !code_.isEmpty())
				map.put("code_", code_);

			if (street_ != null && !street_.isEmpty())
				map.put("street_", street_);
			if (community_ != null && !community_.isEmpty())
				map.put("community_", community_);
			if (origin_address != null && !origin_address.isEmpty())
				map.put("origin_address", origin_address);
			if (similar_address != null && !similar_address.isEmpty())
				map.put("similar_address", similar_address);
			if (standard_address != null && !standard_address.isEmpty())
				map.put("standard_address", standard_address);

			if (iStatus != null) {
				String status = iStatus.toString();
				if (!status.isEmpty()) {
					map.put("status", status);
				}
			}
			if (modifier != null && !modifier.isEmpty())
				map.put("modifier", modifier);
			if (date != null) {
				String update_date = date.toString();
				if (!update_date.isEmpty()) {
					map.put("update_date", update_date);
				}
			}
			if (update_address != null && !update_address.isEmpty())
				map.put("update_address", update_address);
			if (update_building_code != null && !update_building_code.isEmpty())
				map.put("update_building_code", update_building_code);
		}

		if (orderby != null && !orderby.isEmpty())
			map.put("sql_orderBy", orderby);
		if (limit > 0)
			map.put("sql_limit", limit);
		return map;
	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody(List<AddressRow> rows) {
		/*
		 * { "total": 3, "rows": [ { "id": 0, "name": "Item 0", "price": "$0" }, { "id":
		 * 1, "name": "Item 1", "price": "$1" } ] }
		 */
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		// 使用阿里巴巴的fastjson
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody2(List<EnterpriseRow> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody_revision(List<AddressEditorRow> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody3(List<ComparableAddress> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody4(List<SimpleAddressRow> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody5(List<BuildingPositionRow> rows) {
		/*
		 * { "total": 3, "rows": [ { "id": 0, "name": "Item 0", "price": "$0" }, { "id":
		 * 1, "name": "Item 1", "price": "$1" } ] }
		 */
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		// 使用阿里巴巴的fastjson
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody6(List<MatcherResultRow> rows) {
		/*
		 * { "total": 3, "rows": [ { "id": 0, "name": "Item 0", "price": "$0" }, { "id":
		 * 1, "name": "Item 1", "price": "$1" } ] }
		 */
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		// 使用阿里巴巴的fastjson
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody7(List<SimpleAddressRow2> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows, SerializerFeature.DisableCircularReferenceDetect);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param updateRows
	 * @return
	 */
	public static String getUpdateResponseBody(Integer updateRows) {
		// {"update": "ok","total": 10}
		return "{\"update\": \"ok\",\"total\":" + updateRows + "}";
	}

	/**
	 * @param code
	 * @return
	 */
	public static AddressRow getAddressRowByCode(String code) {
		code = coding(code);
		// 街道，取前9位
		String streetCode = code.substring(0, 9);
		String streetString = null;
		// 社区，取前12位
		String communityCode = code.substring(0, 12);
		String communityString = null;

		for (int i = 0; i < IGazetteerConstant.STREET_CODE_LIST.size(); i++) {
			String street_code = IGazetteerConstant.STREET_CODE_LIST.get(i);
			if (0 == streetCode.compareToIgnoreCase(street_code)) {
				streetString = IGazetteerConstant.STREET_LIST.get(i);
			}
		}

		for (int i = 0; i < IGazetteerConstant.COMMUNITY_CODE_LIST.size(); i++) {
			String community_code = IGazetteerConstant.COMMUNITY_CODE_LIST.get(i);
			if (0 == communityCode.compareToIgnoreCase(community_code)) {
				communityString = IGazetteerConstant.COMMUNITY_LIST.get(i);
			}
		}

		AddressRow row = new AddressRow();
		row.setCommunity(communityString);
		row.setStreet(streetString);

		return row;
	}

	/**
	 * @param code
	 * @return
	 */
	public static String coding(String code) {
		String code_t = code;
		// 440306 009003 1200105
		// 440306 007003 3600024 000002
		// 只取前面19位
		if (code.length() > 19) {
			code_t = code.substring(0, 19);
		}
		return code_t;
	}

	public static List<GeoPoint> getPointsByCodes(List<String> codes) {
		List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();

		String TABLENAME = "building_position";
		String fields = "longitude,latitude";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", TABLENAME);

		// sqlite无法一一对应
//		map.put("list", codes);
//
//		List<BuildingPositionRow> building_rows = mapper.selectBuildingByCodes(map);
//		
//		for (BuildingPositionRow row : building_rows) {
//			GeoPoint point = new GeoPoint();
//			point.setX(row.getLongitude());
//			point.setY(row.getLatitude());
//			geoPoints.add(point);
//		}

		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i);
			code = coding(code);
			map.put("code", code);
			List<BuildingPositionRow> building_rows = mapper.findBuildingEquals(map);
			// 如果没有找到
			if (null == building_rows || building_rows.size() < 1) {
				geoPoints.add(null);
				continue;
			}
			// 一般只能找到一个
			BuildingPositionRow bRow = building_rows.get(0);
			GeoPoint point = new GeoPoint();
			point.setX(bRow.getLongitude());
			point.setY(bRow.getLatitude());
			geoPoints.add(point);
		}

		return geoPoints;
	}

	public static GeoPoint getPointByCode(String code) {

		String TABLENAME = "building_position";
		String fields = "longitude,latitude";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", TABLENAME);

		String code_t = coding(code);
		map.put("code", code_t);
		List<BuildingPositionRow> building_rows = mapper.findBuildingEquals(map);
		// 如果没有找到
		if (null == building_rows || building_rows.size() < 1) {
			return null;
		}
		// 一般只能找到一个
		BuildingPositionRow bRow = building_rows.get(0);
		GeoPoint point = new GeoPoint();
		point.setX(bRow.getLongitude());
		point.setY(bRow.getLatitude());
		return point;
	}

}
