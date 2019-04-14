/**
 * 
 */
package top.geomatics.gazetteer.utilities.address;

/**
 * @author whudyj
 *
 */
public class Main2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("功能正在实现中......");
		String addressString1 = "深圳市龙华区民治大道牛栏前大厦B501室";
		String completionString1 = AddressCompletion.complete(addressString1, "深圳市龙华区");
		System.out.println(completionString1);

		String addressString2 = "深圳市龙华区观澜金龙湖社区田心村观平路178号新联大厦607(办公场所)";
		String completionString2 = AddressCompletion.complete(addressString2, "深圳市龙华区");
		System.out.println(completionString2);
	}

}
