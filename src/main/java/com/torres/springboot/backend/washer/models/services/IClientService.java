package com.torres.springboot.backend.washer.models.services;

import java.util.List;

import com.torres.springboot.backend.washer.models.entity.Client;

public interface IClientService {
	public List<Client> findAll();
	
	public Client finById(Long id);
	
	public Client save(Client client);
	
	public void delete(Long id);
	
	
}
