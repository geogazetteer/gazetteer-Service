package top.geomatics.gazetteer.service.address;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EditorControllerTest extends ControllerTests {

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new EditorController()).build();
	}

	@Test
	public void testGetCount() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/editor/count").accept(MediaType.APPLICATION_JSON).param("tablename", "油松社区"))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

}
