package com.torres.springboot.backend.washer.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.torres.springboot.backend.washer.models.entity.User;

public interface IUserDao extends CrudRepository<User, Long>{
	
	
	public User findByUsername(String username);
	
	@Query("SELECT u from User u WHERE u.username=?1")
	public User findByUsername1(String username);
}
