package top.geomatics.gazetteer.utilities.address;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
/**
 * <em>同义词</em><br>
 * 
 * @author yupeiyu
 *
 */
public interface SMeanExchangeableWords {
	
	
		public static final String[] WORDS_ARRAY = { "粤=广东省","南粤=广东省","鹏城=深圳", "路=大道", "小区=社区", "设计之都=深圳",
														"时尚之城=深圳", "创客之城=深圳", "志愿者之城=深圳","弄堂=街道","胡同=街道","街巷=街道"
				};
		public static final Map<String, String> WORDS_MAP = new HashMap<String, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 3674484656034194962L;
			{
				for (int i = 0; i < WORDS_ARRAY.length; i++) {
					String wordString = WORDS_ARRAY[i].trim();
					String words[] = wordString.split("=");
					if (2 != words.length) {
						continue;
					}
					put(words[0], words[1]);
				}
			}

		};
}