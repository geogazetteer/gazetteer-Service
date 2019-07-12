package top.geomatics.gazetteer.utilities.address;

import org.junit.Test;

public class SynonymDictionaryTest {
	private static SynonymDictionary dic = SynonymDictionary.getInstance();

	@Test
	public void testGetSynonym() {
		String s = "阿谀";
		String result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "巴结";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "逢迎";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);
	}

	@Test
	public void testReplace() {
		String s = "我们阿谀你，你巴结我们，他们逢迎你";
		String result = String.format("%s 的同义词替换结果是：%s", s, dic.replace(s));
		System.out.println(result);
	}

}
