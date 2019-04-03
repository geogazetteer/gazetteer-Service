package top.geomatics.gazetteer.service.address;

import java.io.UnsupportedEncodingException;

import com.luhuiguo.chinese.ChineseUtils;
import com.luhuiguo.chinese.pinyin.PinyinFormat;

/**
 * @author chenfa
 */
public class Test {
	public static void main(String[] args) {
//		String s = "头发发财";
//		System.out.println(s + " => " + ChineseUtils.toTraditional(s));
		
		
		//繁体转中文简体
		String s = "龍華";
		System.out.println(s + " => " + ChineseUtils.toSimplified(s));
//		s = "长江成长";
//		System.out.println(s + " => " + ChineseUtils.toPinyin(s) + " ("
//				+ ChineseUtils.toPinyin(s, PinyinFormat.UNICODE_PINYIN_FORMAT)
//				+ ") - "
//				+ ChineseUtils.toPinyin(s, PinyinFormat.ABBR_PINYIN_FORMAT));
	}
	
}
