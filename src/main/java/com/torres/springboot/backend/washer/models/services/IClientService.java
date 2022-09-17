package com.torres.springboot.backend.washer.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.torres.springboot.backend.washer.models.entity.Client;

public interface IClientService {
	public List<Client> findAll();
	
	public Page<Client> findAll(Pageable pageable);
	
	public Client finById(Long id);
	
	public Client save(Client client);
	
	public void delete(Long id);
	
	
}
