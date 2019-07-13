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

import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.SimpleAddressRow;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;
import top.geomatics.gazetteer.utilities.database.BuildingQuery;

/**
 * @author whudyj
 *
 */
public class CoordinateQuery {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(CoordinateQuery.class);

	private static Map<String, List<SimpleAddressRow>> buildingCodeQueryResults;
	private static Map<String, List<SimpleAddressRow>> coordQueryResults;

	/**
	 * <b>根据输入的坐标搜索,获得搜索结果</b><br>
	 * 
	 * @param keywords 输入的坐标
	 * @return 搜索结果
	 */
	public static List<SimpleAddressRow> getCoordQueryResults(String keywords) {
		List<SimpleAddressRow> rowsTotal = new ArrayList<>();
		if (!AddressProcessor.isCoordinatesExpression(keywords)) {
			return rowsTotal;
		}
		String coordString[] = keywords.split(",");
		double x = Double.parseDouble(coordString[0]);
		double y = Double.parseDouble(coordString[1]);
		List<String> codes = BuildingQuery.getInstance().query(x, y);

		for (String code : codes) {
			// 根据建筑物编码搜索
			List<SimpleAddressRow> rows = getBuildingCodeQueryResults(code);
			rowsTotal.addAll(rows);
		}
		return rowsTotal;
	}

	/**
	 * <b>根据输入的建筑物编码,获得搜索结果</b><br>
	 * 
	 * @param keywords 建筑物编码
	 * @return 搜索结果
	 */
	public static List<SimpleAddressRow> getBuildingCodeQueryResults(String keywords) {
		List<SimpleAddressRow> rows = null;
		if (!AddressProcessor.isBuildingCode(keywords)) {
			return rows;
		}
		String fields = "id,address";
		String tablename = AddressProcessor.getCommunityFromBuildingCode(keywords);
		AddressRow aRow = new AddressRow();
		aRow.setCode(keywords);
		Map<String, Object> map = ControllerUtils.getRequestMap(fields, tablename, aRow, null, 0);
		rows = ControllerUtils.mapper.findSimpleEquals(map);

		return rows;
	}

	public static int getCoordQuerys(String keywords) {
		if (null == coordQueryResults) {
			coordQueryResults = new HashMap<String, List<SimpleAddressRow>>();
		}
		if (!coordQueryResults.containsKey(keywords)) {
			List<SimpleAddressRow> rows = getCoordQueryResults(keywords);
			coordQueryResults.put(keywords, rows);
		}
		List<SimpleAddressRow> rows_t = coordQueryResults.get(keywords);

		return rows_t.size();
	}

	public static int getCodeQuerys(String keywords) {
		if (null == buildingCodeQueryResults) {
			buildingCodeQueryResults = new HashMap<String, List<SimpleAddressRow>>();
		}
		if (!buildingCodeQueryResults.containsKey(keywords)) {
			List<SimpleAddressRow> rows = getBuildingCodeQueryResults(keywords);
			buildingCodeQueryResults.put(keywords, rows);
		}
		List<SimpleAddressRow> rows_t = buildingCodeQueryResults.get(keywords);

		return rows_t.size();
	}

	public static List<SimpleAddressRow> getCoordQuerysPage(String keywords, int pageIndex, int pageSize) {
		if (null == coordQueryResults || !coordQueryResults.containsKey(keywords)) {
			getCoordQuerys(keywords);
		}
		List<SimpleAddressRow> saRows = coordQueryResults.get(keywords);
		List<SimpleAddressRow> rows = new ArrayList<SimpleAddressRow>();
		int start = (pageIndex - 1) * pageSize;
		int end = (start + pageSize) < saRows.size() ? (start + pageSize) : saRows.size();
		for (int i = start; i < end; i++) {
			rows.add(saRows.get(i));
		}
		return rows;
	}

	public static List<SimpleAddressRow> getCodeQuerysPage(String keywords, int pageIndex, int pageSize) {
		if (null == buildingCodeQueryResults || !buildingCodeQueryResults.containsKey(keywords)) {
			getCodeQuerys(keywords);
		}
		List<SimpleAddressRow> saRows = buildingCodeQueryResults.get(keywords);
		List<SimpleAddressRow> rows = new ArrayList<SimpleAddressRow>();
		int start = (pageIndex - 1) * pageSize;
		int end = (start + pageSize) < saRows.size() ? (start + pageSize) : saRows.size();
		for (int i = start; i < end; i++) {
			rows.add(saRows.get(i));
		}
		return rows;
	}

}
