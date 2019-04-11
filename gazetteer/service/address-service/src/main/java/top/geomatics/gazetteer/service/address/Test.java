package top.geomatics.gazetteer.service.address;

import java.io.UnsupportedEncodingException;

import com.luhuiguo.chinese.ChineseUtils;
import com.luhuiguo.chinese.pinyin.PinyinFormat;

/**
 * @author chenfa
 */
public class Test {
	public static void main(String[] args) {
		PreController preController=new PreController();
		String string="龍華";
		System.out.println(preController.comToSimple(string));
		
		String input="１２３";
		System.out.println(input);
		System.out.println(preController.fullToHalf(input));
		
		String str="12345";
		System.out.println(preController.toChinese(str));
		
		String  string2="二十三";
		System.out.println(preController.chineseToNumber(string2));		
	}
}
