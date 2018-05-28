package br.kleber.encurtador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.kleber.encurtador.domain.ErrorMessage;
import br.kleber.encurtador.domain.ShortUrl;
import br.kleber.encurtador.domain.Stats;
import br.kleber.encurtador.domain.UrlPojo;
import br.kleber.encurtador.domain.User;
import br.kleber.encurtador.service.ShortUrlService;

@Controller
public class EncurtadorController {

	@Autowired
	private ShortUrlService shortUrlService;

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User userIn) {

		User user = shortUrlService.findUser(userIn);

		if (user != null) {
			user.setShortUrl(null);
			return ResponseEntity.status(409).body(user);
		}
		User userCreated = shortUrlService.createUser(userIn);

		userCreated.setShortUrl(null);
		return ResponseEntity.status(201).body(userCreated);
	}

	@RequestMapping(value = "/users/{userid}/urls", method = RequestMethod.POST)
	public ResponseEntity usersUrl(@PathVariable Long userid, @RequestBody UrlPojo urlPojo) {

		ShortUrl shortUrl = null;
		try {
			shortUrl = shortUrlService.createShortUrl(userid, urlPojo.getUrl());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		shortUrl.setUser(null);
		return ResponseEntity.status(201).body(shortUrl);

	}

	@RequestMapping(value = "/urls/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<UrlPojo> urlById(@PathVariable String id) {
		Long l = Long.parseLong(id);
		ShortUrl shortUrl = shortUrlService.getShortUrl(l);
		UrlPojo urlPojo = new UrlPojo();
		if (shortUrl == null) {
			return ResponseEntity.status(404).body(urlPojo);
		}

		urlPojo.setUrl(shortUrl.getUrl());
		return ResponseEntity.status(301).body(urlPojo);

	}

	@RequestMapping(value = "/urls/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity deleteUrlById(@PathVariable String id) {
		Long l = Long.parseLong(id);
		try {
			shortUrlService.deleteUrl(l);
		} catch (Exception e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		}
		return ResponseEntity.status(200).build();

	}
	
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity deleteUserById(@PathVariable String id) {
		Long l = Long.parseLong(id);
		try {
			shortUrlService.deleteUser(l);
		} catch (Exception e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		}
		return ResponseEntity.status(200).build();

	}

	
	@GetMapping("/stats/{id}")
	@ResponseBody
	public ResponseEntity getUrlStats(@PathVariable String id) {
		Long l = Long.parseLong(id);
		ShortUrl shortUrl = null;
		try {
			shortUrl = shortUrlService.getUrlStats(l);
		} catch (Exception e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		}
		return ResponseEntity.status(200).body(shortUrl);

	}
	
	@GetMapping("/users/{userId}/stats")
	@ResponseBody
	public ResponseEntity statsId(@PathVariable String userId) {
		Stats stats = null;
		Long l = Long.parseLong(userId);
		try {
			stats = shortUrlService.getUserStats(l);
		} catch (Exception e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		}
		return ResponseEntity.status(200).body(stats);

	}

	@GetMapping("/stats")
	@ResponseBody
	public ResponseEntity stats() {
		Stats stats = null;
		try {
			stats = shortUrlService.getGeralStats();
		} catch (Exception e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		}
		return ResponseEntity.status(200).body(stats);


	}

	





}
