package top.geomatics.gazetteer.service.address;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildingControllerTest extends AddressServiceApplicationTests{
	
	private MockMvc mvc;
	@Before 
	   public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new BuildingController()).build();
    }
	
	@SuppressWarnings("null")
	@Test
	 
	 public void getQuery() throws Exception {
		MultiValueMap <String, String> params = null;
		params.add("x","503361.375");
		params.add("y", "2506786.75");
	    mvc.perform(MockMvcRequestBuilders.get("building/query")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params)
	    		)
	    			
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.QNAME").value(""))//不知道x=503361.375，y=2506786.75对应的建筑物是什么
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
	    }
	
	

	

}
