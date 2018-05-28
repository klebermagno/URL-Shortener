package br.kleber.encurtador.service;

import org.springframework.data.repository.Repository;

import br.kleber.encurtador.domain.User;

public interface UserRepository extends Repository<User,Long> {
	
	User findOne(Long id);
	
	User save(User user);
	
	void delete(User user); 

}
