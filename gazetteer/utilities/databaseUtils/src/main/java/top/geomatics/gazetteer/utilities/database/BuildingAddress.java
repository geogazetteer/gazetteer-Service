/**
 * 
 */
package top.geomatics.gazetteer.utilities.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import top.geomatics.gazetteer.database.AddressMapper;
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.BuildingPositionRow;
import top.geomatics.gazetteer.model.GeoPoint;
import top.geomatics.gazetteer.model.IGazetteerConstant;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;

/**
 * @author whudyj
 *
 */
public class BuildingAddress {
	public static DatabaseHelper helper = new DatabaseHelper();
	public static SqlSession session = helper.getSession();
	public static AddressMapper mapper = session.getMapper(AddressMapper.class);

	public static List<AddressRow> findAddressByCoords(double x, double y) {
		List<AddressRow> rows = new ArrayList<>();
		// 根据坐标找到建筑物编码
		List<String> codes = BuildingQuery.query(x, y);
		// 根据建筑物编码找到标准地址
		for (String code : codes) {
			// 根据建筑物编码搜索
			String fields = "id,address";
			String tablename = AddressProcessor.getCommunityFromBuildingCode(code);
			// 查找条件
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sql_fields", fields);
			map.put("sql_tablename", tablename);
			map.put("code", code);
			List<AddressRow> temprows = mapper.findEquals(map);
			rows.addAll(temprows);
		}
		return rows;
	}

	public static List<BuildingPositionRow> findCoordsByAddress(String address) {
		//
		List<BuildingPositionRow> rows = new ArrayList<>();
		// 根据地址找到建筑物编码
		String tablename = "dmdz";
		for (String community : IGazetteerConstant.COMMUNITY_LIST) {
			if (address.contains(community)) {
				tablename = community;
			}
		}
		String fields = "id,code";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql_fields", fields);
		map.put("sql_tablename", tablename);
		map.put("address", address);
		List<AddressRow> temprows = mapper.findEquals(map);
		// 根据建筑物编码找到坐标
		for (AddressRow row : temprows) {
			String code = row.getCode();
			GeoPoint point = BuildingQuery.query(code);
			BuildingPositionRow aRow = new BuildingPositionRow();
			aRow.setCode(code);
			aRow.setLongitude(point.getX());
			aRow.setLatitude(point.getY());
			rows.add(aRow);
			// 只找到第一个
			break;
		}
		return rows;
	}

}