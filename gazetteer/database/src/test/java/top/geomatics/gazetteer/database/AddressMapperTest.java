package top.geomatics.gazetteer.database;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import top.geomatics.gazetteer.model.AddressRow;

public class AddressMapperTest {

	private static DatabaseHelper helper = new DatabaseHelper();
	private static SqlSession session = helper.getSession();
	private static AddressMapper mapper = session.getMapper(AddressMapper.class);

	@Test
	public void testSelectById() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Messages .getString("AddressMapperTest.0"), Messages .getString("AddressMapperTest.1")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(Messages .getString("AddressMapperTest.2"), Messages .getString("AddressMapperTest.3")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(Messages .getString("AddressMapperTest.4"), 1); //$NON-NLS-1$
		List<AddressRow> list = mapper.findEquals(map);
		// 判断查询结果是否正确
		assertTrue(list.size() == 1);
		AddressRow row = list.get(0);
		assertTrue(row.getCode().compareTo(Messages .getString("AddressMapperTest.5")) == 0); //$NON-NLS-1$
	}

	@Test
	public void testSelectByIds() {

	}

	@Test
	public void testSelectAddressForDictionary() {

	}

	@Test
	public void testFindEquals() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Messages .getString("AddressMapperTest.6"), Messages .getString("AddressMapperTest.7")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(Messages .getString("AddressMapperTest.8"), Messages .getString("AddressMapperTest.9")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(Messages .getString("AddressMapperTest.10"), Messages .getString("AddressMapperTest.11")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(Messages .getString("AddressMapperTest.12"), Messages .getString("AddressMapperTest.13")); //$NON-NLS-1$ //$NON-NLS-2$
		List<AddressRow> list = mapper.findEquals(map);
		// 判断查询结果是否正确
		assertTrue(list.size() == 3);
		for (AddressRow row : list) {
			assertTrue(row.getBuilding_id().compareTo(Messages .getString("AddressMapperTest.14")) == 0); //$NON-NLS-1$
		}

	}

	@Test
	public void testFindSimpleEquals() {

	}

	@Test
	public void testFindBuildingEquals() {

	}

	@Test
	public void testFindLike() {

	}

	@Test
	public void testFindBuildingByPoint() {

	}

	@Test
	public void testCreateAddressTable() {

	}

	@Test
	public void testInsertAddress() {

	}

	@Test
	public void testCreateBuildingPositionTable() {

	}

	@Test
	public void testInsertBuildingPosition() {

	}

	@Test
	public void testCreateDistrictTable() {

	}

	@Test
	public void testInsertDistrict() {

	}

	@Test
	public void testCreateStreetTable() {

	}

	@Test
	public void testInsertStreet() {

	}

}
