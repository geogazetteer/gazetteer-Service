package top.geomatics.entity;

import java.sql.Date;

public class Address {
    private String address_id; // 地址标识，数据库自动生成，示例：63EEDE6BA4206A3AE0538CC0C0C07BB0
    private String code; // 地址编码，唯一，由权威机构确定，示例：44030600960102T0117 000000 共25位
    private String building_id; // 建筑标识，唯一，由权威机构确定，示例：44030600960102T0117 共19位
    private String house_id; // 房屋标识
    private String province; // 省名称，缺省：广东省
    private String city; // 市名称，缺省：深圳市
    private String district; // 区名称，缺省：龙华区
    private String street; // 街道名称
    private String community; // 社区名称
    private String road; // 路名称
    private String road_num; // 路牌编号
    private String villige; // 村名称
    private String building_name; // 建筑名称
    private String floor; // 楼层
    private String address_name; // 完整的地址名称
    private Date update_date; // 地址更新日期
    private Date create_date; // 地址创建日期
    private boolean isPublished; // 地址是否已发布

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

    public String getVillige() {
        return villige;
    }

    public void setVillige(String villige) {
        this.villige = villige;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

}
