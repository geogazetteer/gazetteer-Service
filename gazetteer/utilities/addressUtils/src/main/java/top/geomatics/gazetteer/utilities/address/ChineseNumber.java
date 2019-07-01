/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <em>字符串中中文数字的处理</em><br>
 * 
 * @author whudyj
 *
 */
public class ChineseNumber {

	public static String convert(String chinese) {
		String s = chinese;
		Pattern p;
		Matcher m;
		for (String regex : regexMap.keySet()) {
			p = Pattern.compile(regex);
			m = p.matcher(s);
			while (m.find()) {
				String exper = regexMap.get(regex);
				List<String> list = new ArrayList<String>();
				for (int i = 1; i <= m.groupCount(); i++) {
					list.add(NumRegex.numMap.get(m.group(i)));
				}
				exper = MessageFormat.format(exper, list.toArray());
				String text = m.group();
				String value = experToValue(exper);
				s = s.replace(text, value);
			}
		}
		return s;
	}

	public static String experToValue(String exper) {
		String[] experArr = null;
		experArr = exper.split(encodeUnicode("+"));

		int value = 0;
		for (String sExper : experArr) {
			String[] sExperArr = sExper.split(encodeUnicode("*"));
			value += Integer.valueOf(sExperArr[0]) * Integer.valueOf(sExperArr[1]);
		}
		return String.valueOf(value);
	}

	// 转换为unicode
	private static String encodeUnicode(String gbString) {
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int i : utfBytes) {
			String hexB = Integer.toHexString(i);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	static class NumRegex {
		public static final Map<String, String> numMap = new HashMap<String, String>();
		static {
			numMap.put("一", "1");
			numMap.put("二", "2");
			numMap.put("三", "3");
			numMap.put("四", "4");
			numMap.put("五", "5");
			numMap.put("六", "6");
			numMap.put("七", "7");
			numMap.put("八", "8");
			numMap.put("九", "9");
		}

		private static String numRegex;

		public static String getNumRegex() {
			if (numRegex == null || numRegex.length() == 0) {
				numRegex = "([";
				for (String s : numMap.keySet()) {
					numRegex += encodeUnicode(s);
				}
				numRegex += "])";
			}
			return numRegex;
		}
	}

	// 一、十一、二十一、三百二十一、三百零一、二十、三百、三百二、三百二十、十
	private static final Map<String, String> regexMap = new LinkedHashMap<String, String>();
	static {
		// 三百二十一
		String regex = NumRegex.getNumRegex() + encodeUnicode("百") + NumRegex.getNumRegex() + encodeUnicode("十")
				+ NumRegex.getNumRegex();
		String exper = "{0}*100+{1}*10+{2}*1";
		regexMap.put(regex, exper);
		// 三百二十
		regex = NumRegex.getNumRegex() + encodeUnicode("百") + NumRegex.getNumRegex() + encodeUnicode("十");
		exper = "{0}*100+{1}*10";
		regexMap.put(regex, exper);
		
		// 三百零一
		regex = NumRegex.getNumRegex() + encodeUnicode("百") + encodeUnicode("零") + NumRegex.getNumRegex();
		exper = "{0}*100+{1}*1";
		regexMap.put(regex, exper);
		// 三百二
		regex = NumRegex.getNumRegex() + encodeUnicode("百") + NumRegex.getNumRegex();
		exper = "{0}*100+{1}*10";
		regexMap.put(regex, exper);

		// 三百
		regex = NumRegex.getNumRegex() + encodeUnicode("百");
		exper = "{0}*100";
		regexMap.put(regex, exper);
		// 二十一
		regex = NumRegex.getNumRegex() + encodeUnicode("十") + NumRegex.getNumRegex();
		exper = "{0}*10+{1}*1";
		regexMap.put(regex, exper);
		// 二十
		regex = NumRegex.getNumRegex() + encodeUnicode("十");
		exper = "{0}*10";
		regexMap.put(regex, exper);
		// 十一
		regex = encodeUnicode("十") + NumRegex.getNumRegex();
		exper = "1*10+{0}*1";
		regexMap.put(regex, exper);
		// 十
		regex = encodeUnicode("十");
		exper = "1*10";
		regexMap.put(regex, exper);
		// 一
		regex = NumRegex.getNumRegex();
		exper = "{0}*1";
		regexMap.put(regex, exper);
	}

}
