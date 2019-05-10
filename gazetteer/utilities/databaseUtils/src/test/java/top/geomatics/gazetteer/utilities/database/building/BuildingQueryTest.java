package top.geomatics.gazetteer.utilities.database.building;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import top.geomatics.gazetteer.model.GeoPoint;
import top.geomatics.gazetteer.model.IGazetteerConstant;

public class BuildingQueryTest {
	BuildingQuery query = null;

	@Before
	public void testOpen() {
		query = new BuildingQuery();
		query.open();
		assertNotNull(query);
	}

	@Test
	public void testQueryString() {
		String czwCode = Messages.getString("BuildingQueryTest.0"); //$NON-NLS-1$
		GeoPoint point = query.query(czwCode);
		// 判断是否找到坐标，坐标是否有效
		assertNotNull(point);
		assertFalse(point.getX() < IGazetteerConstant.LH_BBOX.getMinx());
		assertFalse(point.getX() > IGazetteerConstant.LH_BBOX.getMaxx());
		assertFalse(point.getY() < IGazetteerConstant.LH_BBOX.getMiny());
		assertFalse(point.getY() > IGazetteerConstant.LH_BBOX.getMaxy());
	}

	@Test
	public void testQueryDoubleDouble() {
		String xString = Messages.getString("BuildingQueryTest.1"); //$NON-NLS-1$
		String yString = Messages.getString("BuildingQueryTest.2"); //$NON-NLS-1$
		double x = Double.parseDouble(xString);
		double y = Double.parseDouble(yString);

		List<String> czwcodes = query.query(x, y);
		// 判断建筑物编码是否有效
		assertNotNull(czwcodes);
		for (String code : czwcodes) {
			assertTrue(code.length() == 19);
		}
	}

	@After
	public void testClose() {
		query.close();
	}

}
