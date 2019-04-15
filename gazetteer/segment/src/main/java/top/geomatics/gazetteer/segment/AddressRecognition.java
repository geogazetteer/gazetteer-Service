package top.geomatics.gazetteer.segment;


import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AddressRecognition implements Recognition{

	/**
	 * @author gj
	 */
	private static final long serialVersionUID = 1L;
	public static final Set<String> Address_suffix = new HashSet<String>();
	public static final Set<String> Address_nature = new HashSet<String>();
	static {
		Address_suffix.add("大厦");
		Address_suffix.add("村");
		Address_suffix.add("街道");
		Address_suffix.add("路");
		Address_suffix.add("公园");
		Address_suffix.add("广场");
		Address_suffix.add("大楼");
		Address_suffix.add("社区");
		Address_suffix.add("工业园");
		Address_suffix.add("老村");
		Address_suffix.add("创意园");
		Address_suffix.add("科技园");
		Address_suffix.add("城");
		Address_suffix.add("区");
		Address_suffix.add("楼");
		Address_suffix.add("工业区");
		Address_suffix.add("公馆");
		Address_suffix.add("中心");
		Address_suffix.add("岭");
		Address_suffix.add("下围");
		Address_suffix.add("湾");
		Address_suffix.add("里");
		Address_suffix.add("办事处");
		Address_suffix.add("商业街");
		Address_suffix.add("新村");
		Address_suffix.add("巷");
		Address_suffix.add("水围");
		Address_suffix.add("径");
		Address_suffix.add("坑");
		Address_suffix.add("园");
		Address_suffix.add("大道");
		
		Address_nature.add("pron");
		Address_nature.add("dict");
		Address_nature.add("street");
		Address_nature.add("comm");
		Address_nature.add("road");
		Address_nature.add("build");
		Address_nature.add("vill");
		Address_nature.add("city");
		
	}
	
	//合并需要合并的Term，再组成新的List
	
	
	public void recognition(Result result) {
		// TODO Auto-generated method stub
		List<Term> terms = result.getTerms() ;
		String name;
		String nature;
		
		LinkedList<Term> mergeList = new LinkedList<Term>();

		List<Term> list = new LinkedList<Term>();
		
		for(Term term: terms) {
			if(term!=null) 
			{
				name = term.getName();
				nature = term.getNatureStr();
				
				//如果在标准词库里，即词性包含在Address_nature里，则不做处理
				
//				if (Address_nature.contains(nature)) 
//				{
//					list.add(term);
//					continue;
//				}
			    //如果不在标准库，判断是否含有地址后缀词，不含有，加入mergeList，含有则进行一次合并，并添加到List，将mergeList清空，进行下一次合并

				
					
					switch(name.length()) 
					{
					case 1:
						if (Address_suffix.contains(name.substring(name.length()-1))) {
							//System.out.println(name.equals("深圳市"));
							mergeList.add(term);
							Term ft = mergeList.pollFirst();
							for(Term sub:mergeList)
							{
								ft.merage(sub);
							}
							ft.setNature(term.natrue());
							list.add(ft);
							mergeList.clear();
							}
						else mergeList.add(term);
						break;
					case 2:
						if (Address_suffix.contains(name.substring(name.length()-2))) {
							mergeList.add(term);
							Term ft = mergeList.pollFirst();
							for(Term sub:mergeList)
							{
								ft.merage(sub);
							}
							ft.setNature(term.natrue());
							list.add(ft);
							mergeList.clear();
							}
						else mergeList.add(term);
						break;
					default:
						if (Address_suffix.contains(name.substring(name.length()-1))||
								Address_suffix.contains(name.substring(name.length()-2))||
								Address_suffix.contains(name.substring(name.length()-3)))
							{
								mergeList.add(term);
								Term ft = mergeList.pollFirst();
								for(Term sub:mergeList)
								{
									ft.merage(sub);
								}
								ft.setNature(term.natrue());
								list.add(ft);
								mergeList.clear();
							}
						else if(name.equals("深圳市")) list.add(term);
						else mergeList.add(term);
						break;

					}
					
				
			}
		}	
		
		if (mergeList!=null) {
			for (Term term : mergeList) {
				list.add(term);
			}
		}
		
		result.setTerms(list) ;
	
	}
	

}
