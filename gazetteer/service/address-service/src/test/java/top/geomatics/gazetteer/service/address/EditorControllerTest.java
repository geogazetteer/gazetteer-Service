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
public class EditorControllerTest extends AddressServiceApplicationTests{
	
	private MockMvc mvc;
	@Before 
	   public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new EditorController()).build();
    }
	
	@SuppressWarnings("null")
	@Test
	 
	 public void getEditAllTest() throws Exception {
		MultiValueMap <String, String> params = null;
		params.add("fields","");
		params.add("tablename", "油松社区");
		params.add("row", "");
		params.add("oderby", "");
		params.add("limit", "10");
	    mvc.perform(MockMvcRequestBuilders.get("editor/all")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params)
	    		)
	    			
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//油松社区需要返回进行修改的记录数
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
	    }
	
	public void getCountTest() throws Exception{
		
		mvc.perform(MockMvcRequestBuilders.get("editor/count")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.param("tablename","油松社区"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//油松社区所有的地址记录数
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void selectByFidTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("fields","*");
		params.add("tablename", "油松社区");
		mvc.perform(MockMvcRequestBuilders.get("editor/fid/1")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//油松社区所有的地址记录数
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void selectByFidsTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("in","1,2,3");
		params.add("field","*");
		params.add("tablename", "民治街道");
		mvc.perform(MockMvcRequestBuilders.get("editor/fids")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//民治街道所有的地址记录数
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editWirhConditionsTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("fields","fid,code,name,address");
		params.add("tablename", "enterprise1");
		params.add("fid", "1");
		params.add("code", "91440300MA5DK2PU7P");
		params.add("name", "深圳市明慧汽配有限公司");
		params.add("street", "龙华");
		params.add("owner", "方海英");
		params.add("address", "深圳市龙华区龙华街道东环一路天汇大厦B座906室");
		params.add("status", "");
		params.add("modifier", "");
		params.add("update_date", "");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/query")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//返回按条件查询的所有地址记录数
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void fuzzyEditWithConditionsTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("fields","fid,code,name,address");
		params.add("tablename", "enterprise1");
		params.add("fid", "1");
		params.add("code", "91440300MA5DK2PU7P");
		params.add("name", "深圳市明慧汽配有限公司");
		params.add("street", "龙华");
		params.add("owner", "方海英");
		params.add("address", "天汇大厦B座906室");
		params.add("status", "");
		params.add("modifier", "");
		params.add("update_date", "");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/fuzzyquery")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//返回模糊查询的所有地址记录数
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editByCodeTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("code","91440300MA5DK2PU7P")
		params.add("fields","*");
		params.add("tablename", "enterprise1");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/code/{code}")//enterprise1表中第一条记录的code
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取统一社会信用代码的所有记录
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editByNameTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("name", "深圳市明慧汽配有限公司");
		params.add("fields","*");
		params.add("tablename", "enterprise1");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/name/{name}")//
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取公司名称对应的所有记录
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editByStreetTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("street", "龙华");
		params.add("fields","*");
		params.add("tablename", "enterprise1");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/street/{street}")//
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取街道对应的所有记录
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editByOwnerTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("owner", "方海英");
		params.add("fields","*");
		params.add("tablename", "enterprise1");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/owner/{owner}")//
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取街道对应的所有记录
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editByAddressTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("address", "深圳市龙华区龙华街道东环一路天汇大厦B座906室");
		params.add("fields","*");
		params.add("tablename", "enterprise1");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/address/{address}")//
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取地址对应的所有记录
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editByStatusTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("status", "0");
		params.add("fields","*");
		params.add("tablename", "enterprise1");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/status/{status}")//
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取对应状态的所有记录
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void editByModifierTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("modifier", "Admin");//编辑者未知
		params.add("fields","*");
		params.add("tablename", "enterprise1");
		params.add("oderby", "DESC");
		params.add("limit", "10");
		
		mvc.perform(MockMvcRequestBuilders.get("editor/modifier/{modifier}")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取编辑者对应的所有记录
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void updateAllTest() throws Exception{
		MultiValueMap <String, String> params = null;
		
		params.add("tablename", "福民社区");
		params.add("new_status", "0");
		params.add("new_modifier", "admin");
		params.add("new_update_date", "");
		params.add("new_update_address", "");
		params.add("new_update_address_id", "");
		
		
		mvc.perform(MockMvcRequestBuilders.get("editor/update/all")//
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取更新的所有记录数目
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void updateAllByFidTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("fid", "1");
		params.add("tablename", "福民社区");
		params.add("new_status", "0");
		params.add("new_modifier", "admin");
		params.add("new_update_date", "");
		params.add("new_update_address", "");
		params.add("new_update_address_id", "");
		
		
		mvc.perform(MockMvcRequestBuilders.get("editor/update/all/{fid}")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取更新的所有记录数目
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void updateStatusTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("fid", "1");
		params.add("tablename", "福民社区");
		params.add("code", "91440300342919578H");
		params.add("name", "");
		params.add("street","");
		params.add("owner", "");
		params.add("address", "");
		params.add("status", "0");
		params.add("modifier", "admin");
		params.add("update_date", "");

		mvc.perform(MockMvcRequestBuilders.get("editor/update/status/{new_status}")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取更新的所有记录数目
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void updateModifierTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("new_modifier", "admin");
	

		mvc.perform(MockMvcRequestBuilders.get("editor/update/modifier/{new_modifier}")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取更新的所有记录数目
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void updateDateTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("new_update_date", "");

		mvc.perform(MockMvcRequestBuilders.get("editor/update/update_date/{new_update_date}")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取更新的所有记录数目
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void updateAddressTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("new_update_address", "");

		mvc.perform(MockMvcRequestBuilders.get("editor/update/update_address/{new_update_address}")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取更新的所有记录数目
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
	@SuppressWarnings("null")
	public void updateAddressIdTest() throws Exception{
		MultiValueMap <String, String> params = null;
		params.add("new_update_address_id", "");

		mvc.perform(MockMvcRequestBuilders.get("editor/update/update_address_id/{new_update_address_id}")
	    		.accept(MediaType.APPLICATION_JSON)
	    		.params(params))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
	                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(""))//获取更新的所有记录数目
	                .andDo(MockMvcResultHandlers.print())
	                .andReturn();
					
	}
	
}
