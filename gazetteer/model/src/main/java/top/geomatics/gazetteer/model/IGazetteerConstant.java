/**
 * 
 */
package top.geomatics.gazetteer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whudyj
 *
 */
public interface IGazetteerConstant {
	// 440306 00960102T0117
	public static final String GD_PROVINCE = "广东省";
	public static final String GD_PROVINCE_CODE = "440000";
	public static final String SZ_CITY = "深圳市";
	public static final String SZ_CITY_CODE = "440300";
	public static final String LH_DISTRICT = "龙华区";
	public static final String LH_DISTRICT_CODE = "440306";

	public final static List<String> STREET_LIST = Arrays.asList(
			"大浪街道", "福城街道","观湖街道","观澜街道","龙华街道",	"民治街道"	);  
	public final static String[] STREET_ARRAY = {"大浪街道", "福城街道","观湖街道","观澜街道","龙华街道",	"民治街道"	};  
	
	public final static List<String> COMMUNITY_LIST = Arrays.asList(
			"大浪社区", "高峰社区","同胜社区","浪口社区","龙平社区",	"新石社区","龙胜社区",
			"章阁社区", "大水坑社区","桔塘社区","茜坑社区","福民社区",
			"观城社区", "润城社区","新田社区","松元厦社区","樟坑径社区","松元社区","鹭湖社区",
			"库坑社区", "大富社区","广培社区","新澜社区","牛湖社区",	"君子布社区","桂花社区","桂香社区","黎光社区","大水田社区",
			"松和社区", "龙园社区","玉翠社区","清湖社区","华联社区",	"清华社区","富康社区","油松社区","景龙社区","三联社区",
			"龙塘社区", "新牛社区","白石龙社区","民乐社区","樟坑社区", "民治社区","民新社区","上芬社区","民强社区","大岭社区","北站社区","民泰社区"
			); 
	
	public final static String[][] COMMUNITY_ARRAY = {
			{"大浪社区", "高峰社区","同胜社区","浪口社区","龙平社区",	"新石社区","龙胜社区"},
			{"章阁社区", "大水坑社区","桔塘社区","茜坑社区","福民社区"},
			{"观城社区", "润城社区","新田社区","松元厦社区","樟坑径社区","松元社区","鹭湖社区"},
			{"库坑社区", "大富社区","广培社区","新澜社区","牛湖社区",	"君子布社区","桂花社区","桂香社区","黎光社区","大水田社区"},
			{"松和社区", "龙园社区","玉翠社区","清湖社区","华联社区",	"清华社区","富康社区","油松社区","景龙社区","三联社区"},
			{"龙塘社区", "新牛社区","白石龙社区","民乐社区","樟坑社区", "民治社区","民新社区","上芬社区","民强社区","大岭社区","北站社区","民泰社区"}
	}; 
	
	public final static Map<String, List<String>> STREET_COMMUNITY_LIST_MAP = new HashMap<String, List<String>>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3410904873848011931L;
		{
			for (int i = 0; i < STREET_ARRAY.length; i++) {
				put(STREET_ARRAY[i], Arrays.asList(COMMUNITY_ARRAY[i]));
			}
		}
		
	};
	
	public final static Map<String, String> STREET_MAP = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3410904873848011931L;

		{
			put("大浪街道", "440306008");
			put("福城街道", "440306010");
			put("观湖街道", "440306010");
			put("观澜街道", "440306010");
			put("龙华街道", "440306007");
			put("民治街道", "440306009");
		}
	};

}
