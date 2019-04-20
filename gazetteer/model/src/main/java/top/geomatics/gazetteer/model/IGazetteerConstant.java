/**
 * 
 */
package top.geomatics.gazetteer.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whudyj
 *
 */
public interface IGazetteerConstant {
	// 440306 009601 02T0117
	public static final String GD_PROVINCE = "广东省";
	public static final String GD_PROVINCE_CODE = "440000";
	public static final String SZ_CITY = "深圳市";
	public static final String SZ_CITY_CODE = "440300";
	public static final String LH_DISTRICT = "龙华区";
	public static final String LH_DISTRICT_CODE = "440306";

	// 龙华街道
	public static final String S1 = "龙华街道";
	public static final String C1 = "440306007";

	public static final String S101 = "景龙社区";
	public static final String C101 = "440306007001";
	public static final String S102 = "龙园社区";
	public static final String C102 = "440306007002";
	public static final String S103 = "清湖社区";
	public static final String C103 = "440306007003";
	public static final String S104 = "三联社区";
	public static final String C104 = "440306007004";
	public static final String S105 = "油松社区";
	public static final String C105 = "440306007005";
	public static final String S106 = "华联社区";
	public static final String C106 = "440306007006";
	public static final String S107 = "松和社区";
	public static final String C107 = "440306007007";
	public static final String S108 = "富康社区";
	public static final String C108 = "440306007009";
	public static final String S109 = "玉翠社区";
	public static final String C109 = "440306007010";
	public static final String S110 = "清华社区";
	public static final String C110 = "440306007012";
	// 大浪街道
	public static final String S2 = "大浪街道";
	public static final String C2 = "440306008";

	public static final String S201 = "大浪社区";
	public static final String C201 = "440306008001";
	public static final String S202 = "高峰社区";
	public static final String C202 = "440306008002";
	public static final String S203 = "龙胜社区";
	public static final String C203 = "440306008003";
	public static final String S204 = "同胜社区";
	public static final String C204 = "440306008004";
	public static final String S205 = "浪口社区";
	public static final String C205 = "440306008005";
	public static final String S206 = "新石社区";
	public static final String C206 = "440306008006";
	public static final String S207 = "龙平社区";
	public static final String C207 = "440306008007";
	// 民治街道
	public static final String S3 = "民治街道";
	public static final String C3 = "440306009";

	public static final String S301 = "民治社区";
	public static final String C301 = "440306009001";
	public static final String S302 = "上芬社区";
	public static final String C302 = "440306009003";
	public static final String S303 = "龙塘社区";
	public static final String C303 = "440306009005";
	public static final String S304 = "民乐社区";
	public static final String C304 = "440306009006";
	public static final String S305 = "民新社区";
	public static final String C305 = "440306009007";
	public static final String S306 = "新牛社区";
	public static final String C306 = "440306009008";
	public static final String S307 = "北站社区";
	public static final String C307 = "440306009009";
	public static final String S308 = "民强社区";
	public static final String C308 = "440306009010";
	public static final String S309 = "大岭社区";
	public static final String C309 = "440306009011";
	public static final String S310 = "樟坑社区";
	public static final String C310 = "440306009012";
	public static final String S311 = "白石龙社区";
	public static final String C311 = "440306009013";
	public static final String S312 = "民泰社区";
	public static final String C312 = "440306009014";
	// 观澜街道
	public static final String S4 = "观澜街道";
	public static final String C4 = "440306010";

	public static final String S401 = "桂花社区";
	public static final String C401 = "440306010004";
	public static final String S402 = "君子布社区";
	public static final String C402 = "440306010005";
	public static final String S403 = "库坑社区";
	public static final String C403 = "440306010006";
	public static final String S404 = "黎光社区";
	public static final String C404 = "440306010007";
	public static final String S405 = "牛湖社区";
	public static final String C405 = "440306010008";
	public static final String S406 = "大富社区";
	public static final String C406 = "440306010006";//
	public static final String S407 = "广培社区";
	public static final String C407 = "440306010008";//
	public static final String S408 = "新澜社区";
	public static final String C408 = "440306010003";//
	public static final String S409 = "桂香社区";
	public static final String C409 = "440306010004";//
	public static final String S410 = "大水田社区";
	public static final String C410 = "440306010008";//

	// 福城街道
	public static final String S5 = "福城街道";
	public static final String C5 = "440306014";

	public static final String S501 = "大水坑社区";
	public static final String C501 = "440306014001";
	public static final String S502 = "福民社区";
	public static final String C502 = "440306014002";
	public static final String S503 = "茜坑社区";
	public static final String C503 = "440306014003";
	public static final String S504 = "章阁社区";
	public static final String C504 = "440306014004";
	public static final String S505 = "桔塘社区";
	public static final String C505 = "440306014005";

	// 观湖街道
	public static final String S6 = "观湖街道";
	public static final String C6 = "440306015";

	public static final String S601 = "观城社区";
	public static final String C601 = "440306015001";
	public static final String S602 = "松元厦社区";
	public static final String C602 = "440306015002";
	public static final String S603 = "新田社区";
	public static final String C603 = "440306015003";
	public static final String S604 = "樟坑径社区";
	public static final String C604 = "440306015004";
	public static final String S605 = "鹭湖社区";
	public static final String C605 = "440306015005";
	public static final String S606 = "润城社区";
	public static final String C606 = "440306015006";
	public static final String S607 = "松元社区";
	public static final String C607 = "440306010009";//

	public final static List<String> STREET_LIST = Arrays.asList(S1, S2, S3, S4, S5, S6);
	public final static String[] STREET_ARRAY = { S1, S2, S3, S4, S5, S6 };
	public final static List<String> STREET_CODE_LIST = Arrays.asList(C1, C2, C3, C4, C5, C6);
	public final static String[] STREET_CODE_ARRAY = { C1, C2, C3, C4, C5, C6 };

	public final static List<String> COMMUNITY_LIST = Arrays.asList(S101, S102, S103, S104, S105, S106, S107, S108,
			S109, S110, S201, S202, S203, S204, S205, S206, S207, S301, S302, S303, S304, S305, S306, S307, S308, S309,
			S310, S311, S312, S401, S402, S403, S404, S405, S406, S407, S408, S409, S410, S501, S502, S503, S504, S505,
			S601, S602, S603, S604, S605, S606, S607);
	public final static List<String> COMMUNITY_CODE_LIST = Arrays.asList(C101, C102, C103, C104, C105, C106, C107, C108,
			C109, C110, C201, C202, C203, C204, C205, C206, C207, C301, C302, C303, C304, C305, C306, C307, C308, C309,
			C310, C311, C312, C401, C402, C403, C404, C405, C406, C407, C408, C409, C410, C501, C502, C503, C504, C505,
			C601, C602, C603, C604, C605, C606, C607);

	public final static String[][] COMMUNITY_ARRAY = { { S101, S102, S103, S104, S105, S106, S107, S108, S109, S110 },
			{ S201, S202, S203, S204, S205, S206, S207 },
			{ S301, S302, S303, S304, S305, S306, S307, S308, S309, S310, S311, S312 },
			{ S401, S402, S403, S404, S405, S406, S407, S408, S409, S410 }, { S501, S502, S503, S504, S505, },
			{ S601, S602, S603, S604, S605, S606, S607 } };

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
		private static final long serialVersionUID = 3410904873848011932L;
		{
			for (int i = 0; i < STREET_ARRAY.length; i++) {
				put(STREET_ARRAY[i], STREET_CODE_ARRAY[i]);
			}
		}
	};
	public final static Map<String, String> COMMUNITY_MAP = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3410904873848011933L;
		{
			for (int i = 0; i < COMMUNITY_LIST.size(); i++) {
				put(COMMUNITY_LIST.get(i), COMMUNITY_CODE_LIST.get(i));
			}
		}
	};

	public final static BoundingBox LH_BBOX = new BoundingBox(113.9654368776450042, 22.5895874795642015,
			114.1060976372070002, 22.7678404032496005);

}
