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
 * 地址匹配服务
 * 
 * @author whudyj
 *
 */
@RestController
@RequestMapping("/matcher")
public class MatcherController {

	/**
	 * 
	 * examples:
	 * http://localhost:8080/matcher/address?keywords=东环一路天汇大厦&min_sim=0.1&pagesize=10
	 * 
	 * @return
	 */
	@ApiOperation(value = "地址匹配", notes = "地址匹配")
	@GetMapping("/address")
	public String addressMatcher(
			@RequestParam(value = "keywords", required = true, defaultValue = IGazetteerConstant.LH_DISTRICT) String keywords,
			@RequestParam(value = "min_sim", required = false, defaultValue = "0.1") Double min_sim,
			@RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize) {
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
