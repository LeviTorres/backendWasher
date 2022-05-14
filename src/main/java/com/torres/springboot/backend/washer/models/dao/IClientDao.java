package com.torres.springboot.backend.washer.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.torres.springboot.backend.washer.models.entity.Client;

public interface IClientDao extends CrudRepository<Client, Long>{
	
}
