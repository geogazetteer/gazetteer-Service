package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiOperation;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.ComparableAddress;
import top.geomatics.gazetteer.model.IGazetteerConstant;
import top.geomatics.gazetteer.utilities.address.AddressSimilarity;

/**
 * <em>地址匹配服务</em><br>
 * <i>说明</i><br>
 * <i>目前地址匹配服务还不完善</i>
 * 
 * @author whudyj
 */
@RestController
@RequestMapping("/matcher")
public class MatcherController {

	/**
	 * <em>查询与指定关键词匹配的地址</em><br>
	 * examples: <br>
	 * <!--http://localhost:8083/matcher/address?keywords=东环一路天汇大厦&min_sim=0.1 -->
	 * 
	 * @param keywords String 请求参数，指定的关键词
	 * @param min_sim  Double 请求参数，指定的最小相似度
	 * @param limit    Integer 请求参数，指定返回记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "地址匹配", notes = "地址匹配")
	@GetMapping("/address")
	public String addressMatcher(
			@RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true, defaultValue = IGazetteerConstant.LH_DISTRICT) String keywords,
			@RequestParam(value = IControllerConstant.SIMILARITY_MIN, required = false, defaultValue = "0.1") Double min_sim,
			@RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit) {
		AddressRow row = new AddressRow();
		String tablename = "";
		String shortKeywords = keywords;
		// <分词>
		for (String str : IGazetteerConstant.COMMUNITY_LIST) {
			if (keywords.contains(str)) {
				tablename = str;
				// 取社区后面的内容
				shortKeywords = keywords.substring(keywords.indexOf(str) + str.length());
				break;
			}
		}
		if (true == tablename.isEmpty()) {
			for (String str : IGazetteerConstant.STREET_LIST) {
				if (keywords.contains(str)) {
					tablename = str;
					// 取街道后面的内容
					shortKeywords = keywords.substring(keywords.indexOf(str) + str.length());
					break;
				}
			}
		}
		if (true == tablename.isEmpty()) {
			tablename = IControllerConstant.ADDRESS_TABLE;
		}
		row.setAddress("%" + shortKeywords + "%");
		// </分词>

		// <查询>
		Map<String, Object> map = ControllerUtils.getRequestMap(IControllerConstant.ADDRESS_FIELDS2, tablename, row,
				null, 0);
		List<AddressRow> rows = ControllerUtils.mapper.findLike(map);
		// </查询>
		// <相似性计算，并排序>
		List<ComparableAddress> sortAddresses = new ArrayList<ComparableAddress>();
		for (AddressRow arow : rows) {
			double sim = AddressSimilarity.jaccard(keywords, arow.getAddress());
			if (sim >= min_sim) {
				ComparableAddress selectedAddress = new ComparableAddress(arow.getAddress(), sim);
				sortAddresses.add(selectedAddress);
			}
		}
		Collections.sort(sortAddresses);
		// </相似性计算，并排序>
		return ControllerUtils.getResponseBody3(sortAddresses);
	}

}
