package top.geomatics.gazetteer.segment;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;

/**
 * <b>地址识别</b><br>
 * 
 * <i>说明：</i><br>
 * <i>b座</i><br>
 * <i>906</i><br>
 * <i>室</i><br>
 * 
 * <i>应合并为:</i><br>
 * <i>b座906室</i><br>
 * 
 * @author whudyj
 *
 */
public class AddressRecognition implements Recognition {
	private static final long serialVersionUID = 1L;
	public static final Set<String> Address_suffix = new HashSet<>();
	public static final Set<String> Address_nature = new HashSet<>();
	static {
		Address_suffix.add(Messages.getString("AddressRecognition.0")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.1")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.2")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.3")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.4")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.5")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.6")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.7")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.8")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.9")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.10")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.11")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.12")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.13")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.14")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.15")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.16")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.17")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.18")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.19")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.20")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.21")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.22")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.23")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.24")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.25")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.26")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.27")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.28")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.29")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.30")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.31")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.32")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.33")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.34")); //$NON-NLS-1$
		Address_suffix.add(Messages.getString("AddressRecognition.35")); //$NON-NLS-1$

		Address_nature.add(Messages.getString("AddressRecognition.36")); //$NON-NLS-1$
		Address_nature.add(Messages.getString("AddressRecognition.37")); //$NON-NLS-1$
		Address_nature.add(Messages.getString("AddressRecognition.38")); //$NON-NLS-1$
		Address_nature.add(Messages.getString("AddressRecognition.39")); //$NON-NLS-1$
		Address_nature.add(Messages.getString("AddressRecognition.40")); //$NON-NLS-1$
//		Address_nature.add("road");
//		Address_nature.add("road_num");
//		Address_nature.add("village");
//		Address_nature.add("building");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ansj.recognition.Recognition#recognition(org.ansj.domain.Result)
	 */
	@Override
	public void recognition(Result result) {
		List<Term> terms = result.getTerms();
		String name;
		String nature;

		LinkedList<Term> mergeList = new LinkedList<Term>();

		List<Term> list = new LinkedList<Term>();

		for (Term term : terms) {
			if (term == null) {
				continue;
			}
			name = term.getName();
			nature = term.getNatureStr();

			// 如果在标准词库里，即词性包含在Address_nature里，则不做处理
			// 社区之前的地址不做识别
			if (Address_nature.contains(nature)) {
				list.add(term);
				continue;
			}
			// 如果不在标准库，判断是否含有地址后缀词，不含有，加入mergeList，
			// 含有则进行一次合并，并添加到List， 将mergeList清空，进行下一次合并
			switch (name.length()) {
			case 1:
				if (Address_suffix.contains(name.substring(name.length() - 1))) {
					mergeList.add(term);
					Term ft = mergeList.pollFirst();
					for (Term sub : mergeList) {
						ft.merage(sub);
					}
					ft.setNature(term.natrue());
					list.add(ft);
					mergeList.clear();
				} else
					mergeList.add(term);
				break;
			case 2:
				if (Address_suffix.contains(name.substring(name.length() - 2))) {
					mergeList.add(term);
					Term ft = mergeList.pollFirst();
					for (Term sub : mergeList) {
						ft.merage(sub);
					}
					ft.setNature(term.natrue());
					list.add(ft);
					mergeList.clear();
				} else
					mergeList.add(term);
				break;
			case 3:
			default:
				if (Address_suffix.contains(name.substring(name.length() - 1))
						|| Address_suffix.contains(name.substring(name.length() - 2))
						|| Address_suffix.contains(name.substring(name.length() - 3))) {
					mergeList.add(term);
					Term ft = mergeList.pollFirst();
					for (Term sub : mergeList) {
						ft.merage(sub);
					}
					ft.setNature(term.natrue());
					list.add(ft);
					mergeList.clear();
				} else
					mergeList.add(term);
				break;
			}
		}

		if (mergeList != null) {
			for (Term term : mergeList) {
				list.add(term);
			}
		}

		result.setTerms(list);

	}

}
