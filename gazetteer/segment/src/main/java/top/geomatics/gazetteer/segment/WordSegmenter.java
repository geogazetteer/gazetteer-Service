/**
 * 
 */
package top.geomatics.gazetteer.segment;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;


import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * <!--提供分词功能-->
 * 
 * @author whudyj
 *
 */
public class WordSegmenter {
	/**
	 * 判断地址中包含街道信息，并返回
	 * @param address
	 * @return
	 */
	public static final String getStreet(String address) {
		for (String street:IGazetteerConstant.STREET_LIST) {
			if (address.contains(street)) {
				return street;
			}
		}
		return null;
	}
	/**
	 * 判断地址中包含社区信息，并返回
	 * @param address
	 * @return
	 */
	public static final String getCommunity(String address) {
		for (String community:IGazetteerConstant.COMMUNITY_LIST) {
			if (address.contains(community)) {
				return community;
			}
		}
		return null;
	}
	/**
	 * @param word
	 * @return
	 */
	public static List<String> houseNumberSegment(String word) {
		List<String> wordResults = new ArrayList<String>();
		// 分词结果的一个封装，主要是一个List<Term>的terms

		Result result = DicAnalysis.parse(word); 
		// 对不标准地址进行分词
		AddressRecognition re = new AddressRecognition();
		re.recognition(result);

		List<Term> terms = result.getTerms(); // 拿到terms
		for (int i = 0; i < terms.size(); i++) {
			String name = terms.get(i).getName(); // 拿到词
			if (name.contains("座")||name.contains("室")||name.contains("楼")||name.contains("栋")||name.contains("单元")||name.contains("号")) {
				
				wordResults.add(name);
		}
		}
	return wordResults;
	}
	
	public static List<String> segment(String word) {
		List<String> wordResults = new ArrayList<String>();
		// 分词结果的一个封装，主要是一个List<Term>的terms

		Result result = DicAnalysis.parse(word); 
		// 对不标准地址进行分词
		AddressRecognition re = new AddressRecognition();
		re.recognition(result);

		List<Term> terms = result.getTerms(); // 拿到terms
		for (int i = 0; i < terms.size(); i++) {
			String name = terms.get(i).getName(); // 拿到词
				wordResults.add(name);
		}
		
	return wordResults;
	}
	

	
	
}
