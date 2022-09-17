package com.torres.springboot.backend.washer.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.torres.springboot.backend.washer.models.entity.Client;
import com.torres.springboot.backend.washer.models.entity.Rent;
import com.torres.springboot.backend.washer.models.entity.Washer;

public interface IRentService {
	
	public List<Rent> findAll();
	
	public Page<Rent> findAll(Pageable pageable);
	
	public Rent findById(Long id);
	
	public Rent save(Rent rent);
	
	public void delete(Long id);
	
	public List<Client> findAllClients();
	
	public List<Washer> findAllWasher();
	
}
