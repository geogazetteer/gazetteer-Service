package org.segment;

import com.hankcs.hanlp.tokenizer.NLPTokenizer;

public class Test {

	public static void main(String[] args) {
	
		
		  NLPTokenizer.ANALYZER.enableCustomDictionary(false); // 中文分词≠词典，不用词典照样分词。
	        System.out.println(NLPTokenizer.segment("我新造一个词叫幻想乡你能识别并正确标注词性吗？")); // “正确”是副形词。
	        // 注意观察下面两个“希望”的词性、两个“晚霞”的词性
	        System.out.println(NLPTokenizer.analyze("我的希望是希望张晚霞的背影被晚霞映红").translateLabels());
	        System.out.println(NLPTokenizer.analyze("支援臺灣正體香港繁體：微软公司於1975年由比爾·蓋茲和保羅·艾倫創立。"));
	}

}
