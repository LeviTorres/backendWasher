package com.torres.springboot.backend.washer.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.torres.springboot.backend.washer.models.entity.Client;

public interface IClientDao extends JpaRepository<Client, Long>{
	
}
