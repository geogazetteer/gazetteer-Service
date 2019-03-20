/**
 * 
 */
package top.geomatics.gazetteer.model;

import java.util.Date;

/**
 * @author whudyj
 *
 */
public class AddressModifier {
	private Integer status;// 状态，0表示未修改，1表示已经修改，2表示无法修改
	private String modifier;// 修改者
	private Date update_date;// 修改时间
	private String update_address;// 修改后的地址
	private String update_address_id;// 修改后的地址编码

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUpdate_address() {
		return update_address;
	}

	public void setUpdate_address(String update_address) {
		this.update_address = update_address;
	}

	public String getUpdate_address_id() {
		return update_address_id;
	}

	public void setUpdate_address_id(String update_address_id) {
		this.update_address_id = update_address_id;
	}

}
