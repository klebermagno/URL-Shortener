
package br.kleber.encurtador.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;

import javax.management.ObjectName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.kleber.encurtador.domain.UrlPojo;
import br.kleber.encurtador.domain.User;
import br.kleber.encurtador.service.ShortUrlRepository;
import br.kleber.encurtador.service.UserRepository;


@RunWith(SpringRunner.class)
@SpringBootTest

@TestPropertySource(properties = { "spring.jmx.enabled:true",
		"spring.datasource.jmx-enabled:true",
		"spring.jpa.hibernate.use-new-id-generator-mappings=false" })
@ActiveProfiles("scratch")
public class ControllerTests {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext context;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ShortUrlRepository shortUrlRepository;
	
	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}



	@Test
	public void testCreateUserSucess() throws Exception{
		User user = new User();
		user.setId(2l);
		 String requestJson = toJson(user);

		    mvc.perform(post("/users").contentType(APPLICATION_JSON_UTF8)
		        .content(requestJson))
		        .andExpect(status().is(201));
	}
	
	@Test
	public void testCreateUserError() throws Exception{
		User user = new User();
		user.setId(99l);
		 String requestJson = toJson(user);

		    userRepository.save(user);
		    
		    mvc.perform(post("/users").contentType(APPLICATION_JSON_UTF8)
			        .content(requestJson))
			        .andExpect(status().is(409));
	}
	


	@Test
	public void testCreateUrl() throws Exception{
		UrlPojo urlPojo = new UrlPojo();
		urlPojo.setUrl("http://www.chaordic.com.br/folks");
		 String requestJson = toJson(urlPojo);

		    mvc.perform(post("/users/99/urls").contentType(APPLICATION_JSON_UTF8)
		        .content(requestJson))
		        .andExpect(status().is(201));
	}

	
	@Test
	public void testGetUrlSucess() throws Exception{


		    mvc.perform(get("/urls/999"))
		        .andExpect(status().is(301)).andExpect(content().string( "{\"url\":\"http://www.chaordic.com.br/folks\"}"));
	}

	@Test
	public void testGetUrlError() throws Exception{


		    mvc.perform(get("/urls/9999"))
		        .andExpect(status().is(404));
	}
	
	@Test
	public void testDeleteUrlSucess() throws Exception{


		    mvc.perform(delete("/urls/998"))
		        .andExpect(status().is(200));
	}
	
	@Test
	public void testDeleteUrlError() throws Exception{


		    mvc.perform(delete("/urls/9998"))
		        .andExpect(status().is(404)).andExpect(content().string( "{\"message\":\"Url don't exist!\"}"));
	}
	
	@Test
	public void testDeleteUserSucess() throws Exception{


		    mvc.perform(delete("/users/97"))
		    .andExpect(status().is(200));
	}
	
	@Test
	public void testDeleteUserConstrantError() throws Exception{


		    mvc.perform(delete("/users/98"))
		    .andExpect(status().is(404));
	}
	
	@Test
	public void testDeleteUserError() throws Exception{


		    mvc.perform(delete("/users/9998"))
		        .andExpect(status().is(404)).andExpect(content().string( "{\"message\":\"User don't exist!\"}"));
	}
	
	@Test
	public void testStatsUrl() throws Exception{
		
	

		    mvc.perform(get("/stats/999"))
		        .andExpect(status().is(200)).andExpect(content().string("{\"id\":999,\"hits\":11,\"url\":\"http://www.chaordic.com.br/folks\",\"shortUrl\":\"http://<host>[:<port>]/asdfeiba\"}"));
	}
	
	@Test
	public void testStats() throws Exception{
		
	

		    mvc.perform(get("/stats"))
		        .andExpect(status().is(200)).andExpect(content().string("{\"hits\":30,\"urlCount\":3,\"shortUrl\":[{\"id\":997,\"hits\":10,\"url\":\"http://www.chaordic.com.br/folks\",\"shortUrl\":\"http://<host>[:<port>]/asdfeiba\"},{\"id\":998,\"hits\":10,\"url\":\"http://www.chaordic.com.br/folks\",\"shortUrl\":\"http://<host>[:<port>]/asdfeiba\"},{\"id\":999,\"hits\":10,\"url\":\"http://www.chaordic.com.br/folks\",\"shortUrl\":\"http://<host>[:<port>]/asdfeiba\"}]}"));
	}
	
	
	@Test
	public void testUserStats() throws Exception{
		
	

		    mvc.perform(get("/users/99/stats"))
		        .andExpect(status().is(200)).andExpect(content().string("{\"hits\":11,\"urlCount\":1,\"shortUrl\":[{\"id\":999,\"hits\":11,\"url\":\"http://www.chaordic.com.br/folks\",\"shortUrl\":\"http://<host>[:<port>]/asdfeiba\"}]}"));
	}
	
	@Test
	public void testJmx() throws Exception {
		assertThat(ManagementFactory.getPlatformMBeanServer()
				.queryMBeans(new ObjectName("jpa.sample:type=ConnectionPool,*"), null))
						.hasSize(1);
	}
	
	private String toJson(Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		    String requestJson=ow.writeValueAsString(o );
		return requestJson;
	}

}
