package top.geomatics.gazetteer.service.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luhuiguo.chinese.ChineseUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author chenfa
 */

@Api(value="/pre",tags="数据预处理服务")
@RestController
@RequestMapping("/pre")
public class PreController {
	
	@ApiOperation(value = "全角转半角", notes = "全角转半角")
	@GetMapping("/fulltohalf")
    public  String fullToHalf(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == '\u3000') {
                 c[i] = ' ';
               } 
               else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                 c[i] = (char) (c[i] - 65248);
               }
             }
        String returnString = new String(c);
        return returnString;
    }	
    
	@ApiOperation(value = "繁体转简体", notes = "繁体转简体")
	@GetMapping("/comtosimple")
    public String comToSimple(String s) {
    	//String s = "头发发财";
    	//System.out.println(s + " => " + ChineseUtils.toTraditional(s));
		
		//繁体转中文简体
		return ChineseUtils.toSimplified(s);

		//s = "长江成长";
//		System.out.println(s + " => " + ChineseUtils.toPinyin(s) + " ("
//				+ ChineseUtils.toPinyin(s, PinyinFormat.UNICODE_PINYIN_FORMAT)
//				+ ") - "
//				+ ChineseUtils.toPinyin(s, PinyinFormat.ABBR_PINYIN_FORMAT));
    }
	
	@ApiOperation(value = "汉字转阿拉伯数字", notes = "汉字转阿拉伯数字")
	@GetMapping("/chinesetonum")
    public  int chineseToNumber(String chineseNumber){
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
        char[] chArr = new char[]{'十','百','千','万','亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
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
            if(b){//单位{'十','百','千','万','亿'}
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
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }
	
//	@ApiOperation(value = "数字转汉字", notes = "数字转汉字")
//	@GetMapping("/toChinese")
//	public String toChinese(String string) {
//	       String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
//	       String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };
//
//	       String result = "";
//	       int n = string.length();
//	       for (int i = 0; i < n; i++) {
//	           int num = string.charAt(i) - '0';
//
//	           if (i != n - 1 && num != 0) {
//	               result += s1[num] + s2[n - 2 - i];
//	           } else {
//	               result += s1[num];
//	           }
//	        }
//	        return result;
//	    }
    
}
