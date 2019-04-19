package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.luhuiguo.chinese.ChineseUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.segment.WordSegmenter;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;

/**
 * @author chenfa
 */

@Api(value="/transform",tags="特殊地址转换处理")
@RestController
@RequestMapping("/transform")
public class TransformController {
	
	/**
	 * <em>全角字符转换为半角字符，示例：１2３</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/full?chars=１2３
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "全角转半角", notes = "全角字符转换为半角字符，示例：/transform/full?chars=１2３")
	@GetMapping("/full")
    public  String fullToHalf(
    		@ApiParam(value = "全角字符，如 １2３")
    		@RequestParam(value = "chars", required = true)  String input) {
             
        return JSON.toJSONString(AddressProcessor.fullToHalf(input));
    }	
    
	/**
	 * <em>繁体汉字转换为简体汉字，示例：龍華</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/complex?chars=龍華
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "繁体汉字转换为简体汉字", notes = "繁体汉字转换为简体汉字，示例：/transform/complex?chars=龍華")
	@GetMapping("/complex")
    public String comToSimple(
    		@ApiParam(value = "繁体汉字，如 龍華")
    		@RequestParam(value = "chars", required = true)  String input){
 		
		//繁体转中文简体
		return JSON.toJSONString(AddressProcessor.comToSimple(input));

   }
	
	/**
	 * <em>汉字数字转换为阿拉伯数字，示例：一,二,三</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/digital?chars=百-->
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "汉字数字转换为阿拉伯数字", notes = "汉字数字转换为阿拉伯数字，示例：/transform/digital?chars=百")
	@GetMapping("/digital")
    public  String chineseToNumber(
    		@ApiParam(value = "汉字数字，如 百")
    		@RequestParam(value = "chars", required = true)  String input){
        
        return JSON.toJSONString(AddressProcessor.chineseToNumber(input));
    }
	
	/**
	 * <em>识别地址中的门牌号，返回门牌号</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/houseNumber?chars=深圳市龙华区大浪街道同胜社区石观工业区19栋3楼
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "识别门牌号", notes = "识别门牌号，示例：/transform/houseNumber?chars=深圳市龙华区大浪街道同胜社区石观工业区19栋3楼")
	@GetMapping("/houseNumber")
    public String recognizeHouse(
    		@ApiParam(value = "地址，如深圳市龙华区大浪街道同胜社区石观工业区19栋3楼")
    		@RequestParam(value = "chars", required = true)  String input){
		//繁体转中文简体
		return JSON.toJSONString(WordSegmenter.segment(input));

   }
    
}
