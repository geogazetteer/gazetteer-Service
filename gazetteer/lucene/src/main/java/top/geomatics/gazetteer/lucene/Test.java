package top.geomatics.gazetteer.lucene;

public class Test {
	public static void main(String[] args) {
		try {
<<<<<<< HEAD
			LuceneUtil.updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("执行成功，索引文件在D:\\lucene_index");
=======
			new LuceneUtil().updateIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("���");
>>>>>>> 0964b85037f46fc591731ed9493f856831cfd1df
	}
}
