package br.kleber.encurtador.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<ShortUrl> shortUrl;
	
	
	public Set<ShortUrl> getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(Set<ShortUrl> shortUrl) {
		this.shortUrl = shortUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
