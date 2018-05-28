package br.kleber.encurtador.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


public class Stats implements Serializable {
	

	private static final long serialVersionUID = 1L;

	private Long hits;
	
	private Long urlCount;
	
	public Stats(Long urlCount, Long hits ){
		this.hits = hits;
		this.urlCount = urlCount;
		shortUrl = new ArrayList();
	}
	private Collection<ShortUrl> shortUrl;

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Long getUrlCount() {
		return urlCount;
	}

	public void setUrlCount(Long urlCount) {
		this.urlCount = urlCount;
	}

	public Collection<ShortUrl> getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(Collection<ShortUrl> shortUrl) {
		this.shortUrl = shortUrl;
	}
	
	

}
