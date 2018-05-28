package br.kleber.encurtador.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import br.kleber.encurtador.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.use-new-id-generator-mappings=false")
public class UserIntegrationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ShortUrlRepository shortUrlRepository;

	@Test
	public void executesQueryMethodsCorrectly() {

		User user = new User();
		user.setName("3");
		
		user = userRepository.save(user);

		User userRet = userRepository.findOne(user.getId());

		assertNotNull( userRet.getId());

	}

	
	


	
}
