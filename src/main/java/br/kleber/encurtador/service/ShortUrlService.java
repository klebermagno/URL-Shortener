package br.kleber.encurtador.service;

import br.kleber.encurtador.domain.ShortUrl;
import br.kleber.encurtador.domain.Stats;
import br.kleber.encurtador.domain.User;
import br.kleber.encurtador.exception.EncurtadorException;

public interface ShortUrlService {

	User createUser(User user);

	ShortUrl createShortUrl(Long userId, String url) throws EncurtadorException;

	ShortUrl getShortUrl(Long urlId)throws EncurtadorException;

	Stats getGeralStats()throws EncurtadorException;

	Stats getUserStats(Long userId)throws EncurtadorException;

	ShortUrl getUrlStats(Long urlId);

	void deleteUrl(Long urlId) throws EncurtadorException;

	void deleteUser(Long userId) throws EncurtadorException;

	User findUser(User userIn);

}
