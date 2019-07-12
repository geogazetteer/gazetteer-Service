package top.geomatics.gazetteer.utilities.address;

import org.junit.Test;

public class HomophoneDictionaryTest {

	private static HomophoneDictionary dic = HomophoneDictionary.getInstance();

	@Test
	public void testGetSynonym() {
		String s = "力";
		String result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "例";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);

		s = "利";
		result = String.format("%s 的同义词是：%s", s, dic.getSynonym(s));
		System.out.println(result);
	}

	@Test
	public void testReplace() {
		String s = "我们砾用你，你示力我们，他们实例你";
		String result = String.format("%s 的同义词替换结果是：%s", s, dic.replace(s));
		System.out.println(result);
	}

}
