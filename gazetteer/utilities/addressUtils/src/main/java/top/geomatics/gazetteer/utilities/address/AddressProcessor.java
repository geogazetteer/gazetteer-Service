/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import com.luhuiguo.chinese.ChineseUtils;

import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * @author whudyj
 *
 */
public class AddressProcessor {

	/**
	 * 全角转半角
	 * 
	 * @param input
	 * @return
	 */
	public static String fullToHalf(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	/**
	 * 汉字转阿拉伯数字
	 * 
	 * @param chineseNumber
	 * @return
	 */
	public static Integer chineseToNumber(String chineseNumber) {
		int result = 0;
		int temp = 1;// 存放一个单位的数字如：十万
		int count = 0;// 判断是否有chArr
		char[] cnArr = new char[] { '一', '二', '三', '四', '五', '六', '七', '八', '九' };
		char[] chArr = new char[] { '十', '百', '千', '万', '亿' };
		for (int i = 0; i < chineseNumber.length(); i++) {
			boolean b = true;// 判断是否是chArr
			char c = chineseNumber.charAt(i);
			for (int j = 0; j < cnArr.length; j++) {// 非单位，即数字
				if (c == cnArr[j]) {
					if (0 != count) {// 添加下一个单位之前，先把上一个单位值添加到结果中
						result += temp;
						temp = 1;
						count = 0;
					}
					// 下标+1，就是对应的值
					temp = j + 1;
					b = false;
					break;
				}
			}
			if (b) {// 单位{'十','百','千','万','亿'}
				for (int j = 0; j < chArr.length; j++) {
					if (c == chArr[j]) {
						switch (j) {
						case 0:
							temp *= 10;
							break;
						case 1:
							temp *= 100;
							break;
						case 2:
							temp *= 1000;
							break;
						case 3:
							temp *= 10000;
							break;
						case 4:
							temp *= 100000000;
							break;
						default:
							break;
						}
						count++;
					}
				}
			}
			if (i == chineseNumber.length() - 1) {// 遍历到最后一个字符
				result += temp;
			}
		}
		return result;
	}

	/**
	 * 繁体转简体
	 * 
	 * @param s
	 * @return
	 */
	public static String comToSimple(String s) {
		// String s = "头发发财";
		// System.out.println(s + " => " + ChineseUtils.toTraditional(s));

		// 繁体转中文简体
		return ChineseUtils.toSimplified(s);

		// s = "长江成长";
//		System.out.println(s + " => " + ChineseUtils.toPinyin(s) + " ("
//				+ ChineseUtils.toPinyin(s, PinyinFormat.UNICODE_PINYIN_FORMAT)
//				+ ") - "
//				+ ChineseUtils.toPinyin(s, PinyinFormat.ABBR_PINYIN_FORMAT));
	}

	// 数字转汉字
//	public String toChinese(String string) {
//    String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
//    String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };
//
//    String result = "";
//    int n = string.length();
//    for (int i = 0; i < n; i++) {
//        int num = string.charAt(i) - '0';
//
//        if (i != n - 1 && num != 0) {
//            result += s1[num] + s2[n - 2 - i];
//        } else {
//            result += s1[num];
//        }
//     }
//     return result;
// }

	public static String transform(String address, SearcherSettings settings) {
		String newAddress = address;
		if (settings.isComplexChar()) {// 繁体转换
			newAddress = comToSimple(newAddress);
		}
		if (settings.isFullChar()) {// 全角转换
			newAddress = fullToHalf(newAddress);
		}
		if (settings.isChineseNumber()) {// 数字转换
			newAddress = chineseToNumber(newAddress).toString();
		}
		if (settings.isInterchangeable()) {// 通假字转换
			newAddress = exchangeWords(newAddress).toString();
		}
		if (settings.isSynonym()) {// 同义词转换
			newAddress = exchangeSmeanWords(newAddress).toString();
		}
		return newAddress;
	}

	/**
	 * <em>判断输入是否为113.9654368776450042,22.5895874795642015形式的坐标</em><br>
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isCoordinatesExpression(String input) {
		boolean flag = false;
		if (!input.contains(",")) {
			return flag;
		}
		String coordString[] = input.split(",");
		if (2 != coordString.length) {
			return flag;
		}
		String xString = coordString[0];
		String yString = coordString[1];
		if ((!xString.matches("[0-9]{3}.[0-9]{6,}")) || (!yString.matches("[0-9]{2}.[0-9]{6,}"))) {
			return flag;
		}
		double x = Double.parseDouble(xString);
		double y = Double.parseDouble(yString);
		if (x < IGazetteerConstant.LH_BBOX.getMinx() || x > IGazetteerConstant.LH_BBOX.getMaxx()) {
			return flag;
		}
		if (y < IGazetteerConstant.LH_BBOX.getMiny() || y > IGazetteerConstant.LH_BBOX.getMaxy()) {
			return flag;
		}
		flag = true;
		return flag;
	}

	/**
	 * <em>判断输入是否为440306 008001 48 00062形式的建筑物编码</em><br>
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isBuildingCode(String input) {
		boolean flag = false;
		if (19 != input.length()) {
			return flag;
		}
		if (0 != input.substring(0, 6).compareTo("440306")) {
			return flag;
		}
		if (!input.matches("440306[0-9]{8}[0-9a-zA-Z]{5}")) {
			return flag;
		}
		flag = true;
		return flag;
	}

	/**
	 * <em>判断是否含有敏感词</em><br>
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isSensitiveWords(String input) {
		boolean flag = false;
		for (String str : ISensitiveWords.WORDS_LIST) {
			flag = input.contains(str);
			if (flag) {
				break;
			}
		}
		return flag;
	}

	/**
	 * <em>通假字转换</em><br>
	 * 
	 * @param input
	 * @return
	 */
	public static String exchangeWords(String input) {
		String newString = input;
		for (String key : IExchangeableWords.WORDS_MAP.keySet()) {
			if (input.contains(key)) {
				newString = newString.replace(key, IExchangeableWords.WORDS_MAP.get(key));
			}
		}
		return newString;
	}

	/**
	 * <em>同义词转换</em><br>
	 * 
	 * @param input
	 * @return
	 */
	public static String exchangeSmeanWords(String input) {
		String newString = input;
		for (String key : SMeanExchangeableWords.WORDS_MAP.keySet()) {
			if (input.contains(key)) {
				newString = newString.replace(key, SMeanExchangeableWords.WORDS_MAP.get(key));
			}
		}
		return newString;
	}

	/**
	 * <em>根据建筑物编码获得社区名称</em><br>
	 * 
	 * @param code
	 * @return
	 */
	public static String getCommunityFromBuildingCode(String code) {
		String newString = code;
		for (String key : IGazetteerConstant.COMMUNITY_MAP.keySet()) {
			String value = IGazetteerConstant.COMMUNITY_MAP.get(key);
			if (0 == value.compareTo(code)) {
				newString = key;
				break;
			}
		}
		return newString;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String string = "龍華";
		System.out.println(AddressProcessor.comToSimple(string));

		String input = "１2３";
		System.out.println(input);
		System.out.println(AddressProcessor.fullToHalf(input));

//		String str="12345";
//		System.out.println(preController.toChinese(str));

		String string2 = "二十三";
		System.out.println(AddressProcessor.chineseToNumber(string2));

		System.out.println(isCoordinatesExpression("113.9654368776450042,22.5895874795642015"));
		System.out.println(isSensitiveWords("基地组织"));

		System.out.println(isBuildingCode("4403060080014800062"));

	}

}
