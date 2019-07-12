package top.geomatics.gazetteer.utilities.address;

import org.junit.Test;

public class ExchangeDictionaryTest {

	private static ExchangeDictionary dic = ExchangeDictionary.getInstance();

	@Test
	public void testGetSynonym() {
		String s = "说";
		String result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "汝";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "贴";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);
	}

	@Test
	public void testReplace() {
		String s = "不是我悦你，女实在不贴心，我也无法";
		String result = String.format("%s 的同义词替换结果是：%s", s, dic.replace(s));
		System.out.println(result);
		
		s = "说德威公司宿舍楼";
		result = String.format("%s 的同义词替换结果是：%s", s, dic.replace(s));
		System.out.println(result);
	}

}
