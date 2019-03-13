/**
 * 
 */
package top.geomatics.gazetteer.model;

/**
 * <!--基础地址信息，25位-->
 * 
 * @author whudyj
 *
 */
public class BasicAddress {
	private Province province;// 2位
	private City city;// 2位
	private District district;// 2位
	private Town town;// 3位
	private Community community; // 3位
	private BasicGrid basicGrid;// 2位
	private Building building;// 5位
	private House house;// 6位

	public BasicAddress() {
		super();
	}

	public BasicAddress(Province province, City city, District district, Town town, Community community,
			BasicGrid basicGrid, Building building, House house) {
		super();
		this.province = province;
		this.city = city;
		this.district = district;
		this.town = town;
		this.community = community;
		this.basicGrid = basicGrid;
		this.building = building;
		this.house = house;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public BasicGrid getBasicGrid() {
		return basicGrid;
	}

	public void setBasicGrid(BasicGrid basicGrid) {
		this.basicGrid = basicGrid;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

}
