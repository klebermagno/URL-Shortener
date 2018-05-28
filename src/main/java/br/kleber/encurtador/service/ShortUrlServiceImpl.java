package br.kleber.encurtador.service;

import java.util.Collection;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import be.kleber.encurtador.shorter.URLShortener;
import br.kleber.encurtador.domain.ShortUrl;
import br.kleber.encurtador.domain.Stats;
import br.kleber.encurtador.domain.User;
import br.kleber.encurtador.exception.EncurtadorException;

@Component("shortUrlService")
@Transactional
public class ShortUrlServiceImpl implements ShortUrlService {

	private UserRepository userRepository;

	private ShortUrlRepository shortUrlRepository;

	public ShortUrlServiceImpl(UserRepository userRepository, ShortUrlRepository shortUrlRepository) {
		super();
		this.userRepository = userRepository;
		this.shortUrlRepository = shortUrlRepository;
	}

	@Override
	public User createUser(User user) {
		User userRet = this.userRepository.save(user);
		return userRet;
	}

	@Override
	public ShortUrl createShortUrl(Long userId, String url) throws EncurtadorException {

		ShortUrl entity = new ShortUrl();
		entity.setHits(new Long(0));
		entity.setUrl(url);
		URLShortener u = new URLShortener(5, "www.tinyurl.com/");
		
		entity.setShortUrl(u.shortenURL(url));
		User user = userRepository.findOne(userId);
		if (user == null)
			throw new EncurtadorException("User not exist!");
		entity.setUser(user);
		ShortUrl shortUrl = this.shortUrlRepository.save(entity);
		return shortUrl;
	}

	@Override
	public Stats getGeralStats() {
		Long hits = this.shortUrlRepository.findAllHitsCountStats();
		Long urlCounts = this.shortUrlRepository.findAllUrlCountStats();

		Collection<ShortUrl> shorts = this.shortUrlRepository.findShortUrlOrderByHits();
		
		Stats stats = new Stats(urlCounts,hits);
		stats.setShortUrl(shorts);
		return stats;
	}

	@Override
	public Stats getUserStats(Long userId) {
		Long hits = this.shortUrlRepository.findHitsByUsertats(userId);
		Long urlCounts = this.shortUrlRepository.findUrlCountByUsertats(userId);
		User userRet = userRepository.findOne(userId);
		Collection<ShortUrl> shorts = this.shortUrlRepository.findShortUrlByUserOrderByHits(userRet);
		Stats stats = new Stats(urlCounts,hits);
		stats.setShortUrl(shorts);
		return stats;
	}

	@Override
	public ShortUrl getUrlStats(Long urlId) {
		ShortUrl shortUrl = this.shortUrlRepository.findOne(urlId);
		if (shortUrl != null) {
			shortUrl.setUser(null);
		}
		return shortUrl;
	}

	@Override
	public void deleteUrl(Long urlId) throws EncurtadorException {
		ShortUrl shortUrl = this.shortUrlRepository.findOne(urlId);
		if (shortUrl != null) {
			shortUrl.setHits(shortUrl.getHits() + 1);
			this.shortUrlRepository.delete(shortUrl);
		} else {
			throw new EncurtadorException("Url don't exist!");
		}

	}

	@Override
	public void deleteUser(Long userId) throws EncurtadorException {
		User user = userRepository.findOne(userId);
		if (user == null)
			throw new EncurtadorException("User don't exist!");
		
		try {
			userRepository.delete(user);
		} catch (DataIntegrityViolationException e) {
			throw new EncurtadorException("The user have urls associeted!");
		}
	}

	@Override
	public ShortUrl getShortUrl(Long urlId) {
		ShortUrl shortUrl = this.shortUrlRepository.findOne(urlId);
		if (shortUrl != null) {
			shortUrl.setHits(shortUrl.getHits() + 1);
			this.shortUrlRepository.save(shortUrl);
		}
		return shortUrl;
	}

	@Override
	public User findUser(User userIn) {
		User user = userRepository.findOne(userIn.getId());
		return user;
	}

}