package com.torres.springboot.backend.washer.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.torres.springboot.backend.washer.models.entity.Client;
import com.torres.springboot.backend.washer.models.entity.Rent;
import com.torres.springboot.backend.washer.models.entity.Washer;

public interface IRentDao extends JpaRepository<Rent, Long>{
	
	@Query("from Client")
	public List<Client> findAllClients();
	
	@Query("from Washer")
	public List<Washer> findAllWashers();
}
