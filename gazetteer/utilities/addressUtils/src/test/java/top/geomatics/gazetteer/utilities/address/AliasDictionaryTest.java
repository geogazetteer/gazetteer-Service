package top.geomatics.gazetteer.utilities.address;

import org.junit.Test;

public class AliasDictionaryTest {

	private static AliasDictionary dic = AliasDictionary.getInstance();

	@Test
	public void testGetSynonym() {
		String s = "粤";
		String result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "鹏城";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "广东省";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);
	}

	@Test
	public void testReplace() {
		String s = "中国银行南粤支行，工商银行广东省分行，粤府办53号";
		String result = String.format("%s 的同义词替换结果是：%s", s, dic.replace(s));
		System.out.println(result);
		
		s = "广东省创客之城龙华区龙华街道清湖社区清沙路8号力德威公司宿舍楼";
		result = String.format("%s 的同义词替换结果是：%s", s, dic.replace(s));
		System.out.println(result);
	}

}
