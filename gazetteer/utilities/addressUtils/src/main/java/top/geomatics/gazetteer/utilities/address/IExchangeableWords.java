/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

/**
 * <em>通假字</em><br>
 * 
 * @author whudyj
 *
 */
public interface IExchangeableWords {
	public static final String[] WORDS_ARRAY = { "说=悦", "女=汝", "知=智", "还=旋", "汤=烫", "齐=剂", "止=只", "阴=荫", "静=净", "屏=摒",
			"帖=贴", "火=伙", "竦=耸", "争=怎", "见=现", "要=邀", "坐=座", "指=直", "反=返", "惠=慧", "亡=无", "厝=措", "那=哪", "阙=缺", "则=即",
			"强=僵", "道=导", "曷=何", "辑=缉", "熙=嬉", "零=丁", "距=拒", "圉=御", "诎=屈", "有=又", "诎=屈", "衡=横", "攀=扳", "甫=父", "简=拣",
			"适=谪", "唱=倡", "已=以", "被=披", "食=饲", "见=现", "材=才", "邪=耶", "僇=戮", "见=现", "畔=叛", "忍=韧", "曾=增", "衡=横", "拂=弼",
			"具=俱", "属=嘱", "直=值", "辟=避", "辩=辨", "得=德", "与=欤", "乡=向", "信=伸", "已=以", "阙=缺", "简=拣", "有=又", "徧=遍", "见=现",
			"曷=何", "桀=橛" };
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
				put(words[1], words[0]);
			}
		}

	};
}
