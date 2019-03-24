package top.geomatics.gazetteer.lucene;

public class Test {
	public static void main(String[] args) {
		try {
			new LuceneUtil().updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("���");
	}
}
