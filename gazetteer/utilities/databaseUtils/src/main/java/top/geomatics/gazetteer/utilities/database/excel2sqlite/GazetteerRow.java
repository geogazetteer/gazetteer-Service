package top.geomatics.gazetteer.utilities.database.excel2sqlite;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class GazetteerRow extends BaseRowModel{
	@ExcelProperty(index = 0)
	private String address_id; // 地址标识，数据库自动生成，示例：63EEDE6BA4206A3AE0538CC0C0C07BB0
	@ExcelProperty(index = 1)
	private String code; // 地址编码，唯一，由权威机构确定，示例：44030600960102T0117 000000 共25位
	@ExcelProperty(index = 2)
	private String building_id; // 建筑标识，唯一，由权威机构确定，示例：44030600960102T0117 共19位
	@ExcelProperty(index = 3)
	private String house_id; // 房屋标识
	@ExcelProperty(index = 4)
	private String province; // 省名称，缺省：广东省
	@ExcelProperty(index = 5)
	private String city; // 市名称，缺省：深圳市
	@ExcelProperty(index = 6)
	private String district; // 区名称，缺省：龙华区
	@ExcelProperty(index = 7)
	private String street; // 街道名称
	@ExcelProperty(index = 8)
	private String community; // 社区名称
	@ExcelProperty(index = 9)
	private String road; // 路名称
	@ExcelProperty(index = 10)
	private String road_num; // 路牌编号
	@ExcelProperty(index = 11)
	private String village; // 村名称
	@ExcelProperty(index = 12)
	private String building; // 建筑名称
	@ExcelProperty(index = 13)
	private String floor; // 楼层
	@ExcelProperty(index = 14)
	private String address; // 完整的地址名称
	@ExcelProperty(index = 15)
	private String update_address_date; // 地址更新日期
	@ExcelProperty(index = 16)
	private String publish; // 地址是否已发布
	@ExcelProperty(index = 17)
	private String create_address_date; // 地址创建日期
	
	public String getAddress_id() {
		return address_id;
	}
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(String building_id) {
		this.building_id = building_id;
	}
	public String getHouse_id() {
		return house_id;
	}
	public void setHouse_id(String house_id) {
		this.house_id = house_id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getRoad() {
		return road;
	}
	public void setRoad(String road) {
		this.road = road;
	}
	public String getRoad_num() {
		return road_num;
	}
	public void setRoad_num(String road_num) {
		this.road_num = road_num;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUpdate_address_date() {
		return update_address_date;
	}
	public void setUpdate_address_date(String update_address_date) {
		this.update_address_date = update_address_date;
	}
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
	public String getCreate_address_date() {
		return create_address_date;
	}
	public void setCreate_address_date(String create_address_date) {
		this.create_address_date = create_address_date;
	}

}
