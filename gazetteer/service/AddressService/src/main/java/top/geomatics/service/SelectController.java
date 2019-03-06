package top.geomatics.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import top.geomatics.utils.SqlliteUtil;

@RestController
@RequestMapping("/AddressWebService")
public class SelectController {

	// 查询全部数据（测试前10条）
	@RequestMapping("/select")
	public String select2() {
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

	// 根据详细地址address查询
	@RequestMapping("/selectByAddress")
	public String selectByAddress() {
		SqlliteUtil sqlliteUtil = new SqlliteUtil();
		List list = sqlliteUtil.selectByAddress("dmdz", "广东省深圳市龙华区民治街道龙塘社区上塘农贸建材市场L25号铁皮房");
		// 使用阿里巴巴的fastjson
		String json = JSON.toJSONString(list);
		System.out.println(json);
		return json;
	}

	// 根据地理编码code查询
	@RequestMapping("/selectByCode")
	public String selectByCode() {
		SqlliteUtil sqlliteUtil = new SqlliteUtil();
		List list = sqlliteUtil.selectByCode("dmdz", "44030600960102T0117");
		// 使用阿里巴巴的fastjson
		String json = JSON.toJSONString(list);
		System.out.println(json);
		return json;
	}

	// 综合地址查询
	@RequestMapping("/selectByMul")
	public String selectByMul() {

		return null;

	}

}
