package top.geomatics.gazetteer.database;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import top.geomatics.gazetteer.model.EnterpriseRow;

public class EnterpriseAddressMapperTest {

	private static EnterpriseDatabaseHelper edHelper = new EnterpriseDatabaseHelper();
	private static SqlSession session = edHelper.getSession();
	private static EnterpriseAddressMapper mapper = session.getMapper(EnterpriseAddressMapper.class);

	@Test
	public void testSelectByFid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Messages .getString("EnterpriseAddressMapperTest.0"), Messages .getString("EnterpriseAddressMapperTest.1")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(Messages .getString("EnterpriseAddressMapperTest.2"), Messages .getString("EnterpriseAddressMapperTest.3")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(Messages .getString("EnterpriseAddressMapperTest.4"), 1); //$NON-NLS-1$
		List<EnterpriseRow> list = mapper.findEquals(map);
		// 判断查询结果是否正确
		assertTrue(list.size() == 1);
		EnterpriseRow row = list.get(0);
		assertTrue(row.getCode().compareTo(Messages .getString("EnterpriseAddressMapperTest.5")) == 0); //$NON-NLS-1$
	}

	@Test
	public void testSelectByFids() {
	}

	@Test
	public void testFindEquals() {
	}

	@Test
	public void testFindLike() {
	}

	@Test
	public void testFindPageEquals() {
	}

	@Test
	public void testGetCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Messages .getString("EnterpriseAddressMapperTest.6"), Messages .getString("EnterpriseAddressMapperTest.7")); //$NON-NLS-1$ //$NON-NLS-2$
		Integer count = mapper.getCount(map);
		// 判断查询结果是否正确
		assertTrue(count == 1576);
	}

}
