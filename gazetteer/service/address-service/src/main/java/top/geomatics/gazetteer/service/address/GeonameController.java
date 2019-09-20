package top.geomatics.gazetteer.service.address;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.AddressEditorRow;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.GeoPoint;
import top.geomatics.gazetteer.model.SimpleAddressRow;
import top.geomatics.gazetteer.model.SimpleAddressRow2;
import top.geomatics.gazetteer.utilities.database.building.BuildingQueryExt;

/**
 * <b>地名/POI查询服务</b><br>
 * 
 * @author whudyj
 *
 */
/**
 * @author whudyj
 *
 */
@Api(value = "/poi", tags = "地名/POI查询服务")
@RestController
@RequestMapping("/poi")
public class GeonameController {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(GeonameController.class);

	// 高得地图API Key和URL
	private final static String API_KEY = "2b499f1c9f1fe0b10e175fdfd452fcb2";
	private final static String API_URL_GEO = "https://restapi.amap.com/v3/geocode/geo";
	private final static String CITY_SZ = "深圳";

	private static final String TABLENAME = "dmdz_edit";// 表名

	private static Map<String, List<SimpleAddressRow>> rows_map = null;
	private static Map<String, List<SimpleAddressRow2>> poi_rows_map = null;
	private static int maxHits = 100;// 为了保证查询耗时短，限制找到的个数。如果找到100个，就不再找了。

	/**
	 * <b>查询数据</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/poi/all?fields=fid,origin_address%26tablename=dmdz_edit%26limit=10
	 * </i>
	 * 
	 * @param dbPath    String 请求参数，数据库目录
	 * @param fields    String 请求参数，需要选择的字段，多个字段以,分隔，如：fid,origin_address
	 * @param tablename String 请求参数，指定查询的数据库表，如：dmdz_edit
	 * @param orderby   String 请求参数，指定查询结果排序方式
	 * @param limit     int 请求参数，限定查询的记录个数，如：limit=10
	 * @param row       AddressEditorRow 指定查询条件
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "查询数据", notes = "查询数据。示例：/poi/all?fields=fid,origin_address&tablename=dmdz_edit&limit=10")
	@GetMapping("/all")
	public String editAll(@ApiParam(value = "数据库目录") @RequestParam(value = "dbPath", required = false) String dbPath,
			@ApiParam(value = "查询字段，如 fid,origin_address") @RequestParam(value = IControllerConstant.TABLE_FIELDS, required = false, defaultValue = "*") String fields,
			@ApiParam(value = "查询的数据库表，如:dmdz_edit") @RequestParam(value = IControllerConstant.TABLE_NAME, required = false, defaultValue = TABLENAME) String tablename,
			@ApiParam(value = "查询结果排序方式") @RequestParam(value = IControllerConstant.SQL_ORDERBY, required = false, defaultValue = "") String orderby,
			@ApiParam(value = "限定查询的记录个数，不指定或指定值为0表示查询所有数据") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "0") int limit,
			AddressEditorRow row) {
		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fields, tablename, row, orderby, limit);
		List<AddressEditorRow> rows = null;
		if (dbPath == null || dbPath.isEmpty()) {
			// 查询所有数据库
			rows = new ArrayList<AddressEditorRow>();
			for (DatabaseInformation dbInformation : DatabaseManager.getInstance().list()) {
				List<AddressEditorRow> rows_t = null;
				if (null != dbInformation) {
					rows_t = dbInformation.getMapper().findEquals(map);
				}
				if (null != rows_t) {
					rows.addAll(rows_t);
				}
			}
		} else {
			DatabaseInformation dbInformation = DatabaseManager.getInstance().getDbInfo(dbPath);
			if (null != dbInformation) {
				rows = dbInformation.getMapper().findEquals(map);
			}
		}

		return ControllerUtils.getResponseBody_revision(rows);
	}

	/**
	 * <b>返回查询的地名/POI记录总数</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/poi/sum?tablename=dmdz_edit </i>
	 * 
	 * @param keywords String 请求参数，查询关键词，如：深圳市乐宾科技有限公司
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "返回查询的地名/POI记录总数", notes = "返回查询的地名/POI记录总数")
	@GetMapping("name/sum")
	public String getCountNameLike(
			@ApiParam(value = "查询关键词，如深圳市乐宾科技有限公司") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true) String keywords,
			@ApiParam(value = "API_KEY") @RequestParam(value = "高得", required = false, defaultValue = API_KEY) String api_key) {

		return getCountPoiNameLike(keywords, api_key);
	}

	/**
	 * <b>分页查询地名/POI数据</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/poi/name/page/1?keywords=深圳市乐宾科技有限公司%26limit=10 </i>
	 * 
	 * @param keywords String 请求参数，查询关键词，如：深圳市乐宾科技有限公司
	 * @param index    Integer 路径变量，当前页，从1开始
	 * @param limit    Integer 请求参数，限定每页查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "分页查询地名/POI数据", notes = "分页查询地名/POI数据，示例：/poi/page/1?keywords=深圳市乐宾科技有限公司&limit=10")
	@GetMapping("name/page/{index}")
	public String queryAllPage(
			@ApiParam(value = "查询关键词，如深圳市乐宾科技有限公司") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true) String keywords,
			@ApiParam(value = "API_KEY") @RequestParam(value = "高得", required = false, defaultValue = API_KEY) String api_key,
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit) {

		return queryPoiPage(api_key, keywords, index, limit);
	}

	/**
	 * <b>返回查询的标准地址记录总数</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/poi/sum?tablename=dmdz_edit </i>
	 * 
	 * @param code String 请求参数，
	 * @return String 返回记录总数
	 */
	@ApiOperation(value = "返回查询的标准地址记录总数", notes = "返回查询的标准地址记录总数")
	@GetMapping("address/sum")
	public String getCountAddress(
			@ApiParam(value = "建筑物编码") @RequestParam(value = "code", required = false) String code,
			@ApiParam(value = "经度") @RequestParam(value = "longitude", required = false) Double x,
			@ApiParam(value = "纬度") @RequestParam(value = "latitude", required = false) Double y,
			@ApiParam(value = "原地址") @RequestParam(value = "address", required = false) String address) {

		AddressEditorRow row = new AddressEditorRow();
		row.setCode_(code);
		row.setLongitude_(x);
		row.setLatitude_(y);
		row.setOrigin_address(address);

		return getCount(row);
	}

	/**
	 * <b>分页查询数据</b><br>
	 * <i>examples:<br>
	 * http://localhost:8083/poi/name/page/1?keywords=深圳市乐宾科技有限公司%26limit=10 </i>
	 * 
	 * @param keywords String 请求参数，查询关键词，如：深圳市乐宾科技有限公司
	 * @param index    Integer 路径变量，当前页，从1开始
	 * @param limit    Integer 请求参数，限定每页查询的记录个数，如：limit=10
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "分页查询标准地址数据", notes = "分页查询标准地址数据，示例：/poi/page/1?keywords=深圳市乐宾科技有限公司&limit=10")
	@GetMapping("address/page/{index}")
	public String queryAllPageAddress(
			@ApiParam(value = "建筑物编码") @RequestParam(value = "code", required = false) String code,
			@ApiParam(value = "经度") @RequestParam(value = "longitude", required = false) Double x,
			@ApiParam(value = "纬度") @RequestParam(value = "latitude", required = false) Double y,
			@ApiParam(value = "原地址") @RequestParam(value = "address", required = false) String address,
			@ApiParam(value = "当前页面索引，从1开始") @PathVariable(value = "index", required = true) Integer index,
			@ApiParam(value = "限定每页查询的记录个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit) {

		AddressEditorRow row = new AddressEditorRow();
		row.setCode_(code);
		row.setLongitude_(x);
		row.setLatitude_(y);
		row.setOrigin_address(address);

		return queryPage(row, index, limit);
	}

	private String getCountPoiNameLike(String keywords, String api_key) {

		List<SimpleAddressRow2> poi_rows_t = getCountFromDB(keywords);
		if (null == poi_rows_t || poi_rows_t.size() < 1) {
			// 用高得地图找
			poi_rows_t = getAddressFromAmap(keywords, api_key);
		}

		if (poi_rows_map == null) {
			poi_rows_map = new HashMap<String, List<SimpleAddressRow2>>();
		}
		poi_rows_map.put(keywords, poi_rows_t);
		String resFormat = "{ \"total\": " + "%d" + "}";
		return String.format(resFormat, poi_rows_t.size());
	}

	private List<SimpleAddressRow2> getCountFromDB(String keywords) {
		String fileds = "fid,name_,code_,longitude_,latitude_,origin_address";
		String tablename = "dmdz_edit";
		AddressEditorRow row = new AddressEditorRow();
		row.setName_("%" + keywords + "%");
		Map<String, Object> map = ControllerUtils.getRequestMap_revision(fileds, tablename, row, null, maxHits);
		DatabaseManager dm = DatabaseManager.getInstance();
		DatabaseInformation[] dbInfos = dm.list();
		List<AddressEditorRow> poi_rows = new ArrayList<AddressEditorRow>();

		for (DatabaseInformation dbInformation : dbInfos) {
			if (poi_rows.size() >= maxHits) {
				break;
			}
			List<AddressEditorRow> rows_edit = null;
			if (null != dbInformation) {
				logger.debug("查询数据库： " + dbInformation.getDbPath());
				rows_edit = dbInformation.getMapper().findLike(map);
			}
			if (null != rows_edit && rows_edit.size() > 0) {
				poi_rows.addAll(rows_edit);
			}
		}

		List<SimpleAddressRow2> poi_rows_t = new ArrayList<SimpleAddressRow2>();
		int max = poi_rows.size() > maxHits ? maxHits : poi_rows.size();
		for (int i = 0; i < max; i++) {
			AddressEditorRow adRow = poi_rows.get(i);
			SimpleAddressRow2 srow2 = getAddress(adRow);
			if (srow2 != null) {
				poi_rows_t.add(srow2);
			} else if (null != adRow)// 如果没有找到，直接用原来的信息
			{
				SimpleAddressRow2 srow3 = new SimpleAddressRow2();
				srow3.setId(-1);
				srow3.setAddress(adRow.getOrigin_address());
				srow3.setName(adRow.getName_());
				srow3.setX(adRow.getLongitude_());
				srow3.setY(adRow.getLatitude_());

				poi_rows_t.add(srow3);
			}
		}

		return poi_rows_t;

	}

	private String queryPoiPage(String api_key, String keywords, Integer index, Integer limit) {

		if (null == poi_rows_map || !poi_rows_map.containsKey(keywords)) {
			getCountPoiNameLike(keywords, api_key);
		}
		List<SimpleAddressRow2> rows = poi_rows_map.get(keywords);

		List<SimpleAddressRow2> rows_t = new ArrayList<SimpleAddressRow2>();
		int start = (index - 1) * limit;
		int end = (start + limit) < rows.size() ? (start + limit) : rows.size();
		for (int i = start; i < end; i++) {
			rows_t.add(rows.get(i));
		}
		return ControllerUtils.getResponseBody7(rows_t);
	}

	private SimpleAddressRow2 getAddress(AddressEditorRow row) {
		if (null == row) {
			return null;
		}
		String code = row.getCode_();
		Double lon = row.getLongitude_();
		Double lat = row.getLatitude_();
	
		SimpleAddressRow2 row2 = null;

		if (null != code && !code.isEmpty()) {
			// 根据代码查找
			row2 = getAddressByCode(code, row.getName_());
			if (null != row2) {
				if (null != lon && null != lat) {
					row2.setX(lon);
					row2.setY(lat);
				}else {
					//查询坐标
					GeoPoint point = ControllerUtils.getPointByCode(code);
					if (null != point) {
						row2.setX(point.getX());
						row2.setY(point.getY());
					}
				}
				return row2;
			}

		} else if (null != lon && null != lat) {
			// 再根据经纬度查找
			BuildingQueryExt buildingQuery = new BuildingQueryExt();
			buildingQuery.open();
			List<String> codes = buildingQuery.query(lon, lat);
			buildingQuery.close();
			if (codes.size() > 0) {
				row2 = getAddressByCode(codes.get(0), row.getName_());
				if (null != row2) {
					row2.setX(lon);
					row2.setY(lat);
					return row2;
				}
			}

		}
		/*
		 * else if (null != oAddress && !oAddress.isEmpty()) { // 最后根据地址查找 String flds =
		 * "id,address"; String tname = "dmdz"; AddressRow arow = new AddressRow();
		 * arow.setAddress("%" + oAddress + "%"); Map<String, Object> map_t =
		 * ControllerUtils.getRequestMap(flds, tname, arow, null, 1);
		 * List<SimpleAddressRow> rows_t = ControllerUtils.mapper.findSimpleLike(map_t);
		 * if (rows_t.size() > 0) { SimpleAddressRow srow = rows_t.get(0);
		 * SimpleAddressRow2 srow2 = new SimpleAddressRow2(); srow2.setId(srow.getId());
		 * srow2.setAddress(srow.getAddress()); srow2.setName(row.getName_()); return
		 * srow2; } }
		 */
		// 没有找到
		return null;
	}

	private SimpleAddressRow2 getAddressByCode(String code, String name) {
		String flds = "id,address";
		String tname = "dmdz";
		// 根据代码查找
		AddressRow arow = new AddressRow();
		arow.setCode(code);
		Map<String, Object> map_t = ControllerUtils.getRequestMap(flds, tname, arow, null, 1);
		List<SimpleAddressRow> rows_t = ControllerUtils.mapper.findSimpleEquals(map_t);

		if (rows_t.size() > 0) {
			SimpleAddressRow srow = rows_t.get(0);
			SimpleAddressRow2 row2 = new SimpleAddressRow2();
			row2.setId(srow.getId());
			row2.setCode(code);
			row2.setAddress(srow.getAddress());
			row2.setName(name);
			return row2;
		}
		return null;
	}

	private String getCount(AddressEditorRow row) {
		if (null == row) {
			return null;
		}
		String code = row.getCode_();
		Double lon = row.getLongitude_();
		Double lat = row.getLatitude_();
		String oAddress = row.getOrigin_address();
		String flds = "id,address";
		String tname = "dmdz";

		BuildingQueryExt buildingQuery = new BuildingQueryExt();
		buildingQuery.open();

		List<SimpleAddressRow> rows = new ArrayList<SimpleAddressRow>();

		if (null != code && !code.isEmpty()) {
			// 根据代码查找
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
		} else if (null != oAddress && !oAddress.isEmpty()) {
			// 最后根据地址查找
			AddressRow arow = new AddressRow();
			arow.setAddress("%" + oAddress + "%");
			Map<String, Object> map_t = ControllerUtils.getRequestMap(flds, tname, arow, null, 0);
			List<SimpleAddressRow> rows_t = ControllerUtils.mapper.findSimpleLike(map_t);

			rows.addAll(rows_t);
		}

		buildingQuery.close();
		if (rows_map == null) {
			rows_map = new HashMap<String, List<SimpleAddressRow>>();
		}
		rows_map.put(row.encoding(), rows);
		String resFormat = "{ \"total\": " + "%d" + "}";
		return String.format(resFormat, rows.size());
	}

	private String queryPage(AddressEditorRow row, Integer index, Integer limit) {

		if (null == rows_map || !rows_map.containsKey(row.encoding())) {
			getCount(row);
		}
		List<SimpleAddressRow> rows = rows_map.get(row.encoding());

		List<SimpleAddressRow> rows_t = new ArrayList<SimpleAddressRow>();
		int start = (index - 1) * limit;
		int end = (start + limit) < rows.size() ? (start + limit) : rows.size();
		for (int i = start; i < end; i++) {
			rows_t.add(rows.get(i));
		}
		return ControllerUtils.getResponseBody4(rows_t);
	}

	private List<SimpleAddressRow2> getAddressFromAmap(String keywords, String api_key) {
		StringBuffer parameters = new StringBuffer();
		parameters.append("key=" + api_key + "&address=" + keywords);
		parameters.append("&city=" + CITY_SZ);
		String res = sendGet(API_URL_GEO, parameters.toString());
//		logger.info(res);
		JSONObject jsonObject = JSONObject.parseObject(res);
		JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("geocodes"));

		int max = jsonArray.size() > maxHits ? maxHits : jsonArray.size();

		List<SimpleAddressRow2> poi_rows_t = new ArrayList<SimpleAddressRow2>();
		for (int i = 0; i < max; i++) {
			JSONObject location = (JSONObject) jsonArray.get(i);
			String ll = location.get("location").toString();
			String loc[] = ll.split(",");
			if (loc.length < 2) {
				continue;
			}
			Double x = Double.parseDouble(loc[0]);
			Double y = Double.parseDouble(loc[1]);
			String address = location.getString("formatted_address");
			AddressEditorRow adRow = new AddressEditorRow();
			adRow.setLongitude_(x);
			adRow.setLatitude_(y);
			adRow.setOrigin_address(address);
			adRow.setName_(keywords);

//	    	SimpleAddressRow2 srow2 = getAddress(adRow);
//			if (srow2 != null) {
//				poi_rows_t.add(srow2);
//			}else if(null != adRow)//如果没有找到，直接用原来的信息
			{
				SimpleAddressRow2 srow3 = new SimpleAddressRow2();
				srow3.setId(-1);
				srow3.setAddress(adRow.getOrigin_address());
				srow3.setName(adRow.getName_());
				srow3.setX(adRow.getLongitude_());
				srow3.setY(adRow.getLatitude_());

				poi_rows_t.add(srow3);
			}
		}

		return poi_rows_t;
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		CloseableHttpResponse response = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
//			logger.info("URL: " + realUrl);
			// 打开和URL之间的连接
			CloseableHttpClient httpclient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).build();
			HttpGet httpget = new HttpGet(realUrl.toURI());
			httpget.setConfig(requestConfig);
			response = httpclient.execute(httpget);
			Header[] headers = response.getAllHeaders();
//			for (Header header : headers) {
//				logger.info(header.getName() + "--->" + header.getValue());
//			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = ((org.apache.http.HttpEntity) entity).getContent();
				// 定义 BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(instream,"utf-8"));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}
		} catch (Exception e) {
			logger.info("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (null != response) {
					response.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

}
