package br.kleber.encurtador.service;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import br.kleber.encurtador.domain.ShortUrl;
import br.kleber.encurtador.domain.User;

public interface ShortUrlRepository extends Repository<ShortUrl,Long>{

	
	ShortUrl findOne(Long id);
	
	ShortUrl save(ShortUrl shortUrl);
	
	void delete(ShortUrl shortUrl); 
	
	@Query(value = "select count(distinct id)  as urlCount  from short_url", nativeQuery = true)
	Long findAllUrlCountStats();
	
	@Query(value = "select  sum(hits)  as hits from short_url", nativeQuery = true)
	Long findAllHitsCountStats();
	
	@Query(value ="select count(distinct id)  as urlCount   from short_url where user_id = ?1 ", nativeQuery = true)
	Long findUrlCountByUsertats(Long idUser);
	
	@Query(value ="select sum(hits)  as hits from short_url where user_id = ?1 ", nativeQuery = true)
	Long findHitsByUsertats(Long idUser);
	
	
	@Query(value =" FROM ShortUrl short ORDER BY short.hits DESC ")
	Collection<ShortUrl> findShortUrlOrderByHits();
	
	@Query(value =" FROM ShortUrl short WHERE short.user = :user ORDER BY short.hits DESC ")
	Collection<ShortUrl>  findShortUrlByUserOrderByHits(@Param("user") User user);

}
