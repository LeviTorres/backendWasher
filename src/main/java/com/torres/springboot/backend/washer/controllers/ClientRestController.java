package com.torres.springboot.backend.washer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.torres.springboot.backend.washer.models.entity.Client;
import com.torres.springboot.backend.washer.models.services.IClientService;

@CrossOrigin(origins= {"http://localhost:4000"})
@RestController
@RequestMapping("/api")
public class ClientRestController {
	
	@Autowired
	private IClientService clientService;
	
	@GetMapping("/clients")
	public List<Client> index(){
		return clientService.findAll();
	}
	
	@GetMapping("/clients/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Client show(@PathVariable Long id){
		return clientService.finById(id);
	}
	
	@PostMapping("/clients")
	@ResponseStatus(HttpStatus.CREATED)
	public Client create(@RequestBody Client client) {
		return clientService.save(client);
	}
	
	@PutMapping("/clients/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Client update(@RequestBody Client client, @PathVariable Long id) {
		Client clientActual = clientService.finById(id);
		
		clientActual.setName(client.getName());
		clientActual.setLastname(client.getLastname());
		clientActual.setEmail(client.getEmail());
		clientActual.setPhone(client.getPhone());
		clientActual.setPhone1(client.getPhone1());
		clientActual.setStreet(client.getStreet());
		clientActual.setHouseNumber(client.getHouseNumber());
		clientActual.setSuburb(client.getSuburb());
		clientActual.setPostalCode(client.getPostalCode());
		
		return clientService.save(clientActual);
	}
	
	@DeleteMapping("/clients/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		clientService.delete(id);
	}
	
}
