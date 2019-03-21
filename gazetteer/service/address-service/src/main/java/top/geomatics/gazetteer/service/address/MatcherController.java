package top.geomatics.gazetteer.service.address;

import java.util.ArrayList;
import java.util.Collection;
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
import top.geomatics.gazetteer.model.EnterpriseRow;
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

	/**
	 * 
	 * examples:
	 * http://localhost:8080/matcher/address?keywords=%25深圳市龙华区龙华街道东环一路天汇大厦B座906室%25&min_sim=0.5&pagesize=10
	 * 
	 * @return
	 */
	@ApiOperation(value = "地址匹配", notes = "地址匹配")
	@GetMapping("/address")
	public String addressMatcher(
			@RequestParam(value = "keywords", required = true, defaultValue = "龙华区") String keywords,
			@RequestParam(value = "min_sim", required = false, defaultValue = "0.5") Double min_sim,
			@RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize) {
		AddressRow row = new AddressRow();
		String fields = "address";
		String tablename = "dmdz";
		row.setAddress(keywords);
		//<分词>
		List<WordEntry> words = WordSegmenter.segment(keywords);
		for (WordEntry entry:words) {
			if (0==entry.getNature().compareToIgnoreCase("comm")) {//按社区来查询，减少查询范围
				tablename= entry.getName();
			}
		}
		//</分词>

		//<查询>
		Map<String, Object> map = DatabaseHelper.getRequestMap(fields, tablename, row, null, 0);
		List<AddressRow> rows = AddressServiceApplication.mapper.findLike(map);
		//</查询>
		if (rows.size() <= pagesize) {
			return JSON.toJSONString(rows);
		}
		//<相似性计算，并排序>
		List<ComparableAddress> sortAddresses = new ArrayList<ComparableAddress>();
		for (AddressRow arow:rows) {
			double sim = AddressSimilarity.jaccard(keywords,arow.getAddress());
			if (sim >= min_sim) {
				ComparableAddress selectedAddress = new ComparableAddress(arow.getAddress(),sim);
				sortAddresses.add(selectedAddress);
			}
		}
		Collections.sort(sortAddresses);
		//</相似性计算，并排序>
		return JSON.toJSONString(sortAddresses);
	}

	/**
	 * 
	 * examples:
	 * http://localhost:8080/matcher/address?keywords=%25深圳市龙华区龙华街道东环一路天汇大厦B座906室%25&min_sim=0.5&pagesize=10
	 * 
	 * @return
	 */
	@ApiOperation(value = "根据条件模糊查询", notes = "根据条件模糊查询")
	@GetMapping("/fuzzyquery")
	public String fuzzyEditWithConditions(
			@RequestParam(value = "fields", required = false, defaultValue = "*") String fields,
			@RequestParam(value = "tablename", required = false, defaultValue = "enterprise1") String tablename,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "street", required = false, defaultValue = "") String street,
			@RequestParam(value = "owner", required = false, defaultValue = "") String owner,
			@RequestParam(value = "address", required = false, defaultValue = "") String address,
			@RequestParam(value = "status", required = false, defaultValue = "") String status,
			@RequestParam(value = "modifier", required = false, defaultValue = "") String modifier,
			@RequestParam(value = "update_date", required = false, defaultValue = "") String update_date,
			@RequestParam(value = "orderby", required = false, defaultValue = "") String orderby,
			@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		EnterpriseRow row = new EnterpriseRow();
		row.setCode(code);
		row.setName(name);
		row.setStreet(street);
		row.setOwner(owner);
		row.setAddress(address);
		if (null != status && !status.isEmpty()) {
			int iStatus = Integer.parseInt(status);
			row.setStatus(iStatus);
		}
		row.setModifier(modifier);

		Map<String, Object> map = getRequestMap(fields, tablename, row, orderby, limit);
		List<EnterpriseRow> rows = mapper.findLike(map);
		return JSON.toJSONString(rows);
	}

}
