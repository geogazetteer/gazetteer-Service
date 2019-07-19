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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.ComparableAddress;
import top.geomatics.gazetteer.model.IGazetteerConstant;
import top.geomatics.gazetteer.utilities.address.AddressSimilarity;

/**
 * <b>地址匹配服务</b><br>
 * <i>说明</i><br>
 * <i>目前地址匹配服务还不完善</i>
 * 
 * @author whudyj
 */
@Api(value = "/matcher", tags = "地名地址匹配服务")
@RestController
@RequestMapping("/matcher")
public class MatcherController {

	/**
	 * <b>查询与指定关键词匹配的地址</b><br>
	 * examples: <br>
	 * <!--http://localhost:8083/matcher/address?keywords=东环一路天汇大厦&min_sim=0.1 -->
	 * 
	 * @param keywords String 请求参数，指定的关键词
	 * @param min_sim  Double 请求参数，指定的最小相似度
	 * @param limit    Integer 请求参数，指定返回记录个数
	 * @return String 返回JSON格式的查询结果
	 */
	@ApiOperation(value = "地址匹配", notes = "查询与指定关键词匹配的地址")
	@GetMapping("/address")
	public String addressMatcher(
			@ApiParam(value = "查询关键词，如 东环一路天汇大厦") @RequestParam(value = IControllerConstant.QUERY_KEYWORDS, required = true, defaultValue = IGazetteerConstant.LH_DISTRICT) String keywords,
			@ApiParam(value = "最小相似度") @RequestParam(value = IControllerConstant.SIMILARITY_MIN, required = false, defaultValue = "0.2") Double min_sim,
			@ApiParam(value = "最多匹配个数") @RequestParam(value = IControllerConstant.SQL_LIMIT, required = false, defaultValue = "10") Integer limit) {
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
//					shortKeywords = keywords.substring(keywords.indexOf(str) + str.length());
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
		int limit_max = limit < 1?30:limit;//
		Map<String, Object> map = ControllerUtils.getRequestMap(IControllerConstant.ADDRESS_FIELDS, tablename, row,
				null, limit_max);
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
		if (sortAddresses.size() >= limit) {
			sortAddresses = sortAddresses.subList(0, limit);
		}
		// </相似性计算，并排序>
		return ControllerUtils.getResponseBody3(sortAddresses);
	}

	/**
	 * <b>度量两个地址的相似度</b><br>
	 * examples: <br>
	 * <!--http://localhost:8083/matcher/fuzzy?address1=广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋&address2=深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2
	 * -->
	 * 
	 * @param address1 String 请求参数，指定的标准地址
	 * @param address2 String 请求参数，指定的待比较（或查询）的地址
	 * @return String 返回JSON格式的相似度计算结果
	 */
	@ApiOperation(value = "度量两个地址的相似度", notes = "相似度指标包括余弦距离、模糊分数、Jaccard相似度和Jaro Winkler 相似度")
	@GetMapping("/fuzzy")
	public String addressFuzzy(
			@ApiParam(value = "标准地址，示例：广东省深圳市龙华区民治街道白石龙社区逸秀新村17栋") @RequestParam(value = "address1", required = true) String address1,
			@ApiParam(value = "待比较（或查询）的地址，示例：深圳市龙华区民治街道白石龙社区白石龙一区58栋58-2") @RequestParam(value = "address2") String address2) {

		return JSON.toJSONString(AddressSimilarity.indicator(address1, address2));
	}

}
