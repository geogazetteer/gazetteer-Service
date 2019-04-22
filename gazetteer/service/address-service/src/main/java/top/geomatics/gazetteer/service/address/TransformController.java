package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.geomatics.gazetteer.segment.WordSegmenter;
import top.geomatics.gazetteer.utilities.address.AddressProcessor;

/**
 * @author chenfa
 */

@Api(value = "/transform", tags = "特殊地址转换处理")
@RestController
@RequestMapping("/transform")
public class TransformController {

	/**
	 * <em>全角字符转换为半角字符，示例：１2３</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/full?chars=１2３ -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "全角转半角", notes = "全角字符转换为半角字符，示例：/transform/full?chars=１2３")
	@GetMapping("/full")
	public String fullToHalf(
			@ApiParam(value = "全角字符，如 １2３") @RequestParam(value = "chars", required = true) String input) {

		return JSON.toJSONString(AddressProcessor.fullToHalf(input));
	}

	/**
	 * <em>繁体汉字转换为简体汉字，示例：龍華</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/complex?chars=龍華 -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "繁体汉字转换为简体汉字", notes = "繁体汉字转换为简体汉字，示例：/transform/complex?chars=龍華")
	@GetMapping("/complex")
	public String comToSimple(
			@ApiParam(value = "繁体汉字，如 龍華") @RequestParam(value = "chars", required = true) String input) {

		// 繁体转中文简体
		return JSON.toJSONString(AddressProcessor.comToSimple(input));

	}

	/**
	 * <em>汉字数字转换为阿拉伯数字，示例：一,二,三</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/digital?chars=百-->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "汉字数字转换为阿拉伯数字", notes = "汉字数字转换为阿拉伯数字，示例：/transform/digital?chars=百")
	@GetMapping("/digital")
	public String chineseToNumber(
			@ApiParam(value = "汉字数字，如 百") @RequestParam(value = "chars", required = true) String input) {

		return JSON.toJSONString(AddressProcessor.chineseToNumber(input));
	}

	/**
	 * <em>识别地址中的门牌号，返回门牌号</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/houseNumber?chars=广东省深圳市龙华区大浪街道浪口社区华霆路58号38栋
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "识别门牌号", notes = "识别门牌号，示例：/transform/houseNumber?chars=广东省深圳市龙华区大浪街道浪口社区华霆路58号38栋")
	@GetMapping("/houseNumber")
	public String recognizeHouse(
			@ApiParam(value = "地址，如广东省深圳市龙华区大浪街道浪口社区华霆路58号38栋") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(WordSegmenter.houseNumberSegment(input));

	}

	/**
	 * <em>判断搜索输入是否含有敏感词</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/sensitive?chars=基地组织 -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "判断搜索输入是否含有敏感词", notes = "判断搜索输入是否含有敏感词，示例：/transform/sensitive?chars=基地组织")
	@GetMapping("/sensitive")
	public String isSensitiveWords(
			@ApiParam(value = "含有敏感词的输入，如基地组织") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.isSensitiveWords(input));

	}

	/**
	 * <em>判断搜索输入是否为正确的坐标</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/coordinate?chars=113.975436877645,22.5995874795642
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "判断搜索输入是否为正确的坐标", notes = "判断搜索输入是否为正确的坐标，示例：/transform/coordinate?chars=113.975436877645,22.5995874795642")
	@GetMapping("/coordinate")
	public String isCoordinatesExpression(
			@ApiParam(value = "坐标表达式，如113.975436877645,22.5995874795642") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.isCoordinatesExpression(input));

	}

	/**
	 * <em>判断搜索输入是否为正确的建筑物编码</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/buildingcode?chars=4403060080014800062
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "判断搜索输入是否为正确的建筑物编码", notes = "判断搜索输入是否为正确的建筑物编码，示例：/transform/buildingcode?chars=4403060080014800062")
	@GetMapping("/buildingcode")
	public String isBuildingcode(
			@ApiParam(value = "建筑物编码，如4403060080014800062") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.isBuildingCode(input));

	}

	/**
	 * <em>通假字转换，示例：得=德</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/exterchange?chars=广东省深圳市龙华区龙华街道清湖社区清沙路8号力得威公司宿舍楼
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "通假字转换", notes = "通假字转换，示例：/exterchange?chars=广东省深圳市龙华区龙华街道清湖社区清沙路8号力得威公司宿舍楼")
	@GetMapping("/exchange")
	public String exchangeWords(
			@ApiParam(value = "通假字转换，示例：得=德") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.exchangeWords(input));

	}

	/**
	 * <em>别名转换，示例：粤=广东省</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/alias?chars=粤深圳市龙华区龙华街道清湖社区清沙路8号力得威公司宿舍楼
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "别名转换", notes = "别名转换，示例：/transform/alias?chars=粤深圳市龙华区龙华街道清湖社区清沙路8号力德威公司宿舍楼")
	@GetMapping("/alias")
	public String aliasWords(
			@ApiParam(value = "别名转换，示例：粤=广东省") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.alias(input));

	}

	/**
	 * <em>同义词转换，示例：中原=华夏</em><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/synonym?chars=中原 -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "同义词转换", notes = "同义词转换，示例：/transform/synonym?chars=中原")
	@GetMapping("/synonym")
	public String synonymWords(
			@ApiParam(value = "同义词转换，示例：中原=华夏") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.synonym(input));

	}

}
