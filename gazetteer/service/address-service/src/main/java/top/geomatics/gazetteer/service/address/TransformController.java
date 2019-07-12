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
import top.geomatics.gazetteer.utilities.address.ChineseNumber;

/**
 * <b>特殊地址转换服务类</b><br>
 * 
 * @author whudyj
 */
@Api(value = "/transform", tags = "特殊地址转换处理")
@RestController
@RequestMapping("/transform")
public class TransformController {

	/**
	 * <b>全角字符转换为半角字符，示例：广东省，深圳市，龙华区民治街道１２３号梅花山庄Ａ区７栋２楼</b><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/full?chars=广东省，深圳市，龙华区民治街道１２３号梅花山庄Ａ区７栋２楼 -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "全角转半角", notes = "全角字符转换为半角字符，示例：/transform/full?chars=广东省，深圳市，龙华区民治街道１２３号梅花山庄Ａ区７栋２楼")
	@GetMapping("/full")
	public String fullToHalf(
			@ApiParam(value = "全角字符，如 广东省，深圳市，龙华区民治街道１２３号梅花山庄Ａ区７栋２楼") @RequestParam(value = "chars", required = true) String input) {

		return JSON.toJSONString(AddressProcessor.fullToHalf(input));
	}

	/**
	 * <b>繁体汉字转换为简体汉字，示例：广东省深圳市龍華區龙华街道民治社區梅花山莊</b><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/complex?chars=广东省深圳市龍華區龙华街道民治社區梅花山莊 -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "繁体汉字转换为简体汉字", notes = "繁体汉字转换为简体汉字，示例：/transform/complex?chars=广东省深圳市龍華區龙华街道民治社區梅花山莊")
	@GetMapping("/complex")
	public String comToSimple(
			@ApiParam(value = "繁体汉字，如 广东省深圳市龍華區龙华街道民治社區梅花山莊") @RequestParam(value = "chars", required = true) String input) {

		// 繁体转中文简体
		return JSON.toJSONString(AddressProcessor.comToSimple(input));

	}

	/**
	 * <b>汉字数字转换为阿拉伯数字，示例：广东省深圳市龙华区民治街道民治社区人民路一百二十九号</b><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/digital?chars=广东省深圳市龙华区民治街道民治社区人民路一百二十九号-->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "汉字数字转换为阿拉伯数字", notes = "汉字数字转换为阿拉伯数字，示例：/transform/digital?chars=广东省深圳市龙华区民治街道民治社区人民路一百二十九号")
	@GetMapping("/digital")
	public String chineseToNumber(
			@ApiParam(value = "汉字数字，如 广东省深圳市龙华区民治街道民治社区人民路一百二十九号") @RequestParam(value = "chars", required = true) String input) {

		return JSON.toJSONString(ChineseNumber.convert(input));
	}

	/**
	 * <b>识别地址中的门牌号，返回门牌号</b><br>
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
	 * <b>判断搜索输入是否含有敏感词</b><br>
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
		boolean flag = AddressProcessor.isSensitiveWords(input);
		return JSON.toJSONString(flag);

	}

	/**
	 * <b>判断搜索输入是否为正确的坐标</b><br>
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
	 * <b>判断搜索输入是否为正确的建筑物编码</b><br>
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
	 * <b>通假字转换，示例：说德威公司宿舍楼</b><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/exterchange?chars=说德威公司宿舍楼
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "通假字转换", notes = "通假字转换，示例：/exterchange?chars=说德威公司宿舍楼")
	@GetMapping("/exchange")
	public String exchangeWords(
			@ApiParam(value = "通假字转换，示例：说德威公司宿舍楼") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.exchangeWords(input));

	}

	/**
	 * <b>别名转换，示例：广东省创客之城龙华区龙华街道清湖社区清沙路8号力德威公司宿舍楼</b><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/alias?chars=广东省创客之城龙华区龙华街道清湖社区清沙路8号力德威公司宿舍楼
	 * -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "别名转换", notes = "别名转换，示例：/transform/alias?chars=广东省创客之城龙华区龙华街道清湖社区清沙路8号力德威公司宿舍楼")
	@GetMapping("/alias")
	public String aliasWords(
			@ApiParam(value = "别名转换，示例：广东省创客之城龙华区龙华街道清湖社区清沙路8号力德威公司宿舍楼") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.alias(input));

	}

	/**
	 * <b>同义词转换，示例：我们阿谀你，你巴结我们，他们逢迎你</b><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/synonym?chars=我们阿谀你，你巴结我们，他们逢迎你 -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "同义词转换", notes = "同义词转换，示例：/transform/synonym?chars=我们阿谀你，你巴结我们，他们逢迎你")
	@GetMapping("/synonym")
	public String synonymWords(
			@ApiParam(value = "同义词转换，示例：我们阿谀你，你巴结我们，他们逢迎你") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.synonym(input));

	}

	/**
	 * <b>同音字转换，示例：我们砾用你，你示力我们，他们实例你</b><br>
	 * examples:<br>
	 * <!--http://localhost:8083/transform/synonym?chars=我们砾用你，你示力我们，他们实例你 -->
	 * 
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "同音字转换", notes = "同音字转换，示例：/transform/synonym?chars=我们砾用你，你示力我们，他们实例你")
	@GetMapping("/homophone")
	public String homophoneWords(
			@ApiParam(value = "同音字转换，示例：我们砾用你，你示力我们，他们实例你") @RequestParam(value = "chars", required = true) String input) {
		return JSON.toJSONString(AddressProcessor.homophone(input));

	}

}
