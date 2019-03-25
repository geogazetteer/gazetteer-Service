package top.geomatics.gazetteer.lucene;

public class TestUpdateIndex {
	public static void main(String[] args) {
		System.out.println("开始更新索引......");
		try {
			LuceneUtil.updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("更新索引结束！");
	}
}
