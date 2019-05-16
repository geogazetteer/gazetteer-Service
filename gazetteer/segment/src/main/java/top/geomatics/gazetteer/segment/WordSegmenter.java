/**
 * 
 */
package top.geomatics.gazetteer.segment;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			terms = NlpAnalysis.parse(text);
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
	 * <b>判断地址中包含街道信息，并返回</b><br>
	 * 
	 * @param address 地址
	 * @return 地址所在的街道
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
	 * <b>判断地址中包含社区信息，并返回</b><br>
	 * 
	 * @param address 地址
	 * @return 地址所在的社区
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
	 * <b>地址中的房屋号码提取</b><br>
	 * 
	 * @param address 含有房屋号码的地址
	 * @return 房屋号码提取提取结果
	 */
	public static List<String> houseNumberSegment(String address) {
		List<String> wordResults = new ArrayList<String>();
		// 拿到分词结果
		List<String> result = segment(address);
		for (int i = 0; i < result.size(); i++) {
			String name = result.get(i);
			if (name.contains(Messages.getString("WordSegmenter.0"))) { //$NON-NLS-1$
				String str = name;
				if (i > 0) {
					str = result.get(i - 1) + name;
				}
				wordResults.add(str);
			}
		}
		return wordResults;
	}

	/**
	 * <b>分词</b><br>
	 * 
	 * @param text 待分词的文本
	 * @return 分词结果
	 */
	public static List<String> segment(String word) {
		List<String> wordResults = new ArrayList<String>();
		// 分词结果的一个封装，主要是一个List<Term>的terms
		Result result = DicAnalysis.parse(word);
		// 对不标准地址进行识别
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
