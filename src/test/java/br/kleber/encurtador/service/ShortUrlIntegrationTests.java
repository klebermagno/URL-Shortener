
package br.kleber.encurtador.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import br.kleber.encurtador.domain.ShortUrl;
import br.kleber.encurtador.domain.User;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.use-new-id-generator-mappings=false")
public class ShortUrlIntegrationTests {

	@Autowired
	ShortUrlRepository shortUrlRepository;

	@Autowired
	UserRepository userRepository;
	
	@Test
	public void shortUrlSaveTest() {
		User user = new User();

		user = userRepository.save(user);
		
		ShortUrl shortUrl = new ShortUrl();
		shortUrl.setUser(user);
		shortUrl.setHits(0l);
		shortUrl.setShortUrl("short");
		shortUrl.setUrl("Url");
		ShortUrl saved = this.shortUrlRepository.save(shortUrl );
		
		assertNotNull(saved);
		
		ShortUrl ret  = this.shortUrlRepository.findOne(saved.getId());
		
		assertNotNull(ret);
		

	}
	
	
	@Test
	public void shortUrlSaveTest99() throws Exception{
		
		User user = new User();
		user = userRepository.save(user);
		
		ShortUrl entity = new ShortUrl();
		entity.setHits(0l);
		entity.setUrl("w");
		entity.setShortUrl("http://<host>[:<port>]/");
		 user = userRepository.findOne(99l);
		if (user == null)
			throw new Exception("User not exist!");
		entity.setUser(user);
		ShortUrl shortUrl = this.shortUrlRepository.save(entity);
		assertNotNull(shortUrl.getId());
	}
	
	@Test
	public void shortUrlFindOneTest() {
		ShortUrl shortUrl = shortUrlRepository.findOne(999l);
		assertEquals(shortUrl.getUrl(), "http://www.chaordic.com.br/folks");
	}
	
	@Test
	public void findAllStatsTest(){
		Long hits = this.shortUrlRepository.findAllHitsCountStats();
		Long urlCounts = this.shortUrlRepository.findAllUrlCountStats();
		assertEquals(hits, new Long(30));
		assertEquals(urlCounts, new Long(3));
	}
	
	@Test
	public void findByUsertats(){
		Long hits = this.shortUrlRepository.findHitsByUsertats(99l);
		Long urlCounts = this.shortUrlRepository.findUrlCountByUsertats(99l);
		assertEquals(hits, new Long(20));
		assertEquals(urlCounts, new Long(2));
	}
	
	@Test
	public void findShortUrlOrderByHits(){
		Collection<ShortUrl> shorts = this.shortUrlRepository.findShortUrlOrderByHits();
		assertEquals(shorts.size(), 5);
		shorts.stream().forEach(shortt -> System.out.println(shortt.getHits()));
	}
	
	@Test
	public void findShortUrlByUserOrderByHits(){
		User userRet = userRepository.findOne(99l);

		Collection<ShortUrl> shorts = this.shortUrlRepository.findShortUrlByUserOrderByHits(userRet);
		assertEquals(shorts.size(), 2);
		shorts.stream().forEach(shortt -> System.out.println(shortt.getHits()));
	}
}
