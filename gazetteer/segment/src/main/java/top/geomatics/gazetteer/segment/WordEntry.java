/**
 * 
 */
package top.geomatics.gazetteer.segment;

/**
 * <b>词条</b><br>
 * 
 * @author whudyj
 *
 */
public class WordEntry {
	private String name;// 词
	private String nature;// 词性

	public WordEntry() {
		super();
	}

	public WordEntry(String name, String nature) {
		super();
		this.name = name;
		this.nature = nature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

}
