package top.geomatics.gazetteer.service.address;

import java.util.List;

import top.geomatics.gazetteer.service.utils.SqlliteUtil;

public class Test {
	public static void main(String[] args) {
		SqlliteUtil sqlliteUtil=new SqlliteUtil();
		List list=sqlliteUtil.selectByCode("dmdz", "44030600960102T0117");
		System.out.println(list);
				
	}

}
