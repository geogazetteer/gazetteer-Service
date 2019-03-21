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
import top.geomatics.gazetteer.database.DatabaseHelper;
import top.geomatics.gazetteer.model.AddressRow;
import top.geomatics.gazetteer.model.ComparableAddress;
import top.geomatics.gazetteer.segment.WordEntry;
import top.geomatics.gazetteer.segment.WordSegmenter;
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
	private WordSegmenter seg = new WordSegmenter();

	/**
	 * 
	 * examples:
	 * http://localhost:8080/matcher/address?keywords=%25东环一路天汇大厦%25&min_sim=0.1&pagesize=10
	 * 
	 * @return
	 */
	@ApiOperation(value = "地址匹配", notes = "地址匹配")
	@GetMapping("/address")
	public String addressMatcher(
			@RequestParam(value = "keywords", required = true, defaultValue = "龙华区") String keywords,
			@RequestParam(value = "min_sim", required = false, defaultValue = "0.1") Double min_sim,
			@RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize) {
		AddressRow row = new AddressRow();
		String fields = "address";
		String tablename = "dmdz";
		row.setAddress(keywords);
		// <分词>

		List<WordEntry> words = seg.segment(keywords);
		for (WordEntry entry : words) {
			if (0 == entry.getNature().compareToIgnoreCase("comm")) {// 按社区来查询，减少查询范围
				tablename = entry.getName();
			}
		}
		// </分词>

		// <查询>
		Map<String, Object> map = DatabaseHelper.getRequestMap(fields, tablename, row, null, 0);
		List<AddressRow> rows = AddressServiceApplication.mapper.findLike(map);
		// </查询>
		if (rows.size() <= pagesize) {
			return JSON.toJSONString(rows);
		}
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
		return JSON.toJSONString(sortAddresses);
	}

}
