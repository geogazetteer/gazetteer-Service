/**
 * 
 */
package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;
import top.geomatics.gazetteer.utilities.database.building.BuildingQuery;

/**
 * @author whudyj
 *
 */
public class QueryUtils {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(QueryUtils.class);

	private static Map<String, List<SimpleAddressRow>> rows_map = null;

	public static String getCountNameLike(String keywords) {
		List<SimpleAddressRow> rows = new ArrayList<SimpleAddressRow>();
		String fileds = "fid,name_,code_,longitude_,latitude_,origin_address";
		String tablename = "dmdz_edit";
		AddressEditorRow row = new AddressEditorRow();
		row.setName_("%" + keywords + "%");
		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fileds, tablename, row, null, 0);
		DatabaseManager dm = DatabaseManager.getInstance();
		DatabaseInformation[] dbInfos = dm.list();
		BuildingQuery buildingQuery = new BuildingQuery();
		buildingQuery.open();
		for (DatabaseInformation dbInformation : dbInfos) {
			List<AddressEditorRow> rows_edit = null;
			if (null != dbInformation) {
				logger.debug("查询数据库： " + dbInformation.getDbPath());
				rows_edit = dbInformation.getMapper().findLike(map);
			}
			if (null != rows_edit && rows_edit.size() > 0) {
				// 数据库二次查找
				for (AddressEditorRow re : rows_edit) {
					String code = re.getCode_();
					Double lon = re.getLongitude_();
					Double lat = re.getLatitude_();
					String oAddress = re.getOrigin_address();
					String flds = "id,address";
					String tname = "dmdz";

					if (null != oAddress && !oAddress.isEmpty()) {
						// 先根据地址查找
						AddressRow arow = new AddressRow();
						arow.setAddress("%" + oAddress + "%");
						Map<String, Object> map_t = ControllerUtils.getRequestMap(flds, tname, arow, null, 0);
						List<SimpleAddressRow> rows_t = ControllerUtils.mapper.findSimpleLike(map_t);

						rows.addAll(rows_t);
					} else if (null != code && !code.isEmpty()) {
						// 再根据代码查找
						AddressRow arow = new AddressRow();
						arow.setCode(code);
						Map<String, Object> map_t = ControllerUtils.getRequestMap(flds, tname, arow, null, 0);
						List<SimpleAddressRow> rows_t = ControllerUtils.mapper.findSimpleEquals(map_t);

						rows.addAll(rows_t);
					} else if (null != lon && null != lat) {
						// 再根据经纬度查找
						List<String> codes = buildingQuery.query(lon, lat);
						for (String code_t : codes) {
							// 根据建筑物编码搜索
							AddressRow arow = new AddressRow();
							arow.setCode(code_t);
							Map<String, Object> map_t = ControllerUtils.getRequestMap(flds, tname, arow, null, 0);
							List<SimpleAddressRow> rows_t = ControllerUtils.mapper.findSimpleEquals(map_t);

							rows.addAll(rows_t);
						}
					}

				}

			}
		}
		buildingQuery.close();
		if (rows_map == null) {
			rows_map = new HashMap<String, List<SimpleAddressRow>>();
		}
		rows_map.put(keywords, rows);
		String resFormat = "{ \"total\": " + "%d" + "}";
		return String.format(resFormat, rows.size());
	}

	public static String queryPage(String keywords, Integer index, Integer limit) {

		if (null == rows_map || !rows_map.containsKey(keywords)) {
			getCountNameLike(keywords);
		}
		List<SimpleAddressRow> rows = rows_map.get(keywords);
		List<SimpleAddressRow> rows_t = new ArrayList<SimpleAddressRow>();
		int start = (index - 1) * limit;
		int end = (start + limit) < rows.size() ? (start + limit) : rows.size();
		for (int i = start; i < end; i++) {
			rows_t.add(rows.get(i));
		}
		return ControllerUtils.getResponseBody4(rows_t);
	}

}
