package top.geomatics.gazetteer.service.address;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class BuildingControllerTest extends ControllerTests {

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new BuildingController()).build();
	}

	@Test
	public void getQueryPoint() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/building/point").accept(MediaType.APPLICATION_JSON).param("code", "4403060070051200001"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
