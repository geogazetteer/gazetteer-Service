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

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EditorControllerTest extends AddressServiceApplicationTests{
	
//	private MockMvc mvc;
//	@Before 
//	   public void setUp() throws Exception {
//        mvc = MockMvcBuilders.standaloneSetup(new EditorController()).build();
//    }
//	
//	@SuppressWarnings("null")
//	@Test
//	 
//	 public void getEditAll() throws Exception {
//		MultiValueMap <String, String> params = null;
//		params.add("fields","");
//		params.add("tablename", "油松社区");
//		params.add("row", "");
//		params.add("oderby", "");
//		params.add("limit", "10");
//	    mvc.perform(MockMvcRequestBuilders.get("editor/all")
//	    		.accept(MediaType.APPLICATION_JSON)
//	    		.params(params)
//	    		)
//	    			
//	                .andExpect(MockMvcResultMatchers.status().isOk())
//	                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
//	                .andExpect(MockMvcResultMatchers.jsonPath("$.QNAME").value(""))
//	                .andDo(MockMvcResultHandlers.print())
//	                .andReturn();
//	    }
//	
//	public void getCount() throws Exception{
//		mvc.perform(MockMvcRequestBuilders.get("editor/all")
//	    		.accept(MediaType.APPLICATION_JSON)
//	    		.param("tablename","油松社区"))
//					.andExpect(MockMvcResultMatchers.status().isOk())
//					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
//	                .andExpect(MockMvcResultMatchers.jsonPath("$.").value(""))
//	                .andDo(MockMvcResultHandlers.print())
//	                .andReturn();
//					
//	}
	
	

	

}
