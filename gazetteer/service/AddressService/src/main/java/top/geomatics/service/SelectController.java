package top.geomatics.service;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiOperation;
import top.geomatics.utils.SqlliteUtil;

@Controller
@RequestMapping("/address")
public class SelectController {

	@ApiOperation(value = "查看所有服务）", notes = "查看所有服务")
	@RequestMapping(value = "/services", method = { RequestMethod.GET })
	public String services() {
		return "services";
	}

	@ApiOperation(value = "查询全部数据（测试前10条）", notes = "查询全部数据（测试前10条）")
	@RequestMapping(value = "/select", method = { RequestMethod.GET })
	public @ResponseBody String select() {
		SqlliteUtil sqlliteUtil = new SqlliteUtil();
		// 查询总记录数
		// int count = sqlliteUtil.count("dmdz");
		// 测试前10条数据
		String sql = "select * from dmdz limit 10";
		List list = sqlliteUtil.selectAll(sql);
		// 使用阿里巴巴的fastjson
		String json = JSON.toJSONString(list);
		System.out.println(json);
		return json;

	}

	@ApiOperation(value = "根据详细地址address查询", notes = "根据详细地址address查询")
	@RequestMapping(value = "/selectByAddress/{address}", method = { RequestMethod.GET })
	public @ResponseBody String selectByAddress(@PathVariable(value = "address") String address) {
		SqlliteUtil sqlliteUtil = new SqlliteUtil();
		// 广东省深圳市龙华区民治街道龙塘社区上塘农贸建材市场L25号铁皮房
		List list = sqlliteUtil.selectByAddress("dmdz", address);
		String json = JSON.toJSONString(list);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "根据地理编码code查询", notes = "根据地理编码code查询")
	@RequestMapping(value = "/selectByCode/{code}", method = { RequestMethod.GET })
	public @ResponseBody String selectByCode(@PathVariable(value = "code") String code) {
		SqlliteUtil sqlliteUtil = new SqlliteUtil();
		// 44030600960102T0117
		List list = sqlliteUtil.selectByCode("dmdz", code);
		String json = JSON.toJSONString(list);
		System.out.println(json);
		return json;
	}

	@ApiOperation(value = "根据关键词查询", notes = "根据关键词查询")
	@RequestMapping(value = "/selectByKeyword/{keyword}", method = { RequestMethod.GET })
	public @ResponseBody String selectByKeyword(@PathVariable(value = "keyword") String keyword) {
		SqlliteUtil sqlliteUtil = new SqlliteUtil();
		// 44030600960102T0117
		List list = sqlliteUtil.selectByCode("dmdz", keyword);
		String json = JSON.toJSONString(list);
		System.out.println(json);
		return json;
	}

}
