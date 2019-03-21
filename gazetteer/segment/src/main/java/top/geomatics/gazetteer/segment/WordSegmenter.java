/**
 * 
 */
package top.geomatics.gazetteer.segment;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;

/**
 * <!--提供分词功能-->
 * @author whudyj
 *
 */
public class WordSegmenter {
	/**
	 * @param word
	 * @return
	 */
	public static List<WordEntry> segment(String word){
		List<WordEntry> wordResults = new ArrayList<WordEntry>();
		// 分词结果的一个封装，主要是一个List<Term>的terms
		Result result = DicAnalysis.parse(word); 

		List<Term> terms = result.getTerms(); // 拿到terms
		for (int i = 0; i < terms.size(); i++) {
			String name = terms.get(i).getName(); // 拿到词
			String nature = terms.get(i).getNatureStr(); // 拿到词性
			WordEntry entry = new WordEntry(name,nature);
			wordResults.add(entry);
		}
		
		return wordResults;
	}
}
