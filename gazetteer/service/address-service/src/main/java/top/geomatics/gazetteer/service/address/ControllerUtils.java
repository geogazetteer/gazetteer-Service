/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.fastjson.JSON;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.database.EnterpriseAddressMapper;
import top.geomatics.gazetteer.database.EnterpriseDatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.BuildingPositionRow;
import top.geomatics.gazetteer.model.ComparableAddress;
import top.geomatics.gazetteer.model.EnterpriseRow;
import top.geomatics.gazetteer.model.MatcherResultRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;

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
		responseString += JSON.toJSONString(rows);
		responseString += "}";
		return responseString;

	}

	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody2(List<EnterpriseRow> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows);
		responseString += "}";
		return responseString;

	}
	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody3(List<ComparableAddress> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows);
		responseString += "}";
		return responseString;

	}
	/**
	 * @param rows
	 * @return
	 */
	public static String getResponseBody4(List<SimpleAddressRow> rows) {
		String responseString = "{ \"total\": " + rows.size() + ", \"rows\": ";
		responseString += JSON.toJSONString(rows);
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
		responseString += JSON.toJSONString(rows);
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
		responseString += JSON.toJSONString(rows);
		responseString += "}";
		return responseString;

	}
	
	
	/**
	 * @param updateRows
	 * @return
	 */
	public static String getUpdateResponseBody(Integer updateRows) {
		//{"update": "ok","total": 10}
		return "{\"update\": \"ok\",\"total\":" +  updateRows + "}";
	}
	

}
