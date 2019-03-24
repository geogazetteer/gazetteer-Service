package top.geomatics.gazetteer.lucene;

public class Test {
	public static void main(String[] args) {
		try {
			LuceneUtil.updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("执行成功，索引文件在D:\\lucene_index");
	}
}
