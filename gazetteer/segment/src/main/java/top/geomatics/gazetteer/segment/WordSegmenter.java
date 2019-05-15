/**
 * 
 */
package top.geomatics.gazetteer.segment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.geomatics.gazetteer.config.ResourcesManager;
import top.geomatics.gazetteer.model.IGazetteerConstant;

/**
 * <b>提供分词功能</b>
 * 
 * @author whudyj
 *
 */
public class WordSegmenter {
	// 添加slf4j日志实例对象
	private final static Logger logger = LoggerFactory.getLogger(WordSegmenter.class);
	// 分词词典文件路径
	private static ResourcesManager manager = ResourcesManager.getInstance();
	private static final String SEGMENT_DICTIONARY_PATH = "segment_dictionary_path";
	private static Forest forest = null;
	static {
		// 加载分词词典文件
		String userLibrary = manager.getValue(SEGMENT_DICTIONARY_PATH);
		userLibrary = userLibrary + File.separator + "userLibrary.dic";
		
		try {
			forest = Library.makeForest(userLibrary);
		} catch (Exception e) {
			e.printStackTrace();
			String longMsgString = String.format("加载分词词典文件：%s 失败！", userLibrary);
			logger.error(longMsgString);
		}
	}

	/**
	 * <b>分词</b><br>
	 * 
	 * @param text 待分词的文本
	 * @param type 分词类型
	 * @return 分词结果
	 */
	public static final Result segment(String text, int type) {
		Result terms = null;
		switch (type) {
		case 0:
			terms = BaseAnalysis.parse(text);
			break;
		case 1:
			terms = ToAnalysis.parse(text);
			break;
		case 2:
			terms = NlpAnalysis.parse(text,forest);
			break;
		case 3:
			terms = IndexAnalysis.parse(text);
			break;
		default:
			terms = ToAnalysis.parse(text);
			break;
		}
		return terms;
	}

	/**
	 * 判断地址中包含街道信息，并返回
	 * 
	 * @param address
	 * @return
	 */
	public static final String getStreet(String address) {
		for (String street : IGazetteerConstant.STREET_LIST) {
			if (address.contains(street)) {
				return street;
			}
		}
		return null;
	}

	/**
	 * 判断地址中包含社区信息，并返回
	 * 
	 * @param address
	 * @return
	 */
	public static final String getCommunity(String address) {
		for (String community : IGazetteerConstant.COMMUNITY_LIST) {
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
			// if
			// (name.contains("座")||name.contains("室")||name.contains("楼")||name.contains("栋")||name.contains("单元")||name.contains("号"))
			// {
			if (name.contains("号")) {
				String str = name;
				if (i > 0) {
					str = terms.get(i - 1).getName() + name;
				}
				wordResults.add(str);
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
