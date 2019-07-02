package top.geomatics.gazetteer.utilities.address;

import static org.junit.Assert.*;

import org.junit.Test;

public class SensitiveDictionaryTest {

	private static SensitiveDictionary dic = SensitiveDictionary.getInstance();

	@Test
	public void testContains() {
		String s = "锦绣御园8栋A座";
		boolean flag = dic.contains(s);
		if (flag) {
			String result = String.format("%s 的敏感词是：%s", s, dic.getSynonym(s));
			System.out.println(result);
		}
		
		System.out.println(flag);

	
	}

	
}
