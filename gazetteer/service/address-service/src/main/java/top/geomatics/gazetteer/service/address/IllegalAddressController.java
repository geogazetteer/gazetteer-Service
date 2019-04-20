package top.geomatics.gazetteer.service.address;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.model.BoundingBox;
import top.geomatics.gazetteer.segment.WordSegmenter;

//非法地址判别
@Api(value = "/illegal_address", tags = "非法地址判别")
@Controller
@RequestMapping("/illegal_address")
public class IllegalAddressController {

	public static final Set<String> Address_word = new HashSet<>();
	static {
		Address_word.add("龙华");
		Address_word.add("大浪");
		Address_word.add("民治");
		Address_word.add("福城");
		Address_word.add("观湖");
		Address_word.add("观澜");
		Address_word.add("三联");
		Address_word.add("上芬");
		Address_word.add("北站");
		Address_word.add("华联");
		Address_word.add("同胜");
		Address_word.add("君子布");
		Address_word.add("大富");
		Address_word.add("大岭");
		Address_word.add("大水坑");
		Address_word.add("大水田");
		Address_word.add("富康");
		Address_word.add("广培");
		Address_word.add("库坑");
		Address_word.add("新澜");
		Address_word.add("新牛");
		Address_word.add("新田");
		Address_word.add("新石");
		Address_word.add("景龙");
		Address_word.add("松元厦");
		Address_word.add("松和");
		Address_word.add("桂花");
		Address_word.add("桂香");
		Address_word.add("桔塘");
		Address_word.add("樟坑径");
		Address_word.add("樟坑");
		Address_word.add("民乐");
		Address_word.add("民强");
		Address_word.add("民新");
		Address_word.add("民泰");
		Address_word.add("油松");
		Address_word.add("浪口");
		Address_word.add("润城");
		Address_word.add("清华");
		Address_word.add("清湖");
		Address_word.add("牛湖");
		Address_word.add("玉翠");
		Address_word.add("白石龙");
		Address_word.add("福民");
		Address_word.add("章阁");
		Address_word.add("茜坑");
		Address_word.add("观城");
		Address_word.add("高峰");
		Address_word.add("鹭湖");
		Address_word.add("黎光");
		Address_word.add("龙园");
		Address_word.add("龙城(作废)");
		Address_word.add("龙城");
		Address_word.add("龙平");
		Address_word.add("龙胜");
	}
	/**
	 * 返回值是true表示是严重错误数据，false在合理范围内
	 * @param query
	 * @return
	 */
	@ApiOperation(value = "判断输入地址是否是严重错误的地址", notes = "判断输入地址是否是严重错误的地址")
    @GetMapping("/error")
    @ResponseBody 
    public String isErrorAddress(@RequestParam("query") String query){
		String flag="true";
		WordSegmenter ws=new WordSegmenter();
		List<String>ls= ws.segment(query);
		for(String s:ls) {
			Iterator i=Address_word.iterator();
			while(i.hasNext()) {
				if(s.contains(i.next().toString())) {
					flag="false";
					break;
				}
			}
			
		}
       return flag;
    }
	
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	@ApiOperation(value = "判断输入的坐标是否越界", notes = "判断输入的坐标是否越界")
    @GetMapping("/out_of_boundary")
    @ResponseBody 
    public String isErrorAddress(
    		@ApiParam(value = "指定x坐标，如x=503361.375")
			@RequestParam(value = "x", required = true) Double x,
			@ApiParam(value = "指定y坐标，如y=2506786.75")
			@RequestParam(value = "y", required = true) Double y){
		String flag="true";
		BoundingBox bb=new BoundingBox();
		Double maxx=bb.getMaxx();
		Double maxy=bb.getMaxy();
		Double minx=bb.getMinx();
		Double miny=bb.getMiny();
		if(x>=minx&&x<=maxx&&y>=miny&&y<=maxy) {
			flag="fasle";
		}
       return flag;
    }	
	
}

